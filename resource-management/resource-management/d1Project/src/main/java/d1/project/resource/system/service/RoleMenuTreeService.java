package d1.project.resource.system.service;

import com.alibaba.fastjson.JSON;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resource.common.model.OperationLog;
import d1.project.resource.common.service.IOperationLogService;
import d1.project.resource.common.utils.BaseUtils;
import d1.project.resource.system.dao.RoleDao;
import d1.project.resource.system.dao.RoleMenuTreeDao;
import d1.project.resource.system.entity.MenuTreeEntity;
import d1.project.resource.system.entity.RoleEntity;
import d1.project.resource.system.entity.RoleMenuTreeEntity;
import d1.project.resource.system.model.MenuTreeTree;
import d1.project.resource.system.model.vm.RoleMenuTreeInsertVm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-10 19:04
 */
@Service
public class RoleMenuTreeService {

    private final RoleDao roleDao;

    private final RoleMenuTreeDao roleMenuTreeDao;

    private final MenuTreeService menuTreeService;

    private final IOperationLogService iOperationLogService;

    public RoleMenuTreeService(RoleMenuTreeDao roleMenuTreeDao, MenuTreeService menuTreeService, IOperationLogService iOperationLogService, RoleDao roleDao) {
        this.roleMenuTreeDao = roleMenuTreeDao;
        this.menuTreeService = menuTreeService;
        this.iOperationLogService = iOperationLogService;
        this.roleDao = roleDao;
    }


    /**
     * 新增并替换
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(RoleMenuTreeInsertVm model, HttpServletRequest request) throws DoValidException {
        //整体替换，删除原有数据
        String roleId = model.getRoleId();
        RoleEntity roleEntity = roleDao.findById(roleId).orElseThrow(() -> new DoValidException("角色不存在"));
        roleMenuTreeDao.deleteAllByRoleId(roleId);

        List<RoleMenuTreeEntity> roleMenuTrees = new ArrayList<>();
        for (String menuTreeId : model.getMenuTreeIds()) {
            RoleMenuTreeEntity roleMenuTree = new RoleMenuTreeEntity(roleId, menuTreeId);
            TokenManager.getInstance().updateCreateIdAndTime(request, roleMenuTree);
            roleMenuTree.setId(BaseUtils.generate32Id());
            roleMenuTrees.add(roleMenuTree);
        }

        roleMenuTreeDao.saveAll(roleMenuTrees);
        iOperationLogService.insert(new OperationLog("系统管理_角色权限", "分配权限", "分配角色" + roleEntity.getName() + "权限", JSON.toJSONString(roleEntity), 1), request);
    }

    /**
     * 根据角色id获取菜单树
     *
     * @param roleId    角色id
     * @param isSidebar 是否是侧边栏
     */
    public List<MenuTreeTree> findAllByRoleId(String roleId, boolean isSidebar) {
        Set<String> collect;
        if ("admin".equals(roleId)) {
            collect = menuTreeService.findAll().stream().map(MenuTreeEntity::getId).collect(Collectors.toSet());
        } else {
            collect = roleMenuTreeDao.findByRoleId(roleId).stream().map(RoleMenuTreeEntity::getMenuTreeId).collect(Collectors.toSet());
        }
        return menuTreeService.findAllByIdIn(collect, isSidebar);
    }

    /**
     * 根据角色id获取菜单树按钮
     *
     * @param rolesIds 角色id列表
     */
    public List<String> findAllByRoleIdInButton(List<String> rolesIds) {
        Set<String> collect = roleMenuTreeDao.findByRoleIdIn(rolesIds).stream().map(RoleMenuTreeEntity::getMenuTreeId).collect(Collectors.toSet());
        List<MenuTreeEntity> allByIdInOrderBySeqAsc;
        if (rolesIds.contains("admin")) {
            allByIdInOrderBySeqAsc = menuTreeService.findAll();
        } else {
            allByIdInOrderBySeqAsc = menuTreeService.findAllByIdInOrderBySeqAsc(collect);
        }

        //返回有权限的菜单数组
        List<String> menuTreeIds = allByIdInOrderBySeqAsc.stream().filter(t -> 2 == t.getType()).map(MenuTreeEntity::getPath).collect(Collectors.toList());

        return menuTreeIds;
    }

    /**
     * 根据角色id获取已选菜单树
     *
     * @param roleId 角色id
     */
    public List<String> findAllRoleIdChoose(String roleId) {
        if ("admin".equals(roleId)) {
            return menuTreeService.findAllAsLongAsId();
        }
        List<String> collect = roleMenuTreeDao.findByRoleId(roleId).stream().map(RoleMenuTreeEntity::getMenuTreeId).collect(Collectors.toList());
        return menuTreeService.findAllNoChildrenAsLongAsId(collect);
    }

}
