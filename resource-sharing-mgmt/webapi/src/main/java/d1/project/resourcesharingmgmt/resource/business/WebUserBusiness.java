package d1.project.resourcesharingmgmt.resource.business;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.TokenManager;
import net.dcrun.component.http.HttpService;
import net.dcrun.component.security.HmacSignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 首页
 * @author maoyuying
 */
@Service
public class WebUserBusiness {
    @Value("${apiIntegrate.url}")
    private String apiManage_url;

    @Value("${apiIntegrate.appid}")
    private String apiManage_appid;

    @Value("${apiIntegrate.appkey}")
    private String apiManage_appkey;
    private static Logger log = LoggerFactory.getLogger(WebUserBusiness.class);

    public WebUserBusiness() {
    }

    /**
     * 需求统计
     * @return
     */
    public String findKeyByUserId(HttpServletRequest request) throws Exception{
        HttpService httpService=new HttpService();
        Map<String,Object> headers=this.getHttpHeaders();
        Map<String,Object> params=new HashMap<>();
        params.put("userId", TokenManager.getInstance().getUserByToken(request).getString("id"));
        String result=  httpService.get(apiManage_url+"/webadmin/application/ext/findKeyByUserId",headers,params);
        JSONObject jsonObject=JSONObject.parseObject(result);
        log.debug(result);
        String data=jsonObject.getString("data");
        return data;
    }

    private Map<String,Object> getHttpHeaders() throws Exception {
        String appid=apiManage_appid;
        String appkey=apiManage_appkey;
        Map<String,Object> headers=new HashMap<>();
        headers.put("Content-Type","application/json");
        String timestamp=String.valueOf(System.currentTimeMillis());
        headers.put("timestamp",timestamp);
        HmacSignService hmacSignService=new HmacSignService();
        String sign=hmacSignService.sign(appid,timestamp,appkey);
        String authorization="sign "+sign;
        headers.put("Authorization",authorization);
        headers.put("appid",appid);
        return headers;
    }
}
