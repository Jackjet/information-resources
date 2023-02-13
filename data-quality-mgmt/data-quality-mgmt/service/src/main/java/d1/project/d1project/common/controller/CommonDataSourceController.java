package d1.project.d1project.common.controller;


import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.d1project.business.entity.DataSource;
import d1.project.d1project.common.service.IDataSourceService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 通用接口_数据源
 *
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/common/datasource")
public class CommonDataSourceController {
    private final IDataSourceService iDataSourceService;

    public CommonDataSourceController(IDataSourceService iDataSourceService) {
        this.iDataSourceService = iDataSourceService;
    }

    /**
     * 查询数据源
     */
    @GetMapping("/findAll")
    public Result<List<DataSource>> findAll() {
        try {
            return ResultUtil.success("成功", iDataSourceService.findAllEnable());
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }
}
