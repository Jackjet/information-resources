package d1.project.api.integration.application.controller.webadmin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.api.integration.application.business.ApplicationBusiness;
import d1.project.api.integration.application.entity.Application;
import d1.project.api.integration.application.entity.NormalConsumer;
import d1.project.api.integration.application.model.ApplicationView;
import d1.project.api.integration.application.service.NormalConsumerService;
import d1.project.api.integration.common.annotation.ApiAuth;
import d1.project.api.integration.common.utils.BaseUtils;
import d1.project.api.integration.kong.business.KongBusiness;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;

@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/application")
public class ApplicationController {
    private final KongBusiness kongBusiness;
    private final ApplicationBusiness applicationBusiness;

    public ApplicationController(KongBusiness kongBusiness, ApplicationBusiness applicationBusiness) {
        this.kongBusiness = kongBusiness;
        this.applicationBusiness = applicationBusiness;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<ApplicationView>> findAll(ApplicationFindAllGetVm params) throws Exception {
        Page<ApplicationView> result = this.applicationBusiness.findAll((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("查询成功", result);
    }

    @PostMapping(value = "/add")
    @Transactional(rollbackOn = Exception.class)
    public Result<String> add(HttpServletRequest request, @RequestBody ApplicationAddPostVm params) throws Exception {
        this.applicationBusiness.existByName(params.getName(), null);
        Application application = this.applicationBusiness.insert(request, (JSONObject) JSON.toJSON(params));

        //向kong添加consumer
        NormalConsumer consumer = this.applicationBusiness.initConsumer(params.getContainer(),application);
        consumer.setId(BaseUtils.generate32Id());
        this.kongBusiness.addConsumer(params.getContainer(), consumer);

        this.applicationBusiness.insertNormalConsumer(consumer);
        return ResultUtil.success("添加成功");
    }

    @GetMapping(value = "/findById")
    public Result<Application> findById(String id) throws Exception {
        Application result = this.applicationBusiness.findById(id);
        return ResultUtil.success("查询成功", result);
    }

    @PutMapping(value = "/update")
    public Result<String> update(HttpServletRequest request, @RequestBody ApplicationUpdatePutVm params) throws Exception {
        this.applicationBusiness.existByName(params.getName(), params.getId());
        this.applicationBusiness.update(request, (JSONObject) JSON.toJSON(params));
        return ResultUtil.success("修改成功");
    }

    @PutMapping(value = "/updateKey")
    public Result<String> updateKey(HttpServletRequest request, @RequestBody ApplicationUpdateKeyPutVm params) throws Exception {
        //检查应用信息是否存在
        Application application = this.applicationBusiness.findById(params.getId());
        if(application == null){
            throw new DoValidException("应用信息不存在");
        }

        NormalConsumer consumer = this.applicationBusiness.updateKey(application);

        this.kongBusiness.updateKey(application.getContainer(), consumer);
        return ResultUtil.success("更新成功");
    }

    @DeleteMapping(value = "/deleteById")
    @Transactional(rollbackOn = Exception.class)
    public Result<String> deleteById(HttpServletRequest request, String id) throws Exception {
        //检查应用信息是否存在
        Application application = this.applicationBusiness.findById(id);
        if(application == null){
            throw new DoValidException("应用信息不存在");
        }

        NormalConsumer consumer = applicationBusiness.findNormalConsumerByAppid(id);

        //删除应用信息
        this.applicationBusiness.delete(request, application,consumer);

        kongBusiness.deleteConsumer(application.getContainer(), consumer);
        return ResultUtil.success("删除成功");
    }

    @PostMapping(value = "/export")
    public ResponseEntity<Resource> export(HttpServletRequest request, ApplicationExportPostVm params) throws Exception {
        ResponseEntity<Resource> result = this.applicationBusiness.export(request, (JSONObject) JSON.toJSON(params));
        return result;
    }
}

class ApplicationFindAllGetVm extends PageableVm {
    /**
     * 应用名称
     */
    private String name;
    private String container;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }
}

class ApplicationExportPostVm {
    /**
     * 应用名称
     */
    private String name;
    private String container;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }
}

class ApplicationAddPostVm {
    private String container;
    /**
     * 应用名称
     */
    private String name;

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class ApplicationUpdatePutVm {
    private String id;
    /**
     * 应用名称
     */
    private String name;

    /**
     * 容器
     */
    private String container;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }
}

class ApplicationUpdateKeyPutVm {
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}