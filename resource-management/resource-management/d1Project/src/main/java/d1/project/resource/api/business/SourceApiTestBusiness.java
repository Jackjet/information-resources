package d1.project.resource.api.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.resource.api.entity.SourceApiTestCase;
import d1.project.resource.api.entity.SourceApiTestRecord;
import d1.project.resource.api.model.InsertTestRecord;
import d1.project.resource.api.model.SourceApiTestCaseGroup;
import d1.project.resource.api.model.SourceApiTestRecordGroup;
import d1.project.resource.api.service.SourceApiTestCaseService;
import d1.project.resource.api.service.SourceApiTestRecordService;
import d1.project.resource.api.service.TokenManager;
import d1.project.resource.common.model.OperationLog;
import d1.project.resource.common.service.IOperationLogService;
import d1.project.resource.common.utils.BaseUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author baozh
 */
@Service
public class SourceApiTestBusiness {
    private final SourceApiTestRecordService sourceApiTestRecordService;
    private final SourceApiTestCaseService sourceApiTestCaseService;
    private final IOperationLogService iOperationLogService;

    public SourceApiTestBusiness(SourceApiTestRecordService sourceApiTestRecordService, SourceApiTestCaseService sourceApiTestCaseService, IOperationLogService iOperationLogService) {
        this.sourceApiTestRecordService = sourceApiTestRecordService;
        this.sourceApiTestCaseService = sourceApiTestCaseService;
        this.iOperationLogService = iOperationLogService;
    }

    public void insert(HttpServletRequest request, JSONObject params) throws DoValidException {
        SourceApiTestRecord sourceApiTestRecord = JSON.toJavaObject(params, SourceApiTestRecord.class);
        sourceApiTestRecord.setId(BaseUtils.generate32Id());
        //?????????????????????????????????
        sourceApiTestRecord.setRequestDay(BaseUtils.timeFormat(sourceApiTestRecord.getRequestTime().getTime()));
        this.sourceApiTestRecordService.save(sourceApiTestRecord);
        iOperationLogService.insert(new OperationLog("???API??????-???API??????", "??????", "?????????API", JSON.toJSONString(sourceApiTestRecord), 1), request);
    }

    /**
     * ??????????????????
     *
     * @param str ????????????
     */
    public void insertTestResult(InsertTestRecord str, HttpServletRequest request) throws Exception {
        SourceApiTestRecord apiTestLog = new SourceApiTestRecord();
        Calendar date = Calendar.getInstance();
        apiTestLog.setSourceApiId(str.getApiId());
        apiTestLog.setRequestTime(date);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String requestDay = format.format(date.getTime());
        apiTestLog.setRequestDay(requestDay);
        String resultStr = str.getResultStr();
        JSONObject json = JSONObject.parseObject(resultStr);
        apiTestLog.setResponseCode(json.getString("status"));
        apiTestLog.setRequestContent(str.getRequestStr());
        apiTestLog.setResponseTime(str.getTime());
        apiTestLog.setResponseContent(json.toJSONString());
        apiTestLog.setMethod(str.getMethod());
        apiTestLog.setId(BaseUtils.generate32Id());
        sourceApiTestRecordService.save(apiTestLog);
        iOperationLogService.insert(new OperationLog("API??????", "??????????????????", "??????????????????", "??????????????????", 1), request);
    }

    public List<SourceApiTestRecordGroup> findAllRecordGroup(JSONObject params) throws DoValidException {
        List<SourceApiTestRecord> sourceApiTestRecord = this.sourceApiTestRecordService.findAllBySourceApiId(params.getString("sourceApiId"));
        Map<String, List<SourceApiTestRecord>> groups = sourceApiTestRecord.stream().collect(Collectors.groupingBy(SourceApiTestRecord::getRequestDay, LinkedHashMap::new, Collectors.toList()));
        Integer id = 0;
        List<SourceApiTestRecordGroup> sourceApiTestRecordGroups = new Vector<>();
        for (Map.Entry<String, List<SourceApiTestRecord>> entry : groups.entrySet()) {
            SourceApiTestRecordGroup sourceApiTestRecordGroup = new SourceApiTestRecordGroup();
            sourceApiTestRecordGroup.setId(id);
            sourceApiTestRecordGroup.setRequestDay(entry.getKey());
            sourceApiTestRecordGroup.setSourceApiTestRecords(entry.getValue());
            sourceApiTestRecordGroups.add(sourceApiTestRecordGroup);
            id++;
        }
        return sourceApiTestRecordGroups;
    }

    public void delete(HttpServletRequest request, String id) throws DoValidException {
        SourceApiTestRecord sourceApiTestRecord = sourceApiTestRecordService.findById(id);
        sourceApiTestRecordService.delete(sourceApiTestRecord);
        iOperationLogService.insert(new OperationLog("???API??????-???API??????-??????", "??????????????????", "??????????????????", JSON.toJSONString(sourceApiTestRecord), 1), request);
    }

