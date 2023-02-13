package d1.project.tangshan.operation.manage.controller.webadmin.bigdata;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.tangshan.operation.manage.entity.BigDataApiEntity;
import d1.project.tangshan.operation.manage.service.BigDataApiService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author chenghan
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/dataApi")
@Api(value = "/webadmin/dataApi", tags = "数据服务接口管理")
public class BigDataApiController {

    private final BigDataApiService bigDataApiService;

    @Autowired
    public BigDataApiController(BigDataApiService bigDataApiService) {
        this.bigDataApiService = bigDataApiService;
    }

    @ApiOperation(value = "查询", notes = "查询所有数据")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<BigDataApiEntity>> findAll(HttpServletRequest request, @Valid BigDataFindAllVm model) {
        try {
            Page<BigDataApiEntity> result = bigDataApiService.findAll((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("查询成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("查询失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<BigDataApiEntity> findById(String id) {
        try {
            return ResultUtil.success("成功", bigDataApiService.findById(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "插入", notes = "插入数据")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @Valid @RequestBody BigDataInsertVm model) {
        try {
            this.bigDataApiService.insert((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("添加成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新", notes = "更新数据")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody BigDataUpdateVm model) {
        try {
            this.bigDataApiService.update((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除", notes = "删除数据")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<String> delete(@Valid @RequestBody BigDataDeleteVm model, HttpServletRequest request) {
        try {
            this.bigDataApiService.delete(model.getId(),request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }

    @ApiOperation(value = "测试接口")
    @PostMapping(value = "/testInterface")
    public Result<String> testInterface(@Valid @RequestBody BigDataApiControllerTestInterfaceVm model, HttpServletRequest request) {
        try {
            return ResultUtil.success("测试成功", bigDataApiService.testInterface((JSONObject) JSON.toJSON(model),request));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

class BigDataFindAllVm extends PageableVm {
    @ApiModelProperty(value = "接口名称")
    private String name;
    @ApiModelProperty(value = "接口类型，http/webservice/socket")
    private String type;
    @ApiModelProperty(value = "排除的接口类型")
    private String excludeType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getExcludeType() {
        return excludeType;
    }

    public void setExcludeType(String excludeType) {
        this.excludeType = excludeType;
    }
}

class BigDataInsertVm {
    @NotBlank(message = "接口名称不能为空")
    @ApiModelProperty("接口名称")
    private String name;
    @NotBlank(message = "接口类型不能为空")
    @ApiModelProperty("接口类型，http/webservice/socket")
    private String type;
    @NotBlank(message = "url/Ip不能为空")
    @ApiModelProperty("url/Ip")
    private String urlOrIp;
    @ApiModelProperty("方法类型，get/post/put/delete，当接口类型为socket时，方法类型为空")
    private String methodType;
    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUrlOrIp() {
        return urlOrIp;
    }

    public void setUrlOrIp(String urlOrIp) {
        this.urlOrIp = urlOrIp;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

class BigDataUpdateVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;
    @NotBlank(message = "url/Ip不能为空")
    @ApiModelProperty("url/Ip")
    private String urlOrIp;
    @ApiModelProperty("方法类型，get/post/put/delete，当接口类型为socket时，方法类型为空")
    private String methodType;
    @ApiModelProperty("类型")
    private String type;
    @ApiModelProperty("备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlOrIp() {
        return urlOrIp;
    }

    public void setUrlOrIp(String urlOrIp) {
        this.urlOrIp = urlOrIp;
    }

    public String getMethodType() {
        return methodType;
    }

    public void setMethodType(String methodType) {
        this.methodType = methodType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

class BigDataDeleteVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

@ApiModel(value = "BigDataApiControllerTestInterfaceVm", description = "测试接口")
class BigDataApiControllerTestInterfaceVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;
    @ApiModelProperty("url参数")
    private String urlParameter;
    @ApiModelProperty("发送数据")
    private String body;
    @ApiModelProperty("请求头")
    private String header;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrlParameter() {
        return urlParameter;
    }

    public void setUrlParameter(String urlParameter) {
        this.urlParameter = urlParameter;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }
}

