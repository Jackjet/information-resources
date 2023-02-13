package d1.project.container.api.model;

public class RunModelFindAll extends PageableVm {

    private String name;

    private String method;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
