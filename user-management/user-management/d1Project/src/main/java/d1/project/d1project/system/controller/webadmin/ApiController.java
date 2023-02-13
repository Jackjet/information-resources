package d1.project.d1project.system.controller.webadmin;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.utils.TokenManager;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


/**
 * @author baozh
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/system/api")
public class ApiController {
    /**
     * 获取用户信息
     */
    @GetMapping(value = "/getUserInfo")
    public Result<JSONObject> getUserInfo(HttpServletRequest request) throws Exception {
        return ResultUtil.success("", TokenManager.getInstance().getUserByToken(request));
    }

}
