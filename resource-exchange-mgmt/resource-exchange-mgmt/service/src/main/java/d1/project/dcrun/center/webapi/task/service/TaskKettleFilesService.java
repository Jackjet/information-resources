package d1.project.dcrun.center.webapi.task.service;

import com.alibaba.fastjson.JSONObject;
import d1.project.dcrun.center.webapi.common.Constants;
import d1.project.dcrun.center.webapi.common.model.OperationLog;
import d1.project.dcrun.center.webapi.common.service.IOperationLogService;
import d1.project.dcrun.center.webapi.common.utils.BaseUtils;
import d1.project.dcrun.center.webapi.resource.dao.ContainerInfoDao;
import d1.project.dcrun.center.webapi.resource.entity.ContainerInfo;
import d1.project.dcrun.center.webapi.task.dao.TaskDao;
import d1.project.dcrun.center.webapi.task.dao.TaskKettleFilesDao;
import d1.project.dcrun.center.webapi.task.entity.Task;
import d1.project.dcrun.center.webapi.task.entity.TaskKettleFiles;
import d1.project.dcrun.center.webapi.task.model.FileChild;
import d1.project.dcrun.center.webapi.task.model.TaskScheduleVm;
import d1.project.dcrun.center.webapi.task.utils.ZipUtil;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhengyang
 */
@Service
public class TaskKettleFilesService {
    private final TaskKettleFilesDao taskKettleFilesDao;
    private final TaskDao taskDao;
    private final String fileSeparator = File.separator;
    private final IOperationLogService iOperationLogService;
    private final ContainerInfoDao containerInfoDao;
    @Value("${d1.project.root.utl}")
    private String rootUrl = "";
    @Value("${d1.project.http.url}")
    private String httpUrl;
    @Value("${d1.project.node.upload.url}")
    private String uploadUrl;
    @Value("${d1.project.node.delete.url}")
    private String deleteUrl;

    public TaskKettleFilesService(TaskKettleFilesDao taskKettleFilesDao, TaskDao taskDao, IOperationLogService iOperationLogService, ContainerInfoDao containerInfoDao) {
        this.taskKettleFilesDao = taskKettleFilesDao;
        this.taskDao = taskDao;
        this.iOperationLogService = iOperationLogService;
        this.containerInfoDao = containerInfoDao;
    }

    /**
     * ????????????????????????
     * @param taskId
     * @return
     * @throws Exception
     */
    public List<TaskKettleFiles> findByTaskId(String taskId) throws Exception {
        Task task = taskDao.findById(taskId).orElseThrow(() -> new Exception("???????????????"));
        List<TaskKettleFiles> taskKettleFilesList = taskKettleFilesDao.findByTaskId(taskId);
        for (TaskKettleFiles taskKettleFiles : taskKettleFilesList) {
            taskKettleFiles.setXmlUrl(httpUrl+taskKettleFiles.getXmlUrl());
        }
        return taskKettleFilesList;
    }

    /**
     * ????????????????????????
     * @param taskId
     * @return
     * @throws Exception
     */
    public TaskKettleFiles findByTaskIdAndName(String taskId, String name) throws Exception {
        return taskKettleFilesDao.findByTaskIdAndName(taskId, name);
    }

    /**
     * ????????????????????????
     * @param taskId
     * @return
     * @throws Exception
     */
    public TaskKettleFiles findByTaskIdAndType(String taskId, String type) throws Exception {
        return taskKettleFilesDao.findByTaskIdAndType(taskId, type);
    }

