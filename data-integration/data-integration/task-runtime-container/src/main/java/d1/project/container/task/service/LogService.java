package d1.project.container.task.service;


import d1.project.container.task.utils.Constants;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 用于管理Kettle作业相关操作的类
 *
 * @author feng.xh
 * @since 1.0.0
 */
@Service
public class LogService {

    /**
     * @param day
     * @return
     */
    public List<String> findLogs(String day) throws Exception {
        List<String> fileNameList = new ArrayList<>();
        File file = new File(Constants.LOG_ROOT, day);
        if (!file.exists()) {
            return null;
        }
        //用数组接收
        File fileList[] = file.listFiles();
        for (int i = 0; i < fileList.length; i++) {
            File fs = fileList[i];
            fileNameList.add(fs.getName());
        }
        return fileNameList;
    }

    public ResponseEntity<FileSystemResource> export(String day, String fileName) throws Exception {
        String logDir = Constants.LOG_ROOT + File.separator + day;
        File file = new File(logDir, fileName);
        if (!file.exists()) {
            throw new Exception("文件不存在");
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", "attachment; filename=" + fileName);
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        headers.add("Last-Modified", new Date().toString());
        headers.add("ETag", String.valueOf(System.currentTimeMillis()));

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(new FileSystemResource(file));
    }
}

