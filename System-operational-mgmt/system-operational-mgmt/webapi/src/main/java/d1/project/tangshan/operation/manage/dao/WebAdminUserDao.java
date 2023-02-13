package d1.project.tangshan.operation.manage.dao;

import d1.project.tangshan.operation.manage.entity.WebAdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author Buter
 */
public interface WebAdminUserDao extends JpaRepository<WebAdminUser, String>, JpaSpecificationExecutor<WebAdminUser> {

    WebAdminUser findByName(String name);

    boolean existsByName(String name);

    boolean existsByTel(String name);


    List<WebAdminUser> findAllByStatus(String status);

    /**
     * 将所有用户的访问次数重置为0，所有用户的状态设置为正常
     * @return
     */
    @Modifying
    @Query(value = "update d1_web_admin_user set status = '1', access_limit = 0 ",nativeQuery = true)
    int resetUserInfo();
}
