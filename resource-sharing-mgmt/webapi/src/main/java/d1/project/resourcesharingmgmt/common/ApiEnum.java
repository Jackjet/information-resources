package d1.project.resourcesharingmgmt.common;

public enum ApiEnum {
    /**
     * 新增
     */
    INSERT("insert", "新增"),
    /**
     * 新增全部
     */
    INSERT_ALL("insertAll", "新增全部"),
    /**
     * 修改
     */
    UPDATE("insertAll", "修改"),
    /**
     * 修改全部
     */
    UPDATE_ALL("insertAll", "修改全部"),
    /**
     * 删除
     */
    DELETE("insertAll", "删除"),
    /**
     * 删除全部
     */
    DELETE_ALL("insertAll", "删除全部");

    private final String api;
    private final String apiName;

    ApiEnum(String api, String apiName) {
        this.api = api;
        this.apiName = apiName;
    }

    public static String getApiNameValue(String api) {
        for (ApiEnum value : ApiEnum.values()) {
            if (value.api.equals(api)){
                return value.apiName;
            }
        }
        return null;
    }

    public String getApi() {
        return api;
    }

    public String getApiName() {
        return apiName;
    }

}