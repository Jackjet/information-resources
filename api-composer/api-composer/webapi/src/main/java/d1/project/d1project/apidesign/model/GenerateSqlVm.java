package d1.project.d1project.apidesign.model;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class GenerateSqlVm {

    /**
     * 数据库类型
     */
    @NotBlank(message = "数据库类型不能为空")
    private String databaseType;

    /**
     * 表名
     */
    @NotBlank(message = "表名不能为空")
    private String tableName;

    /**
     * 需要查询的字段
     */
    private List<String> fields;

    /**
     * where条件
     */
    private List<List<RequestMetaDataItemVm>> wheres;

    public String getDatabaseType() {
        return databaseType;
    }

    public void setDatabaseType(String databaseType) {
        this.databaseType = databaseType;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<List<RequestMetaDataItemVm>> getWheres() {
        return wheres;
    }

    public void setWheres(List<List<RequestMetaDataItemVm>> wheres) {
        this.wheres = wheres;
    }
}