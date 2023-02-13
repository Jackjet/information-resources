package d1.project.resourcesharingmgmt.resource.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.WebUserBusiness;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 门户端_个人中心
 *
 * @author kikki
 * @date 2020-09-07 15:25
 */
@Auth("webadmin")
@RestController
@RequestMapping("/web/webUser")
public class WebUserController {
    private final WebUserBusiness webUserBusiness;

    public WebUserController(WebUserBusiness webUserBusiness) {
        this.webUserBusiness = webUserBusiness;
    }

    /**
     * 获取用户key
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findKeyByUserId")
    public Result<String> findKeyByUserId(HttpServletRequest request) throws Exception {
        return ResultUtil.success("", webUserBusiness.findKeyByUserId(request));
    }
}
