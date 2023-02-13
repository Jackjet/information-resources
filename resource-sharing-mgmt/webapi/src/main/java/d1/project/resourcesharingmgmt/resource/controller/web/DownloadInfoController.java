package d1.project.resourcesharingmgmt.resource.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.common.Constants;
import d1.project.resourcesharingmgmt.resource.entity.DownloadInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.DownloadInfo.DownloadInfoFindAllVm;
import d1.project.resourcesharingmgmt.resource.service.DownloadInfoService;
import net.dcrun.component.file.server.IFileServerService;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 门户端_下载中心
 *
 * @author zhengyang
 */
@Auth("noauth")
@RestController("/web/downloadInfo")
@RequestMapping("/web/downloadInfo")
public class DownloadInfoController {
    private final DownloadInfoService downloadInfoService;
    private final IFileServerService fileServerService;

    public DownloadInfoController(DownloadInfoService downloadInfoService, IFileServerService fileServerService) {
        this.downloadInfoService = downloadInfoService;
        this.fileServerService = fileServerService;
    }

    /**
     * 查询列表
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAll")
    public Result<Page<DownloadInfoEntity>> find(DownloadInfoFindAllVm model) throws Exception {
        return ResultUtil.success("", downloadInfoService.findAll(model));
    }

    /**
     * 文件下载
     * @param id
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/download")
    public ResponseEntity<Resource> download(String id, HttpServletRequest request) throws Exception {
        DownloadInfoEntity downloadInfoEntity = downloadInfoService.download(id);
        return fileServerService.downloadFile(request, Constants.FILE, downloadInfoEntity.getFileName());
    }
}
