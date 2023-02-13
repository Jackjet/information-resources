package com.digitalchina.resourcecatalog.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digitalchina.resourcecatalog.db.domain.FileCenterRel;
import com.digitalchina.resourcecatalog.db.dao.FileCenterRelMapper;
import com.digitalchina.resourcecatalog.db.domain.SysStorage;
import com.digitalchina.resourcecatalog.db.service.FileCenterRelService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.digitalchina.resourcecatalog.db.service.SysStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 资料中心文件关联表 服务实现类
 * </p>
 *
 * @author wangmh
 * @since 2020-05-21
 */
@Service
public class FileCenterRelServiceImpl extends ServiceImpl<FileCenterRelMapper, FileCenterRel> implements FileCenterRelService {

    @Autowired
    SysStorageService sysStorageService;

    @Override
    public IPage<Map<String, Object>> myPage(IPage page, String fileName) {
        return this.baseMapper.myPage(page, fileName);
    }

    @Override
    @Transactional
    public boolean updateInfo(FileCenterRel fileCenterRel) {
        FileCenterRel oldFileCenterRel = this.baseMapper.selectById(fileCenterRel.getId());
        if (!oldFileCenterRel.getStorageId().equals(fileCenterRel.getStorageId())) {
            SysStorage sysStorage = new SysStorage();
            sysStorage.setId(oldFileCenterRel.getStorageId());
            sysStorage.setDeleted(1);
            //逻辑删除附件表
            sysStorageService.updateById(sysStorage);
            fileCenterRel.setDwCount(0);
            fileCenterRel.setCreateTime(new Date());
        }
        //更新
        this.baseMapper.updateById(fileCenterRel);
        return true;
    }

    @Override
    @Transactional
    public boolean removeInfo(List<Integer> ids) {
        List<FileCenterRel> fileCenterRels = this.baseMapper.selectBatchIds(ids);
        SysStorage sysStorage = new SysStorage();
        sysStorage.setDeleted(1);
        //逻辑删除附件表
        sysStorageService.update(sysStorage, new UpdateWrapper<SysStorage>().lambda().in(SysStorage::getId,
                fileCenterRels.stream().map(FileCenterRel::getStorageId).collect(Collectors.toList())));
        //删除附件表
        this.baseMapper.deleteBatchIds(ids);
        return true;
    }

    @Override
    public Map<String, Object> detail(Integer id) {
        return this.baseMapper.detail(id);
    }
}
