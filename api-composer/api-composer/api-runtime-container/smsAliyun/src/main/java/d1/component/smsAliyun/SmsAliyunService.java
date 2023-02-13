package d1.component.smsAliyun;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import d1.project.container.api.base.Context;
import d1.project.container.api.base.bean.Input;
import d1.project.container.api.base.service.BaseNodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * 阿里云短信服务
 * https://help.aliyun.com/document_detail/55284.html
 */
public class SmsAliyunService extends BaseNodeService {
    private static final Logger logger = LoggerFactory.getLogger(SmsAliyunService.class);

    /**
     * 短信API产品名称（短信产品名固定，无需修改）
     */
    private static final String product = "Dysmsapi";
    /**
     * 短信API产品域名（接口地址固定，无需修改）
     */
    private static final String domain = "dysmsapi.aliyuncs.com";

    private String accessKeyId;
    private String accessKeySecret;
    private String signName;
    private String templateId;

    private String mobiles;
    private String content;

    @Override
    public void init(Map<String, Object> properties) throws Exception {
        super.init(properties);
        System.setProperty("sun.net.client.defaultConnectTimeout", "10000");
        System.setProperty("sun.net.client.defaultReadTimeout", "10000");

        //获取属性
        accessKeyId = properties.get("accessKeyId").toString();
        accessKeySecret = properties.get("accessKeySecret").toString();
        signName = properties.get("signName").toString();
        templateId = properties.get("templateId").toString();

        mobiles = properties.get("mobiles").toString();
        content = properties.get("content").toString();
    }

    @Override
    public Object run(Context context, Input input) throws Exception {
        super.run(context, input);

        // select * from test where age> ${request.body.age}
        String mobileList = processExpressions(context, input.getInput(), mobiles);
        String param = processExpressions(context, input.getInput(), content);

        JSONObject result = new JSONObject();
        result.put("result", send(mobileList, param));

        Input nextInput = new Input();
        nextInput.setInput(result);
        return getNextNode(context).run(context, nextInput);
    }


    @Override
    public void destroy() throws Exception {
        super.destroy();
    }


    ////////////////
    public boolean send(String mobile, String param) throws Exception {
        IClientProfile profile = DefaultProfile.getProfile("cn-hangzhou", accessKeyId, accessKeySecret);
        DefaultProfile.addEndpoint("cn-hangzhou", "cn-hangzhou", product, domain);
        IAcsClient client = new DefaultAcsClient(profile);

        //组装请求对象
        SendSmsRequest request = new SendSmsRequest();
        //使用post提交
        request.setMethod(MethodType.POST);
        //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为00+国际区号+号码，如“0085200000000”
        request.setPhoneNumbers(mobile);
        //必填:短信签名-可在短信控制台中找到
        request.setSignName(signName);
        //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
        request.setTemplateCode(templateId);
        //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
        //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
        request.setTemplateParam(param);
        //可选-上行短信扩展码(扩展码字段控制在7位或以下，无特殊需求用户请忽略此字段)
        //request.setSmsUpExtendCode("90997");
        //可选:outId为提供给业务方扩展字段,最终在短信回执消息中将此值带回给调用者
        //request.setOutId("yourOutId");
        //请求失败这里会抛ClientException异常
        SendSmsResponse sendSmsResponse = client.getAcsResponse(request);
        if (sendSmsResponse.getCode() != null && "OK".equals(sendSmsResponse.getCode())) {
            return true;
        }
        if (sendSmsResponse.getMessage() != null) {
            throw new Exception(sendSmsResponse.getMessage());
        }
        return false;
    }
}
