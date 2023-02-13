package com.digitalchina.resourcecatalog.db.dao;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digitalchina.resourcecatalog.db.domain.FileUploadRel;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件上传关联表 Mapper 接口
 * </p>
 *
 * @author wangmh
 * @since 2020-05-21
 */
public interface FileUploadRelMapper extends BaseMapper<FileUploadRel> {
    IPage<Map<String, Object>> page(IPage page, @Param("name") String name, @Param("status") String status, @Param("startTime") String startTime, @Param("endTime") String endTime, @Param("userId") Integer userId, @Param("deptId") Integer deptId);

    Map<String, Object> detail(@Param("id") Integer id);

    List<Map<String, Object>> waitReviewFileList(@Param("status") String status);
}
