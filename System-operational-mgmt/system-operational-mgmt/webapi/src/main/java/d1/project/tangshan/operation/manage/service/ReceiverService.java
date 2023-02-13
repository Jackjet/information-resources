package d1.project.tangshan.operation.manage.service;

import d1.project.tangshan.operation.manage.model.ContentVm;
import d1.project.tangshan.operation.manage.model.ReceiverVm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lin
 */
@Service
public class ReceiverService {
    @Value("${d1.receiver.receiverUrl}")
    private String receiverUrl;
    @Value("${sms.ali.accessKeyId}")
    private String accessKeyId;
    @Value("${sms.ali.accessKeySecret}")
    private String accessKeySecret;
    @Value("${sms.ali.signName}")
    private String signName;


    public void sendSms(String phone, String template, String message) {
        ReceiverVm receiverVm = new ReceiverVm();
        receiverVm.setPhone(phone);
        receiverVm.setTemplate(template);
        receiverVm.setMessage(message);
        receiverVm.setType("sms");
        postReceiver(receiverVm);
    }

    public void sendAiSms(String phone, String template, String message) {
        ReceiverVm receiverVm = new ReceiverVm();
        receiverVm.setPhone(phone);
        receiverVm.setTemplate(template);
        receiverVm.setAccessKeyId(accessKeyId);
        receiverVm.setAccessKeySecret(accessKeySecret);
        receiverVm.setSignName(signName);
        Map<String,String> map =new HashMap<>();
        map.put("var",message);
        receiverVm.setAliMessage(map);
        receiverVm.setType("sms");
        postReceiver(receiverVm);
    }

    public void sendEmail(String[] arr, String subject, String msg) {
        ReceiverVm receiverVm = new ReceiverVm();
        receiverVm.setType("email");
        receiverVm.setTo(arr);
        ContentVm contentVm = new ContentVm();
        contentVm.setSubject(subject);
        contentVm.setContent(msg);
        receiverVm.setContent(contentVm);
        postReceiver(receiverVm);
    }

    private void postReceiver(ReceiverVm model) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.postForObject(receiverUrl, model, String.class);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
    }
}
