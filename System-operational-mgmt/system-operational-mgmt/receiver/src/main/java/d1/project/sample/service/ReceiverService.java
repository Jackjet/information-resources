package d1.project.sample.service;

import com.alibaba.fastjson.JSONObject;
import d1.project.sample.model.ReceiverVm;
import net.dcrun.component.file.util.FileUtilService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author lin
 */
@Service
public class ReceiverService {
    public static final String FILE_SEPARATOR = System.getProperty("file.separator");
    private final Logger logger = LoggerFactory.getLogger(ReceiverService.class);

    public void writeFile(ReceiverVm model) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String timestamp = sdf.format(date);
            String filePath = getRealFilePath(getPath().replace("file:", "") + "/receiver.txt");


            JSONObject file = new JSONObject();
            file.put("timestamp", timestamp);
            file.put("type",model.getType());
            switch (model.getType()) {
                case "email":
                    if(StringUtils.isEmpty(model.getTo())){
                        throw new Exception("发送邮箱不可为空");
                    }

                    if(StringUtils.isEmpty(model.getContent().getSubject())){
                        throw new Exception("邮件主题不可为空");
                    }
                    if(StringUtils.isEmpty(model.getContent().getContent())){
                        throw new Exception("邮件内容不可为空");
                    }

                    file.put("to", model.getTo());
                    file.put("content", model.getContent());
                    break;
                case "sms":
                    if(StringUtils.isEmpty(model.getPhone())){
                        throw new Exception("手机号不可为空");
                    }
                    if(StringUtils.isEmpty(model.getTemplate())){
                        throw new Exception("模板不可为空");
                    }
                    if(!StringUtils.isEmpty(model.getAliMessage())){
                        if(StringUtils.isEmpty(model.getAccessKeyId())){
                            throw new Exception("accessKeyId不可为空");
                        }
                        if(StringUtils.isEmpty(model.getAccessKeySecret())){
                            throw new Exception("accessKeySecret不可为空");
                        }
                        if(StringUtils.isEmpty(model.getSignName())){
                            throw new Exception("签名不可为空");
                        }
                    }
                    file.put("phone", model.getPhone());
                    file.put("message", model.getMessage());
                    file.put("template", model.getTemplate());
                    file.put("aliMessage",model.getAliMessage());
                    file.put("signName",model.getSignName());
                    file.put("accessKeyId",model.getAccessKeyId());
                    file.put("accessKeySecret",model.getAccessKeySecret());
                    break;
                default:
                    break;
            }
            FileUtilService fileUtilService = new FileUtilService();
            List<Object> content = new ArrayList<>();
            content.add(JSONObject.toJSONString(file));
            fileUtilService.writeLinesWithParam(filePath, content, StandardCharsets.UTF_8.toString(), true);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }

    }

    public List<String> readFile() {
        try {
            FileUtilService fileUtilService = new FileUtilService();
            String filePath = getRealFilePath(getPath().replace("file:", "") + "/receiver.txt");
            List<String> list = fileUtilService.readLines(filePath, StandardCharsets.UTF_8.toString());
            fileUtilService.writeStringToFile(filePath, "");
            return list;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return null;
        }
    }

    public String getPath() {
        String path = this.getClass().getProtectionDomain().getCodeSource().getLocation().getPath();
        if (System.getProperty("os.name").contains("dows")) {
            path = path.substring(1, path.length());
        }
        if (path.contains("jar")) {
            path = path.substring(0, path.lastIndexOf("."));
            return path.substring(0, path.lastIndexOf("/"));
        }
        path = path.replace("target/classes/", "");

        return path;
    }

    public static String getRealFilePath(String path) {
        return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
    }


}
