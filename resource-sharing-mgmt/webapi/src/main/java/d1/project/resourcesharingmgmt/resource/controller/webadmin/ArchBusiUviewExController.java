package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.ArchBusiUviewExBusiness;
import d1.project.resourcesharingmgmt.resource.entity.ArchBusiUviewExEntity;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExUpdateVm;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExVm;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 信息资源目录管理
 *
 * @author zhengyang
 */
@Auth("webadmin")
@RestController("/webadmin/archBusiUviewEx")
@RequestMapping("/webadmin/archBusiUviewEx")
public class ArchBusiUviewExController {

    private final ArchBusiUviewExBusiness archBusiUviewExBusiness;

    public ArchBusiUviewExController(ArchBusiUviewExBusiness archBusiUviewExBusiness) {
        this.archBusiUviewExBusiness = archBusiUviewExBusiness;
    }

    /**
     * 根据Id,上下架资源目录
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody ArchBusiUviewExUpdateVm model, HttpServletRequest request) throws DoValidException {
        archBusiUviewExBusiness.update(model,request);
        return ResultUtil.success("操作成功");
    }

    /**
     * 资源目录详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<ArchBusiUviewExVm> find(String id, HttpServletRequest request) throws DoValidException {
        return ResultUtil.success("", archBusiUviewExBusiness.find(id, request));
    }

    /**
     * 查询资源目录信息
     * @param model
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAll")
    public Result<ArchBusiUviewExEntity> findAll(ArchBusiUviewExFindAllVm model, HttpServletRequest request) throws Exception {
        return ResultUtil.success("",archBusiUviewExBusiness.findAllBySql(model, request));
    }

    /**
     * 同步资源目录等信息
     * @return
     */
    @Auth("noauth")
    @GetMapping(value = "/syncData")
    public Result<String> syncData() throws Exception {
        try {
            archBusiUviewExBusiness.syncData();
            return ResultUtil.success("同步成功");
        }catch (Exception e){
            return ResultUtil.fail("同步失败",e);
        }
    }

    /**
     * 批量文件挂载
     * @return
     */
    @Auth("noauth")
    @GetMapping(value = "/fileMount")
    public Result<String> fileMount() throws Exception {
        try {
            archBusiUviewExBusiness.fileMount();
            return ResultUtil.success("挂载成功");
        }catch (Exception e){
            return ResultUtil.fail("挂载失败",e.getMessage());
        }
    }
}
