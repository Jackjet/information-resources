package d1.project.container.task.service;


import com.alibaba.fastjson.JSONObject;
import d1.project.container.task.utils.*;
import org.apache.commons.io.FileUtils;
import org.pentaho.di.core.KettleEnvironment;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.logging.LogLevel;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobMeta;
import org.pentaho.di.repository.Repository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * 用于管理Kettle作业相关操作的类
 *
 * @author feng.xh
 * @since 1.0.0
 */
@Service
public class KettleService {
    private final JobEntityLogService jobEntityLogService;

    @Value("${d1.project.access.url}")
    private String accessUrl;

    @Value("${d1.project.node.id}")
    private String nodeId;

    @Value("${d1.project.node.ip}")
    private String nodeIp;

    @Value("${server.port}")
    private String nodePort;

    public KettleService(JobEntityLogService jobEntityLogService) {
        this.jobEntityLogService = jobEntityLogService;
    }

    /**
     * 运行kettle程序
     *
     * @param jobId 运行ID
     * @param name  运行job的路径
     */
    @Async("asyncServiceExecutor")
    public void runKettle(String jobId, String name) {
        try {
            //初始化作业
            KettleEnvironment.init();

            //运行作业
            String filePath = Constants.FILES_ROOT + File.separator + jobId + File.separator + name;
            runKjb(jobId, filePath);
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            LogUtils.wirteLogs(jobId, errorMsg);
            insertLog(jobId);
        }
    }

    public void stopJob(String jobId) {
        Job job = JobCache.getInstance().getCacheData(jobId);
        if (job != null) {
            job.stopAll();
            removeCache(jobId);
        }
    }

    public void uploadJob(MultipartFile[] files, String jobId) {
        for (MultipartFile file : files) {
            String fileName = file.getOriginalFilename();
            String fileType = fileName.substring(fileName.indexOf(".") + 1);
            receiveFile(file, fileType, jobId);
        }
    }

    public void deleteJob(String jobId) throws Exception {
        File file = new File(Constants.FILES_ROOT, jobId);
        FileUtils.deleteDirectory(file);
    }


    ////////////////////////////////////////////

    /**
     * 执行Kettle kjb文件，并返回操作结果
     *
     * @param jobId 作业ID
     * @param path  需要执行的kjb文件名称
     * @throws KettleException 失败时弹出的错误信息
     * @see Job
     */
    private void runKjb(String jobId, String path) throws Exception {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (StringUtils.isEmpty(path)) {
            throw new Exception("kettle作业文件不能为空");
        }
        JobMeta jobMeta = new JobMeta(path, null);
        jobMeta.addParameterDefinition("characterEncoding", "utf8", "character encoding");
        /*获取Job类实例*/
        Class<?> clazz = Class.forName("org.pentaho.di.job.Job");
        Constructor constructor = clazz.getDeclaredConstructor(Repository.class, JobMeta.class);

        /*找到内部变量LogChannel的对象log*/
        Field field = clazz.getDeclaredField("log");
        field.setAccessible(true);

        Job job = (Job) constructor.newInstance(null, jobMeta);

        /* job下所有节点的监听点 */
        job.addJobEntryListener(new MyJobEntryListener(jobEntityLogService));
        job.setLogLevel(LogLevel.DETAILED);
        JobCache.getInstance().addCacheData(jobId, job);
        JobIdCache.getInstance().addCacheData(job.getLogChannelId(), jobId);
        job.start();
        job.waitUntilFinished();
        removeCache(jobId);
        if (job.getErrors() > 0) {
            throw new KettleException("job文件执行异常");
        } else {
            job.getLogChannelId();
        }
    }

    private void removeCache(String jobId) {
        JobCache.getInstance().removeCacheData(jobId);
        JobIdCache.getInstance().removeCacheData(jobId);
    }

    private String receiveFile(MultipartFile multipartFile, String type, String id) {
        try {
            File file = new File(Constants.FILES_ROOT, id);
            if (!file.exists()) {
                boolean mdk = file.mkdirs();
                if (!mdk) {
                    throw new Exception("文件创建失败");
                }
            }
            File newFile = new File(file, multipartFile.getOriginalFilename());
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), newFile);
            return newFile.getAbsolutePath();
        } catch (Exception e) {
            String errorMsg = e.getMessage();
            pushKtrLog("null", 0L, 0L, 0L, 0L, 0L, 1L, errorMsg, id, Calendar.getInstance(), Calendar.getInstance());
            return null;
        }
    }


    /**
     * 发送异常数据
     *
     * @param taskId
     */
    private void insertLog(String taskId) {
        JSONObject jobEntityLog = new JSONObject();
        jobEntityLog.put("taskId", taskId);
        jobEntityLog.put("status", 1);
        jobEntityLog.put("channelId", Utils.getUid());
        jobEntityLog.put("groupId", Utils.getUid());
        jobEntityLog.put("stepName", "");
        jobEntityLog.put("createTime", Calendar.getInstance());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(accessUrl + "/webadmin/task/entityLogs/insert", jobEntityLog, String.class);
    }

    private void pushKtrLog(String channelId, long inputNum, long outputNum, long writeNum, long readNum, long updateNum, long errorNum, String log, String jobId, Calendar stTime, Calendar edTime) {
        JSONObject json = new JSONObject();
        json.put("channelId", channelId);
        json.put("inputNum", inputNum);
        json.put("outputNum", outputNum);
        json.put("writeNum", writeNum);
        json.put("readNum", readNum);
        json.put("updateNum", updateNum);
        json.put("errorNum", errorNum);
        json.put("log", log);
        json.put("jobId", jobId);
        json.put("startTime", stTime);
        json.put("endTime", edTime);
        json.put("nodeIp", nodeIp);
        json.put("nodeId", nodeId);
        json.put("nodePort", nodePort);
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(accessUrl + "/webadmin/task/logs/insert", json, String.class);
    }
}

