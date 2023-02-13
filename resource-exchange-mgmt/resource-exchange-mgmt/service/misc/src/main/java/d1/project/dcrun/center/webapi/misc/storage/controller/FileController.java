package d1.project.dcrun.center.webapi.misc.storage.controller;

import d1.framework.webapi.annotation.Auth;
import io.swagger.annotations.Api;
import net.dcrun.component.file.server.IFileServerService;
import net.dcrun.component.file.server.UploadResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

import static d1.project.dcrun.center.webapi.common.util.Constants.*;

/**
 * 文件上传下载类
 *
 * @Author chengh
 **/
@Auth("noauth")
@Api(value = "/service/file", tags = "文件存储服务")
@RestController
@RequestMapping("/service/file")
public class FileController {

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Value("${file.server.uri}")
    private String fileDownload;

    @Autowired
    private IFileServerService fileServerService;

    /**
     * 下载EMQ的json 文件(开发者及开发者权限数据)
     *
     * @param fileName 文件名
     * @param request  下载请求
     * @throws Exception 异常
     * @return文件
     */
    @RequestMapping(value = "/downloadEmq/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadEmq(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, FILES_DOWNLOAD_EMQ, fileName);
    }

    /**
     * 下载EMQ配置json 文件emq_config.json
     *
     * @param fileName 文件名
     * @param request  下载请求
     * @throws Exception 异常
     * @return文件
     */
    @RequestMapping(value = "/downloadEmqConfig/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadEmqConfig(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, FILES_DOWNLOAD_EMQ, fileName);
    }

    /**
     * 下载打包前的压缩包
     *
     * @param fileName 文件名
     * @param request  下载请求
     * @return 文件
     * @throws Exception 异常
     */
    @RequestMapping(value = "/downloadBuildZip/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadBuildZip(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        //TODO sign认证
        return fileServerService.downloadFile(request, FILES_BUILD_WEBAPI_TEMP, fileName);
    }

    /**
     * 上传打包后的压缩包
     *
     * @param file 文件
     * @return 上传结果
     * @throws Exception 异常
     */
    @RequestMapping(value = "/uploadBuiltZip", method = RequestMethod.POST)
    public UploadResult uploadBuiltZip(@RequestParam("file") MultipartFile file) throws Exception {
        logger.info("收到上传压缩包的请求：" + file.getOriginalFilename());
        return fileServerService.uploadFile(file, FILES_BUILD_WEBAPI, fileDownload + "downloadBuiltZip/");
    }

    /**
     * 下载打包后的压缩包
     *
     * @param fileName 文件名
     * @param request  下载请求
     * @return 文件
     * @throws Exception 异常
     */
    @RequestMapping(value = "/downloadBuiltZip/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadBuiltZip(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        //TODO sign认证
        return fileServerService.downloadFile(request, FILES_BUILD_WEBAPI, fileName);
    }

    /**
     * 下载系统服务自定义配置升级的config文件
     *
     * @param fileName 文件名
     * @param request  下载请求
     * @return 文件
     * @throws Exception 异常
     */
    @RequestMapping(value = "/downloadCustomConfigZip/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadCustomConfigZip(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        //TODO sign认证
        return fileServerService.downloadFile(request, FILES_DOWNLOAD_WEBAPI, fileName);
    }

    /**
     * 安装webapi/cmd
     *
     * @param fileName 文件名
     * @param request  下载请求
     * @return 文件
     * @throws Exception 异常
     */
    @RequestMapping(value = "/downloadWebapiInstallPackage/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadWebapiInstallPackage(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        //TODO sign认证
        return fileServerService.downloadFile(request, FILES_DOWNLOAD_WEBAPI, fileName);
    }

    /**
     * 下载routes.json
     *
     * @param fileName 文件名
     * @param request  下载请求
     * @return 文件
     * @throws Exception 异常
     */
    @RequestMapping(value = "/downloadDcapiRoutes/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadDcapiRoutes(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, FILES_DOWNLOAD_DCAPI, fileName);
    }

    /**
     * 下载dcapigateway的application.properties文件
     *
     * @param fileName 文件名
     * @param request  下载请求
     * @return 文件
     * @throws Exception 异常
     */
    @RequestMapping(value = "/downloadDcapiConfig/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadDcapiConfig(@PathVariable String sysServiceId, @PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, FILES_DOWNLOAD_DCAPI, fileName);
    }


    /**
     * 安装文件下载
     *
     * @param fileName
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/downloadApiGateway/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadApiGateway(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, FILES_DOWNLOAD_DCAPI, fileName);
    }

    /**
     * 下载数据缓存服务安装文件
     *
     * @param fileName
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/downloadDataCache/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadDataCache(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, FILES_DOWNLOAD_DATACACHE, fileName);
    }

    /**
     * 同步数据缓存服务配置信息
     *
     * @param fileName
     * @param request
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/downloadDataCacheKey/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadDataCacheKey(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, FILES_DOWNLOAD_DATACACHE, fileName);
    }

    /**
     * 下载节点基础服务安装文件
     *
     * @param request 下载请求
     * @return 文件
     * @throws Exception 异常
     */
    @RequestMapping(value = "/downloadNode/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadNode(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, FILES_DOWNLOAD_NODE, fileName);
    }
}