    /**
     * ????????????
     *
     * @param taskId
     * @param files
     * @param request
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public List<FileChild> uploadFile(String taskId, List<MultipartFile> files, HttpServletRequest request) throws Exception {
        Task task = taskDao.findById(taskId).orElseThrow(() -> new Exception("???????????????"));
        //??????????????????
        if(task.getStatus() == 1){
            throw new Exception(Constants.TASK_STATUS_ERR);
        }
        //??????????????????job
        isContainKjb(files);
        int taskKettleFilesCount = taskKettleFilesDao.countAllByTaskId(taskId);
        if(taskKettleFilesCount>0){
            throw new Exception(Constants.TASK_IS_EXIST);
        }
        List<FileChild> fileChildList = uploadTransFile(taskId, files);
        for (FileChild fileChild : fileChildList) {
            TaskKettleFiles taskKettleFiles = new TaskKettleFiles();
            taskKettleFiles.setTaskId(taskId);
            taskKettleFiles.setName(fileChild.getName());
            taskKettleFiles.setType(fileChild.getType());
            taskKettleFiles.setFileUrl(fileChild.getFileUrl());
            taskKettleFiles.setXmlUrl(fileChild.getXmlUrl());
            taskKettleFiles.setId(BaseUtils.generate32Id());
            taskKettleFilesDao.save(taskKettleFiles);
        }
        List<String> filePathList = fileChildList.stream().map(p -> p.getFileUrl()).collect(Collectors.toList());
        Optional<ContainerInfo> containerInfoOptional = containerInfoDao.findById(task.getContainer());
        if(!containerInfoOptional.isPresent()){
            throw new Exception(Constants.CONTAINER_IS_NULL);
        }
        ContainerInfo containerInfo = containerInfoOptional.get();
        String containerPath = containerInfo.getIp() + ":" + containerInfo.getPort();
        checkContainerOnline(containerPath);
        sendKettleFile(taskId, containerPath, filePathList);
        //??????kjb????????????
        saveKjbSchedule(task, fileChildList);
        iOperationLogService.insert(new OperationLog("????????????", "??????????????????", "??????????????????", "??????????????????:" + task.getName(), 1), request);
        return fileChildList;
    }

    /**
     * ??????????????????
     *
     * @param taskId
     * @param request
     * @throws Exception
     */
    public String downloadFile(String taskId, HttpServletRequest request) throws Exception {
        Task task = taskDao.findById(taskId).orElseThrow(() -> new Exception("???????????????"));
        List<TaskKettleFiles> taskKettleFilesList = taskKettleFilesDao.findAllByTaskId(taskId);
        if (taskKettleFilesList.size() > 0) {
            String path = System.getProperty("user.dir") + fileSeparator + "static" + fileSeparator + "downFile"+ fileSeparator;
            String fileName = Calendar.getInstance().getTimeInMillis() + ".zip";
            File downPath = new File(path);
            if (!downPath.exists()) {
                boolean mdk = downPath.mkdirs();
                if (!mdk) {
                    throw new Exception(Constants.FILE_CREATE_DIR_ERROR);
                }
            }
            List<File> srcFiles = new ArrayList<>();
            for (TaskKettleFiles taskKettleFiles : taskKettleFilesList) {
                File file = new File(taskKettleFiles.getFileUrl());
                srcFiles.add(file);
            }
            String zipPath = path + fileName;
            ZipUtil.zipFile(zipPath, srcFiles);
            iOperationLogService.insert(new OperationLog("????????????", "??????????????????", "??????????????????", "??????????????????:" + task.getName(), 1), request);
            return "/downFile/" + fileName;
        } else {
            throw new Exception("?????????????????????");
        }
    }

