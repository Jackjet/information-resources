package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.ArchBusiUviewStrExEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

/**
 * 信息项
 *
 * @author zhengyang
 */
public interface ArchBusiUviewStrExDao extends JpaRepository<ArchBusiUviewStrExEntity, String>, JpaSpecificationExecutor<ArchBusiUviewStrExEntity> {
    /**
     * 根据信息项Id,判断是否存在资源目录信息项
     * @param UviewstrId
     * @return
     */
    boolean existsByUviewstrId(String UviewstrId);

    /**
     * 根据资源目录ID删除信息项
     * @param uviewId
     */
    void deleteByUviewId(String uviewId);
}
