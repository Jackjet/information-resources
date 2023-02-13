package d1.project.tangshan.operation.manage.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.BaseEnvironmentMonitorDao;
import d1.project.tangshan.operation.manage.entity.BaseEnvironmentMonitorEntity;
import d1.project.tangshan.operation.manage.entity.ResourceMonitorEntity;
import d1.project.tangshan.operation.manage.model.BaseEnvironmentMonitorVm;
import d1.project.tangshan.operation.manage.model.SystemMonitorEntityVm;
import d1.project.tangshan.operation.manage.service.operations.module.NodeService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import d1.project.tangshan.operation.manage.utils.StatusType;
import net.dcrun.component.email.IEmailService;
import net.dcrun.component.sms.ucpass.ISmsUcpassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Buter
 * @date 2020/3/16 6:50
 */
@Service
public class BaseEnvironmentMonitorService {
    private final BaseEnvironmentMonitorDao baseEnvironmentMonitorDao;
    private final TokenService tokenService;
    private final ResourceMonitorService resourceMonitorService;
    private final NodeService nodeService;
    private final ISmsUcpassService smsUcpassService;
    private final IEmailService emailService;
    private final BaseEnvironmentMonitorAlarmService baseEnvironmentMonitorAlarmService;

    @Autowired
    public BaseEnvironmentMonitorService(BaseEnvironmentMonitorDao baseEnvironmentMonitorDao, TokenService tokenService, ResourceMonitorService resourceMonitorService, NodeService nodeService, ISmsUcpassService smsUcpassService, IEmailService emailService, BaseEnvironmentMonitorAlarmService baseEnvironmentMonitorAlarmService) {
        this.baseEnvironmentMonitorDao = baseEnvironmentMonitorDao;
        this.tokenService = tokenService;
        this.resourceMonitorService = resourceMonitorService;
        this.nodeService = nodeService;
        this.smsUcpassService = smsUcpassService;
        this.emailService = emailService;
        this.baseEnvironmentMonitorAlarmService = baseEnvironmentMonitorAlarmService;
    }

    public Page<BaseEnvironmentMonitorVm> findAll(JSONObject params) throws Exception {
        String nodeName = "";
        String beginTime = "";
        String endTime = "";
        if (!StringUtils.isEmpty(params.getString("nodeName"))) {
            nodeName = params.getString("nodeName");
        }
        if (!StringUtils.isEmpty(params.getString("beginTime"))) {
            beginTime = params.getString("beginTime");
        }
        if (!StringUtils.isEmpty(params.getString("endTime"))) {
            endTime = params.getString("endTime");
        }
        Integer page = params.getInteger("page");
        Integer size = params.getInteger("size");
        Pageable pageable = MyUtils.getPageable(page, size, null);

        Page<Map<String, Object>> result = this.baseEnvironmentMonitorDao.findBaseEnvironmentMonitorEntityBy(nodeName, beginTime, endTime, pageable);
        Page<BaseEnvironmentMonitorVm> entities = MyUtils.listEntity2Model(result, BaseEnvironmentMonitorVm.class);
        return entities;
    }

    public void insert(JSONObject baseEnvironmentMonitor) throws Exception {
        BaseEnvironmentMonitorEntity baseEnvironmentMonitorEntity = MyUtils.model2Entity(baseEnvironmentMonitor, BaseEnvironmentMonitorEntity.class);
        baseEnvironmentMonitorEntity.setId(MyUtils.generate32Id());
        baseEnvironmentMonitorEntity.setCreateTime(Calendar.getInstance());
        baseEnvironmentMonitorEntity.setLogTime(Calendar.getInstance());

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        JSONObject params = new JSONObject();
        params.put("intranetIp", baseEnvironmentMonitorEntity.getIntranetIp());

        Date endTime = Calendar.getInstance().getTime();
        String endTimeStr = format.format(endTime);

        //10分钟
        long time = 10 * 60 * 1000;
        Date startTime = new Date(endTime.getTime() - time);
        String startTimeStr = format.format(startTime);
        params.put("startTime", startTimeStr);
        params.put("endTime", endTimeStr);
        List<ResourceMonitorEntity> data = resourceMonitorService.findByNodeIdAndCreateTime(params);
        baseEnvironmentMonitorEntity.setStatus(StatusType.ABNORMAL.getName());
        baseEnvironmentMonitorEntity.setNetwork(StatusType.ABNORMAL.getName());
        if (data.size() > 0) {
            baseEnvironmentMonitorEntity.setStatus(StatusType.NORMAL.getName());
            baseEnvironmentMonitorEntity.setNetwork(StatusType.NORMAL.getName());
        }
        baseEnvironmentMonitorDao.save(baseEnvironmentMonitorEntity);
    }
}
