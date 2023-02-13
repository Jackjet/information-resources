package d1.project.resourcesharingmgmt.resource.service;

import d1.project.resourcesharingmgmt.resource.dao.DataPushLogDao;
import d1.project.resourcesharingmgmt.resource.entity.DataPushLogEntity;
import org.springframework.stereotype.Service;

/**
 * 数据推送日志
 *
 * @author zhengyang
 */
@Service
public class DataPushLogService {
    private final DataPushLogDao dataPushLogDao;

    public DataPushLogService(DataPushLogDao dataPushLogDao) {
        this.dataPushLogDao = dataPushLogDao;
    }

    public void save(DataPushLogEntity entity){
        dataPushLogDao.save(entity);
    }

    public long countSum(){
        return dataPushLogDao.countSum();
    }

    public long countByDay(String day){
        return dataPushLogDao.countByDay(day);
    }

    public long countUseByOrgId(String orgId){
        return dataPushLogDao.countUseByOrgId(orgId);
    }

    public long countGiveByOrgId(String orgId){
        return dataPushLogDao.countGiveByOrgId(orgId);
    }
}
