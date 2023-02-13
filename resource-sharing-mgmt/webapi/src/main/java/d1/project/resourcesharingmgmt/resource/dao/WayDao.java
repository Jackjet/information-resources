package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.WayEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author dy
 */
public interface WayDao extends JpaRepository<WayEntity, String>, JpaSpecificationExecutor<WayEntity> {

    /**
     * 通过type获取所有列表
     *
     * @param type （0代办、1服务指引）
     * @return
     */
    List<WayEntity> findAllByTypeOrderBySeq(String type);

    /**
     * 获取最新way
     *
     * @param type
     * @return
     */
    WayEntity findFirstByTypeOrderBySeqDesc(String type);

    List<WayEntity> findAllByTypeAndSeqLessThanOrderBySeqDesc(String type, Integer seq);

    List<WayEntity> findAllByTypeAndSeqGreaterThanOrderBySeq(String type, Integer seq);

    List<WayEntity>findAllByIdIn(List<String>ids);
}
