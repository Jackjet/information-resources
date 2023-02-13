package d1.project.resourcesharingmgmt.resource.model.way;

import d1.framework.webapi.annotation.ApiModel;

/**
 * @author dy
 */
@ApiModel(value = "WayCatalogResVm", description = "代办调用资源目结果vm")
public class WayCatalogResVm {
    private  Integer num;
    private  Object data;

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
}
