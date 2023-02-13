package d1.framework.permission.service;

import d1.framework.permission.dao.DeptDao;
import d1.framework.permission.entity.Dept;
import d1.framework.permission.impl.DoServiceImpBase;
import d1.framework.permission.model.DeptGetVm;
import d1.framework.permission.model.DeptPostVm;
import d1.framework.permission.model.DeptPutVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class DeptService extends DoServiceImpBase<Dept> {

    @Override
    protected JpaRepository<Dept, String> getDao() {
        return deptDao;
    }


    @Autowired
    private DeptDao deptDao;

    public List<DeptGetVm> findAllDept() {
        List<Dept> depts = deptDao.findAll();
        List<DeptGetVm> results = new ArrayList<DeptGetVm>();
        for (int i = 0; i < depts.size(); i++) {

            // 一级菜单没有parentId
            if (StringUtils.isEmpty(depts.get(i).getParentId())) {
                DeptGetVm deptGetVm = findAllToModelVm(depts, depts.get(i));
                results.add(deptGetVm);
            }
        }
        return results;
    }

    //vue页面要求json结构
    public DeptGetVm findAllToModelVm(List<Dept> depts, Dept dept) {
        DeptGetVm deptVm = new DeptGetVm();
        deptVm.setId(dept.getId());
        deptVm.setName(dept.getName());
        deptVm.setPath(dept.getPath());
        deptVm.setLevel(dept.getLevel());
        List<DeptGetVm> child = findChilds(depts, dept.getId());
        deptVm.setChildren(child);

        return deptVm;
    }

    private List<DeptGetVm> findChilds(List<Dept> depts, String parentId) {
        List<DeptGetVm> childs = new ArrayList<>();
        for (int i = 0; i < depts.size(); i++) {

            if (!StringUtils.isEmpty(depts.get(i).getParentId()) && depts.get(i).getParentId().equals(parentId)) {
                DeptGetVm deptGetVm = findAllToModelVm(depts, depts.get(i));
                childs.add(deptGetVm);
            }
        }
        if (childs.size() == 0) {
            return null;
        }
        return childs;
    }


    public void insert(DeptPostVm model) throws Exception {

        //获取父级信息
        Dept parent = this.findById(model.getParentId());
        if (parent == null) throw new Exception("未找到父级部门信息");

        //计算path
        String path = "";
        Dept brother = deptDao.findFirstByParentIdOrderByCreateTimeDesc(model.getParentId());
        if (brother == null) {
            path = parent.getPath() + "0000";
        } else {
            //保留4位数
            Integer temp = Integer.parseInt(brother.getPath().substring(brother.getPath().length() - 4)) + 1;
            path = parent.getPath() + String.format("%04d", temp);
        }

        //赋值
        Dept dept = new Dept();
        dept.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        dept.setName(model.getName());
        dept.setParentId(model.getParentId());
        dept.setPath(path);
        dept.setLevel(parent.getLevel() + 1);
        dept.setCreateTime(new Date());

        this.insert(dept);
    }

    public void update(DeptPutVm model) throws Exception {
        if (model == null) throw new Exception("请求参数不正确");

        Dept role = this.findById(model.getId());
        if (role == null) throw new Exception("未找到id=" + model.getId() + "的资源");

        role.setName(model.getName());

        this.update(role);
    }

}
