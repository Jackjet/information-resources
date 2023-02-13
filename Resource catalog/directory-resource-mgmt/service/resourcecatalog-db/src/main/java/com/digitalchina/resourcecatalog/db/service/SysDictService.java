package com.digitalchina.resourcecatalog.db.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digitalchina.resourcecatalog.db.domain.ArchOrg;
import com.digitalchina.resourcecatalog.db.domain.SysDict;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 字典表 服务类
 * </p>
 *
 * @author LiangChuanQi
 * @since 2020-05-09
 */
public interface SysDictService extends IService<SysDict> {
    /**
     * 获取全部树结构
     * @return
     */
    public List<SysDict> getTree();

    /**
     * 删除当前级及子节点
     *
     * @param id
     */
    void deleteTree(Integer id);

    IPage<List<SysDict>> getNotUseList(IPage page, Integer pid);
}
