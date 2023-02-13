package com.digitalchina.resourcecatalog.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digitalchina.resourcecatalog.db.domain.SysDict;
import com.digitalchina.resourcecatalog.db.dao.SysDictMapper;
import com.digitalchina.resourcecatalog.db.service.SysDictService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 字典表 服务实现类
 * </p>
 *
 * @author LiangChuanQi
 * @since 2020-05-09
 */
@Service
public class SysDictServiceImpl extends ServiceImpl<SysDictMapper, SysDict> implements SysDictService {
    public List<SysDict> getTree(){
        List<SysDict> sysDictList = this.baseMapper.selectList(new QueryWrapper<SysDict>().lambda().isNull(SysDict::getPid).orderByAsc(SysDict::getValue));
        List<SysDict> sysDictChildList = this.baseMapper.selectList(new QueryWrapper<SysDict>().lambda().isNotNull(SysDict::getPid).select(SysDict::getId, SysDict::getPid, SysDict::getName, SysDict::getValue));
        for (SysDict sysDict : sysDictList) {
            sysDict.setChildren(getChildrenTree(sysDict.getId(), sysDictChildList));
        }
        return sysDictList;
    }

    @Override
    @Transactional
    public void deleteTree(Integer id) {
        List<SysDict> sysDictList = this.baseMapper.selectList(new QueryWrapper<SysDict>().lambda().eq(SysDict::getPid, id));
        for (SysDict sysDict : sysDictList) {
            deleteTrees(sysDict.getId());
        }
        this.baseMapper.delete(new UpdateWrapper<SysDict>().lambda()
                .eq(SysDict::getPid, id));
        this.baseMapper.deleteById(id);
    }

    @Override
    public IPage<List<SysDict>> getNotUseList(IPage page, Integer pid){
        return this.baseMapper.getNotUseList(page, pid);
    }

    /***
     * 递归获取树
     * @param parentId
     * @return
     */
    private List<SysDict> getChildrenTree(Integer parentId, List<SysDict> sysDictChildList) {
        List<SysDict> list = new ArrayList<>();
        List<SysDict> children = sysDictChildList.stream().filter(item -> item.getPid()!=null && item.getPid().equals(parentId)).collect(Collectors.toList());
        sysDictChildList.removeAll(children);
        for (SysDict sysDict : children) {
            sysDict.setChildren(getChildrenTree(sysDict.getId(), sysDictChildList));
            list.add(sysDict);
        }
        return list;
    }

    /***
     * 删除子节点递归
     * @param pId
     */
    private void deleteTrees(Integer pId) {
        List<SysDict> sysDictList = this.baseMapper.selectList(new QueryWrapper<SysDict>().lambda().eq(SysDict::getPid, pId));
        for (SysDict sysDict : sysDictList) {
            deleteTrees(sysDict.getId());
        }
        this.baseMapper.delete(new UpdateWrapper<SysDict>().lambda()
                .eq(SysDict::getPid, pId));
    }
}
