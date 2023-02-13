package com.digitalchina.resourcecatalog.admin.web;


import io.swagger.annotations.Api;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 权限表 前端控制器
 * </p>
 *
 * @author wangmh
 * @since 2020-04-30
 */
@RestController
@RequestMapping("/admin/permission")
public class SysPermissionController {
    private final Log logger = LogFactory.getLog(SysPermissionController.class);

}
