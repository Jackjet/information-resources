package d1.project.d1project.business.service.template;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.business.model.template.FieldModel;
import d1.project.d1project.business.model.template.MetadataDataModel;
import d1.project.d1project.business.model.template.TableModel;
import d1.project.d1project.business.service.template.base.SqlTemplateService;
import d1.project.d1project.business.utils.SqlTemplateValidate;

import java.util.List;

/**
 * Mysql sql模板
 */
public class MysqlSqlTemplateService extends SqlTemplateService {

    /**
     * 内容规范约束，支持正则表达式
     * <p>
     * tableName  表名
     * tableField 表字段
     * regExp     正则表达式
     *
     * @return sql 语句
     */
    @Override
    public String getContentSpecificationSql(MetadataDataModel model) throws DoValidException {
        //SELECT * FROM my_user WHERE name NOT REGEXP '^[A-H]' OR name IS NULL;

        TableModel source = SqlTemplateValidate.validateTableModelIsNull(model, "元数据不可为空");
        List<FieldModel> tableFields = SqlTemplateValidate.validateFieldModelIsNull(source, "元数据字段不可为空");
        String tableName = SqlTemplateValidate.validateTableNameIsNull(source, "表名不可为空");
        List<String> values = SqlTemplateValidate.validateValueIsNull(model, "规范内容(正则表达式)不可为空");

        String tableField = tableFields.get(0).getName();
        String regExp = values.get(0);

        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName + " WHERE " + tableField + " NOT REGEXP " + "'" + regExp + "' OR " + tableField + " IS NULL;");
        return sql.toString();
    }
}
