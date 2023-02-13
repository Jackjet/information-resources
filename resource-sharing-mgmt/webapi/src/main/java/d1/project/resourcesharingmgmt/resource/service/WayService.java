package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.dao.WayDao;
import d1.project.resourcesharingmgmt.resource.dao.WayRoleDao;
import d1.project.resourcesharingmgmt.resource.entity.WayEntity;
import d1.project.resourcesharingmgmt.resource.entity.WayRoleEntity;
import d1.project.resourcesharingmgmt.resource.model.way.*;
import d1.project.resourcesharingmgmt.system.dao.RoleDao;
import d1.project.resourcesharingmgmt.system.dao.RoleUserDao;
import d1.project.resourcesharingmgmt.system.entity.RoleEntity;
import d1.project.resourcesharingmgmt.system.entity.RoleUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author dy
 */
@Service
public class WayService {
    private final WayDao wayDao;
    private final WayRoleDao wayRoleDao;
    private final RoleDao roleDao;
    private final HttpServletRequest request;
    private final RoleUserDao roleUserDao;

    public WayService(WayDao wayDao,
                      WayRoleDao wayRoleDao,
                      RoleDao roleDao, HttpServletRequest request, RoleUserDao roleUserDao) {
        this.wayDao = wayDao;
        this.wayRoleDao = wayRoleDao;
        this.roleDao = roleDao;
        this.request = request;
        this.roleUserDao = roleUserDao;
    }

    /**
     * 获取代办列表--通过角色Id标记已选列表
     */
    public WayRoleResVm chargeRoleGet(WayRoleFindVm model) {
        //所有way-类型为0 （0代办、1服务指引)
        String wayType="0";
        List<WayEntity> ways = wayDao.findAllByTypeOrderBySeq(wayType);

        //已选择way
        List<WayRoleEntity> wayRoles = wayRoleDao.findAllByRoleIdAndType(model.getRoleId(),wayType);
        List<String> wayRoleIds = wayRoles.stream().map(WayRoleEntity::getWayId).collect(Collectors.toList());

        //结果
        WayRoleResVm res = new WayRoleResVm();
        res.setWayEntities(ways);
        res.setChoose(wayRoleIds);

        return res;
    }

    /**
     * 保存代办列表--关联角色保存
     */
    @Transactional(rollbackFor = Exception.class)
    public void chargeRoleInsert(WayRoleInsertVm model) {
        //所有way-类型为0 （0代办、1服务指引)
        String wayType="0";

        //历史选择wayRoles
        List<WayRoleEntity> wayRoles = wayRoleDao.findAllByRoleIdAndType(model.getRoleId(),wayType);
        wayRoleDao.deleteAll(wayRoles);

        //角色
        RoleEntity role = roleDao.findById(model.getRoleId()).orElse(null);

        //代办角色记录s
        List<WayRoleEntity> results = new ArrayList<>();
        for (String wayId : model.getWayIds()) {

            //代办
            WayEntity way = wayDao.findById(wayId).orElse(null);

            //代办角色记录
            WayRoleEntity res = new WayRoleEntity();
            res.setId(BaseUtils.generate32Id());
            res.setType(wayType);
            res.setRoleId(model.getRoleId());
            res.setRoleName(role == null ? "" : role.getName());
            res.setWayId(wayId);
            res.setWayName(way == null ? "" : way.getName());
            res.setCreateTime(Calendar.getInstance());
            res.setUpdateTime(Calendar.getInstance());

            results.add(res);
        }
        wayRoleDao.saveAll(results);
    }

    /**
     * 服务指引查询列表
     */
    public Page<WayEntity> guideFindAll(WayGuideFindAllVm model) throws Exception {
        SpecificationBuilder<WayEntity> builder = new SpecificationBuilder<>();
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        paramObject.put("type","1");
        Specification<WayEntity> specification = builder.init(paramObject)
                .sEqual("type", "type")
                .sEqual("name", "name")
                .aOrder("seq")
                .build();
        return wayDao.findAll(specification, builder.getPageable());
    }

    /**
     * 服务指引新增
     */
    public void guideInsert(WayGuideInsertVm model) {
        String wayType = "1";
        WayEntity wayOld = wayDao.findFirstByTypeOrderBySeqDesc(wayType);

        WayEntity way = new WayEntity();
        way.setId(BaseUtils.generate32Id());
        way.setType(wayType);
        way.setName(model.getName());
        way.setPath(model.getPath());
        way.setSeq(wayOld == null ? 1 : wayOld.getSeq() + 1);
        way.setCreateTime(Calendar.getInstance());
        way.setUpdateTime(Calendar.getInstance());
        wayDao.save(way);
    }

    /**
     * 服务指引修改
     */
    public void guideUpdate(WayGuideUpdateVm model) throws DoValidException {
        WayEntity way = wayDao.findById(model.getId()).orElseThrow(() -> new DoValidException("服务指引为查询到，指引id：" + model.getId()));

        way.setName(model.getName());
        way.setPath(model.getPath());
        way.setUpdateTime(Calendar.getInstance());

        wayDao.save(way);
    }

