package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.entity.FileDownLogEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 文件下载日志
 *
 * @author zhengyang
 */
public interface FileDownLogDao extends JpaRepository<FileDownLogEntity, String>, JpaSpecificationExecutor<FileDownLogEntity> {
    /**
     * 查找某一条访问的数据量
     *
     * @param day
     * @return
     */
    @Query(value = "select count(id) from d1_file_down_log where to_char(download_time\\:\\:DATE, 'YYYY-MM-DD') =?1 ", nativeQuery = true)
    Long countByDay(String day);

    /**
     * 查找部门访问的数据量
     *
     * @param orgId
     * @return
     */
    @Query(value = "select count(fd.id) from FileDownLogEntity as fd join ResourceUseInfoEntity ru on fd.resourceUseInfoId=ru.id where ru.createDeptId =?1 ")
    Long countUseByOrgId(String orgId);

    /**
     * 查找部门提供的数据量
     *
     * @param orgId
     * @return
     */
    @Query(value = "select count(fd.id) from FileDownLogEntity as fd join ResourceUseInfoEntity ru on fd.resourceUseInfoId=ru.id where ru.provOrgId =?1 ")
    Long countGiveByOrgId(String orgId);
}
