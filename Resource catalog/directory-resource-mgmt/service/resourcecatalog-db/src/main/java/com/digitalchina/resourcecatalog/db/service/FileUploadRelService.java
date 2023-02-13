package com.digitalchina.resourcecatalog.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digitalchina.resourcecatalog.db.domain.FileUploadRel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文件上传关联表 服务类
 * </p>
 *
 * @author wangmh
 * @since 2020-05-21
 */
public interface FileUploadRelService extends IService<FileUploadRel> {

    /***
     * 分页
     * @param page
     * @param name
     * @param status
     * @param startTime
     * @param endTime
     * @param userId
     * @param deptId
     * @return
     */
    IPage<Map<String, Object>> page(IPage page, String name, String status, String startTime, String endTime, Integer userId, Integer deptId);

    /***
     * 详情
     * @param id
     * @return
     */
    Map<String, Object> detail(Integer id);

    /***
     * 编辑
     * @param fileUploadRel
     */
    void updateInfo(FileUploadRel fileUploadRel);

    /***
     * 待审核文件List
     * @param s
     * @return
     */
    List<Map<String, Object>> waitReviewFileList(String status);

    /**
     * 删除
     */
	String delete(Integer id);
}
