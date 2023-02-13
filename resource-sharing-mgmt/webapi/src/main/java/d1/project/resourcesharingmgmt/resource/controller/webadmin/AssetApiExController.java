package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.entity.AssetApiExEntity;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.*;
import d1.project.resourcesharingmgmt.resource.service.AssetApiExService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 云接口挂接
 * @author maoyuying
 */
@Auth("webadmin")
@RestController("/webadmin/assetApiEx")
@RequestMapping("/webadmin/assetApiEx")
public class AssetApiExController {
    private final AssetApiExService assetApiExService;

    public AssetApiExController(AssetApiExService assetApiExService){
        this.assetApiExService = assetApiExService;
    }

    /**
     * 删除资源目录下云接口
     * @param id
     * @param request
     * @return
     * @throws DoValidException
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) throws DoValidException {
        assetApiExService.delete(id,request);
        return ResultUtil.success("删除成功");
    }

    /**
     * 批量删除资源目录下云接口
     * @param ids
     * @param request
     * @return
     * @throws DoValidException
     */
    @DeleteMapping(value = "/deleteAll")
    public Result<String> deleteAll(@Valid @Size(min = 1, message = "无效操作") @RequestParam("ids") List<String> ids, HttpServletRequest request) throws DoValidException {
        assetApiExService.deleteAll(ids,request);
        return ResultUtil.success("删除成功");
    }

    /**
     * 资源目录下，新增云接口
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody AssetApiExInsertVm model, HttpServletRequest request) throws DoValidException {
        assetApiExService.insert(model,request);
        return ResultUtil.success("添加成功");
    }

    /**
     * 资源目录下，批量新增云接口
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PostMapping(value = "/insertAll")
    public Result<String> insertAll(@Valid @RequestBody AssetApiExInsertAllVm model, HttpServletRequest request) throws DoValidException {
        assetApiExService.insertAll(model,request);
        return ResultUtil.success("添加成功");
    }

    /**
     * 根据资源目录与云数据关联ID,查询接口详情信息(资源使用管理API详情)
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<AssetApiExEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", assetApiExService.find(id).orElseThrow(() -> new DoValidException("信息资源目录与云数据关联表信息不存在")));
    }

    @GetMapping(value = "/findByApiId")
    public Result<ApiEntityVm> findByApiId(String id) throws Exception {
        return ResultUtil.success("", assetApiExService.findByApiId(id));
    }

    /**
     *查询资源目录下，云接口(分页)
     * @param model
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAll")
    public Result<AssetApiExEntity> findAll(AssetApiExFindAllVm model) throws Exception {
        return ResultUtil.success("", assetApiExService.findAll(model));
    }

    /**
     * 查询所有云接口,调用api系统接口
     * @param model
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAllApi")
    public Result<Page<ApiEntityVm>> findAllApi(AssetApiExFindAllVm model) throws Exception {
        return ResultUtil.success("",assetApiExService.findAllByApi(model));
    }

    /**
     * 查询所有云接口,调用api系统接口
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findGroupTree")
    public Result<List<ApiGroupVm>> findGroupTree() throws Exception {
        return ResultUtil.success("",assetApiExService.findGroupTree());
    }
}
