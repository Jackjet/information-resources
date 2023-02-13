package d1.project.resource.message.dao;

import d1.project.resource.message.entity.AnnouncementEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * MY_JAR_NAME
 *
 * @author kikki
 * @date 2020-11-19 16:04
 */
public interface AnnouncementDao extends JpaRepository<AnnouncementEntity, String>, JpaSpecificationExecutor<AnnouncementEntity> {
}
