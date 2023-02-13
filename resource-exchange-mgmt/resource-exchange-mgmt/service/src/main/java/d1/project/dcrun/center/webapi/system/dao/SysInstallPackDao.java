package d1.project.dcrun.center.webapi.system.dao;

import d1.project.dcrun.center.webapi.system.entity.SysInstallPack;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author libin
 */
public interface SysInstallPackDao extends JpaRepository<SysInstallPack, String>, JpaSpecificationExecutor<SysInstallPack> {
    /**
     * 根据模板类型倒序查询
     *
     * @param type 类型
     * @return
     */
    SysInstallPack findFirstByTypeOrderByVersionDesc(String type);

    /**
     * 查询该用户下面的所有安装包版本
     *
     * @param createById 所属用户
     * @param type       类型
     * @return
     */
    @Query(value = "select * from d1_sys_install_pack where create_by_id=(?1) and type =(?2) order by create_time desc", nativeQuery = true)
    List<SysInstallPack> findAllVersionByCreateById(String createById, String type);

    /**
     * 根据类型查询所有安装包版本
     *
     * @param type 类型
     * @return
     */
    @Query(value = "select * from d1_sys_install_pack where type =(?1) order by create_time desc", nativeQuery = true)
    List<SysInstallPack> findAllVersion(String type);
}
