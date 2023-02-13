package com.digitalchina.resourcecatalog.db.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections4.keyvalue.DefaultKeyValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalchina.resourcecatalog.db.dao.ArchOrgMapper;
import com.digitalchina.resourcecatalog.db.domain.ArchOrg;
import com.digitalchina.resourcecatalog.db.domain.Result;
//import com.digitalchina.resourcecatalog.db.rabbitmq.RabbitConfig;
import com.digitalchina.resourcecatalog.db.service.ArchBusiService;
import com.digitalchina.resourcecatalog.db.service.ArchOrgService;
//import com.digitalchina.resourcecatalog.db.service.MqResourceService;

/**
 * <p>
 * 组织机构 服务实现类
 * </p>
 *
 * @author wangmh
 * @since 2020-05-09
 */
@Service
public class ArchOrgServiceImpl extends ServiceImpl<ArchOrgMapper, ArchOrg> implements ArchOrgService {

    @Value("${resourcecatalog.tree.init.dept}")
    private String initDept;

    @Autowired
    ArchBusiService archBusiService;
    
//    @Autowired
//    private MqResourceService mqResourceService;

    @Override
    public List<ArchOrg> myPage(Integer orgId, List<Integer> deptIds, boolean isDeptAdmin) {
        List list = new ArrayList();
        List<ArchOrg> archOrgList;
        if (isDeptAdmin) {//只显示当前部门及其组织机构
            if (deptIds == null || deptIds.size() < 1) {
                return null;
            }
            if (orgId == null || orgId == 0) {
                archOrgList = this.baseMapper.selectList(new QueryWrapper<ArchOrg>().lambda().in(ArchOrg::getOrgId, deptIds).orderByAsc(ArchOrg::getDispalySn));
            } else {
                archOrgList = getOrgListByPId(orgId);
            }
        } else {
            if (orgId == null) {
                archOrgList = this.baseMapper.selectList(new QueryWrapper<ArchOrg>().lambda().isNull(ArchOrg::getParOrgId).orderByAsc(ArchOrg::getDispalySn));
            } else {
                if (orgId == 0) {
                    return this.baseMapper.selectList(new QueryWrapper<ArchOrg>().lambda().orderByAsc(ArchOrg::getDispalySn));
                }
                archOrgList = getOrgListByPId(orgId);
            }
        }
        for (ArchOrg archOrg : archOrgList) {
            list.add(archOrg);
            getChildrenList(archOrg.getOrgId(), list);
        }
        return list;
    }

    private List<ArchOrg> getOrgListByPId(Integer orgId) {
        return this.baseMapper.selectList(new QueryWrapper<ArchOrg>().lambda().eq(ArchOrg::getParOrgId, orgId).orderByAsc(ArchOrg::getDispalySn));
    }

    /***
     * 获取子集集合
     * @param parentId
     * @param list
     * @return
     */
    private List<ArchOrg> getChildrenList(Integer parentId, List list) {
        List<ArchOrg> archOrgList = this.baseMapper.selectList(new QueryWrapper<ArchOrg>().lambda().eq(ArchOrg::getParOrgId, parentId).orderByAsc(ArchOrg::getDispalySn));
        for (ArchOrg archOrg : archOrgList) {
            list.add(archOrg);
            getChildrenList(archOrg.getOrgId(), list);
        }
        return list;
    }

    @Override
    public List<ArchOrg> tree(Integer isTop, boolean idDeptAdmin, List<Integer> deptIds) {
        ArchOrg archOrg = new ArchOrg();
        archOrg.setOrgNm(initDept);
        archOrg.setOrgId(0);
        archOrg.setOrgCd("0");
        archOrg.setChildren(findTypeTree(isTop, idDeptAdmin, deptIds));
        List<ArchOrg> archOrgs = new ArrayList<>();
        archOrgs.add(archOrg);
        return archOrgs;
    }

    @Override
    public List<ArchOrg> tree2(boolean idDeptAdmin, List<Integer> deptIds) {
        ArchOrg archOrg = new ArchOrg();
        archOrg.setOrgNm(initDept);
        archOrg.setOrgId(0);
        archOrg.setOrgCd("0");
        archOrg.setChildren(findTypeTree(1, idDeptAdmin, deptIds));
        List<ArchOrg> archOrgs = new ArrayList<>();
        archOrgs.add(archOrg);
        return archOrgs;
    }

    @Override
    public ArchOrg detail(Integer orgId) {
        return this.baseMapper.detail(orgId);
    }

    @Override
    public Integer getMaxOrgCdByPidIsNull() {
        return this.baseMapper.getMaxOrgCdByPidIsNull();
    }

    @Override
    public Integer getMaxOrgCdByPid(Integer parOrgId) {
        return this.baseMapper.getMaxOrgCdByPid(parOrgId);
    }

