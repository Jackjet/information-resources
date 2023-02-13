package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.entity.AssetFileExEntity;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.AssetFileExFindAllVm;
import d1.project.resourcesharingmgmt.resource.service.AssetFileExService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 云文件挂接
 * @author maoyuying
 */
@Auth("webadmin")
@RestController("/webadmin/assetFileEx")
@RequestMapping("/webadmin/assetFileEx")
public class AssetFileExController {
    private final AssetFileExService assetFileExService;

    public AssetFileExController(AssetFileExService assetFileExService){
        this.assetFileExService = assetFileExService;
    }

    /**
     * 删除资源目录下云文件
     * @param id
     * @param request
     * @return
     * @throws DoValidException
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) throws DoValidException {
        assetFileExService.delete(id,request);
        return ResultUtil.success("删除成功");
    }

    /**
     * 批量删除资源目录下云文件
     * @param ids
     * @param request
     * @return
     * @throws DoValidException
     */
    @DeleteMapping(value = "/deleteAll")
    public Result<String> deleteAll(@Valid @Size(min = 1, message = "无效操作") @RequestParam("ids") List<String> ids, HttpServletRequest request) throws DoValidException {
        assetFileExService.deleteAll(ids,request);
        return ResultUtil.success("删除成功");
    }


    /**
     * 云文件详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<AssetFileExEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", assetFileExService.find(id).orElseThrow(() -> new DoValidException("信息资源目录与云数据关联表信息不存在")));
    }

    /**
     *查询资源目录下，云文件(分页)
     * @param model
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAll")
    public Result<AssetFileExEntity> findAll(AssetFileExFindAllVm model) throws Exception {
        return ResultUtil.success("", assetFileExService.findAll(model));
    }
}
