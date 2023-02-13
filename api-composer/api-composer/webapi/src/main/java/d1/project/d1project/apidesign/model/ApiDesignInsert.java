package d1.project.d1project.apidesign.model;

/**
 * @author zhengyang
 */
public class ApiDesignInsert {
    private DesignInfo designInfo;

    private String[] hostInfo;

    public DesignInfo getDesignInfo() {
        return designInfo;
    }

    public void setDesignInfo(DesignInfo designInfo) {
        this.designInfo = designInfo;
    }

    public String[] getHostInfo() {
        return hostInfo;
    }

    public void setHostInfo(String[] hostInfo) {
        this.hostInfo = hostInfo;
    }
}
