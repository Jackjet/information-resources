package d1.project.api.integration.external.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.utils.SignManager;
import d1.project.api.integration.apianalysis.service.ApiLogRecordService;
import d1.project.api.integration.apimanage.entity.SourceApi;
import d1.project.api.integration.common.Constants;
import d1.project.api.integration.common.annotation.ApiAuth;
import d1.project.api.integration.common.model.TimePageableVm;
import d1.project.api.integration.external.business.ApiExtBusiness;
import d1.project.api.integration.external.model.ApiExtFindAllGetVm;
import d1.project.api.integration.external.model.ApiExtInsertPostVm;
import d1.project.api.integration.external.model.ApiExtLogGetVm;
import d1.project.api.integration.kong.entity.KongLogDir;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * API对外接口
 *
 * @author libin
 */
//@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/api/ext")
public class ApiExtController {
    @Value("${d1.project.external.container}")
    private String container;
    private final ApiExtBusiness apiExtBusiness;
    private final ApiLogRecordService apiLogRecordService;

    public ApiExtController(ApiExtBusiness apiExtBusiness, ApiLogRecordService apiLogRecordService) {
        this.apiExtBusiness = apiExtBusiness;
        this.apiLogRecordService = apiLogRecordService;
    }

    /**
     * 查询API
     *
     * @author libin
     */
    @GetMapping(value = "/findAll")
    public Result<JSONObject> findAll(ApiExtFindAllGetVm param) throws Exception {
        try {
            return ResultUtil.success("成功", apiExtBusiness.findAll((JSONObject) JSON.toJSON(param)));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 统计API日志
     *
     * @param container
     * @return
     */
    @GetMapping(value = "/statistical")
    public Result<JSONObject> statistical(String container) {
        try {
            return ResultUtil.success("成功", apiExtBusiness.statistical(container));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 查询日志
     *
     * @param param
     * @return
     */
    @GetMapping(value = "/log")
    public Result<List<KongLogDir>> log(ApiExtLogGetVm param) {
        try {
            return ResultUtil.success("成功", apiExtBusiness.log((JSONObject) JSON.toJSON(param)));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 添加API并授权
     *
     * @param param
     * @return
     */
    @PostMapping(value = "/insert")
    public Result<SourceApi> insert(HttpServletRequest request, @Valid @RequestBody ApiExtInsertPostVm param) {
        try {
            //先通过HMAC签名验证
            SignManager.getInstance().verifyHmacSign(request, ApiExtController.this::getAppKeyById);
            return ResultUtil.success("成功", apiExtBusiness.insert(param, container, Constants.KEY_AUTH));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail("失败：" + e.getMessage());
        }
    }

    /**
     * 查询API
     *
     * @author libin
     */
    @GetMapping(value = "/findAllLog")
    public Result<JSONObject> findAll(TimePageableVm param) throws Exception {
        try {
            return ResultUtil.success("成功", apiLogRecordService.findAll((JSONObject) JSON.toJSON(param)));
        } catch (Exception e) {
            return ResultUtil.fail("失败：" + e.getMessage());
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
            for (int i = 0; i < singKeysJson.size(); i++) {
                if (appid.equals(singKeysJson.getJSONObject(i).get("appid").toString())) {
                    return singKeysJson.getJSONObject(i).get("appkey").toString();
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}
