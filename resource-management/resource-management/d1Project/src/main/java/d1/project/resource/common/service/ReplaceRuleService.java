package d1.project.resource.common.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-16 10:35
 */
@Service
public class ReplaceRuleService {
    @Value("${d1.project.resourceSharingMgmt}")
    private String resourceSharingMgmtPath;

    public Object findAllList(@RequestParam(name = "name") String name) throws DoValidException {
        String path = resourceSharingMgmtPath + "/ext/replaceRule/findAllList";
        if (!StringUtils.isEmpty(name)) {
            path = path + "?name=" + name;
        }
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        ResponseEntity<JSONObject> res = restTemplate.exchange(path, HttpMethod.GET, new HttpEntity<String>(headers), JSONObject.class);
        return Objects.requireNonNull(res.getBody()).get("data");
    }

}
