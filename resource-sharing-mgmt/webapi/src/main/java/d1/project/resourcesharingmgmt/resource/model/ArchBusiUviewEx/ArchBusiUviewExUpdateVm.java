package d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Calendar;

/**
 * @author maoyuying
 */
public class ArchBusiUviewExUpdateVm {
    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    @ApiModelProperty("下架状态,0未下架 1已下架")
    private String status;

    @ApiModelProperty("挂接状态,0未挂接 1已挂接")
    private String hookStatus;

    @ApiModelProperty("文件挂接状态,0未挂接 1已挂接")
    private String fileHookStatus;

    @ApiModelProperty("文件挂接状态,0未挂接 1已挂接")
    private String dataHookStatus;

    @ApiModelProperty("挂接时间")
    private Calendar hookTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getHookStatus() {
        return hookStatus;
    }

    public void setHookStatus(String hookStatus) {
        this.hookStatus = hookStatus;
    }

    public String getFileHookStatus() {
        return fileHookStatus;
    }

    public void setFileHookStatus(String fileHookStatus) {
        this.fileHookStatus = fileHookStatus;
    }

    public String getDataHookStatus() {
        return dataHookStatus;
    }

    public void setDataHookStatus(String dataHookStatus) {
        this.dataHookStatus = dataHookStatus;
    }

    public Calendar getHookTime() {
        return hookTime;
    }

    public void setHookTime(Calendar hookTime) {
        this.hookTime = hookTime;
    }
}
