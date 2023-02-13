package d1.project.resourcesharingmgmt.resource.service;

import d1.project.resourcesharingmgmt.resource.dao.FileDownLogDao;
import d1.project.resourcesharingmgmt.resource.entity.FileDownLogEntity;
import org.springframework.stereotype.Service;

/**
 * 文件下载日志
 *
 * @author zhengyang
 */
@Service
public class FileDownLogService {
    private final FileDownLogDao fileDownLogDao;

    public FileDownLogService(FileDownLogDao fileDownLogDao) {
        this.fileDownLogDao = fileDownLogDao;
    }

    public void save(FileDownLogEntity entity){
        fileDownLogDao.save(entity);
    }

    public long count(){
        return fileDownLogDao.count();
    }

    public long countByDay(String day){
        return fileDownLogDao.countByDay(day);
    }

    public long countUseByOrgId(String orgId){
        return fileDownLogDao.countUseByOrgId(orgId);
    }

    public long countGiveByOrgId(String orgId){
        return fileDownLogDao.countGiveByOrgId(orgId);
    }
}
