package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.entity.AssetExLogEntity;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.AssetExLogFindAllVm;
import d1.project.resourcesharingmgmt.resource.service.AssetExLogService;
import org.springframework.web.bind.annotation.*;

/**
 * 云文件和云数据库挂接申请日志
 * @author zhengyang
 */
@Auth("webadmin")
@RestController("/webadmin/assetExLog")
@RequestMapping("/webadmin/assetExLog")
public class AssetExLogController {
    private final AssetExLogService assetExLogService;

    public AssetExLogController(AssetExLogService assetExLogService){
        this.assetExLogService = assetExLogService;
    }

    /**
     * 详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<AssetExLogEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", assetExLogService.find(id).orElseThrow(() -> new DoValidException("信息资源目录与云数据关联表信息不存在")));
    }

    /**
     * 查询日志分页
     * @param model
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAll")
    public Result<AssetExLogEntity> findAll(AssetExLogFindAllVm model) throws Exception {
        return ResultUtil.success("", assetExLogService.findAll(model));
    }
}
