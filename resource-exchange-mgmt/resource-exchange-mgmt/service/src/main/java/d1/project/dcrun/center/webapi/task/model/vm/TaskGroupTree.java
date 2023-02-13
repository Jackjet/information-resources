package d1.project.dcrun.center.webapi.task.model.vm;

import java.util.List;

/**
 * @author baozh
 */
public class TaskGroupTree {
    private String name;
    private int level;
    private String id;
    private List<TaskGroupTree> child;

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

    public List<TaskGroupTree> getChild() {
        return child;
    }

    public void setChild(List<TaskGroupTree> child) {
        this.child = child;
    }
}
