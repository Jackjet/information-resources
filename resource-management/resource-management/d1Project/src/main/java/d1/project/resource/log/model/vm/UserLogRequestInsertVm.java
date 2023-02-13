package d1.project.resource.log.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 18:12
 */
@ApiModel(value = "WebAdminUserUpdateVm", description = "用户日志新增")
public class UserLogRequestInsertVm {
    @ApiModelProperty("内容\ttext\t添加的详情为json")
    private String content;

    @ApiModelProperty("内容描述\ttext\t内容描述为简易信息")
    private String contentMsg;

    @ApiModelProperty("接口")
    private String api;

    @ApiModelProperty("结果:0->失败.1->成功")
    private int result;

    public UserLogRequestInsertVm(String content, String contentMsg, String api, int result) {
        this.content = content;
        this.contentMsg = contentMsg;
        this.api = api;
        this.result = result;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentMsg() {
        return contentMsg;
    }

    public void setContentMsg(String contentMsg) {
        this.contentMsg = contentMsg;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
