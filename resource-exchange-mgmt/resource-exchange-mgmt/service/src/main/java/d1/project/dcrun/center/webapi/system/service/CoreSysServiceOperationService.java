package d1.project.dcrun.center.webapi.system.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecord;
import d1.project.dcrun.center.webapi.common.service.operation.record.OperationRecordService;
import d1.project.dcrun.center.webapi.common.util.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

/**
 * @author maoyy
 */
@Service
public class CoreSysServiceOperationService {

    @Autowired
    private OperationRecordService operationRecordService;

    public Page<OperationRecord> findAllByAppId(JSONObject params) throws Exception {
        return operationRecordService.findAllByAppId(params);
    }

    public JSONArray findAllOperationType() {
        JSONArray array = new JSONArray();
        for (OperationType operationType : OperationType.values()) {
            JSONObject obj = new JSONObject();
            obj.put("id", operationType.getName());
            obj.put("name", operationType.getChineseName());
            array.add(obj);
        }
        return array;
    }
}
