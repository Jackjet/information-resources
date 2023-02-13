package d1.project.d1project.log.model.vm;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.d1project.common.model.TimePageableVm;

/**
 * 分页查询操作日志基础
 *
 * @author kikki
 * @date 2020-09-09 10:25
 */
@ApiModel(value = "OperationLogBaseFindAllVm", description = "分页查询操作日志基础")
public class OperationLogBaseFindAllVm extends TimePageableVm {
    @ApiModelProperty("创建人")
    private String createByName;
    @ApiModelProperty("接口")
    private String api;
    @ApiModelProperty("结果")
    private String result;

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
