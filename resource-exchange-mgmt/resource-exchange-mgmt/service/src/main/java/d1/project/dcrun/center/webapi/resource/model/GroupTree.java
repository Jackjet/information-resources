package d1.project.dcrun.center.webapi.resource.model;

import java.util.List;

/**
 * @author baozh
 */
public class GroupTree {
    private String name;
    private int level;
    private String id;
    private List<GroupTree> child;

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

    public List<GroupTree> getChild() {
        return child;
    }

    public void setChild(List<GroupTree> child) {
        this.child = child;
    }
}