    public List<SourceApiTestCaseGroup> findAllTestCaseGroup(String sourceApiId) {
        List<SourceApiTestCase> sourceApiTestCases = this.sourceApiTestCaseService.findAllBySourceApiId(sourceApiId);
        Map<String, List<SourceApiTestCase>> groups = sourceApiTestCases.stream().collect(Collectors.groupingBy(SourceApiTestCase::getCreateDay, LinkedHashMap::new, Collectors.toList()));
        Integer id = 0;
        List<SourceApiTestCaseGroup> sourceApiTestCaseGroups = new Vector<>();
        for (Map.Entry<String, List<SourceApiTestCase>> entry : groups.entrySet()) {
            SourceApiTestCaseGroup sourceApiTestCaseGroup = new SourceApiTestCaseGroup();
            sourceApiTestCaseGroup.setId(id);
            sourceApiTestCaseGroup.setCreateDay(entry.getKey());
            sourceApiTestCaseGroup.setSourceApiTestCases(entry.getValue());
            sourceApiTestCaseGroups.add(sourceApiTestCaseGroup);
            id++;
        }
        return sourceApiTestCaseGroups;
    }

    /**
     * ??????????????????
     *
     * @param inputStream ?????????
     * @throws IOException ????????????????????????
     */
    public void importTestCase(HttpServletRequest request, InputStream inputStream, String method, String sourceApiId) throws IOException, DoValidException {
        String content = new BufferedReader(new InputStreamReader(inputStream)).lines().collect(Collectors.joining("\n"));
        inputStream.close();
        JSONObject json = JSONObject.parseObject(content);
        SourceApiTestCase sourceApiTestCase = new SourceApiTestCase();
        sourceApiTestCase.setId(BaseUtils.generate32Id());
        sourceApiTestCase.setIp(json.getString("ip"));
        sourceApiTestCase.setContent(json.getJSONObject("content").toJSONString());
        sourceApiTestCase.setMethod(method);
        sourceApiTestCase.setSaveResult(0);
        sourceApiTestCase.setSourceApiId(sourceApiId);
        TokenManager.updateCreateIdAndTime(request, sourceApiTestCase);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        sourceApiTestCase.setCreateDay(format.format(sourceApiTestCase.getCreateTime().getTime()));
        sourceApiTestCaseService.save(sourceApiTestCase);
        iOperationLogService.insert(new OperationLog("API??????-API??????-??????", "??????????????????", "??????????????????", JSON.toJSONString(sourceApiTestCase), 1), request);
    }

    /**
     * ??????????????????
     *
     * @param request ????????????
     * @param params  ??????
     * @throws DoValidException ????????????
     */
    public void addTestCase(HttpServletRequest request, JSONObject params) throws DoValidException {
        SourceApiTestCase sourceApiTestCase = JSON.toJavaObject(params, SourceApiTestCase.class);
        if (StringUtils.isEmpty(sourceApiTestCase.getId())) {
            sourceApiTestCase.setId(BaseUtils.generate32Id());
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            TokenManager.updateCreateIdAndTime(request, sourceApiTestCase);
            sourceApiTestCase.setCreateDay(format.format(sourceApiTestCase.getCreateTime().getTime()));
        } else {
            SourceApiTestCase data = sourceApiTestCaseService.findById(sourceApiTestCase.getId());
            data.setIp(sourceApiTestCase.getIp());
            data.setSaveResult(sourceApiTestCase.getSaveResult());
            data.setMethod(sourceApiTestCase.getMethod());
            data.setContent(sourceApiTestCase.getContent());
            sourceApiTestCase = data;
        }
        sourceApiTestCaseService.save(sourceApiTestCase);
        iOperationLogService.insert(new OperationLog("API??????-API??????-??????", "??????????????????", "??????????????????", JSON.toJSONString(sourceApiTestCase), 1), request);
    }


    public void deleteTestCase(HttpServletRequest request, String id) throws DoValidException {
        SourceApiTestCase sourceApiTestCase = sourceApiTestCaseService.findById(id);
        sourceApiTestCaseService.delete(sourceApiTestCase);
        iOperationLogService.insert(new OperationLog("API??????-API??????-??????", "??????????????????", "??????????????????", JSON.toJSONString(sourceApiTestCase), 1), request);
    }

    /**
     * ??????????????????
     *
     * @param id       ????????????ID
     * @param response ????????????
     * @param request  ????????????
     * @throws Exception ??????????????????
     */
    public void downloadTestCase(String id, HttpServletResponse response, HttpServletRequest request) throws Exception {
        SourceApiTestCase sourceApiTestCase = this.sourceApiTestCaseService.findById(id);
        JSONObject data = new JSONObject();
        data.put("apiName", "");
        data.put("ip", sourceApiTestCase.getIp());
        data.put("method", sourceApiTestCase.getMethod());
        data.put("content", JSONObject.parseObject(sourceApiTestCase.getContent()));
        String content = data.toJSONString();
        BaseUtils.exportText(content, response);
        iOperationLogService.insert(new OperationLog("???API??????-???API??????-??????", "??????????????????", "??????????????????", JSON.toJSONString(sourceApiTestCase), 1), request);
    }
}
