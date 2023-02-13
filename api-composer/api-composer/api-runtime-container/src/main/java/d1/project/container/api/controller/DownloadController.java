package d1.project.container.api.controller;

import d1.project.container.api.base.utils.Constants;
import net.dcrun.component.file.server.IFileServerService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.File;


/**
 * @author maorui
 */

@RestController
@RequestMapping("api/download")
public class DownloadController {
    private final IFileServerService fileServerService;

    public DownloadController(IFileServerService fileServerService) {
        this.fileServerService = fileServerService;
    }

    @GetMapping(value = "/excel/{fileName:.+}")
    public ResponseEntity<Resource> downloadExcel(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, Constants.EXCEL, fileName);
    }

    @GetMapping(value = "/log/{day:.+}/{fileName:.+}")
    public ResponseEntity<Resource> downloadLog(@PathVariable String day, @PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, Constants.LOG_ROOT, day + File.separator + fileName);
    }
}
