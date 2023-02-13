package d1.project.api.integration.apianalysis.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.api.integration.apianalysis.business.ApiAnalysisBusiness;
import d1.project.api.integration.apianalysis.model.*;
import d1.project.api.integration.apianalysis.view.entity.*;
import d1.project.api.integration.common.annotation.ApiAuth;
import d1.project.api.integration.common.model.TimePageableVm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.util.List;

/**
 * @author baozh
 */
@ApiAuth("webadmin")
@RestController
@RequestMapping("/webadmin/apiAnalysis")
public class ApiAnalysisController {
    private static final Logger logger = LoggerFactory.getLogger(ApiAnalysisController.class);
    private final ApiAnalysisBusiness apiAnalysisBusiness;

    public ApiAnalysisController(ApiAnalysisBusiness apiAnalysisBusiness) {
        this.apiAnalysisBusiness = apiAnalysisBusiness;
    }

    @GetMapping(value = "/apiList")
    public Result<Page<ApiAnalysisList>> apiList(FindApiListVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", apiAnalysisBusiness.findApiList(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/appList")
    public Result<Page<AppAnalysisList>> appList(FindAppListVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", apiAnalysisBusiness.findAppList(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/logRecord")
    public Result<Page<ApiLogRecordList>> logRecord(FindLogRecordVm findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", apiAnalysisBusiness.findLogRecordList(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/statistic")
    public Result<List<DateNumVm>> statistic(ApiStatistic findVm) {
        try {
            return ResultUtil.success("成功", apiAnalysisBusiness.statistic(findVm));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/authApiList")
    public Result<Page<AppApiList>> authApiList(FindAuthApiList findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", apiAnalysisBusiness.findAuthApiList(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/authAppList")
    public Result<Page<ApiAppList>> authAppList(FindAuthAppList findVm) {
        JSONObject json = JSONObject.parseObject(JSONObject.toJSONString(findVm));
        try {
            return ResultUtil.success("成功", apiAnalysisBusiness.findAuthAppList(json));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/apiVisitNum")
    public Result<ApiVisitNumVm> apiVisitNum(FindVisitNumVm findVm) {
        try {
            return ResultUtil.success("成功", apiAnalysisBusiness.apiVisitNum(findVm));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/ipVisitNum")
    public Result<List<NameNumVm>> ipVisitNum(FindVisitNumVm findVm) {
        try {
            return ResultUtil.success("成功", apiAnalysisBusiness.ipVisitNum(findVm));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/codeVisitNum")
    public Result<CodeVm> codeVisitNum(FindVisitNumVm findVm) {
        try {
            return ResultUtil.success("成功", apiAnalysisBusiness.codeVisitNum(findVm));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @GetMapping(value = "/durationNum")
    public Result<DurationVm> durationNum(FindVisitNumVm findVm) {
        try {
            return ResultUtil.success("成功", apiAnalysisBusiness.durationNum(findVm));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }

    @ApiAuth("noauth")
    @PostMapping(value = "/httpLog/{ip}")
    public Result<String> httpLog(HttpServletRequest request,@PathVariable("ip") String ip) throws Exception {
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder("");

        br = request.getReader();
        String str;
        while ((str = br.readLine()) != null) {
            sb.append(str);
        }
        br.close();

        if (null != br) {
            br.close();
        }

        String httpLog = sb.toString();
        logger.info("http-log信息->" + httpLog);
        JSONObject jsonObject = JSON.parseObject(httpLog);

        this.apiAnalysisBusiness.insertApiLogRecord(jsonObject,ip);
        return ResultUtil.success("日志数据接收成功");
    }

    @GetMapping(value = "/getContent")
    public Result<String> getContent(String id, Integer type) {
        try {
            return ResultUtil.success("成功", apiAnalysisBusiness.getContent(id, type));
        } catch (Exception e) {
            return ResultUtil.fail("失败:" + e.getMessage());
        }
    }
}

class FindApiListVm extends PageableVm {
    private String apiName;
    private String routeInfo;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getRouteInfo() {
        return routeInfo;
    }

    public void setRouteInfo(String routeInfo) {
        this.routeInfo = routeInfo;
    }
}

class FindAppListVm extends PageableVm {
    private String id;
    private String appName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}

class FindAuthApiList extends PageableVm {
    private String appId;
    private String apiName;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}

class FindAuthAppList extends PageableVm {
    private String apiId;
    private String appName;
    private String appId;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }
}

class FindLogRecordVm extends TimePageableVm {
    private String appId;
    private String appName;
    private String apiId;
    private String apiName;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getApiId() {
        return apiId;
    }

    public void setApiId(String apiId) {
        this.apiId = apiId;
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }
}
