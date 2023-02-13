package d1.project.resource.api.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resource.api.business.SourceApiTestBusiness;
import d1.project.resource.api.model.InsertTestRecord;
import d1.project.resource.api.model.SourceApiTestCaseGroup;
import d1.project.resource.api.model.SourceApiTestRecordGroup;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author baozh
 */
//@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/sourceApiTest")
public class SourceApiTestController {
    private final SourceApiTestBusiness sourceApiTestBusiness;

    public SourceApiTestController(SourceApiTestBusiness sourceApiTestBusiness) {
        this.sourceApiTestBusiness = sourceApiTestBusiness;
    }


    @PostMapping(value = "/insertTestResult")
    public Result<String> insertTestResult(@RequestBody InsertTestRecord str, HttpServletRequest request) {
        try {
            sourceApiTestBusiness.insertTestResult(str, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/findAllRecordGroup")
    public Result<List<SourceApiTestRecordGroup>> findAllRecordGroup(SourceApiTestFindAllGetVm params) throws Exception {
        List<SourceApiTestRecordGroup> result = sourceApiTestBusiness.findAllRecordGroup((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("查询成功", result);
    }

    @GetMapping(value = "/findAllTestCaseGroup")
    public Result<List<SourceApiTestCaseGroup>> findAllTestCaseGroup(String sourceApiId) throws Exception {
        List<SourceApiTestCaseGroup> result = sourceApiTestBusiness.findAllTestCaseGroup(sourceApiId);
        return ResultUtil.success("查询成功", result);
    }

    @PostMapping(value = "/importTestCase")
    public Result<String> importTestCase(HttpServletRequest request, @RequestParam("file") MultipartFile file, @RequestParam("method") String method, @RequestParam("sourceApiId") String sourceApiId) throws IOException, DoValidException {
        sourceApiTestBusiness.importTestCase(request, file.getInputStream(), method, sourceApiId);
        return ResultUtil.success("导入成功");
    }

    @DeleteMapping(value = "/deleteTestCase")
    public Result<String> deleteTestCase(HttpServletRequest request, String id) throws DoValidException {
        sourceApiTestBusiness.deleteTestCase(request, id);
        return ResultUtil.success("删除成功");
    }

//    @GetMapping(value = "/downloadTestCase")
//    public ResponseEntity<Resource> downloadTestCase(HttpServletRequest request, String id) throws Exception {
//        return sourceApiTestBusiness.downloadTestCase(request, id);
//    }

    @GetMapping(value = "/downloadTestCase")
    public Result<String> downloadTestCase(String id, HttpServletResponse response, HttpServletRequest request) {
        try {
            sourceApiTestBusiness.downloadTestCase(id, response, request);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @PostMapping(value = "/addTestCase")
    public Result<String> addTestCase(HttpServletRequest request, @RequestBody AddTestCasePostVm params) throws IOException, DoValidException {
        sourceApiTestBusiness.addTestCase(request, (JSONObject) JSON.toJSON(params));
        return ResultUtil.success("保存成功");
    }

    @DeleteMapping(value = "/sourceApiTestRecordDelete")
    public Result<String> sourceApiTestRecordDelete(HttpServletRequest request, String id) throws DoValidException {
        sourceApiTestBusiness.delete(request, id);
        return ResultUtil.success("删除成功");
    }
}

class SourceApiTestFindAllGetVm {
    /**
     * 源APIId
     */
    private String sourceApiId;

    public String getSourceApiId() {
        return sourceApiId;
    }

    public void setSourceApiId(String sourceApiId) {
        this.sourceApiId = sourceApiId;
    }
}

class AddTestCasePostVm {
    private String id;
    /**
     * 调用测试API的设备IP
     */
    private String ip;
    /**
     * 是否保存测试结果 0 否 1 是
     */
    private Integer saveResult;

    /**
     * 请求方式
     */
    private String method;
    /**
     * 测试用例内容
     */
    private String content;
    /**
     * 源APIId
     */
    private String sourceApiId;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getSaveResult() {
        return saveResult;
    }

    public void setSaveResult(Integer saveResult) {
        this.saveResult = saveResult;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceApiId() {
        return sourceApiId;
    }

    public void setSourceApiId(String sourceApiId) {
        this.sourceApiId = sourceApiId;
    }
}