package d1.project.api.integration.application.business;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;
import d1.project.api.integration.application.Constants;
import d1.project.api.integration.application.entity.Application;
import d1.project.api.integration.application.entity.NormalConsumer;
import d1.project.api.integration.application.model.ApplicationView;
import d1.project.api.integration.application.service.ApplicationService;
import d1.project.api.integration.application.service.NormalConsumerService;
import d1.project.api.integration.common.model.OperationLog;
import d1.project.api.integration.common.service.IApiAuthManageService;
import d1.project.api.integration.common.service.IOperationLogService;
import d1.project.api.integration.common.utils.BaseUtils;
import d1.project.api.integration.kong.entity.DefaultConsumer;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Service
public class ApplicationBusiness {
    private final ApplicationService applicationService;
    private final IOperationLogService iOperationLogService;
    private final IApiAuthManageService iApiAuthManageService;
    private final NormalConsumerService normalConsumerService;

    public ApplicationBusiness(ApplicationService applicationService, IOperationLogService iOperationLogService, IApiAuthManageService iApiAuthManageService, NormalConsumerService normalConsumerService) {
        this.applicationService = applicationService;
        this.iOperationLogService = iOperationLogService;
        this.iApiAuthManageService = iApiAuthManageService;
        this.normalConsumerService = normalConsumerService;
    }

    public Page<ApplicationView> findAll(JSONObject params) throws Exception {
        return applicationService.findAll(params);
    }

    public void existByName(String name, String id) throws Exception {
        Boolean isExist;
        if ((StringUtils.isEmpty(id))) {
            isExist = this.applicationService.existByName(name);
        } else {
            isExist = this.applicationService.existByNameAndIdNot(name, id);
        }

        if (isExist) {
            throw new DoValidException("已存在同名应用");
        }
    }

    public Application insert(HttpServletRequest request, JSONObject params) throws Exception {
        Application application = JSON.toJavaObject(params, Application.class);
        application.setId(BaseUtils.generate32Id());
        application.setKey(BaseUtils.generate32Id());

        BaseUtils.setCreateTimeAndCreateById(request,application);

        this.applicationService.save(application);
        iOperationLogService.insert(new OperationLog("应用管理", "添加", "添加应用", JSON.toJSONString(application), 1), request);

        return application;
    }

    public Application findById(String id) throws Exception {
        return applicationService.findById(id);
    }

    public void update(HttpServletRequest request, JSONObject params) throws Exception {
        Application application = JSON.toJavaObject(params, Application.class);
        Application data = this.applicationService.findById(application.getId());

        data.setName(application.getName());
        data.setContainer(application.getContainer());
        this.applicationService.save(data);
        iOperationLogService.insert(new OperationLog("应用管理", "编辑", "编辑应用", JSON.toJSONString(application), 1), request);
    }

    public void updateKey(HttpServletRequest request, String id, String secret) throws Exception {
        Application application = this.applicationService.findById(id);

        application.setKey(secret);

        this.applicationService.save(application);
        iOperationLogService.insert(new OperationLog("应用管理", "更新密钥", "更新密钥", JSON.toJSONString(application), 1), request);
    }

    public void delete(HttpServletRequest request, Application application,NormalConsumer consumer) throws Exception {
        this.applicationService.delete(application);
        this.normalConsumerService.delete(consumer);

        this.iApiAuthManageService.deleteAllByAppId(application.getId());
        iOperationLogService.insert(new OperationLog("应用管理", "删除应用", "删除应用", JSON.toJSONString(application), 1), request);
    }

    public ResponseEntity<Resource> export(HttpServletRequest request, JSONObject params) throws Exception {
        List<ApplicationView> data = this.applicationService.findAllByNameAndContainer(params);

        File dir = new File(Constants.APPLICATION_FILE_ROOT);
        if (!dir.isDirectory() || !dir.exists()) {
            dir.mkdirs();
        }

        String fileName = Constants.APPLICATION_FILE_ROOT + "导出报表.xlsx";
        //检查文件是否存在，存在旧文件则删除
        File file = new File(fileName);
        if (!file.exists()) {
            file.createNewFile();
        }

        try {
            EasyExcel.write(fileName, ApplicationView.class).sheet("Sheet1").doWrite(data);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (file.isFile() && file.exists()) {
            Resource resource = new UrlResource(file.toURI());
            String contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            iOperationLogService.insert(new OperationLog("应用管理", "导出应用", "导出应用", JSON.toJSONString(data), 1), request);
            return ((ResponseEntity.BodyBuilder) ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header("Content-Disposition", new String[]{"attachment; filename=\"" + this.encode(resource.getFilename()) + "\""})).body(resource);
        } else {
            return null;
        }
    }

    public void existsAllByAppId(String appid) throws DoValidException {
        if (this.iApiAuthManageService.existsAllByAppId(appid)) {
            throw new DoValidException("当前应用处于在用状态不可删除，请重新核对数据");
        }
    }

    public NormalConsumer initConsumer(String container,Application application){
        NormalConsumer consumer = new NormalConsumer();
        consumer.setUsername(application.getId());
        consumer.setKey(application.getKey());
        consumer.setContainer(container);
        consumer.setGroups(application.getId());
        consumer.setAppid(application.getId());
        return consumer;
    }

    public void insertNormalConsumer(NormalConsumer normalConsumer){
        normalConsumerService.save(normalConsumer);
    }

    @Transactional(rollbackOn = Exception.class)
    public NormalConsumer updateKey(Application application) throws Exception {
        NormalConsumer consumer = normalConsumerService.findByAppid(application.getId());
        if(consumer == null){
            throw new DoValidException("应用信息不存在");
        }

        String key =BaseUtils.generate32Id();

        application.setKey(key);
        applicationService.save(application);

        consumer.setKey(key);
        normalConsumerService.save(consumer);
        return consumer;
    }

    public NormalConsumer findNormalConsumerByAppid(String appid){
        return normalConsumerService.findByAppid(appid);
    }
    //=================================================

    private String encode(String filename) throws UnsupportedEncodingException {
        return filename == null ? "null" : URLEncoder.encode(FilenameUtils.getBaseName(filename), "UTF-8") + "." + FilenameUtils.getExtension(filename);
    }
}
