package d1.project.dcrun.center.webapi.backup.dao;

import d1.project.dcrun.center.webapi.backup.entity.Backup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author xuaa
 * @date 2020-06-28-17:54
 */
public interface BackupDao extends JpaRepository<Backup, String>, JpaSpecificationExecutor<Backup> {
    /**
     *  是否存在相同的名称
     * @param name 名称
     * @return 返回值
     */
    boolean existsByName(String name);
}
