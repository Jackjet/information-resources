package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.common.Constants;
import d1.project.resourcesharingmgmt.resource.entity.DemandedInfoEntity;
import d1.project.resourcesharingmgmt.resource.entity.DownloadInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.DownloadInfo.DownloadInfoFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.DownloadInfo.DownloadInfoInsertVm;
import d1.project.resourcesharingmgmt.resource.model.DownloadInfo.DownloadInfoUpdateVm;
import d1.project.resourcesharingmgmt.resource.service.DownloadInfoService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;

/**
 * 文件管理
 * @author maoyuying
 */
@Auth("webadmin")
@RestController("/webadmin/downloadInfo")
@RequestMapping("/webadmin/downloadInfo")
public class DownloadInfoController {

    private final DownloadInfoService downloadInfoService;

    public DownloadInfoController(DownloadInfoService downloadInfoService) {
        this.downloadInfoService = downloadInfoService;
    }

    /**
     * 删除文件
     * @param id
     * @return
     * @throws DoValidException
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String id) throws DoValidException {
        DownloadInfoEntity entity = downloadInfoService.find(id).orElseThrow(() -> new DoValidException("文件不存在"));
        downloadInfoService.delete(id);
        //如果是文件挂接，驳回后删除文件
        String fileDownPath = entity.getFileDownloadUri();
        String fileName = fileDownPath.replace("webadmin/file/download/", "");

        File file = new File(Constants.FILE + File.separator + fileName);
        if (file.exists()) {
            file.delete();
        }

        return ResultUtil.success("删除成功");
    }

    /**
     * 新增文件
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody DownloadInfoInsertVm model, HttpServletRequest request) throws DoValidException {
        downloadInfoService.insert(model,request);
        return ResultUtil.success("添加成功");
    }

    /**
     * 查询列表
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAll")
    public Result<DownloadInfoEntity> findAll(DownloadInfoFindAllVm model) throws Exception {
        return ResultUtil.success("", downloadInfoService.findAll(model));
    }

    /**
     * 修改
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody DownloadInfoUpdateVm model, HttpServletRequest request) throws DoValidException {
        downloadInfoService.update(model, request);
        return ResultUtil.success("");
    }

    /**
     * 查询详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<DownloadInfoEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", downloadInfoService.find(id).orElseThrow(() -> new DoValidException("文件不存在")));
    }
}
