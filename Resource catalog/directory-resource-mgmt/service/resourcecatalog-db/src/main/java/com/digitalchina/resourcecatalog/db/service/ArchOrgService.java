package com.digitalchina.resourcecatalog.db.service;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import com.baomidou.mybatisplus.extension.service.IService;
import com.digitalchina.resourcecatalog.db.domain.ArchOrg;
import com.digitalchina.resourcecatalog.db.domain.Result;

/**
 * <p>
 * 组织机构 服务类
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
public interface ArchOrgService extends IService<ArchOrg> {

    /***
     * 组织结构分页
     * @param orgId
     * @return
     */
    List<ArchOrg> myPage(Integer orgId, List<Integer> deptIds, boolean isDeptAdmin);

    /***
     * 树
     * @param isTop
     * @param idDeptAdmin
     * @param deptIds
     * @return
     */
    List<ArchOrg> tree(Integer isTop, boolean idDeptAdmin, List<Integer> deptIds);
    

    List<ArchOrg> tree2(boolean idDeptAdmin, List<Integer> deptIds);
    /***
     * 详情
     * @param orgId
     * @return
     */
    ArchOrg detail(Integer orgId);

    /***
     * 获取最大的部门code
     * @return
     */
    Integer getMaxOrgCdByPidIsNull();

    /***
     * 获取最大的组织机构code
     * @return
     * @param parOrgId
     */
    Integer getMaxOrgCdByPid(Integer parOrgId);

    /**
     * 删除当前级及子节点
     *
     * @param orgId
     */
    void deleteTree(Integer orgId);

    /**
     * 根据组织机构的id获取部门
     *
     * @param orgList
     * @return
     */
    List<DefaultKeyValue<Integer, String>> getDeptsByOrgIds(List<ArchOrg> orgList);

    /***
     * 应用系统内下机构树
     * @param orgId
     * @return
     */
    List<ArchOrg> treeAppOrg(Integer orgId);

    /***
     * 资源信息提交前10部门
     * @return
     */
    List<Result> top10Dept(String shareType);

    /***
     * 获取显示序号
     * @param parOrgId
     * @return
     */
    Integer getDispalySn(Integer parOrgId);

    List<Map<String, Object>> getMaxSecondLevel();
}
