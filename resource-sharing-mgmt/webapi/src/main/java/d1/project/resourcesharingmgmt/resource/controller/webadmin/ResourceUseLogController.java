package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import com.alibaba.excel.EasyExcel;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.entity.ResourceUseLogEntity;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseLogExcelExport;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseLogFindAllVm;
import d1.project.resourcesharingmgmt.resource.service.ResourceUseLogService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * 管理端-资源使用日志
 *
 * @author zhengyang
 */
@Auth("webadmin")
@RestController("/webadmin/resourceUseLog")
@RequestMapping("/webadmin/resourceUseLog")
public class ResourceUseLogController {

    private final ResourceUseLogService resourceUseLogService;

    public ResourceUseLogController(ResourceUseLogService resourceUseLogService) {
        this.resourceUseLogService = resourceUseLogService;
    }

    /**
     * 详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findById")
    public Result<ResourceUseLogEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", resourceUseLogService.find(id));
    }

    /**
     * 查询分页列表
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAll")
    public Result<ResourceUseLogEntity> findAll(ResourceUseLogFindAllVm model) throws Exception{
        return ResultUtil.success("", resourceUseLogService.findAll(model));
    }

    /**
     * 资源使用日志导出
     * @param model
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/exportByProvOrgId")
    public Result<String> exportByProvOrgId(ResourceUseLogFindAllVm model, HttpServletResponse response) throws Exception{
        List<ResourceUseLogExcelExport> allList = resourceUseLogService.findExportList(model);
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("资源使用日志", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ResourceUseLogExcelExport.class).sheet("资源使用日志").doWrite(allList);
        return null;
    }
}
