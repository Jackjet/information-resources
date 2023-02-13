package d1.framework.permission.service;


import d1.framework.permission.dao.DeptPermissionDao;
import d1.framework.permission.entity.DeptPermission;
import d1.framework.permission.model.DeptPermissionPostVm;
import d1.framework.permission.model.DeptPermissionSelectVm;
import d1.framework.permission.model.MenuTreeGetVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class DeptPermissionService {

    @Autowired
    private DeptPermissionDao deptPermissionDao;

    @Autowired
    private MenuTreeService menuTreeService;

    public List<DeptPermission> findAllByDeptId(String deptId) throws Exception {
        List<DeptPermission> deptPermissions = deptPermissionDao.findAllByDeptId(deptId);
        return deptPermissions;
    }


    public DeptPermissionSelectVm getMenuAndSelectByDeptId(String deptId) throws Exception {
        //完整功能树
        List<MenuTreeGetVm> menuTrees = menuTreeService.findAllTree();

        //已选择功能树Id
        List<String> permissionIds = new ArrayList<>();
        List<DeptPermission> deptPermissionList = deptPermissionDao.findAllByDeptId(deptId);
        deptPermissionList.forEach(t -> {
            permissionIds.add(t.getMenuTreeId());
        });

        //拼装结果
        DeptPermissionSelectVm deptPerAndMenuTree = new DeptPermissionSelectVm();
        deptPerAndMenuTree.setMenuTree(menuTrees);
        deptPerAndMenuTree.setChoose(permissionIds);

        return deptPerAndMenuTree;
    }


    public void insert(DeptPermissionPostVm model) {

        //整体替换，删除原有数据
        deptPermissionDao.deleteAllByDeptId(model.getDeptId());

        //新添加权限列表
        List<DeptPermission> deptPermissions = new ArrayList<>();

        for (String menuTreeId : model.getMenuTreeIds()) {

            DeptPermission deptPermission = new DeptPermission();
            deptPermission.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            deptPermission.setDeptId(model.getDeptId());
            deptPermission.setMenuTreeId(menuTreeId);

            deptPermissions.add(deptPermission);
        }
        deptPermissionDao.saveAll(deptPermissions);
    }
}
