package d1.project.resourcesharingmgmt.resource.model.correction;

import d1.framework.webapi.annotation.ApiModel;

import javax.validation.constraints.NotBlank;

@ApiModel(value = "CorrectionInsertVm", description = "纠错添加")
public class CorrectionInsertVm {
    /**
     * 信息资源ID
     */
    @NotBlank(message = "信息资源ID不可为空")
    private String uviewId;

    /**
     * 反馈类别，0其他，1数据与实际情况不符，2资源过时，3数据无法下载，
     */
    private int type;

    /**
     * 描述
     */
    private String describe;

    public String getUviewId() {
        return uviewId;
    }

    public void setUviewId(String uviewId) {
        this.uviewId = uviewId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }
}