    /**
     * 服务指引删除
     */
    public void guideDelete(String id) throws DoValidException {
        WayEntity way = wayDao.findById(id).orElseThrow(() -> new DoValidException("服务指引为查询到，指引id：" + id));
        wayDao.delete(way);

        List<WayRoleEntity> wayRoles = wayRoleDao.findAllByWayId(id);
        wayRoleDao.deleteAll(wayRoles);
    }

    /**
     * 服务指引上移
     */
    public void guideUp(String id) throws DoValidException {
        //当前way记录
        WayEntity way = wayDao.findById(id).orElseThrow(() -> new DoValidException("服务指引为查询到，指引id：" + id));

        //上一条记录
        List<WayEntity> wayUps = wayDao.findAllByTypeAndSeqLessThanOrderBySeqDesc(way.getType(), way.getSeq());

        if (wayUps.size() < 1) {
            throw new DoValidException("第一条无法上移");
        }
        WayEntity wayUp = wayUps.get(0);

        Integer tempSeq = way.getSeq();
        way.setSeq(wayUp.getSeq());
        wayUp.setSeq(tempSeq);

        wayDao.save(way);
        wayDao.save(wayUp);

    }

    /**
     * 服务指引下移
     */
    public void guideDown(String id) throws DoValidException {
        //当前way记录
        WayEntity way = wayDao.findById(id).orElseThrow(() -> new DoValidException("服务指引未查询到，指引id：" + id));

        //下一条记录
        List<WayEntity> wayDowns = wayDao.findAllByTypeAndSeqGreaterThanOrderBySeq(way.getType(), way.getSeq());

        if (wayDowns.size() < 1) {
            throw new DoValidException("最后一条无法下移");
        }
        WayEntity wayDown = wayDowns.get(0);

        Integer tempSeq = way.getSeq();
        way.setSeq(wayDown.getSeq());
        wayDown.setSeq(tempSeq);

        wayDao.save(way);
        wayDao.save(wayDown);
    }

    /**
     * 获取服务指引列表--通过角色Id标记已选列表
     */
    public WayRoleResVm guideRoleGet(WayRoleFindVm model) {
        String wayType = "1";
        //所有way-类型为0 （0代办、1服务指引)
        List<WayEntity> ways = wayDao.findAllByTypeOrderBySeq(wayType);

        //已选择way
        List<WayRoleEntity> wayRoles = wayRoleDao.findAllByRoleIdAndType(model.getRoleId(),wayType);
        List<String> wayRoleIds = wayRoles.stream().map(WayRoleEntity::getWayId).collect(Collectors.toList());

        //结果
        WayRoleResVm res = new WayRoleResVm();
        res.setWayEntities(ways);
        res.setChoose(wayRoleIds);

        return res;
    }

    /**
     * 保存服务指引列表--关联角色保存
     */
    public void guideRoleInsert(WayRoleInsertVm model) {
        String wayType = "1";

        //历史选择wayRoles
        List<WayRoleEntity> wayRoles = wayRoleDao.findAllByRoleIdAndType(model.getRoleId(),wayType);
        wayRoleDao.deleteAll(wayRoles);

        //角色
        RoleEntity role = roleDao.findById(model.getRoleId()).orElse(null);

        //代办角色记录s
        List<WayRoleEntity> results = new ArrayList<>();
        for (String wayId : model.getWayIds()) {

            //代办
            WayEntity way = wayDao.findById(wayId).orElse(null);

            //代办角色记录
            WayRoleEntity res = new WayRoleEntity();
            res.setId(BaseUtils.generate32Id());
            res.setType(wayType);
            res.setRoleId(model.getRoleId());
            res.setRoleName(role == null ? "" : role.getName());
            res.setWayId(wayId);
            res.setWayName(way == null ? "" : way.getName());
            res.setCreateTime(Calendar.getInstance());
            res.setUpdateTime(Calendar.getInstance());

            results.add(res);
        }
        wayRoleDao.saveAll(results);
    }

    /**
     * 获取当前用户服务指引-门户使用
     * @return
     * @throws DoValidException
     */
    public List<WayEntity> guideFindAllWeb()throws  DoValidException{
        JSONObject userObj=TokenManager.getInstance().getUserByToken(request);
        String roleId = userObj.getString("roleId");

        List<WayRoleEntity>wayRoles=wayRoleDao.findAllByRoleIdAndType(roleId,"1");
        List<String> wayIds=wayRoles.stream().map(WayRoleEntity::getWayId).collect(Collectors.toList());

        List<WayEntity>ways=wayDao.findAllByIdIn(wayIds);
        return ways.stream().sorted(Comparator.comparing(WayEntity::getSeq)).collect(Collectors.toList());
    }

}
