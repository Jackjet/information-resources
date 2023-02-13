package d1.project.tangshan.operation.manage.model;

/**
 * @author lin
 */
public class ModuleCountVm {
    private String date;
    private Long number;
    private String name;
    private String moduleName;

    public ModuleCountVm() {
    }

    public ModuleCountVm(String date, Long number, String name, String moduleName) {
        this.date = date;
        this.number = number;
        this.name = name;
        this.moduleName = moduleName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Long getNumber() {
        return number;
    }

    public void setNumber(Long number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }
}
