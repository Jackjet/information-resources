package com.digitalchina.resourcecatalog.db.service;

import com.digitalchina.resourcecatalog.db.domain.DictAssetCate;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 目录分类管理 服务类
 * </p>
 *
 * @author baokd
 * @since 2020-05-12
 */
public interface DictAssetCateService extends IService<DictAssetCate> {
    public Integer getMaxCodeByPid(Integer pid);

    public List<DictAssetCate> getTreeByPid(Integer pid);

    public List<DictAssetCate> getTreeByPidAndOrgId(DictAssetCate dictAssetCate,List<Integer> orgIds);

    public List<DictAssetCate> getTreeByPower(Integer userId);

    public boolean saveAndUpdateUserType(DictAssetCate dictAssetCate,Integer userId);

    public boolean deleteAndUpdateUserType(DictAssetCate dictAssetCate);
    Map<String, Object> generateCode(Integer pId);
}
