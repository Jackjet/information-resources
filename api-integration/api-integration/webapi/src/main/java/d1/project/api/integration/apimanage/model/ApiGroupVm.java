package d1.project.api.integration.apimanage.model;

import java.util.List;

/**
 * @author baozh
 */
public class ApiGroupVm {
    private String groupId;
    private List<String> apiIds;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public List<String> getApiIds() {
        return apiIds;
    }

    public void setApiIds(List<String> apiIds) {
        this.apiIds = apiIds;
    }
}