    /**
     * ????????????
     *
     * @param taskId
     * @param request
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteFile(String taskId, HttpServletRequest request) throws Exception {
        Task task = taskDao.findById(taskId).orElseThrow(() -> new Exception("???????????????"));
        if(task.getStatus()==1){
            throw new Exception("????????????????????????????????????");
        }
        taskKettleFilesDao.deleteAllByTaskId(taskId);
        String taskDirPath = rootUrl + fileSeparator + "kettleFile" + fileSeparator + taskId;
        File file =new File(taskDirPath);
        FileUtils.deleteDirectory(file);
        Optional<ContainerInfo> containerInfoOptional = containerInfoDao.findById(task.getContainer());
        if(!containerInfoOptional.isPresent()){
            throw new Exception(Constants.CONTAINER_IS_NULL);
        }
        ContainerInfo containerInfo = containerInfoOptional.get();
        String containerPath = containerInfo.getIp() + ":" + containerInfo.getPort();
        checkContainerOnline(containerPath);
        deleteKettleFile(taskId, containerPath);
        //?????????????????????kettle????????????
        deleteKjbSchedule(task);
        iOperationLogService.insert(new OperationLog("????????????", "??????????????????", "??????????????????", "??????????????????:" + task.getName(), 1), request);
    }

    private void checkContainerOnline(String container) throws Exception {
        String runUrl = "http://" + container + "/task/container/status";
        try {
            SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
            // ????????????
            requestFactory.setConnectTimeout(3000);
            requestFactory.setReadTimeout(3000);
            RestTemplate restTemplate = new RestTemplate(requestFactory);
            ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(runUrl, JSONObject.class);
            JSONObject result = responseEntity.getBody();
            if (result != null) {
                String code = result.getString("code");
                String fail = "0";
                if (fail.equals(code)) {
                    throw new Exception(result.getString("msg"));
                }
            }
        }catch (Exception e) {
            throw new Exception(Constants.CONTAINER_STATUS_ERR);
        }
    }

    private void deleteKettleFile(String taskId, String container) throws Exception {
        String runUrl = "http://" + container + "/" + deleteUrl + "?id=" + taskId;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> responseEntity = restTemplate.getForEntity(runUrl, JSONObject.class);
        JSONObject result = responseEntity.getBody();
        if (result != null) {
            String code = result.getString("code");
            String fail = "0";
            if (fail.equals(code)) {
                throw new Exception(result.getString("msg"));
            }
        }
    }

    /**
     * ??????????????????????????????
     *
     * @param taskId            ??????ID
     * @param multipartFileList ?????????
     * @return ??????????????????
     * @throws Exception ??????????????????
     */
    private List<FileChild> uploadTransFile(String taskId, List<MultipartFile> multipartFileList) throws Exception {
        List<FileChild> fileChildList = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFileList) {
            String fileName = multipartFile.getOriginalFilename();
            if (StringUtils.isEmpty(fileName)) {
                throw new Exception(Constants.FILE_ERROR);
            }
            String javaUrl = System.getProperty("user.dir") + fileSeparator + "static";
            String tempXmlUrl = fileSeparator + "kettleFile" + fileSeparator + taskId;
            String xmlDirPath = javaUrl + tempXmlUrl;
            String kettleDirPath = rootUrl + fileSeparator + "kettleFile" + fileSeparator + taskId;
            String filePath = kettleDirPath + fileSeparator + fileName;
            File kettleDirFile = new File(kettleDirPath);
            if (!kettleDirFile.exists()) {
                boolean mdk = kettleDirFile.mkdirs();
                if (!mdk) {
                    throw new Exception(Constants.FILE_CREATE_DIR_ERROR);
                }
            }
            File newFile = new File(filePath);
            multipartFile.transferTo(newFile);
            fileChildList.add(getTaskFile(fileName, filePath, xmlDirPath, tempXmlUrl));
        }
        return fileChildList;
    }

    /**
     * ??????kettle??????
     *
     * @param taskId       ??????ID
     * @param container ??????ID
     * @param files        ??????
     * @return ????????????
     */
    private void sendKettleFile(String taskId, String container, List<String> files) throws Exception {
        String runUrl = "http://" + container + "/" +uploadUrl;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> postData = new LinkedMultiValueMap<>();
        postData.add("id", taskId);
        for(String filepath :files){
            FileSystemResource file = new FileSystemResource(filepath);
            postData.add("files", file);
        }
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(postData, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<JSONObject> responseEntity = restTemplate.postForEntity(runUrl, httpEntity, JSONObject.class);
        JSONObject result = responseEntity.getBody();
        if (result != null) {
            String code = result.getString("code");
            String fail = "0";
            if (fail.equals(code)) {
                throw new Exception(result.getString("msg"));
            }
        }
    }

    /**
     * ??????????????????xml??????????????????
     *
     * @param fileName   ?????????
     * @param filePath   ????????????
     * @param xmlDirPath xml????????????
     * @return ??????????????????
     * @throws Exception ????????????????????????
     */
    private FileChild getTaskFile(String fileName, String filePath, String xmlDirPath, String xmlTempUrl) throws Exception {
        String name = copyFile(filePath, fileName, xmlDirPath);
        FileChild fileChild = new FileChild();
        fileChild.setName(fileName);
        fileChild.setXmlUrl(xmlTempUrl + fileSeparator + name);
        fileChild.setFileUrl(filePath);
        String fileType = fileName.substring(fileName.indexOf(".") + 1);
        fileChild.setType(fileType);
        return fileChild;
    }

    /**
     * ????????????
     *
     * @param sourcePath ???????????????
     * @param fileName   ???????????????
     * @param targetPath ??????????????????
     * @throws Exception ????????????????????????
     */
    private String copyFile(String sourcePath, String fileName, String targetPath) throws Exception {
        File sourceFile = new File(sourcePath);
        FileInputStream fileInputStream = new FileInputStream(sourceFile);
        String xmlPath = targetPath + fileSeparator + fileName;
        File targetFile = new File(xmlPath);
        File fileParent = targetFile.getParentFile();
        if (!fileParent.exists()) {
            if (!fileParent.mkdirs()) {
                throw new Exception(Constants.FILE_CREATE_DIR_ERROR);
            }
        }
        if (!targetFile.exists()) {
            if (!targetFile.createNewFile()) {
                throw new Exception(Constants.FILE_CREATE_ERROR);
            }
        }
        FileOutputStream fileOutputStream = new FileOutputStream(targetFile);
        byte[] buffer = new byte[1024];
        int len;
        while ((len = fileInputStream.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, len);
            fileOutputStream.flush();
        }
        fileInputStream.close();
        fileOutputStream.close();
        return fileName;
    }

    private void saveKjbSchedule(Task task, List<FileChild> fileChildList)throws Exception{
        for (FileChild fileChild : fileChildList) {
            String type = fileChild.getType();
            if("kjb".equals(type)){
                TaskScheduleVm taskScheduleVm = readKjbXml(fileChild.getFileUrl());
                BeanUtils.copyProperties(taskScheduleVm, task);
                taskDao.save(task);
            }
        }
    }

    private void deleteKjbSchedule(Task task){
        task.setMode(0);
        task.setRepeats("");
        task.setSchedulerType("");
        task.setIntervalMinutes("");
        task.setIntervalSeconds("");
        task.setDayOfHour("");
        task.setDayOfMinutes("");
        task.setWeekDay("");
        task.setDayOfMonth("");
        taskDao.save(task);
    };

    private TaskScheduleVm readKjbXml(String path) throws Exception{
        TaskScheduleVm taskScheduleVm = new TaskScheduleVm();
        SAXReader reader = new SAXReader();
        Document document = reader.read(new File(path));
        Element root = document.getRootElement();
        Element element1 = root.element("entries");
        Iterator<Element> it =element1.elementIterator();
        while(it.hasNext()){
            Element entrie = it.next();
            Element entrieType = entrie.element("type");
            if("SPECIAL".equals(entrieType.getStringValue())){
                String setschedulerType = entrie.element("schedulerType").getStringValue();
                if(!"0".equals(setschedulerType)){
                    taskScheduleVm.setMode(1);
                }else{
                    taskScheduleVm.setMode(2);
                }
                taskScheduleVm.setRepeats(entrie.element("repeat").getStringValue());
                taskScheduleVm.setSchedulerType(setschedulerType);
                taskScheduleVm.setIntervalMinutes(entrie.element("intervalMinutes").getStringValue());
                taskScheduleVm.setIntervalSeconds(entrie.element("intervalSeconds").getStringValue());
                taskScheduleVm.setDayOfHour(entrie.element("hour").getStringValue());
                taskScheduleVm.setDayOfMinutes(entrie.element("minutes").getStringValue());
                taskScheduleVm.setWeekDay(entrie.element("weekDay").getStringValue());
                taskScheduleVm.setDayOfMonth(entrie.element("DayOfMonth").getStringValue());
            }
        }
        return taskScheduleVm;
    }

    private void isContainKjb(List<MultipartFile> files)  throws Exception{
        boolean isVerifi = false;
        for (MultipartFile multipartFile : files) {
            String fileName = multipartFile.getOriginalFilename();
            String type = fileName.substring(fileName.indexOf(".") + 1);
            if("kjb".equals(type)){
                isVerifi = true;
            }
        }
        if(!isVerifi){
            throw new Exception(Constants.IS_CONTAIN_KJB);
        }
    }
}
