package d1.project.tangshan.operation.manage.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.AutomatedDeploymentDao;
import d1.project.tangshan.operation.manage.dao.AutomatedDeploymentLogDao;
import d1.project.tangshan.operation.manage.entity.AutomatedDeploymentEntity;
import d1.project.tangshan.operation.manage.entity.AutomatedDeploymentLogEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.List;

/**
 * @author Buter
 * @date 2020/3/16 6:50
 */
@Service
public class AutomatedDeploymentLogService {
    private final AutomatedDeploymentLogDao automatedDeploymentLogDao;
    private final TokenService tokenService;
    final
    AutomatedDeploymentDao automatedDeploymentDao;

    @Autowired
    public AutomatedDeploymentLogService(AutomatedDeploymentLogDao automatedDeploymentLogDao, TokenService tokenService, AutomatedDeploymentDao automatedDeploymentDao) {
        this.automatedDeploymentLogDao = automatedDeploymentLogDao;
        this.tokenService = tokenService;
        this.automatedDeploymentDao = automatedDeploymentDao;
    }

    public Page<AutomatedDeploymentLogEntity> findAll(JSONObject params, HttpServletRequest request) throws Exception {
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SpecificationBuilder<AutomatedDeploymentLogEntity> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("nodeId", "nodeId")
                .sEqual("sysId", "sysId")
                .sEqual("type", "type")
                .tBetween("beginTime", "beginTime", "endTime", dateFormat)
                .dOrder("beginTime").build();

        return this.automatedDeploymentLogDao.findAll(specification, builder.getPageable());
    }

//    @Transactional(rollbackFor = Exception.class)
    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        String id = params.getString("id");
        AutomatedDeploymentLogEntity entity = automatedDeploymentLogDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));

        entity.setEndTime(Calendar.getInstance());
        String result = params.getString("result");
        String resultLog = params.getString("resultLog");
        entity.setResult(result);
        entity.setResultLog(resultLog);
        entity.setUpdateTime(Calendar.getInstance());
        automatedDeploymentLogDao.save(entity);
//        if ("0".equals(result)) {
//            return;
//        }

        AutomatedDeploymentEntity automatedDeploymentEntity = automatedDeploymentDao.findById(entity.getAutomatedDeploymentEntityId()).orElseThrow(() -> new ValidException("找不到记录：" + id));
        automatedDeploymentEntity.setTypeStage("Deployed");
        String versionNumber = automatedDeploymentEntity.getVersionNumber();
        String type = automatedDeploymentEntity.getType();
        String sysId = automatedDeploymentEntity.getSysId();
        if (!"rollback".equals(entity.getType())) {
            if(automatedDeploymentDao.countBySysIdAndType(automatedDeploymentEntity.getSysId(),type)<=1){
                automatedDeploymentEntity.setTypeStage("InitDeployAble");
            }

            /*部署阶段:未部署:NotDeployed,部署中:Deploying,已部署:Deployed,不可部署:NotDeployAble*/
            List<AutomatedDeploymentEntity> byVersionNumberLessThanEqualAndType = automatedDeploymentDao.findByVersionNumberLessThanAndTypeAndSysIdAndTypeStage(versionNumber, type, sysId,"Deployed");
            byVersionNumberLessThanEqualAndType.forEach(automatedDeploymentEntity1 -> {
                        /*不可部署*/
                        automatedDeploymentEntity1.setTypeStage("NotDeployAble");
                    });
            automatedDeploymentDao.saveAll(byVersionNumberLessThanEqualAndType);
            automatedDeploymentDao.save(automatedDeploymentEntity);
            return;
        }

        /*未部署*/
        automatedDeploymentEntity.setTypeStage("NotDeployed");
//        AutomatedDeploymentEntity firstByVersionNumberLessThanAndType = automatedDeploymentDao.findFirstByVersionNumberLessThanAndTypeAndSysId(versionNumber, type, sysId);
//        if (firstByVersionNumberLessThanAndType!=null) {
//            firstByVersionNumberLessThanAndType.setTypeStage("NotDeployAble");
//            automatedDeploymentDao.save(firstByVersionNumberLessThanAndType);
//        }
        automatedDeploymentDao.save(automatedDeploymentEntity);




    }

    public AutomatedDeploymentLogEntity findById(String id) throws Exception {
        return automatedDeploymentLogDao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
    }
    public AutomatedDeploymentLogEntity findByAutomatedDeploymentEntityId(String id) throws Exception {
        return automatedDeploymentLogDao.findFirstByAutomatedDeploymentEntityId(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
    }


    public void save(AutomatedDeploymentLogEntity entity) {
        automatedDeploymentLogDao.save(entity);
    }
}
