package com.digitalchina.resourcecatalog.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digitalchina.resourcecatalog.db.domain.CataInfoApprove;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 信息资源目录审核记录表 服务类
 * </p>
 *
 * @author baokd
 * @since 2020-05-15
 */
public interface CataInfoApproveService extends IService<CataInfoApprove> {
    public IPage<List<Map<String,Object>>> getCataInfoList(IPage page, String name, Integer pid, String auditStatus);

    public IPage<List<Map<String,Object>>> getDeleteCheckCataInfoList(IPage page, String name, Integer pid, String auditStatus, String deleteStatus);

    public boolean approveSecond(String optType, List<Integer> infoIds,String approveStauts,Integer userId,String userName,String comment);

    public boolean approve(String optType, List<Integer> infoIds,String cataInfoStatusNew, String approveStatus,Integer userId,String userName,String comment);
}
