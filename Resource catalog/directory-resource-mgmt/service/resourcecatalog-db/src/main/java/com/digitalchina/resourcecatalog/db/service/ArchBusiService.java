package com.digitalchina.resourcecatalog.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalchina.resourcecatalog.db.domain.ArchAppSys;
import com.digitalchina.resourcecatalog.db.domain.ArchBusi;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 权责清单 服务类
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public interface ArchBusiService extends IService<ArchBusi> {

    /***
     * 部门的权责清单树
     * @param idSuperAdmin
     * @param deptIds
     * @return
     */
    List tree(boolean idSuperAdmin, List<Integer> deptIds);

    /**
     *根据部门Id获取不包含组织机构的权责树
     * @param deptId
     * @return
     */
    List treeByDeptId(Integer deptId);

    /***
     * 分页
     * @param name
     * @param id
     * @param isDept
     * @return
     */
    List selectByPage(String name, Integer isDept, Integer id, Integer filterCata);

    /**
     * 详情
     *
     * @param busiId
     * @return
     */
    ArchBusi detail(Integer busiId);

    /***
     * 获取同级最大编码
     *
     * @param deptId
     * @param pId
     * @return
     */
    Long getBusiNoMaxByPId(Integer pId, Integer deptId);

    /***
     * 删除权责清单
     * @param busiId
     * @return
     */
    boolean delete(Integer busiId);

    /***
     * orgId与cata_org_item_rel关联，获取权责清单列表
     * @param orgId
     * @return
     */
    List<ArchBusi> getBusInfoByOrgId(Integer orgId, String containsChild);

    boolean saveOrUpdateInfo(ArchBusi busi, List<Integer> appIds);

    /***
     * 获取当前权责清单所关联的应用系统
     * @param busiId
     * @return
     */
    List<ArchAppSys> getAppListByBusiId(Integer busiId);


    /***
     * 新增-产生|需要的信息资源分页
     * @param page
     * @param orgId
     * @param cataName
     * @param infoIds
     * @return
     */
    IPage<List<Map<String, Object>>> cataInfoPage(Page page, Integer orgId, String cataName, List<Integer> infoIds, Integer type);

    int countByDepts(List<Integer> deptIds);

    /***
     * o通过部门id,获取该部门下机构关联了的权责清单List
     * @param depId
     * @return
     */
    List<ArchBusi> getExportBusisByDepId(Integer depId);

    /**
     * 导入权责清单excel
     * @param inputStream
     * @return
     */
    String importBusisExcel(InputStream inputStream) throws IOException;

    List<ArchBusi> selectListContainsOrgNm(List<Integer> deptIds);

    /**
     * 获取编码
     * @param deptId
     * @param pId
     * @return
     */
    Map getBusiNo(Integer deptId, Integer pId);
}
