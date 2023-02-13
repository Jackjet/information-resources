package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.DownloadInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * 下载中心
 *
 * @author zhengyang
 */
public interface DownloadInfoDao extends JpaRepository<DownloadInfoEntity, String>, JpaSpecificationExecutor<DownloadInfoEntity> {

}
