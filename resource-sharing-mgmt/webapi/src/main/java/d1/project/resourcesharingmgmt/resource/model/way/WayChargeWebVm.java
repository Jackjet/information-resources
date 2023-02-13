package d1.project.resourcesharingmgmt.resource.model.way;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

/**
 * @author dy
 */
@ApiModel(value = "WayChargeWebVm", description = "代办门户查询结果vm")
public class WayChargeWebVm {
    @ApiModelProperty("模块标识")
    private String remark;
    @ApiModelProperty("模块名称")
    private String remarkName;
    @ApiModelProperty("跳转路径")
    private String path;
    @ApiModelProperty("代办数量")
    private Integer num;
    @ApiModelProperty("数据")
    private Object data;
    @ApiModelProperty("排序")
    private Integer seq;

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemarkName() {
        return remarkName;
    }

    public void setRemarkName(String remarkName) {
        this.remarkName = remarkName;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public Integer getSeq() {
        return seq;
    }

    public void setSeq(Integer seq) {
        this.seq = seq;
    }
}
