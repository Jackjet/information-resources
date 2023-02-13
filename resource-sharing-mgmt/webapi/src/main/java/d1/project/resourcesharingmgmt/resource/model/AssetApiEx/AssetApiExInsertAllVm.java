package d1.project.resourcesharingmgmt.resource.model.AssetApiEx;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import java.util.List;

/**
 * @author maoyuying
 */
@ApiModel(value = "AssetApiExInsertAllVm", description = "批量新增云数据")
public class AssetApiExInsertAllVm {
    @ApiModelProperty("信息资源ID")
    private String uviewId;

    @ApiModelProperty("信息资源名称")
    private String uviewNm;

    @ApiModelProperty("云数据列表List")
    private List<ApiExInsertAllVm> sourceApis;

    public String getUviewId() {
        return uviewId;
    }

    public void setUviewId(String uviewId) {
        this.uviewId = uviewId;
    }

    public String getUviewNm() {
        return uviewNm;
    }

    public void setUviewNm(String uviewNm) {
        this.uviewNm = uviewNm;
    }

    public List<ApiExInsertAllVm> getSourceApis() {
        return sourceApis;
    }

    public void setSourceApis(List<ApiExInsertAllVm> sourceApis) {
        this.sourceApis = sourceApis;
    }
}
