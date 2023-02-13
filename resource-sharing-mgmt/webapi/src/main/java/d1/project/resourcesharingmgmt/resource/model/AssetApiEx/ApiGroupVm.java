package d1.project.resourcesharingmgmt.resource.model.AssetApiEx;

import java.util.List;

/**
 * @author zhengyang
 */
public class ApiGroupVm {
    private String name;
    private int level;
    private String id;
    private List<ApiGroupVm> child;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<ApiGroupVm> getChild() {
        return child;
    }

    public void setChild(List<ApiGroupVm> child) {
        this.child = child;
    }
}
