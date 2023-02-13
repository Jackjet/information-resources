package d1.framework.permission.service;

import d1.framework.permission.dao.RolePermissionDao;
import d1.framework.permission.entity.MenuTree;
import d1.framework.permission.entity.Role;
import d1.framework.permission.entity.RolePermission;
import d1.framework.permission.model.MenuTreeGetVm;
import d1.framework.permission.model.RolePermissionPostVm;
import d1.framework.permission.model.RolePermissionSelectVm;
import d1.framework.webapi.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RolePermissionService {
    @Autowired
    private OperationLogService1 operationLogService;
    @Autowired
    private TokenService tokenService;

    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuTreeService menuTreeService;

    public List<RolePermission> findRolePermissionByUserId(String userId) {
        return rolePermissionDao.findRolePermissionByUserId(userId);
    }


    public RolePermissionSelectVm getMenuAndSelectByRoleId(String roleId) throws Exception {

        //完整功能树
        List<MenuTreeGetVm> menuTrees = menuTreeService.findAllTree();

        //已选择功能树Id
        List<String> rolePermissionIds = new ArrayList<>();
        List<RolePermission> rolePermissionList = rolePermissionDao.findAllByRoleId(roleId);
        rolePermissionList.forEach(t -> {
            if (!menuTreeService.existsByParentId(t.getMenuTreeId())) {
                rolePermissionIds.add(t.getMenuTreeId());
            }
        });

        //拼装结果
        RolePermissionSelectVm rolePerAndMenuTree = new RolePermissionSelectVm();
        rolePerAndMenuTree.setMenuTree(menuTrees);
        rolePerAndMenuTree.setChoose(rolePermissionIds);

        return rolePerAndMenuTree;
    }


    public void insert(RolePermissionPostVm model, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";

        try {
            Role role = roleService.findById(model.getRoleId());
            name = role.getName();
            //整体替换，删除原有数据
            rolePermissionDao.deleteAllByRoleId(model.getRoleId());

            //新添加权限列表
            List<MenuTree> menuTrees = menuTreeService.findAllById(model.getMenuTreeIds());

            List<RolePermission> rolePermissions = new ArrayList<>();

            for (int i = 0; i < menuTrees.size(); i++) {

                RolePermission rolePermission = new RolePermission();
                rolePermission.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                rolePermission.setRoleId(model.getRoleId());
                rolePermission.setMenuTreeId(menuTrees.get(i).getId());

                rolePermissions.add(rolePermission);
            }

            rolePermissionDao.saveAll(rolePermissions);

            operationLogService.setLog("角色管理—权限分配", userName, "运维行为管理-组件管理-角色管理", "为" + name + "分配了权限", "1", request);
        } catch (Exception e) {
            operationLogService.setLog("角色管理—权限分配", userName, "运维行为管理-组件管理-角色管理", "为" + name + "分配了权限", "0", request);
            throw new Exception(e.getMessage());
        }
    }

}
