package d1.project.dcrun.center.webapi.dcapi.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author libin
 */
@ApiModel(value = "SyncApiRouteUserVm", description = "开发者信息同步")
public class SyncApiRouteUserVm {
    @ApiModelProperty("开发者id")
    private String appid;
    @ApiModelProperty("开发者密钥")
    private String appkey;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppkey() {
        return appkey;
    }

    public void setAppkey(String appkey) {
        this.appkey = appkey;
    }
}
