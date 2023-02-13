package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.AssetExLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * 资源挂接审批日志表
 *
 * @author zhengyang
 */
public interface AssetExLogDao extends JpaRepository<AssetExLogEntity, String>, JpaSpecificationExecutor<AssetExLogEntity> {
}
