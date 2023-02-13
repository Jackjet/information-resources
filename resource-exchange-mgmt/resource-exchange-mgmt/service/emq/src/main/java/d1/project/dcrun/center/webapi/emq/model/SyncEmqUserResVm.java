package d1.project.dcrun.center.webapi.emq.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author xuaa
 */
@ApiModel(value = "SyncEmqUserResVm", description = "同步开发者信息发送的json")
public class SyncEmqUserResVm {
    private String appid;
    @ApiModelProperty(value = "开发者密钥")
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
