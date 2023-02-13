package d1.project.sample.model;

import java.util.List;
import java.util.Map;

/**
 * @author lin
 */
public class ReceiverVm {
    //类型 sms 短信 email 邮件
    private String type;
    //时间
    private String timestamp;
    //电话  [“1332425235”,”185323452342”]
    private String phone;
    //要发送的内容
    private String message;
    //阿里短信内容
    private Map<String,String> aliMessage;
    private String accessKeyId;

    private String accessKeySecret;
    //阿里云短信签名
    private String signName;
    //模板
    private String template;

    //[“ddf@126.com”,”xxxe@qq.com”]
    private String[] to;

    private ContentVm content;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public ContentVm getContent() {
        return content;
    }

    public void setContent(ContentVm content) {
        this.content = content;
    }

    public Map<String, String> getAliMessage() {
        return aliMessage;
    }

    public void setAliMessage(Map<String, String> aliMessage) {
        this.aliMessage = aliMessage;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }
}
