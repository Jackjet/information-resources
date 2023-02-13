package d1.project.resource.api.business;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.resource.api.entity.SourceApi;
import d1.project.resource.api.mapper.ApiMapper;
import d1.project.resource.api.model.DesignInfo;
import d1.project.resource.api.model.SourceApiView;
import d1.project.resource.api.service.SourceApiService;
import d1.project.resource.api.service.TokenManager;
import d1.project.resource.common.Constants;
import d1.project.resource.common.model.OperationLog;
import d1.project.resource.common.service.IOperationLogService;
import d1.project.resource.common.utils.BaseUtils;
import org.apache.commons.io.FilenameUtils;
import org.mapstruct.factory.Mappers;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

/**
 * API管理
 *
 * @author baozh
 */
@Service
public class SourceApiBusiness {
    private final SourceApiService sourceApiService;
    private final IOperationLogService iOperationLogService;
    private final ApiMapper mapper;

    public SourceApiBusiness(SourceApiService sourceApiService, IOperationLogService iOperationLogService) {
        this.sourceApiService = sourceApiService;
        this.iOperationLogService = iOperationLogService;
        this.mapper = Mappers.getMapper(ApiMapper.class);
    }

    public SourceApiView findSourceApiViewById(String id) throws DoValidException {
        return sourceApiService.findSourceApiViewById(id);
    }

    public Page<SourceApiView> findAll(JSONObject params) throws Exception {
        return sourceApiService.findAll(params);
    }

    public Page<SourceApi> findForOtherSys(JSONObject params) throws Exception {
        return sourceApiService.findForOtherSys(params);
    }

    public void insert(HttpServletRequest request, JSONObject params) throws Exception {
        this.sourceApiService.insert(request, params);
        iOperationLogService.insert(new OperationLog("源API管理-源API列表", "添加", "添加源API", params.toJSONString(), 1), request);
    }

    public void update(HttpServletRequest request, SourceApi sourceApi) throws Exception {
        sourceApiService.update(request, sourceApi);
        iOperationLogService.insert(new OperationLog("源API管理-源API列表", "编辑", "修改源API", JSON.toJSONString(sourceApi), 1), request);

    }

    public void deleteById(HttpServletRequest request, String id) throws Exception {
        sourceApiService.deleteById(id);
        iOperationLogService.insert(new OperationLog("源API管理-源API列表", "删除", "删除ID为" + id + "的源API", id, 1), request);
    }

    public SourceApi findById(String id) throws Exception {
        return sourceApiService.findById(id);
    }

    public String importFile(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));
    }

    public ResponseEntity<Resource> export(HttpServletRequest request, JSONObject params) throws Exception {
        List<SourceApiView> sourceApiViews = sourceApiService.findAllForExcel(params);

        File dir = new File(Constants.SOURCE_API_FILE_ROOT);
        if (!dir.isDirectory() || !dir.exists()) {
            boolean res = dir.mkdirs();
            if (!res) {
                throw new Exception(Constants.DIR_CREATE_ERROR);
            }
        }

        String fileName = Constants.SOURCE_API_FILE_ROOT + "导出报表.xlsx";
        //检查文件是否存在，存在旧文件则删除
        File file = new File(fileName);
        if (!file.exists()) {
            boolean res = file.createNewFile();
            if (!res) {
                throw new Exception(Constants.FILE_CREATE_ERROR);
            }
        }

        try {
            EasyExcel.write(fileName, SourceApiView.class).sheet("Sheet1").doWrite(sourceApiViews);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (file.isFile() && file.exists()) {
            Resource resource = new UrlResource(file.toURI());
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            iOperationLogService.insert(new OperationLog("源API管理-源API列表", "导出", "导出源API", JSON.toJSONString(sourceApiViews), 1), request);
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header("Content-Disposition", "attachment; filename=\"" + this.encode(resource.getFilename()) + "\"").body(resource);
        } else {
            return null;
        }
    }

    public void importSourceApi(HttpServletRequest request, InputStream inputStream, String groupId) throws Exception {
        String content = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        List<SourceApi> list = new Vector<>();
        boolean blank = false;
        try {
            JSONArray jsonArray = JSON.parseArray(content);
            //生成源API数据
            List<String> names = new Vector<>();
            for (Object obj : jsonArray) {
                JSONObject jsonObject = (JSONObject) obj;
                SourceApi sourceApi = jsonObject.toJavaObject(SourceApi.class);
                if (StringUtils.isEmpty(sourceApi.getName())) {
                    blank = true;
                    continue;
                }
                sourceApi.setId(BaseUtils.generate32Id());
                sourceApi.setGroupId(groupId);
                TokenManager.updateCreateIdAndTime(request, sourceApi);
                TokenManager.updateUpdateIdAndTime(request, sourceApi);
                list.add(sourceApi);
                names.add(sourceApi.getName());
            }

            List<SourceApi> sourceApis = sourceApiService.findAllByNameIn(names);
            if (sourceApis.size() > 0) {
                StringBuilder message = new StringBuilder();
                for (SourceApi sourceApi : sourceApis) {
                    message.append(sourceApi.getName()).append("，");
                }
                message = new StringBuilder(message.substring(0, message.length() - 1));
                message.append("等API已存在");
                throw new DoValidException(message.toString());
            }
        } catch (DoValidException e) {
            throw new DoValidException(e.getMessage());
        } catch (Exception e) {
            throw new DoValidException("文件内容格式不正确");
        }
        if (list.size() > 0) {
            sourceApiService.batchSave(list);
            iOperationLogService.insert(new OperationLog("源API管理-源API列表", "导入", "批量添加源API", JSON.toJSONString(list), 1), request);
        }
        if (blank) {
            throw new Exception(Constants.API_IMPORT_ERROR);
        }
    }


    public void existsByName(String name, String id) throws DoValidException {
        Boolean isExist;
        if ((StringUtils.isEmpty(id))) {
            isExist = sourceApiService.existsByName(name);
        } else {
            isExist = sourceApiService.existsByNameAndIdNot(name, id);
        }

        if (isExist) {
            throw new DoValidException("已存在同名源API");
        }
    }

    public void apiDesignInsert(DesignInfo designInfo, List<String> hostInfo, HttpServletRequest request) throws DoValidException {
        String id = designInfo.getId();
        String name = designInfo.getName();
        if (sourceApiService.existsByNameAndIdNot(name, id)) {
            throw new DoValidException(Constants.DATA_NAME_REPEAT);
        }
        SourceApi sourceApi = mapper.dataSourceInsert(designInfo);
        sourceApi.setLoadBalancing(hostInfo.size() > 1);
        JSONArray array = new JSONArray();
        for (String s : hostInfo) {
            JSONObject json = new JSONObject();
            json.put("target", s);
            array.add(json);
        }
        sourceApi.setHost(array.toJSONString());
        sourceApiService.insertDesign(request, sourceApi);
    }


    private String encode(String filename) throws UnsupportedEncodingException {
        return filename == null ? "null" : URLEncoder.encode(FilenameUtils.getBaseName(filename), "UTF-8") + "." + FilenameUtils.getExtension(filename);
    }
}
