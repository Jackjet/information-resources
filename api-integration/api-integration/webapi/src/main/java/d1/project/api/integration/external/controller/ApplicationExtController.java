package d1.project.api.integration.external.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.utils.SignManager;
import d1.project.api.integration.common.annotation.ApiAuth;
import d1.project.api.integration.external.business.ApplicationExtBusiness;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 应用对外接口
 *
 * @author libin
 */
@RestController
@RequestMapping("/webadmin/application/ext")
public class ApplicationExtController {
    private final ApplicationExtBusiness applicationExtBusiness;

    public ApplicationExtController(ApplicationExtBusiness applicationExtBusiness) {
        this.applicationExtBusiness = applicationExtBusiness;
    }

    @GetMapping("/findKeyByUserId")
    public Result<String> findKeyByUserId(HttpServletRequest request, @Valid @NotBlank(message = "用户id不能为空") String userId) {
        try {
            //先通过HMAC签名验证
            SignManager.getInstance().verifyHmacSign(request, ApplicationExtController.this::getAppKeyById);
            return ResultUtil.success("查询成功", applicationExtBusiness.findKeyByUserId(userId));
        } catch (Exception e) {
            return ResultUtil.fail("查询失败,密钥信息不存在");
        }
    }

    //==========================================================
    private String getAppKeyById(String appid) {
        //在这里实现通过appid获取appkey的内容
        try {
            ClassPathResource classPathResource = new ClassPathResource("signKey.json");
            byte[] bytes = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            String singKeys = new String(bytes);
            JSONArray singKeysJson = JSON.parseArray(singKeys);
            for(int i=0;i<singKeysJson.size();i++) {
                if(appid.equals(singKeysJson.getJSONObject(i).get("appid").toString())){
                    return singKeysJson.getJSONObject(i).get("appkey").toString();
                }
            }
            return  null;
        }catch (Exception e){
            return  null;
        }
    }
}
