package d1.project.dcrun.center.webapi.common.service.operation.record;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.dcrun.center.webapi.common.service.system.SysService;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import d1.project.dcrun.center.webapi.common.util.OperationType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author maoyy
 */
@Service
public class OperationRecordService {

    private final OperationRecordDao operationRecordDao;

    @Autowired
    public OperationRecordService(OperationRecordDao operationRecordDao) {
        this.operationRecordDao = operationRecordDao;
    }


    /**
     * 查询系统服务操作日志
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Page<OperationRecord> findAllByAppId(JSONObject params) throws Exception {
        SpecificationBuilder<OperationRecord> builder = new SpecificationBuilder<>();
        Specification specification;

        // 脚本运行服务的类型为"webapi,cmd"，需特殊处理
        String sysServiceType = params.getString("sysServiceType");
        if (sysServiceType.contains(",")) {
            JSONArray array = new JSONArray();
            for (String str : sysServiceType.split(",")) {
                array.add(str);
            }
            params.put("sysServiceType", array);
            specification = builder.init(params)
                    .sEqual("appid", "appid")
                    .sEqual("sysServiceName", "sysServiceName")
                    .sIn("sysServiceType", "sysServiceType")
                    .sEqual("type", "type")
                    .dOrder("startTime").build();
        } else {
            specification = builder.init(params)
                    .sEqual("appid", "appid")
                    .sEqual("sysServiceName", "sysServiceName")
                    .sEqual("sysServiceType", "sysServiceType")
                    .sEqual("type", "type")
                    .dOrder("startTime").build();
        }
        Page<OperationRecord> page = operationRecordDao.findAll(specification, builder.getPageable());
        // 将操作类型的英文名改成中文名
        for (OperationRecord record : page.getContent()) {
            record.setType(OperationType.getChineseName(record.getType()));
        }
        return page;
    }

    /**
     * 增加服务操作记录
     *
     * @param sysService 系统服务
     * @param type       操作类型
     * @throws Exception
     */
    public String insert(SysService sysService, OperationType type) throws Exception {
        OperationRecord operationRecord = new OperationRecord();

        String id = MyUtils.generatePrimaryKeyId();
        operationRecord.setId(id);
        operationRecord.setAppid(sysService.getId());
        operationRecord.setSysServiceName(sysService.getName());
        operationRecord.setSysServiceType(sysService.getTemplateType());
        operationRecord.setType(type.getName());
        operationRecord.setStartTime(null);
        operationRecord.setEndTime(null);
        operationRecordDao.save(operationRecord);
        return id;
    }

    public OperationRecord findById(String id) {
        Optional<OperationRecord> operationRecordOptional = operationRecordDao.findById(id);
        return operationRecordOptional.orElse(null);
    }

    public void update(OperationRecord operationRecord) {
        operationRecordDao.save(operationRecord);
    }
}
