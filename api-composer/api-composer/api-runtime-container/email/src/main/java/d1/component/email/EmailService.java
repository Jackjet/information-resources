package d1.component.email;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.project.container.api.base.Context;
import d1.project.container.api.base.bean.Input;
import d1.project.container.api.base.service.BaseNodeService;
import d1.project.container.api.base.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Address;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

public class EmailService extends BaseNodeService {
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);

    private JSONObject propertiesObj;

    @Override
    public void init(Map<String, Object> properties) throws Exception {
        super.init(properties);
        propertiesObj = new JSONObject(properties);
    }

    @Override
    public Object run(Context context, Input input) throws Exception {
        super.run(context, input);

        String emailJson = processExpressions(context, input.getInput(), propertiesObj.toJSONString());
        send(emailJson);

        Input nextInput = new Input();
//        nextInput.setInput(JSON.toJSON(result));
        return getNextNode(context).run(context, nextInput);
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
    }

    ////////

    public void send(String emailJson) throws Exception {
        JSONObject emailJsonObj = JSON.parseObject(emailJson);
        //收件服务器地址 smtp.office365.com
        String host = emailJsonObj.getString("host");
        if (Utils.isEmpty(host)) {
            throw new Exception("smtp服务器地址不能为空");
        }

        //smtp服务器端口
        String port = emailJsonObj.getString("port");
        if (Utils.isEmpty(port)) {
            throw new Exception("smtp服务器端口不能为空");
        }

        String from = emailJsonObj.getString("from");
        JSONArray to = emailJsonObj.getJSONArray("to");
        //收件人邮箱
        if (to == null || to.size() == 0) {
            throw new Exception("收件人不能为空");
        }


        Properties props = new Properties();
        props.setProperty("mail.smtp.auth", "true");
        props.setProperty("mail.transport.protocol", "smtp");

        // smtp服务器地址
        props.put("mail.smtp.host", host);
        props.setProperty("mail.smtp.port", port);
        props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.setProperty("mail.smtp.socketFactory.fallback", "false");
        props.setProperty("mail.smtp.socketFactory.port", port);

        Session session = Session.getInstance(props);
        session.setDebug(true);

        String subject = emailJsonObj.getString("subject");
        String content = emailJsonObj.getString("content");
        MimeMessage msg = getMimeMessage(session, from, to, subject, content);

        //邮箱登录账户
        String account = emailJsonObj.getString("account");
        String password = emailJsonObj.getString("password");

        //通过session得到transport对象
        Transport transport = session.getTransport();
        //发件人邮箱,授权码(可以在邮箱设置中获取到授权码的信息)
        transport.connect(account, password);
        transport.sendMessage(msg, msg.getAllRecipients());

        logger.info("邮件已发送");
        transport.close();
    }

    //region deprecated

    private Address[] getToAddress(JSONArray to) throws AddressException {
        Address[] address = new InternetAddress[to.size()];
        for (int i = 0; i < to.size(); i++) {
            address[i] = new InternetAddress(to.get(i).toString());
        }
        return address;
    }


    /**
     * 获得创建一封邮件的实例对象
     */
    private MimeMessage getMimeMessage(Session session, String senderAddress, JSONArray receiverAddress, String subject, String content) throws Exception {
        //创建一封邮件的实例对象
        MimeMessage msg = new MimeMessage(session);
        //设置发件人地址
        msg.setFrom(new InternetAddress(senderAddress));
        /**
         * 设置收件人地址（可以增加多个收件人、抄送、密送），即下面这一行代码书写多行
         * MimeMessage.RecipientType.TO:发送
         * MimeMessage.RecipientType.CC：抄送
         * MimeMessage.RecipientType.BCC：密送
         */
        msg.setRecipients(MimeMessage.RecipientType.TO, getToAddress(receiverAddress));
        //设置邮件主题
        msg.setSubject(subject, "UTF-8");
        //设置邮件正文
        msg.setContent(content, "text/html;charset=UTF-8");
        //设置邮件的发送时间,默认立即发送
        msg.setSentDate(new Date());
        return msg;
    }
}
