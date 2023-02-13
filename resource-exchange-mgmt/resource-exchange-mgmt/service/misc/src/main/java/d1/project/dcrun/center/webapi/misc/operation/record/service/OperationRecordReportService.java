package d1.project.dcrun.center.webapi.misc.operation.record.service;

import com.alibaba.fastjson.JSONObject;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecord;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author maoyy
 */
@Service
public class OperationRecordReportService {
    @Autowired
    private OperationRecordService operationRecordService;


    /**
     * 上报服务操作日志
     *
     * @param params
     * @throws Exception
     */
    public void update(JSONObject params) throws Exception {
        String id = params.getString("id");
        String result = params.getString("result");
        Date startTime = new Date(params.getLong("startTime"));
        Date endTime = new Date(params.getLong("endTime"));

        OperationRecord operationRecord = operationRecordService.findById(id);
        if (operationRecord != null) {
            operationRecord.setResult(result);
            operationRecord.setStartTime(startTime);
            operationRecord.setEndTime(endTime);
            operationRecordService.update(operationRecord);
        }
    }
}
