package d1.project.d1project.business.service.template;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.business.model.template.FieldModel;
import d1.project.d1project.business.model.template.MetadataDataModel;
import d1.project.d1project.business.model.template.TableModel;
import d1.project.d1project.business.service.template.base.SqlTemplateService;
import d1.project.d1project.business.utils.SqlTemplateValidate;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * SqlServer sql模板
 */
public class SqlServerSqlTemplateService extends SqlTemplateService {


    /**
     * 长度约束，字段需要都是字符型，否则会报错
     * <p>
     * tableName   表名
     * tableFields 表字段
     * operator    运算符，支持：=、<>、>、>=、<、<=
     * length      字符长度
     * isUnion     是否联合
     *
     * @return sql 语句
     */
    @Override
    public String getLengthSql(MetadataDataModel model) throws DoValidException {
        //SELECT * FROM my_user WHERE len(name)>5 OR len(phone)>5

        TableModel source = SqlTemplateValidate.validateTableModelIsNull(model, "元数据不可为空");
        List<FieldModel> tableFields = SqlTemplateValidate.validateFieldModelIsNull(source, "元数据字段不可为空");
        String tableName = SqlTemplateValidate.validateTableNameIsNull(source, "表名不可为空");

        List<String> operators = SqlTemplateValidate.validateOperatorIsNull(model, "运算符不可为空");
        String operator = operators.get(0);

        List<String> values = SqlTemplateValidate.validateValueIsNull(model, "长度不可为空");
        String length = values.get(0);

        boolean isUnion = model.isUnion();

        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName + " WHERE ");
        for (int i = 0; i < tableFields.size(); i++) {
            sql.append("len(" + tableFields.get(i).getName() + ")" + operator + length + " ");
            if (i != tableFields.size() - 1) {
                sql.append(isUnion ? "AND " : "OR ");
            } else {
                sql.append(";");
            }
        }
        return sql.toString();
    }

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
        //SELECT * FROM my_user WHERE name NOT LIKE '[A-H]%' OR name IS NULL;

        TableModel source = SqlTemplateValidate.validateTableModelIsNull(model, "元数据不可为空");
        List<FieldModel> tableFields = SqlTemplateValidate.validateFieldModelIsNull(source, "元数据字段不可为空");
        String tableName = SqlTemplateValidate.validateTableNameIsNull(source, "表名不可为空");
        List<String> values = SqlTemplateValidate.validateValueIsNull(model, "规范内容(正则表达式)不可为空");

        String tableField = tableFields.get(0).getName();
        String regExp = values.get(0);

        if (StringUtils.isEmpty(regExp)) {
            throw new DoValidException("规范内容(正则表达式)不可为空");
        }

        if (regExp.charAt(0) == '^') { //去掉^ 加上 %
            regExp = regExp.substring(1);

        } else { //没有^ 就需要加上%
            regExp = "%" + regExp;
        }
        regExp = regExp + "%";

        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName + " WHERE " + tableField + " NOT LIKE " + "'" + regExp + "' OR " + tableField + " IS NULL;");
        return sql.toString();
    }

}
