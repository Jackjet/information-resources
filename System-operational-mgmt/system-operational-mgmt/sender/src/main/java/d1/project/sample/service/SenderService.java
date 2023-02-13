package d1.project.sample.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.project.sample.model.ReceiverVm;
import net.dcrun.component.email.IEmailService;
import net.dcrun.component.http.HttpService;
import net.dcrun.component.sms.aliyun.SmsAliyunService;
import net.dcrun.component.sms.ucpass.ISmsUcpassService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lin
 */
@Service
public class SenderService {
    private final Logger logger = LoggerFactory.getLogger(SenderService.class);
    @Autowired
    private IEmailService emailService;
    @Autowired
    private ISmsUcpassService smsUcpassService;

    @Value("${email.host}")
    private String host;
    @Value("${email.from}")
    private String from;
    @Value("${email.account}")
    private String account;
    @Value("${email.password}")
    private String password;
    @Value("${email.ssl}")
    private String ssl;
    @Value("${email.port}")
    private String port;
    @Value("${receiver.service.url}")
    private String url;

    SmsAliyunService init(String accessKeyId,String accessKeySecret,String signName) {
        SmsAliyunService smsAliyunService = new SmsAliyunService();
        smsAliyunService.init(accessKeyId, accessKeySecret, signName);
        return smsAliyunService;
    }

    public void send() {
        try {
            HttpService httpService = new HttpService();
            String rootUrl = url + "/receiver/read";
            String result = httpService.get(rootUrl, null, null);
            JSONObject result1 = JSONObject.parseObject(result);
            System.out.println(JSONObject.toJSONString(result1));
            if (result1.getIntValue("code") == 0 || result1.get("data") == null) {
                return;
            }
            JSONArray array = result1.getJSONArray("data");
            List<ReceiverVm> list = new ArrayList<>();
            for (int i = 0; i < array.size(); i++) {
                JSONObject jsonObject = JSONObject.parseObject((array.getString(i)));
                ReceiverVm receiverVm = JSON.toJavaObject(jsonObject, ReceiverVm.class);
                list.add(receiverVm);
            }

            for (ReceiverVm receiverVm : list) {
                switch (receiverVm.getType()) {
                    case "email":
                        JSONObject email = new JSONObject();
                        email.put("host", host);
                        email.put("from", from);
                        email.put("to", receiverVm.getTo());
                        email.put("account", account);
                        email.put("password", password);
                        email.put("ssl", ssl);
                        email.put("port", port);

                        JSONObject content = new JSONObject();
                        content.put("subject", receiverVm.getContent().getSubject());
                        content.put("content", receiverVm.getContent().getContent());

                        email.put("content", content);

                        try {
                            emailService.send(email.toJSONString());
                        } catch (Exception e) {
                            logger.error("邮件error:" + e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    case "sms":
                        try {
                            //阿里短信
                            if (!StringUtils.isEmpty(receiverVm.getAliMessage())) {
                                init(receiverVm.getAccessKeyId(),receiverVm.getAccessKeySecret(),receiverVm.getSignName()).send(receiverVm.getAliMessage(), receiverVm.getPhone(), receiverVm.getTemplate());
                            }
                            //云之讯
                            if (!StringUtils.isEmpty(receiverVm.getMessage())) {
                                smsUcpassService.send(receiverVm.getMessage(), receiverVm.getPhone(), receiverVm.getTemplate());
                            }

                        } catch (Exception e) {
                            logger.error("短信error:" + e.getMessage());
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }
}
