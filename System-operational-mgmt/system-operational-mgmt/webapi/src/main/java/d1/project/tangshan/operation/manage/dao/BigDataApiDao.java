package d1.project.tangshan.operation.manage.dao;

import d1.project.tangshan.operation.manage.entity.BigDataApiEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Buter
 */
public interface BigDataApiDao extends JpaRepository<BigDataApiEntity, String>, JpaSpecificationExecutor<BigDataApiEntity> {
    Boolean existsByName(String name);

    @Query(value="select count(*) from d1_big_data_api where type = ?1 and url_or_ip = ?2 and method_type = ?3", nativeQuery = true)
    Long existsByTypeAndUrlOrIpAndMethodType(String type,String url,String methodType);
}
