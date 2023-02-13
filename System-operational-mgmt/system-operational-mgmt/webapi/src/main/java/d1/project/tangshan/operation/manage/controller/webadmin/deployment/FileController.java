package d1.project.tangshan.operation.manage.controller.webadmin.deployment;

import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.tangshan.operation.manage.Constants;
import d1.project.tangshan.operation.manage.dao.AutomatedDeploymentDao;
import io.swagger.annotations.Api;
import net.dcrun.component.file.server.FileServerService;
import net.dcrun.component.file.server.UploadResult;
import net.dcrun.component.file.util.FileUtilService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * 文件上传下载类
 *
 * @Author maoyy
 **/
@Api(value = "/webadmin/service/file", tags = "文件存储服务")
@RestController
@RequestMapping("/webadmin/service/file")
public class FileController {

    final
    AutomatedDeploymentDao automatedDeploymentDao;

    public FileController(AutomatedDeploymentDao automatedDeploymentDao) {
        this.automatedDeploymentDao = automatedDeploymentDao;
    }

    /**
     * 上传压缩包
     *
     * @param file 文件
     * @return 上传结果
     * @throws Exception 异常
     */
    @RequestMapping(value = "/uploadDeploymentFile", method = RequestMethod.POST)
    public Result<UploadResult> uploadBuiltZip(@RequestParam("file") MultipartFile file, String type, String nodeId, String systemId, String versionNumber, String isUpdate) throws Exception {
        if (automatedDeploymentDao.existsByVersionNumberGreaterThanEqualAndSysId(versionNumber, systemId) && "0".equals(isUpdate)) {
            return ResultUtil.fail("版本号应大于当前版本");
        }

        String s = File.separator + nodeId + File.separator + systemId + File.separator + versionNumber + File.separator;
        try {
            switch (type) {
                case "deployment":
                    if (new FileUtilService().fileExists(Constants.FILES_DEPLOYMENT + s + file.getOriginalFilename())) {
                        return ResultUtil.fail("文件已存在");
                    }
                    return ResultUtil.success("上传成功", new FileServerService().uploadFile(file, Constants.FILES_DEPLOYMENT + s, "downloadDeploymentFile/"));
                case "upgrade":
                    if (new FileUtilService().fileExists(Constants.FILES_UPGRADE + s + file.getOriginalFilename())) {
                        return ResultUtil.fail("文件已存在");
                    }
                    return ResultUtil.success("上传成功", new FileServerService().uploadFile(file, Constants.FILES_UPGRADE + s, "downloadDeploymentFile/"));
                case "configUpdate":
                    if (new FileUtilService().fileExists(Constants.FILES_CONFIG_UPDATE + s + file.getOriginalFilename())) {
                        return ResultUtil.fail("文件已存在");
                    }
                    return ResultUtil.success("上传成功", new FileServerService().uploadFile(file, Constants.FILES_CONFIG_UPDATE + s, "downloadDeploymentFile/"));
                case "rollback":
                    if (new FileUtilService().fileExists(Constants.FILES_ROLLBACK + s + file.getOriginalFilename())) {
                        return ResultUtil.fail("文件已存在");
                    }
                    return ResultUtil.success("上传成功", new FileServerService().uploadFile(file, Constants.FILES_ROLLBACK + s, "downloadDeploymentFile/"));
                default:
                    return ResultUtil.fail("类型不正确");
            }
        } catch (Exception e) {
            return ResultUtil.fail("失败", e);
        }
    }

    /**
     * 下载法规与知识文件
     *
     * @param fileName 文件名
     * @param request  下载请求
     * @return 文件
     * @throws Exception 异常
     */
    @RequestMapping(value = "/downloadDeploymentFile/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadBuildZip(@PathVariable String fileName, HttpServletRequest request, String type, String nodeId, String systemId, String versionNumber) throws Exception {
        String s = File.separator + nodeId + File.separator + systemId + File.separator + versionNumber + File.separator;
        //TODO sign认证
        switch (type) {
            case "deployment":
                return new FileServerService().downloadFile(request, Constants.FILES_DEPLOYMENT + s, fileName);
            case "upgrade":
                return new FileServerService().downloadFile(request, Constants.FILES_UPGRADE + s, fileName);
            case "configUpdate":
                return new FileServerService().downloadFile(request, Constants.FILES_CONFIG_UPDATE + s, fileName);
            case "rollback":
                return new FileServerService().downloadFile(request, Constants.FILES_ROLLBACK + s, fileName);
            default:
                throw new Exception("类型不正确");
        }
    }

    @RequestMapping(value = "/deleteDeploymentFile/{fileName:.+}", method = RequestMethod.DELETE)
    public Result<String> deleteBuildZip(@PathVariable String fileName, String type, String nodeId, String systemId, String versionNumber) throws Exception {
        String s = File.separator + nodeId + File.separator + systemId + File.separator + versionNumber + File.separator;
        //TODO sign认证
        try {
            switch (type) {
                case "deployment":
                    new FileUtilService().deleteFile(Constants.FILES_DEPLOYMENT + s + fileName);
                    break;
                case "upgrade":
                    new FileUtilService().deleteFile(Constants.FILES_UPGRADE + s + fileName);
                    break;
                case "configUpdate":
                    new FileUtilService().deleteFile(Constants.FILES_CONFIG_UPDATE + s + fileName);
                    break;
                case "rollback":
                    new FileUtilService().deleteFile(Constants.FILES_ROLLBACK + s + fileName);
                    break;
                default:
                    throw new Exception("类型不正确");
            }
            return ResultUtil.success(Constants.DELETE_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail("失败", e);
        }

    }
}
