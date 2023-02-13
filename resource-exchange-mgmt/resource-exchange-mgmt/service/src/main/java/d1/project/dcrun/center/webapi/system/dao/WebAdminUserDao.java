package d1.project.dcrun.center.webapi.system.dao;

import d1.project.dcrun.center.webapi.system.entity.WebAdminUser;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * @author chengh
 */
public interface WebAdminUserDao extends JpaRepository<WebAdminUser, String> {

    /**
     * 根据name查询用户
     *
     * @param name 用户名
     * @return 用户
     */
    WebAdminUser findByName(String name);
}
