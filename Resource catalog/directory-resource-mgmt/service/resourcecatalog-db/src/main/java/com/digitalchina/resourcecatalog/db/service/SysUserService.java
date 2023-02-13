package com.digitalchina.resourcecatalog.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digitalchina.resourcecatalog.db.domain.SysUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员表 服务类
 * </p>
 *
 * @author wangmh
 * @since 2020-04-30
 */
public interface SysUserService extends IService<SysUser> {

    /**
     * 自定义分页
     *
     * @param page
     * @param name
     * @param orgId
     * @param roleId
     * @return
     */
    IPage<List<Map>> myPage(IPage page, String name, Integer orgId, Integer roleId);

    /***
     * 添加用户
     * @param user
     * @param orgId
     * @param roleId
     */
    void addUser(SysUser user, Integer[] orgId, Integer roleId[]);

    /**
     * 添加用户 排除keycloak
     * @param user
     * @param orgId
     * @param roleId
     */
    void addUserExcludeKeycloak(SysUser user, Integer[] orgId, Integer roleId[]);

    /***
     * 更新用户
     * @param user
     * @param orgId
     * @param roleId
     * @return
     */
    boolean updateInfo(SysUser user, Integer[] orgId, Integer roleId[]);

    /***
     * 根据id查询
     * @param id
     * @return
     */
    SysUser findByIdOrUsername(Integer id, String username);

    /***
     * 删除
     * @param user
     */
    void delete(SysUser user);

    /***
     * 设置目录分类管理权限
     * @param id
     * @param typeIds
     */
    void setCataRole(Integer id, List<HashMap> typeIds);
}
