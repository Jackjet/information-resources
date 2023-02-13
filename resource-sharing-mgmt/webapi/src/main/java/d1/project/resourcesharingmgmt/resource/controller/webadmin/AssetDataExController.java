package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.entity.AssetDataExEntity;
import d1.project.resourcesharingmgmt.resource.service.AssetDataExService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 云数据挂接
 * @author maoyuying
 */
@Auth("webadmin")
@RestController("/webadmin/assetDataEx")
@RequestMapping("/webadmin/assetDataEx")
public class AssetDataExController {
    private final AssetDataExService assetDataExService;

    public AssetDataExController(AssetDataExService assetDataExService){
        this.assetDataExService = assetDataExService;
    }

    /**
     * 删除资源目录下云数据
     * @param id
     * @param request
     * @return
     * @throws DoValidException
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id, HttpServletRequest request) throws DoValidException {
        assetDataExService.delete(id,request);
        return ResultUtil.success("删除成功");
    }

    /**
     * 批量删除资源目录下云数据
     * @param ids
     * @param request
     * @return
     * @throws DoValidException
     */
    @DeleteMapping(value = "/deleteAll")
    public Result<String> deleteAll(@Valid @Size(min = 1, message = "无效操作") @RequestParam("ids") List<String> ids, HttpServletRequest request) throws DoValidException {
        assetDataExService.deleteAll(ids,request);
        return ResultUtil.success("删除成功");
    }


    /**
     * 详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<AssetDataExEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", assetDataExService.find(id).orElseThrow(() -> new DoValidException("信息资源目录与云数据关联表信息不存在")));
    }

    /**
     * 查找云数据挂接信息
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findByUviewId")
    public Result<AssetDataExEntity> findByUviewId(String id) throws DoValidException {
        return ResultUtil.success("", assetDataExService.findByUviewId(id));
    }
}
