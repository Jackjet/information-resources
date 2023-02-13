package com.digitalchina.resourcecatalog.db.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digitalchina.resourcecatalog.db.domain.CataInfoApprove;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 信息资源目录审核记录表 Mapper 接口
 * </p>
 *
 * @author baokd
 * @since 2020-05-15
 */
public interface CataInfoApproveMapper extends BaseMapper<CataInfoApprove> {
    public IPage<List<Map<String,Object>>> getCataInfoList(IPage page, @Param("name") String name, @Param("pid")Integer pid, @Param("auditStatus")String auditStatus);
    public IPage<List<Map<String,Object>>> getDeleteCheckCataInfoList(IPage page, @Param("name") String name, @Param("pid")Integer pid, @Param("auditStatus")String auditStatus, @Param("deleteStatus")String deleteStatus);

    public List<Map<String,Object>> getApproveList(IPage page,  @Param("infoId")Integer infoId);
}
