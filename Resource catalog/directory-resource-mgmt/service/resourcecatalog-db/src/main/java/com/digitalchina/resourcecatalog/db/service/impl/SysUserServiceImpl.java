package com.digitalchina.resourcecatalog.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalchina.resourcecatalog.db.dao.CataUserTypeRelMapper;
import com.digitalchina.resourcecatalog.db.dao.SysUserMapper;
import com.digitalchina.resourcecatalog.db.domain.CataUserTypeRel;
import com.digitalchina.resourcecatalog.db.domain.SysUser;
import com.digitalchina.resourcecatalog.db.domain.SysUserOrgRel;
import com.digitalchina.resourcecatalog.db.domain.SysUserRoleRel;
import com.digitalchina.resourcecatalog.db.service.CataUserTypeRelService;
import com.digitalchina.resourcecatalog.db.service.SysUserOrgRelService;
import com.digitalchina.resourcecatalog.db.service.SysUserRoleRelService;
import com.digitalchina.resourcecatalog.db.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 管理员表 服务实现类
 * </p>
 *
 * @author wangmh
 * @since 2020-04-30
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {
	
	
    @Autowired
    CataUserTypeRelMapper cataUserTypeRelMapper;

    @Autowired
    private SysUserRoleRelService sysUserRoleRelService;

    @Autowired
    private SysUserOrgRelService sysUserOrgRelService;

    @Autowired
    private CataUserTypeRelService cataUserTypeRelService;

    @Autowired
    KeycloakSynchUserOneServiceImpl keycloakService;
    
    @Override
    public IPage<List<Map>> myPage(IPage page, String name, Integer orgId, Integer roleId) {
        return this.baseMapper.myPage(page, name, orgId, roleId);
    }

    @Override
    @Transactional
    public void addUser(SysUser user, Integer[] orgId, Integer[] roleId) {
        user.setDisabled(0);
        user.setDeleted(0);
        user.setAddTime(LocalDateTime.now());
        //保存用户
        this.baseMapper.insert(user);
        //保存用户与角色关系
        List<SysUserRoleRel> roleList = new ArrayList<SysUserRoleRel>(roleId.length);
        for (Integer id : roleId) {
            roleList.add(new SysUserRoleRel(user.getId(), id));
        }
        sysUserRoleRelService.saveBatch(roleList);
        //保存用户与部门关系
        List<SysUserOrgRel> orgList = new ArrayList<SysUserOrgRel>(orgId.length);
        for (Integer id : orgId) {
            orgList.add(new SysUserOrgRel(user.getId(), id));
        }
        sysUserOrgRelService.saveBatch(orgList);
        
    	keycloakService.synchUser("add", user);
    }

    @Override
    @Transactional
    public void addUserExcludeKeycloak(SysUser user, Integer[] orgId, Integer[] roleId) {
        user.setDisabled(0);
        user.setDeleted(0);
        user.setAddTime(LocalDateTime.now());
        //保存用户
        this.baseMapper.insert(user);
        //保存用户与角色关系
        List<SysUserRoleRel> roleList = new ArrayList<SysUserRoleRel>(roleId.length);
        for (Integer id : roleId) {
            roleList.add(new SysUserRoleRel(user.getId(), id));
        }
        sysUserRoleRelService.saveBatch(roleList);
        //保存用户与部门关系
        List<SysUserOrgRel> orgList = new ArrayList<SysUserOrgRel>(orgId.length);
        for (Integer id : orgId) {
            orgList.add(new SysUserOrgRel(user.getId(), id));
        }
        sysUserOrgRelService.saveBatch(orgList);
    }


    @Override
    @Transactional
    public boolean updateInfo(SysUser user, Integer[] orgId, Integer roleId[]) {
        user.setUpdateTime(LocalDateTime.now());
        //更新用户信息
        this.baseMapper.updateById(user);
        Integer userId = user.getId();
        //删除用户和角色关联关系
        sysUserRoleRelService.remove(new UpdateWrapper<SysUserRoleRel>().lambda().eq(SysUserRoleRel::getUserId, userId));
        //删除用户和部门关联关系
        sysUserOrgRelService.remove(new UpdateWrapper<SysUserOrgRel>().lambda().eq(SysUserOrgRel::getUserId, userId));

        //保存用户与角色关系
        List<SysUserRoleRel> roleList = new ArrayList<SysUserRoleRel>(roleId.length);
        for (Integer id : roleId) {
            roleList.add(new SysUserRoleRel(user.getId(), id));
        }
        sysUserRoleRelService.saveBatch(roleList);
        //保存用户与部门关系
        List<SysUserOrgRel> orgList = new ArrayList<SysUserOrgRel>(orgId.length);
        for (Integer id : orgId) {
            orgList.add(new SysUserOrgRel(user.getId(), id));
        }
        sysUserOrgRelService.saveBatch(orgList);
    	keycloakService.synchUser("update", user);
        return true;
    }

    @Override
    public SysUser findByIdOrUsername(Integer id, String username) {
        SysUser byIdOrUsername = this.baseMapper.findByIdOrUsername(id, username);
        if(byIdOrUsername!=null){
            List<CataUserTypeRel> cataUserTypeRels = cataUserTypeRelMapper.selectList(new QueryWrapper<CataUserTypeRel>().lambda().eq(CataUserTypeRel::getUserId, byIdOrUsername.getId()).eq(CataUserTypeRel::getIsLeaf, 1));
            byIdOrUsername.setTypeIds(cataUserTypeRels.stream().map(CataUserTypeRel::getTypeId).collect(Collectors.toList()));
        }
        return byIdOrUsername;
    }

    @Override
    @Transactional
    public void delete(SysUser user) {
        user = this.baseMapper.selectById(user);
        if(user == null){
            return ;
        }
        this.baseMapper.deleteById(user);
        Integer userId = user.getId();
        sysUserRoleRelService.remove(new UpdateWrapper<SysUserRoleRel>().lambda().eq(SysUserRoleRel::getUserId, userId));
        sysUserOrgRelService.remove(new UpdateWrapper<SysUserOrgRel>().lambda().eq(SysUserOrgRel::getUserId, userId));
    	keycloakService.synchUser("delete", user);
    }

    @Override
    @Transactional
    public void setCataRole(Integer id, List<HashMap> typeIds) {
        cataUserTypeRelService.remove(new UpdateWrapper<CataUserTypeRel>().lambda().in(CataUserTypeRel::getUserId, id));
        List<CataUserTypeRel> cataUserTypeList = new ArrayList<CataUserTypeRel>(typeIds.size());
        typeIds.forEach(item -> {
            cataUserTypeList.add(new CataUserTypeRel(id, Integer.parseInt(item.get("id").toString()), Integer.parseInt(item.get("isLeaf").toString())));
        });
        cataUserTypeRelService.saveBatch(cataUserTypeList);
    }
}
