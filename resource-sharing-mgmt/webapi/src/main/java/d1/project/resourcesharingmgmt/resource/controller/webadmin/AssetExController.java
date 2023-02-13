package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.common.Constants;
import d1.project.resourcesharingmgmt.resource.entity.AssetExEntity;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.AssetExExamineVm;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.AssetExFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.AssetExInsertVm;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.AssetExUpdateVm;
import d1.project.resourcesharingmgmt.resource.service.AssetExService;
import d1.project.resourcesharingmgmt.system.model.vm.IdBaseVm;
import net.dcrun.component.file.server.IFileServerService;
import net.dcrun.component.file.server.UploadResult;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 云文件和云数据库挂接申请
 *
 * @author zhengyang
 */
@Auth("webadmin")
@RestController("/webadmin/assetEx")
@RequestMapping("/webadmin/assetEx")
public class AssetExController {
    private final AssetExService assetExService;

    public AssetExController(AssetExService assetExService) {
        this.assetExService = assetExService;
    }

    /**
     * 挂接申请
     *
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody AssetExInsertVm model, HttpServletRequest request) throws DoValidException {
        assetExService.insert(model, 0, request);
        return ResultUtil.success("保存成功");
    }

    /**
     * 提交审核
     *
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PostMapping(value = "/insertAndSubmit")
    public Result<String> insertAndSubmit(@Valid @RequestBody AssetExInsertVm model, HttpServletRequest request) throws DoValidException {
        assetExService.insert(model, 1, request);
        return ResultUtil.success("申请成功，请等待审核");
    }

    /**
     * 编辑
     *
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody AssetExUpdateVm model, HttpServletRequest request) throws DoValidException {
        assetExService.update(model, 0, request);
        return ResultUtil.success("保存成功");
    }

    /**
     * 编辑
     *
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PutMapping(value = "/updateAndSubmit")
    public Result<String> updateAndSubmit(@Valid @RequestBody AssetExUpdateVm model, HttpServletRequest request) throws DoValidException {
        assetExService.update(model, 1, request);
        return ResultUtil.success("申请成功，请等待审核");
    }

    /**
     * 详情
     *
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<AssetExEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", assetExService.find(id).orElseThrow(() -> new DoValidException("信息资源目录与云数据关联表信息不存在")));
    }

    /**
     * 查询资源挂接申请(分页)
     *
     * @param model
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAll")
    public Result<AssetExEntity> findAll(AssetExFindAllVm model) throws Exception {
        return ResultUtil.success("", assetExService.findAll(model));
    }

    /**
     * 查询资源挂接审核列表(分页)
     *
     * @param model
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAllByExamine")
    public Result<AssetExEntity> findAllByExamine(AssetExFindAllVm model) throws Exception {
        JSONObject parm = (JSONObject) JSON.toJSON(model);
        parm.put("notStatus", 0);
        return ResultUtil.success("", assetExService.findAllByExamine(parm));
    }

    /**
     * 申请提交
     *
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PutMapping(value = "/submit")
    public Result<String> submit(@Valid @RequestBody IdBaseVm model, HttpServletRequest request) throws DoValidException {
        assetExService.submit(model, request);
        return ResultUtil.success("添加成功");
    }

    /**
     * 终审
     *
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PutMapping(value = "/examine")
    public Result<String> examine(@Valid @RequestBody AssetExExamineVm model, HttpServletRequest request) throws DoValidException {
        assetExService.examine(model, request);
        return ResultUtil.success("添加成功");
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/upload")
    public UploadResult upload(@RequestParam("uviewId") String uviewId, @RequestParam("file") MultipartFile file) throws Exception {
        return assetExService.upload(uviewId, file);
    }
}
