package d1.project.resourcesharingmgmt.log.model.vm;

import d1.project.resourcesharingmgmt.common.model.TimePageableVm;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-09 10:25
 */

public class OperationLogBaseFindAllVm extends TimePageableVm {

    private String createByName;

    private String api;

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
