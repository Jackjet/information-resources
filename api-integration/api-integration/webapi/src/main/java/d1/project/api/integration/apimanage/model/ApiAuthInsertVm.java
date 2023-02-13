package d1.project.api.integration.apimanage.model;

import java.util.List;

/**
 * @author baozh
 */
public class ApiAuthInsertVm {
    private String container;
    private List<String> appIdList;
    private List<String> apiIdList;
    private Integer listType;
    private String listContent;


    public List<String> getAppIdList() {
        return appIdList;
    }

    public void setAppIdList(List<String> appIdList) {
        this.appIdList = appIdList;
    }

    public List<String> getApiIdList() {
        return apiIdList;
    }

    public void setApiIdList(List<String> apiIdList) {
        this.apiIdList = apiIdList;
    }

    public Integer getListType() {
        return listType;
    }

    public void setListType(Integer listType) {
        this.listType = listType;
    }

    public String getListContent() {
        return listContent;
    }

    public void setListContent(String listContent) {
        this.listContent = listContent;
    }

    public String getContainer() {
        return container;
    }

    public void setContainer(String container) {
        this.container = container;
    }
}
