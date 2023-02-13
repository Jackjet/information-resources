package d1.framework.permission.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.permission.dao.RoleDao;
import d1.framework.permission.dao.RolePermissionDao;
import d1.framework.permission.dao.UserRoleDao;
import d1.framework.permission.entity.Role;
import d1.framework.permission.entity.RolePermission;
import d1.framework.permission.entity.UserRole;
import d1.framework.permission.impl.DoServiceImpBase;
import d1.framework.permission.model.RoleGetVm;
import d1.framework.permission.model.RolePostVm;
import d1.framework.permission.model.RolePutVm;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class RoleService extends DoServiceImpBase<Role> {
    @Autowired
    private OperationLogService1 operationLogService;
    @Autowired
    private TokenService tokenService;

    @Override
    protected JpaRepository<Role, String> getDao() {
        return roleDao;
    }

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private RolePermissionDao rolePermissionDao;


    public void insert(RolePostVm model, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = model.getName();
        try {

            if (name == null) {
                throw new Exception("角色名称不可为空");
            }
            if (roleDao.findByName(name) != null) {
                throw new Exception("该角色已存在");
            }
            Role role = new Role();
            role.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            role.setName(model.getName());
            role.setRemark(model.getRemark());
            role.setCreateTime(new Date());

            this.insert(role);
            operationLogService.setLog("角色管理—添加", userName, "运维行为管理-组件管理-角色管理", "添加了一个角色:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("角色管理—添加", userName, "运维行为管理-组件管理-角色管理", "添加了一个角色:" + name, "0", request);
            throw new Exception(e.getMessage());
        }
    }


    public void update(RolePutVm model, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            if (model == null) {
                throw new Exception("请求参数不正确");
            }
            name = model.getName();
            Role role = this.findById(model.getId());
            if (role == null) {
                throw new Exception("未找到id=" + model.getId() + "的资源");
            }
            if (!role.getName().equals(model.getName()) && roleDao.existsByName(model.getName())) {
                throw new Exception("该角色已存在");
            }
            role.setName(model.getName());
            role.setRemark(model.getRemark());

            this.update(role);
            operationLogService.setLog("角色管理—编辑", userName, "运维行为管理-组件管理-角色管理", "编辑了一个角色:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("角色管理—编辑", userName, "运维行为管理-组件管理-角色管理", "编辑了一个角色:" + name, "0", request);
            throw new Exception(e.getMessage());
        }
    }

    public Page<Role> findAll(RoleGetVm model) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            SpecificationBuilder<Role> builder = new SpecificationBuilder<>();
            Specification<Role> specification = builder.init(jsonObject)
                    .sContain("name", "name")
                    .dOrder("createTime")
                    .build();
            return roleDao.findAll(specification, builder.getPageable());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteUser(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            Role role = findById(id);
            if (role == null) {
                throw new Exception("该角色不存在");
            }
            name = role.getName();
            List<RolePermission> rolePermissions = rolePermissionDao.findAllByRoleId(id);
            if (rolePermissions.size() > 0) {
                throw new Exception("该角色已被分配不可删除");
            }
            List<UserRole> userToRoles = userRoleDao.findAllByRoleId(id);
            if (userToRoles.size() > 0) {
                throw new Exception("该角色已被分配不可删除");
            }
            roleDao.deleteById(id);
            operationLogService.setLog("角色管理—删除", userName, "运维行为管理-组件管理-角色管理", "删除了一个角色:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("角色管理—删除", userName, "运维行为管理-组件管理-角色管理", "删除了一个角色:" + name, "0", request);
            throw new Exception(e.getMessage());
        }
    }

    private void moveRole(List<Role> roleList, String name) {
        Integer index = null;
        for (int i = 0; i < roleList.size(); i++) {
            if (name.equals(roleList.get(i).getName())) {
                index = i;
                break;
            }
        }
        if (index != null) {
            Role temp = roleList.get(index);
            Iterator<Role> it = roleList.iterator();
            while (it.hasNext()) {
                if (name.equals(it.next().getName())) {
                    it.remove();
                }
            }
            roleList.add(0, temp);
        }
    }

    public List<Role> findAllRoles() {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        return roleDao.findAll(sort);
    }
}
