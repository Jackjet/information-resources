package d1.project.dcrun.center.webapi.common.service.keydict;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author buter
 */
public interface KeyDictDao extends JpaRepository<KeyDictEntity, String>, JpaSpecificationExecutor<KeyDictEntity> {
    /**
     * 根据key查询value
     *
     * @param key 目前只支持appid
     * @return 对应的value值
     */
    Optional<KeyDictEntity> findByKey(String key);
}
