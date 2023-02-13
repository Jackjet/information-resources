package d1.project.resourcesharingmgmt.resource.controller;

import d1.framework.webapi.annotation.Auth;
import d1.project.resourcesharingmgmt.common.Constants;
import d1.project.resourcesharingmgmt.resource.business.ArchExFileBusiness;
import net.dcrun.component.file.server.IFileServerService;
import net.dcrun.component.file.server.UploadResult;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 附件管理
 *
 * @author zhengyang
 */
@Auth("webadmin")
@RestController("/webadmin/file")
@RequestMapping("/webadmin/file")
public class FileController {
    private final IFileServerService fileServerService;
    private final ArchExFileBusiness archExFileBusiness;

    public FileController(IFileServerService fileServerService, ArchExFileBusiness archExFileBusiness) {
        this.fileServerService = fileServerService;
        this.archExFileBusiness = archExFileBusiness;
    }

    /**
     * 文件上传
     *
     * @param file
     * @return
     * @throws Exception
     */
    @PostMapping(value = "/upload")
    public UploadResult upload(@RequestParam("file") MultipartFile file) throws Exception {
        return fileServerService.uploadFile(file, Constants.FILE, "webadmin/file/download");
    }

    /**
     * 文件下载
     *
     * @param fileName
     * @param request
     * @return
     * @throws Exception
     */
    @Auth("noauth")
    @RequestMapping(value = "/download/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, Constants.FILE, fileName);
    }

    /**
     * 文件下载
     *
     * @param fileName
     * @param request
     * @return
     * @throws Exception
     */
    @Auth("noauth")
    @RequestMapping(value = "/download/{uviewId}/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable String uviewId, @PathVariable String fileName, @RequestParam(value="orgId",required=false) String orgId, HttpServletRequest request) throws Exception {
        archExFileBusiness.insertDownLog(orgId, "webadmin/file/download/" + uviewId + "/" + fileName, request);
        return fileServerService.downloadFile(request, Constants.FILE, uviewId + "/" + fileName);
    }

    /**
     * 文件下载
     *
     * @param fileName
     * @param request
     * @return
     * @throws Exception
     */
    @Auth("noauth")
    @RequestMapping(value = "/download/{org}/{code}/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable String org, @PathVariable String code, @PathVariable String fileName, @RequestParam(value="orgId",required=false) String orgId, HttpServletRequest request) throws Exception {
        archExFileBusiness.insertDownLog(orgId, "webadmin/file/download/" + org + "/" + code + "/" + fileName, request);
        return fileServerService.downloadFile(request, Constants.FILE, org + "/" + code + "/" + fileName);
    }
}
