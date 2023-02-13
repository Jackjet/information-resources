package com.digitalchina.resourcecatalog.db.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.digitalchina.resourcecatalog.db.domain.FileCenterRel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 资料中心文件关联表 服务类
 * </p>
 *
 * @author wangmh
 * @since 2020-05-21
 */
public interface FileCenterRelService extends IService<FileCenterRel> {

    IPage<Map<String, Object>> myPage(IPage page, String fileName);

    boolean updateInfo(FileCenterRel fileCenterRel);

    boolean removeInfo(List<Integer> ids);

    Map<String, Object> detail(Integer id);
}
