package com.digitalchina.resourcecatalog.db.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digitalchina.resourcecatalog.db.domain.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 管理员表 Mapper 接口
 * </p>
 *
 * @author wangmh
 * @since 2020-04-30
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    IPage<List<Map>> myPage(IPage page, @Param("name") String name, @Param("orgId") Integer orgId, @Param("roleId") Integer roleId);

    SysUser findByIdOrUsername(@Param("id") Integer id, @Param("username") String username);
}
