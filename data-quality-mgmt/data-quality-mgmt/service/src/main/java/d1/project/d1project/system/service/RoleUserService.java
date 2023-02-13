package d1.project.d1project.system.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.model.PageableVm;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.d1project.common.utils.BaseUtils;
import d1.project.d1project.system.dao.RoleUserDao;
import d1.project.d1project.system.dao.WebAdminUserDao;
import d1.project.d1project.system.entity.RoleUserEntity;
import d1.project.d1project.system.entity.WebAdminUserEntity;
import d1.project.d1project.system.model.MenuTreeTree;
import d1.project.d1project.system.model.vm.RoleUserFindRoleIdVm;
import d1.project.d1project.system.model.vm.RoleUserInsertAllVm;
import d1.project.d1project.system.model.vm.RoleUserMenuTreeVm;
import d1.project.d1project.system.model.vm.WebAdminUserBaseVm;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-10 10:11
 */

@Service
public class RoleUserService {

    private final RoleMenuTreeService roleMenuTreeService;

    private final RoleUserDao roleUserDao;

    private final WebAdminUserDao webAdminUserDao;

    public RoleUserService(RoleUserDao roleUserDao, RoleMenuTreeService roleMenuTreeService, WebAdminUserDao webAdminUserDao) {
        this.roleUserDao = roleUserDao;
        this.roleMenuTreeService = roleMenuTreeService;
        this.webAdminUserDao = webAdminUserDao;
    }

    public void insert(RoleUserEntity model, HttpServletRequest request) throws DoValidException {
        JSONObject userByToken = TokenManager.getInstance().getUserByToken(request);
        if (userByToken!=null) {
            TokenManager.getInstance().updateCreateIdAndTime(request, model);
        }
        model.setId(BaseUtils.generate32Id());
        roleUserDao.save(model);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertAll(RoleUserInsertAllVm model, HttpServletRequest request) throws DoValidException {
        List<WebAdminUserEntity> webAdminUserEntities = new ArrayList<>();
        List<RoleUserEntity> roleUserEntities = new ArrayList<>();
        List<String> userIds = model.getUserIds();
        if (userIds.contains("admin")) {
            throw new DoValidException("不可为系统管理员分配组织机构");
        }
        for (WebAdminUserEntity webAdminUserEntity : webAdminUserDao.findAllById(userIds)) {
            /*新增关联关系*/
            RoleUserEntity roleUserEntity = new RoleUserEntity(model.getRoleId(), model.getRoleName(), webAdminUserEntity);
            TokenManager.getInstance().updateCreateIdAndTime(request, roleUserEntity);
            roleUserEntity.setId(BaseUtils.generate32Id());
            roleUserEntities.add(roleUserEntity);

            webAdminUserEntity.setRoleName(model.getRoleName());
            webAdminUserEntities.add(webAdminUserEntity);
        }
        roleUserDao.deleteByUserIdIn(userIds);
        roleUserDao.saveAll(roleUserEntities);
        webAdminUserDao.saveAll(webAdminUserEntities);
    }

    void deleteByUserId(String userId) {
        roleUserDao.deleteByUserId(userId);
    }

    void deleteByUserIdIn(Collection<String> userId) {
        roleUserDao.deleteByUserIdIn(userId);
    }

    void deleteByRoleId(String roleId) {
        roleUserDao.deleteByRoleId(roleId);
    }

    public Page<RoleUserEntity> findRoleId(RoleUserFindRoleIdVm model) throws Exception {
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        return getRoleUserEntities(paramObject);
    }

    private Page<RoleUserEntity> getRoleUserEntities(JSONObject paramObject) throws Exception {
        SpecificationBuilder<RoleUserEntity> builder = new SpecificationBuilder<>();
        Specification<RoleUserEntity> specification = builder.init(paramObject)
                .sContain("userName", "userName")
                .sEqual("roleId", "roleId")
                .sContain("userAccount", "userAccount")
                .sContain("userPhone", "userPhone")
                .sContain("organizationName", "organizationName")
                .sNotIn("id", "notIds")
                .dOrder("createTime")
                .build();
        return roleUserDao.findAll(specification, builder.getPageable());
    }

    public Page<WebAdminUserBaseVm> findAllByRoleId(boolean selected, String roleId, String userName, String organizationName, PageableVm pageableVm) throws Exception {
        SpecificationBuilder<RoleUserEntity> builder = new SpecificationBuilder<>();
        builder.init((JSONObject) JSON.toJSON(pageableVm));
        if (selected) {
            return roleUserDao.findAllByRoleIdIsNotNullAndUserNameAndRoleName(roleId, userName, organizationName, builder.getPageable());
        } else {
            return roleUserDao.findAllByRoleIdIsNullAndUserNameAndRoleName(roleId, userName, organizationName, builder.getPageable());
        }
    }

    public List<RoleUserMenuTreeVm> findCurrentUser(HttpServletRequest request) throws DoValidException {
        List<RoleUserMenuTreeVm> menuTreeVms = new ArrayList<>();
        JSONObject userByToken = TokenManager.getInstance().getUserByToken(request);
        String id = userByToken.getString("id");
        List<RoleUserEntity> byUserId = roleUserDao.findByUserId(id);
        for (RoleUserEntity roleUser : byUserId) {
            List<MenuTreeTree> allByRoleId = roleMenuTreeService.findAllByRoleId(roleUser.getRoleId(), false);
            RoleUserMenuTreeVm roleUserMenuTreeVm = new RoleUserMenuTreeVm(roleUser, allByRoleId);
            menuTreeVms.add(roleUserMenuTreeVm);
        }

        return menuTreeVms;
    }

    public List<String> findCurrentUserButton(HttpServletRequest request) throws DoValidException {
        JSONObject userByToken = TokenManager.getInstance().getUserByToken(request);
        String id = userByToken.getString("id");
        List<String> roles = roleUserDao.findByUserId(id).stream().map(RoleUserEntity::getRoleId).collect(Collectors.toList());
        return roleMenuTreeService.findAllByRoleIdInButton(roles);
    }

    public List<RoleUserMenuTreeVm> findCurrentUserSidebar(HttpServletRequest request) throws DoValidException {
        JSONObject userByToken1 = TokenManager.getInstance().getUserByToken(request);
        System.out.println(userByToken1.toJSONString());

        List<RoleUserMenuTreeVm> menuTreeVms = new ArrayList<>();
        JSONObject userByToken = TokenManager.getInstance().getUserByToken(request);
        String id = userByToken.getString("id");
        List<RoleUserEntity> byUserId = roleUserDao.findByUserId(id);
        for (RoleUserEntity roleUser : byUserId) {
            List<MenuTreeTree> allByRoleId = roleMenuTreeService.findAllByRoleId(roleUser.getRoleId(), true);
            RoleUserMenuTreeVm roleUserMenuTreeVm = new RoleUserMenuTreeVm(roleUser, allByRoleId);
            menuTreeVms.add(roleUserMenuTreeVm);
        }
        return menuTreeVms;
    }


    public List<RoleUserEntity> findByUserId(String userId) {
        return roleUserDao.findByUserId(userId);
    }

    public List<RoleUserEntity> findByRoleId(String roleId) {
        return roleUserDao.findByRoleId(roleId);
    }

    boolean existsByRoleId(String roleId) {
        return roleUserDao.existsByRoleId(roleId);
    }

    public void saveAll(Iterable<RoleUserEntity> entities) {
        roleUserDao.saveAll(entities);
    }

}
