package d1.project.resource.api.controller.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.utils.SignManager;
import d1.project.resource.api.business.SourceApiBusiness;
import d1.project.resource.api.model.SourceApiFindAllGetVm;
import d1.project.resource.api.model.SourceApiView;
import d1.project.resource.common.utils.HmacUtils;
import d1.project.resource.group.business.GroupInfoBusiness;
import d1.project.resource.group.model.GroupTree;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 对外提供接口_源api
 *
 * @author baozh
 */
@RestController
@RequestMapping("/service/api")
public class ApiController {
    private static final Logger logger = LoggerFactory.getLogger(ApiController.class);
    private final SourceApiBusiness sourceApiBusiness;
    private final GroupInfoBusiness groupInfoBusiness;

    public ApiController(SourceApiBusiness sourceApiBusiness, GroupInfoBusiness groupInfoBusiness) {
        this.sourceApiBusiness = sourceApiBusiness;
        this.groupInfoBusiness = groupInfoBusiness;
    }


    /**
     * 分页查询源API
     */
    @GetMapping(value = "/source/findAll")
    public Result<Page<SourceApiView>> findAll(SourceApiFindAllGetVm params, HttpServletRequest request) throws Exception {
        SignManager.getInstance().verifyHmacSign(request, s -> {
            try {
                return HmacUtils.getAppKeyById(request.getHeader("appid"));
            } catch (Exception e) {
                logger.error("/source/findAll", e);
            }
            return null;
        });
        JSONObject model = (JSONObject) JSON.toJSON(params);
        if(!StringUtils.isEmpty(params.getIds())) {
            String[] ids = params.getIds().split(",");
            JSONArray idsNot = new JSONArray();
            for(String s : ids){
                idsNot.add(s);
            }
            model.put("idsNot", idsNot);
        }
        return ResultUtil.success("查询成功", this.sourceApiBusiness.findAll(model));
    }

    /**
     * 查询源API详情
     */
    @GetMapping(value = "/source/find")
    public Result<SourceApiView> find(String id, HttpServletRequest request) throws Exception {
        SignManager.getInstance().verifyHmacSign(request, s -> {
            try {
                return HmacUtils.getAppKeyById(request.getHeader("appid"));
            } catch (Exception e) {
                logger.error("/source/find", e);
            }
            return null;
        });
        return ResultUtil.success("查询成功", sourceApiBusiness.findSourceApiViewById(id));
    }

    /**
     * 分页查询源API分组
     */
    @GetMapping(value = "/source/findGroupTree")
    public Result<List<GroupTree>> getTree() {
        try {
            return ResultUtil.success("SUCCESS", groupInfoBusiness.findTreeList("0"));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }
}









