package d1.project.d1project.apidesign.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import d1.framework.webapi.entity.BaseCreateEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Objects;

@Service
public class WebAdminUserService {

    @Value("${d1.project.apiAuth.userRoot}")
    private String userManagementRootUrl;

    public JSONObject getUserInfo(HttpServletRequest request) {
        String accessToken = getTokenByHttpServletRequest(request);
        RestTemplate restTemplate = new RestTemplate();
        String url = userManagementRootUrl + "/webadmin/system/api/getUserInfo";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "token " + accessToken);
        ResponseEntity<JSONObject> res = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<String>(headers), JSONObject.class);
        return Objects.requireNonNull(res.getBody()).getJSONObject("data");
    }

    public void updateCreateIdAndTime(HttpServletRequest request, BaseCreateEntity entity) {
        JSONObject user = getUserInfo(request);
        entity.setCreateById(user.getString("id"));
        entity.setCreateTime(Calendar.getInstance());
    }

    public void updateUpdateIdAndTime(HttpServletRequest request, BaseCreateAndUpdateEntity entity) {
        JSONObject user = getUserInfo(request);
        entity.setUpdateById(user.getString("id"));
        entity.setUpdateTime(Calendar.getInstance());
    }


    public String getTokenByHttpServletRequest(HttpServletRequest request) {
        String authPrefix = "token";
        String auth = request.getHeader("Authorization");
        if (!StringUtils.isEmpty(auth)) {
            String[] authArray = auth.split(" ");
            return authPrefix.equals(authArray[0]) && authArray.length > 1 ? authArray[1] : null;
        }
        return null;
    }
}
