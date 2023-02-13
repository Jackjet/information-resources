package d1.project.resourcesharingmgmt.resource.controller.webadmin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.entity.DownloadInfoEntity;
import d1.project.resourcesharingmgmt.resource.service.UserSystemSettingsService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 用户子系统访问权限设置
 * @author maoyuying
 */
@Auth("webadmin")
@RestController("/webadmin/userSystemSettings")
@RequestMapping("/webadmin/userSystemSettings")
public class UserSystemSettingsController {

    private final UserSystemSettingsService userSystemSettingsService;

    public UserSystemSettingsController(UserSystemSettingsService userSystemSettingsService) {
        this.userSystemSettingsService = userSystemSettingsService;
    }

    /**
     * 新增权限
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody UserSystemSettingsInsertVm model, HttpServletRequest request) throws DoValidException {
        userSystemSettingsService.insert((JSONObject) JSON.toJSON(model));
        return ResultUtil.success("设置成功");
    }

    /**
     * 查询权限
     * @return
     * @throws Exception
     */
    @Auth("noauth")
    @GetMapping(value = "/findAll")
    public Result<DownloadInfoEntity> findAll(String account) throws Exception {
        return ResultUtil.success("", userSystemSettingsService.findByAccount(account));
    }

}

@ApiModel(value = "UserSystemSettingsInsertVm", description = "用户子系统访问权限设置申请添加")
class UserSystemSettingsInsertVm{
    @ApiModelProperty("账户")
    private String account;

    @ApiModelProperty("值")
    private String value;

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
