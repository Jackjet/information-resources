package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import com.alibaba.excel.EasyExcel;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.DemandedInfoBusiness;
import d1.project.resourcesharingmgmt.resource.entity.DemandedInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.*;
import d1.project.resourcesharingmgmt.resource.service.DemandedInfoService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.net.URLEncoder;
import java.util.List;

/**
 * 需求管理
 *
 * @author zhengyang
 */
@Auth("webadmin")
@RestController("/webadmin/demandedInfo")
@RequestMapping("/webadmin/demandedInfo")
public class DemandedInfoController {
    private final DemandedInfoService demandedInfoService;
    private final DemandedInfoBusiness demandedInfoBusiness;

    public DemandedInfoController(DemandedInfoService demandedInfoService,DemandedInfoBusiness demandedInfoBusiness) {
        this.demandedInfoService = demandedInfoService;
        this.demandedInfoBusiness=demandedInfoBusiness;
    }

    /**
     * 审批
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody DemandedInfoUpdateVm model, HttpServletRequest request) throws DoValidException {
        demandedInfoService.update(model, request);
        return ResultUtil.success("");
    }

    /**
     * 查询当前登录人需要受理的需求
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAllByAcceptDeptId")
    public Result<DemandedInfoEntity> findAllByAcceptDeptId(DemandedInfoFindAllVm model,HttpServletRequest request) throws Exception{
        return ResultUtil.success("", demandedInfoBusiness.findAllByAcceptDeptId(model,request));
    }

    /**
     * 查询所有
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAll")
    public Result<DemandedInfoEntity> findAll(DemandedInfoFindAllVm model) throws Exception{
        if("headquarters".equals(model.getCreateDeptId())){
            model.setCreateDeptId("");
        }
        return ResultUtil.success("", demandedInfoService.findAll(model));
    }

    /**
     * 详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<DemandedInfoVm> find(String id) throws DoValidException {
        return ResultUtil.success("", demandedInfoBusiness.find(id));
    }

    /**
     * 需求统计按照部门
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findDemandedInfoCountByProvOrgId")
    public Result<Page<DemandedInfoByProvOrgIdVm>> findDemandedInfoCountByProvOrgId(DemandedInfoFindByProvOrgIdVm model) throws Exception {
        return ResultUtil.success("",demandedInfoBusiness.findResourceUseInfoByProvOrgId(model));
    }

    /**
     * 需求统计导出
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/exportDemandedInfoCountByProvOrgId")
    public Result<String> exportDemandedInfoCountByProvOrgId(DemandedInfoFindByProvOrgIdVm model, HttpServletResponse response) throws Exception {
        List<DemandedInfoByProvOrgIdExport> allList = demandedInfoBusiness.exportDemandedInfoCountByProvOrgId(model);

        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("需求统计", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), DemandedInfoByProvOrgIdExport.class).sheet("需求统计").doWrite(allList);
        return null;
    }
}