    @Override
    @Transactional
    public void deleteTree(Integer orgId) {
        List<ArchOrg> archOrgList = this.baseMapper.selectList(new QueryWrapper<ArchOrg>().lambda().eq(ArchOrg::getParOrgId, orgId));
        for (ArchOrg archOrg : archOrgList) {
            deleteTrees(archOrg.getOrgId());
        }
        this.baseMapper.delete(new UpdateWrapper<ArchOrg>().lambda()
                .eq(ArchOrg::getParOrgId, orgId));
        this.baseMapper.deleteById(orgId);
//        mqResourceService.sendMessage(RabbitConfig.ORG_DELETE, orgId);
    }

    /***
     * 删除子节点递归
     * @param parOrgId
     */
    private void deleteTrees(Integer parOrgId) {
        List<ArchOrg> archOrgList = this.baseMapper.selectList(new QueryWrapper<ArchOrg>().lambda().eq(ArchOrg::getParOrgId, parOrgId));
        for (ArchOrg archOrg : archOrgList) {
            deleteTrees(archOrg.getOrgId());
        }
        this.baseMapper.delete(new UpdateWrapper<ArchOrg>().lambda()
                .eq(ArchOrg::getParOrgId, parOrgId));
    }


    /***
     * 判断是部门还是组织
     * @param isTop
     * @return
     */
    public List<ArchOrg> findTypeTree(Integer isTop, boolean idDeptAdmin, List<Integer> deptIds) {
        //判断用户的权限
        QueryWrapper<ArchOrg> queryWrapper = new QueryWrapper<ArchOrg>();
        queryWrapper.select(ArchOrg.ORG_ID, ArchOrg.PAR_ORG_ID, ArchOrg.ORG_CD, ArchOrg.ORG_NM, ArchOrg.SOCIAL_CREDIT_CD);
        if (deptIds!=null && deptIds.size()>0) {//只显示部门树
            queryWrapper.in(ArchOrg.ORG_ID, deptIds);
        }
        queryWrapper.orderByAsc(ArchOrg.DISPALY_SN);
        List<ArchOrg> archOrgList  = this.baseMapper.selectList(queryWrapper);
        List<ArchOrg> firstLevel = archOrgList.stream().filter(item -> item.getParOrgId()==null).collect(Collectors.toList());
        if (isTop != 0) {
            archOrgList.removeAll(firstLevel);
            for (ArchOrg archOrg : firstLevel) {
                archOrg.setChildren(getChildrenTree(archOrg.getOrgId(), archOrgList));
            }
        }
        return firstLevel;
    }

    /***
     * 递归获取树
     * @param parentId
     * @return
     */
    public List<ArchOrg> getChildrenTree(Integer parentId, List<ArchOrg> archOrgList) {
        List<ArchOrg> list = new ArrayList<>();
        List<ArchOrg> children = archOrgList.stream().filter(item -> item.getOrgId()!=null && item.getParOrgId().equals(parentId)).collect(Collectors.toList());
        archOrgList.removeAll(children);
        for (ArchOrg archOrg : children) {
            archOrg.setChildren(getChildrenTree(archOrg.getOrgId(), archOrgList));
            list.add(archOrg);
        }
        return list;
    }

    @Override
    public List<DefaultKeyValue<Integer, String>> getDeptsByOrgIds(List<ArchOrg> orgList) {
        List<String> deptCodes = new ArrayList<String>(orgList.size());
        orgList.forEach(org1 -> {
            if (org1 != null && org1.getOrgCd().length() > 1)
                deptCodes.add(org1.getOrgCd().substring(0, 2));
        });
        List<ArchOrg> deptList = this.baseMapper.selectList(new QueryWrapper<ArchOrg>().lambda().in(ArchOrg::getOrgCd, deptCodes).orderByAsc(ArchOrg::getDispalySn));
        List<DefaultKeyValue<Integer, String>> deptIdList = new ArrayList<DefaultKeyValue<Integer, String>>(deptList.size());
        deptList.forEach(dept -> deptIdList.add(new DefaultKeyValue(dept.getOrgId(), dept.getOrgNm())));
        return deptIdList;
    }

    @Override
    public List<ArchOrg> treeAppOrg(Integer orgId) {
        QueryWrapper<ArchOrg> queryWrapper = new QueryWrapper<ArchOrg>();
        queryWrapper.select(ArchOrg.ORG_ID, ArchOrg.PAR_ORG_ID, ArchOrg.ORG_CD, ArchOrg.ORG_NM, ArchOrg.SOCIAL_CREDIT_CD);
        queryWrapper.eq(ArchOrg.PAR_ORG_ID, orgId);
        queryWrapper.orderByAsc(ArchOrg.DISPALY_SN);
        return this.baseMapper.selectList(queryWrapper);
    }
    @Override
    public List<Result> top10Dept(String shareType) {
        return this.baseMapper.top10Dept(shareType);
    }

    @Override
    public Integer getDispalySn(Integer parOrgId) {
        return this.baseMapper.getDispalySn(parOrgId);
    }

    @Override
    public List<Map<String, Object>> getMaxSecondLevel(){
        return this.baseMapper.getMaxSecondLevel();
    }
}
