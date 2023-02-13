package d1.project.d1project.common.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.apidesign.service.WebAdminUserService;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-16 10:35
 */
@Service
public class MyTokenService {

    private final WebAdminUserService webAdminUserService;

    public MyTokenService(WebAdminUserService webAdminUserService) {
        this.webAdminUserService = webAdminUserService;
    }

    public void makeUpRoleJsonObjectByToken(HttpServletRequest request, JSONObject paramObject) throws DoValidException {
        JSONObject webAdminUserVm = webAdminUserService.getUserInfo(request);
        if (!webAdminUserVm.getString("roleId").contains("admin")) {
            paramObject.put("createById", webAdminUserVm.getString("id"));
        }
    }

}
