package d1.framework.permission.service;

import d1.framework.permission.dao.UserPermissionDao;
import d1.framework.permission.entity.UserPermission;
import d1.framework.permission.model.MenuTreeGetVm;
import d1.framework.permission.model.UserPermissionPostVm;
import d1.framework.permission.model.UserPermissionSelectVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserPermissionService {
    @Autowired
    private UserPermissionDao userPermissionDao;

    @Autowired
    private MenuTreeService menuTreeService;

    public List<UserPermission> FindAllByUserId(String userId) throws Exception {
        List<UserPermission> userPermissions = userPermissionDao.findAllByUserId(userId);
        return userPermissions;
    }


    public UserPermissionSelectVm getMenuAndSelectByUserId(String userId) throws Exception {
        //完整功能树
        List<MenuTreeGetVm> menuTrees = menuTreeService.findAllTree();

        //已选择功能树Id
        List<String> permissionIds = new ArrayList<>();
        List<UserPermission> userPermissionList = userPermissionDao.findAllByUserId(userId);
        userPermissionList.forEach(t -> {
            permissionIds.add(t.getMenuTreeId());
        });

        //拼装结果
        UserPermissionSelectVm userPerAndMenuTree = new UserPermissionSelectVm();
        userPerAndMenuTree.setMenuTree(menuTrees);
        userPerAndMenuTree.setChoose(permissionIds);

        return userPerAndMenuTree;
    }


    public void insert(UserPermissionPostVm model) {

        //整体替换，删除原有数据
        userPermissionDao.deleteAllByUserId(model.getUserId());

        //新添加权限列表
        List<UserPermission> userPermissions = new ArrayList<>();

        for (String menuTreeId : model.getMenuTreeIds()) {

            UserPermission userPermission = new UserPermission();
            userPermission.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            userPermission.setUserId(model.getUserId());
            userPermission.setMenuTreeId(menuTreeId);

            userPermissions.add(userPermission);
        }
        userPermissionDao.saveAll(userPermissions);
    }
}
