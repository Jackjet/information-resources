package d1.framework.permission.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.permission.dao.MenuTreeDao;
import d1.framework.permission.entity.MenuTree;
import d1.framework.permission.impl.DoServiceImpBase;
import d1.framework.permission.model.MenuTreeGN;
import d1.framework.permission.model.MenuTreeGetVm;
import d1.framework.permission.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author all
 */
@Service
public class MenuTreeService extends DoServiceImpBase<MenuTree> {

    @Override
    protected JpaRepository<MenuTree, String> getDao() {
        return menuTreeDao;
    }

    @Autowired
    private MenuTreeDao menuTreeDao;

    boolean existsByParentId(String parentId) {
        return menuTreeDao.existsByParentId(parentId);
    }

    public List<MenuTreeGetVm> findAllTree() {
        List<MenuTree> all = menuTreeDao.findAllOrderById();
        int allSize = all.size();
        List<MenuTreeGetVm> menus = new ArrayList<>(allSize);
        for (int i = 0; i < all.size(); i++) {
            // 一级菜单没有parentId
            if (StringUtils.isEmpty(all.get(i).getParentId())) {
                setMenuTree(all, i, menus);
            }
        }
        return menus;
    }

    public MenuTreeGetVm findByMenuId(String menuId) {
        List<MenuTreeGetVm> allTree = findAllTree();
        return getMenuTreeGetVm(menuId, allTree);
    }

    private MenuTreeGetVm getMenuTreeGetVm(String menuId, List<MenuTreeGetVm> allTree) {
        for (MenuTreeGetVm menuTreeGetVm : allTree) {
            if (menuId.equals(menuTreeGetVm.getId())) {
                return menuTreeGetVm;
            } else {
                List<MenuTreeGetVm> children = menuTreeGetVm.getChildren();
                if (children != null) {
                    MenuTreeGetVm menuTreeGetVm1 = getMenuTreeGetVm(menuId, children);
                    if (menuTreeGetVm1 != null) {
                        return menuTreeGetVm1;
                    }
                }
            }
        }
        return null;
    }


    private void setMenuTree(List<MenuTree> menus, Integer i, List<MenuTreeGetVm> childList) {
        MenuTreeGetVm mo = new MenuTreeGetVm();
        MenuTree menuTree = menus.get(i);
        String childId = menuTree.getId();
        mo.setId(childId);
        mo.setParentId(menuTree.getParentId());
        mo.setName(menuTree.getName());
        mo.setType(menuTree.getType());
        mo.setRoute(menuTree.getRoute());
        mo.setOrderNum(menuTree.getOrderNum());
        List<MenuTreeGetVm> child = findChild(menus, childId);
        mo.setChildren(child);
        mo.setLabel(menuTree.getName());
        childList.add(mo);
    }


    private List<MenuTreeGetVm> findChild(List<MenuTree> menus, String id) {
        List<MenuTreeGetVm> childList = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            if (!StringUtils.isEmpty(menus.get(i).getParentId())) {
                if (menus.get(i).getParentId().equals(id)) {
                    setMenuTree(menus, i, childList);
                }
            }
        }
        if (childList.isEmpty()) {
            return null;
        }
        return childList;
    }

    public void insert(JSONObject jsonObject) throws Exception {
        MenuTree model = MyUtils.model2Entity(jsonObject, MenuTree.class);
        this.insert(model);
    }

    @Override
    protected void beforeInsert(MenuTree opt) throws Exception {
        super.beforeInsert(opt);
        MyUtils.throwMsg(menuTreeDao.existsByOrderNumAndParentId(opt.getOrderNum(), opt.getParentId()), "同级别已存在相同排序");
    }


    @Override
    protected void beforeUpdate(MenuTree opt) throws Exception {
        super.beforeUpdate(opt);
        MenuTree byId = menuTreeDao.findById(opt.getId()).orElseThrow(() -> new Exception("未找到要求改的内容"));
        if (!byId.getParentId().equals(opt.getParentId()) || !byId.getOrderNum().equals(opt.getOrderNum())) {
            MyUtils.throwMsg(menuTreeDao.existsByOrderNumAndParentId(opt.getOrderNum(), opt.getParentId()), "同级别已存在相同排序");
        }
    }

    public void update(JSONObject jsonObject) throws Exception {
        MenuTree model = MyUtils.model2Entity(jsonObject, MenuTree.class);
        MyUtils.throwMsg(model == null, "请求参数不正确");
        MenuTree menuTree = menuTreeDao.findById(model.getId()).orElseThrow(() -> new Exception("未找到id=" + model.getId() + "的资源"));
        this.update(model);
    }


    public List<MenuTree> findAllById(List<String> ids) {
        return menuTreeDao.findAllById(ids);
    }

    public List<MenuTree> findAllOrderById() {
        return menuTreeDao.findAllOrderById();
    }

    public List<MenuTree> findOrderById(List<String> ids) {
        return menuTreeDao.findOrderById(ids);
    }

    public MenuTreeGN findByIdGn(String id) throws Exception {
        MenuTree byId = menuTreeDao.findById(id).orElseThrow(() -> new Exception("该id无法找到"));
        String parentId = byId.getParentId();
        MenuTreeGN menuTreeGN = MyUtils.model2Entity(byId, MenuTreeGN.class);
        if (!StringUtils.isEmpty(parentId)) {
            MenuTree byParentId = menuTreeDao.findById(parentId).orElseThrow(() -> new Exception("该id无法找到"));
            menuTreeGN.setParentName(byParentId.getName());
        }
        return menuTreeGN;
    }


}
