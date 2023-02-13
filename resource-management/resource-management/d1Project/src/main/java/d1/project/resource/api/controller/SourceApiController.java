package d1.project.resource.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resource.api.business.SourceApiBusiness;
import d1.project.resource.api.entity.SourceApi;
import d1.project.resource.api.model.DesignInfo;
import d1.project.resource.api.model.SourceApiFindAllGetVm;
import d1.project.resource.api.model.SourceApiView;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author baozh
 */
//@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/sourceApi")
public class SourceApiController {
    private final SourceApiBusiness sourceApiBusiness;

    public SourceApiController(SourceApiBusiness sourceApiBusiness) {
        this.sourceApiBusiness = sourceApiBusiness;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<SourceApiView>> findAll(SourceApiFindAllGetVm params) throws Exception {
        Page<SourceApiView> result = this.sourceApiBusiness.findAll((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("查询成功", result);
    }

    @PostMapping(value = "/add")
    public Result<String> add(HttpServletRequest request, @RequestBody SourceApiAddPostVm params) throws Exception {
        sourceApiBusiness.existsByName(params.getName(), null);
        if (StringUtils.isEmpty(params.getPath()) || params.getPath().charAt(0) != '/') {
            throw new DoValidException("Path必须以“/”开头");
        }
        JSONObject jsonObject = (JSONObject) JSON.toJSON(params);
        sourceApiBusiness.insert(request, jsonObject);
        return ResultUtil.success("添加成功");
    }

    @PutMapping(value = "/update")
    public Result<String> update(HttpServletRequest request, @RequestBody SourceApiUpdatePutVm params) throws Exception {
        SourceApi sourceApi = sourceApiBusiness.findById(params.getId());
        BeanUtils.copyProperties(params, sourceApi);
        sourceApiBusiness.update(request, sourceApi);
        return ResultUtil.success("修改成功");
    }

    @GetMapping(value = "/findById")
    public Result<SourceApiView> findById(String id) throws Exception {
        SourceApiView result = sourceApiBusiness.findSourceApiViewById(id);
        return ResultUtil.success("查询成功", result);
    }

    @DeleteMapping(value = "/deleteById")
    public Result<String> deleteById(HttpServletRequest request, String id) throws Exception {
        try {
            sourceApiBusiness.deleteById(request, id);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail("删除失败" + e.getMessage());
        }
    }

    @PostMapping(value = "/importFile")
    public Result<String> importFile(@RequestParam("file") MultipartFile file) throws Exception {
        String result = sourceApiBusiness.importFile(file.getInputStream());
        return ResultUtil.success("导入成功", result);
    }

    @PostMapping(value = "/import")
    public Result<String> importSourceApi(HttpServletRequest request, @RequestParam("file") MultipartFile file, String groupId) throws Exception {
        sourceApiBusiness.importSourceApi(request, file.getInputStream(), groupId);
        return ResultUtil.success("导入成功");
    }

    @PostMapping(value = "/export")
    public ResponseEntity<Resource> export(HttpServletRequest request, SourceApiExportPostVm param) throws Exception {
        return sourceApiBusiness.export(request, (JSONObject) JSON.toJSON(param));
    }

    @GetMapping(value = "/findForOtherSys")
    public Result<Page<SourceApi>> findForOtherSys(SourceApiFindAllGetVm params) throws Exception {
        Page<SourceApi> result = this.sourceApiBusiness.findForOtherSys((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("查询成功", result);
    }

    @PostMapping(value = "/apiDesignInsert")
    public Result<Page<SourceApi>> apiDesignInsert(@RequestBody DesignInsertVm insertVm, HttpServletRequest request) throws DoValidException {
        DesignInfo designInfo = insertVm.getDesignInfo();
        List<String> hostInfo = insertVm.getHostInfo();
        sourceApiBusiness.apiDesignInsert(designInfo, hostInfo, request);
        return ResultUtil.success("成功");
    }

}

class SourceApiAddPostVm {
    /**
     * 名称
     */
    private String name;
    /**
     * Host
     */
    private String host;
    /**
     * 是否支持负载均衡
     */
    private Boolean loadBalancing;
    /**
     * Path
     */
    private String path;
    /**
     * 请求协议
     */
    private String protocol;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 内容格式类型
     */
    private String formatType;
    /**
     * body
     */
    private String body;
    /**
     * 入参内容
     */
    private String params;
    /**
     * 常量
     */
    private String constants;
    /**
     * 返回结果
     */
    private String response;
    /**
     * 标签信息
     */
    private String tagName;

    private String tagValue;
    /**
     * 分组ID
     */
    private String groupId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Boolean getLoadBalancing() {
        return loadBalancing;
    }

    public void setLoadBalancing(Boolean loadBalancing) {
        this.loadBalancing = loadBalancing;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getConstants() {
        return constants;
    }

    public void setConstants(String constants) {
        this.constants = constants;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}

class SourceApiUpdatePutVm {
    private String id;
    /**
     * 名称
     */
    private String name;
    /**
     * Host
     */
    private String host;
    /**
     * 是否支持负载均衡
     */
    private Boolean loadBalancing;
    /**
     * Path
     */
    private String path;
    /**
     * 请求协议
     */
    private String protocol;
    /**
     * 请求方式
     */
    private String method;
    /**
     * 是否支持自定义内容
     */
    private Boolean isUserDefined;
    /**
     * 内容大小限定
     */
    private Double sizeLimit;
    /**
     * 内容格式类型
     */
    private String formatType;
    /**
     * body
     */
    private String body;
    /**
     * 入参内容
     */
    private String params;
    /**
     * 常量
     */
    private String constants;
    /**
     * 返回结果
     */
    private String response;
    /**
     * 标签信息
     */
    private String tagName;
    /**
     * 标签信息
     */
    private String tagValue;
    /**
     * 分组ID
     */
    private String groupId;

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

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Boolean getLoadBalancing() {
        return loadBalancing;
    }

    public void setLoadBalancing(Boolean loadBalancing) {
        this.loadBalancing = loadBalancing;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Boolean getUserDefined() {
        return isUserDefined;
    }

    public void setUserDefined(Boolean userDefined) {
        isUserDefined = userDefined;
    }

    public Double getSizeLimit() {
        return sizeLimit;
    }

    public void setSizeLimit(Double sizeLimit) {
        this.sizeLimit = sizeLimit;
    }

    public String getFormatType() {
        return formatType;
    }

    public void setFormatType(String formatType) {
        this.formatType = formatType;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getConstants() {
        return constants;
    }

    public void setConstants(String constants) {
        this.constants = constants;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}

class SourceApiExportPostVm {
    private String name;

    private String tagName;

    private String tagValue;

    private String groupId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getTagName() {
        return tagName;
    }

    public void setTagName(String tagName) {
        this.tagName = tagName;
    }

    public String getTagValue() {
        return tagValue;
    }

    public void setTagValue(String tagValue) {
        this.tagValue = tagValue;
    }
}

class DesignInsertVm {
    private DesignInfo designInfo;
    private List<String> hostInfo;

    public DesignInfo getDesignInfo() {
        return designInfo;
    }

    public void setDesignInfo(DesignInfo designInfo) {
        this.designInfo = designInfo;
    }

    public List<String> getHostInfo() {
        return hostInfo;
    }

    public void setHostInfo(List<String> hostInfo) {
        this.hostInfo = hostInfo;
    }
}