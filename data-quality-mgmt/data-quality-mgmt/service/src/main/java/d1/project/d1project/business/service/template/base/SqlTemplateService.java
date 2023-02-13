package d1.project.d1project.business.service.template.base;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.business.model.template.FieldModel;
import d1.project.d1project.business.model.template.MetadataDataModel;
import d1.project.d1project.business.model.template.TableModel;
import d1.project.d1project.business.utils.Operator;
import d1.project.d1project.business.utils.SqlTemplateValidate;

import java.lang.reflect.Method;
import java.util.List;

/**
 * sql 规则模板
 * <p>
 * 完整性非空约束
 * 实体唯一性约束
 * 长度约束
 * 标志取值约束
 * 代码值域约束
 * 取值范围约束
 * 内容规范约束
 * 等值一致性依赖约束
 * 逻辑一致性依赖约束
 * 取值准确性约束
 * <p>
 * 以 Postgresql 数据库为准，sql语句不同需要重新对应方法
 */
public class SqlTemplateService {

    /**
     * 获取规则对应的sql语句
     *
     * @param code  方法唯一标识，初始化存入数据库中
     * @param model 模板数据
     * @return sql 语句
     * @throws Exception 异常
     */
    public final String getSql(String code, MetadataDataModel model) throws Exception {
        Method get = getClass().getMethod("get" + code + "Sql", MetadataDataModel.class);
        Object result = get.invoke(this, model);
        return (String) result;
    }


    /**
     * 完整性非空约束
     * <p>
     * tableName   表名
     * tableFields 表字段
     * isUnion     是否联合为空
     *
     * @return sql 语句
     */
    public String getNotNullSql(MetadataDataModel model) throws DoValidException {
        //SELECT * FROM my_user WHERE name IS NULL OR age IS NULL OR sex IS NULL ;
        //SELECT * FROM my_user WHERE name IS NULL AND age IS NULL AND sex IS NULL ;

        TableModel source = SqlTemplateValidate.validateTableModelIsNull(model, "元数据不可为空");
        List<FieldModel> tableFields = SqlTemplateValidate.validateFieldModelIsNull(source, "元数据字段不可为空");
        String tableName = SqlTemplateValidate.validateTableNameIsNull(source, "表名不可为空");
        boolean isUnion = model.isUnion();

        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName + " WHERE ");
        for (int i = 0; i < tableFields.size(); i++) {
            sql.append(tableFields.get(i).getName() + " IS NULL ");
            if (i != tableFields.size() - 1) {
                sql.append(isUnion ? "AND " : "OR ");
            } else {
                sql.append(";");
            }
        }
        return sql.toString();
    }

    /**
     * 实体唯一性约束
     * <p>
     * tableName   表名
     * tableFields 表字段
     *
     * @return sql 语句
     */
    public String getUniqueSql(MetadataDataModel model) throws DoValidException {
        //SELECT name,age,sex FROM my_user GROUP BY name,age,sex HAVING count(1)>1;

        TableModel source = SqlTemplateValidate.validateTableModelIsNull(model, "元数据不可为空");
        List<FieldModel> tableFields = SqlTemplateValidate.validateFieldModelIsNull(source, "元数据字段不可为空");
        String tableName = SqlTemplateValidate.validateTableNameIsNull(source, "表名不可为空");

        String fields = joinFields(",", tableFields);
        StringBuffer sql = new StringBuffer("SELECT " + fields + " FROM " + tableName + " GROUP BY " + fields + " HAVING count(1)>1;");
        return sql.toString();
    }

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
    public String getLengthSql(MetadataDataModel model) throws DoValidException {
        //SELECT * FROM my_user WHERE length(name)>5 OR length(phone)>5

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
            sql.append("length(" + tableFields.get(i).getName() + ")" + operator + length + " ");
            if (i != tableFields.size() - 1) {
                sql.append(isUnion ? "AND " : "OR ");
            } else {
                sql.append(";");
            }
        }
        return sql.toString();
    }

    /**
     * 标志取值约束
     * <p>
     * tableName  表名
     * tableField 表字段
     * tagValues  标记值（枚举值）
     *
     * @return sql 语句
     */
    public String getTagValueSql(MetadataDataModel model) throws DoValidException {
        //SELECT * FROM my_user WHERE id NOT in('1','0');

        TableModel source = SqlTemplateValidate.validateTableModelIsNull(model, "元数据不可为空");
        List<FieldModel> tableFields = SqlTemplateValidate.validateFieldModelIsNull(source, "元数据字段不可为空");
        String tableName = SqlTemplateValidate.validateTableNameIsNull(source, "表名不可为空");
        List<String> tagValues = SqlTemplateValidate.validateValueIsNull(model, "标记值不可为空");

        String tableField = tableFields.get(0).getName();

        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName + " WHERE " + tableField + " NOT IN " + "('" + String.join("','", tagValues) + "');");
        return sql.toString();
    }

    /**
     * 代码值域约束
     * <p>
     * tableName        表名
     * targetTableName  代码表名
     * tableField       表字段
     * targetTableField 代码表字段
     *
     * @return sql 语句
     */
    public String getCodeRangeSql(MetadataDataModel model) throws DoValidException {
        //SELECT * FROM my_user WHERE name not in (SELECT name FROM d1_role_copy1 WHERE name IS NOT NULL);

        TableModel source = SqlTemplateValidate.validateTableModelIsNull(model, "元数据不可为空");
        TableModel target = SqlTemplateValidate.validateTargetTableModelIsNull(model, "比较元数据不可为空");
        List<FieldModel> tableFields = SqlTemplateValidate.validateFieldModelIsNull(source, "元数据字段不可为空");
        List<FieldModel> targetTableFields = SqlTemplateValidate.validateFieldModelIsNull(target, "比较元数据中字段不可为空");
        String tableName = SqlTemplateValidate.validateTableNameIsNull(source, "表名不可为空");
        String targetTableName = SqlTemplateValidate.validateTableNameIsNull(target, "比较元数据中表名不可为空");

        String tableField = tableFields.get(0).getName();
        String targetTableField = targetTableFields.get(0).getName();

        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName + " WHERE " + tableField + " NOT IN " + "(SELECT " + targetTableField + " FROM " + targetTableName + " WHERE " + targetTableField + " IS NOT NULL);");
        return sql.toString();
    }

    /**
     * 取值范围约束
     * <p>
     * tableName  表名
     * tableField 表字段
     * operator   Operator
     * values     范围值，都必须两个值
     *
     * @return sql 语句
     * @throws DoValidException 异常
     */
    public String getValueRangeSql(MetadataDataModel model) throws DoValidException {
        //SELECT * FROM my_user WHERE (age < 10 OR age > 20);

        TableModel source = SqlTemplateValidate.validateTableModelIsNull(model, "元数据不可为空");
        List<FieldModel> tableFields = SqlTemplateValidate.validateFieldModelIsNull(source, "元数据字段不可为空");
        String tableName = SqlTemplateValidate.validateTableNameIsNull(source, "表名不可为空");
        List<String> operators = SqlTemplateValidate.validateOperatorIsNull(model, "运算符不可为空");
        List<String> values = SqlTemplateValidate.validateValueIsNull(model, "范围值不可为空");

        if (values.size() != 2) {
            throw new DoValidException("范围值需要有两个");
        }

        String tableField = tableFields.get(0).getName();
        String operator = operators.get(0);

        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName + " WHERE ");
        if (Operator.BETWEEN.getId().equals(operator)) {
            sql.append("(" + tableField + " > '" + values.get(0) + "' AND " + tableField + " < '" + values.get(1) + "');");
        } else if (Operator.NOT_BETWEEN.getId().equals(operator)) {
            sql.append("(" + tableField + " < '" + values.get(0) + "' OR " + tableField + " > '" + values.get(1) + "');");
        } else {
            sql.append(tableField + operator + "'" + values.get(0) + "';");
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
    public String getContentSpecificationSql(MetadataDataModel model) throws DoValidException {
        //SELECT * FROM my_user WHERE name !~* '^[A-H]' OR name IS NULL;

        TableModel source = SqlTemplateValidate.validateTableModelIsNull(model, "元数据不可为空");
        List<FieldModel> tableFields = SqlTemplateValidate.validateFieldModelIsNull(source, "元数据字段不可为空");
        String tableName = SqlTemplateValidate.validateTableNameIsNull(source, "表名不可为空");
        List<String> values = SqlTemplateValidate.validateValueIsNull(model, "规范内容(正则表达式)不可为空");

        String tableField = tableFields.get(0).getName();
        String regExp = values.get(0);

        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName + " WHERE " + tableField + " !~* " + "'" + regExp + "' OR " + tableField + " IS NULL;");
        return sql.toString();
    }

    /**
     * 存在一致性依赖约束
     *
     * @return sql 语句
     *
     *public String getExistConsistencySql(String tableName, String tableField, String targetTableName, String targetTableField) {
     *   //SELECT * FROM my_user s LEFT JOIN d1_role_copy1 t ON s.name = t.name WHERE t.name IS NULL;
     *
     *  StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName + " WHERE " + tableField + " NOT IN " + "(SELECT " + targetTableField + " FROM " + targetTableName + ") OR " + tableField + " IS NULL;");
     *  return sql.toString();
    }*/

    /**
     * 等值一致性依赖约束
     * <p>
     * tableName         表名
     * tableFields       表字段
     * targetTableName   要比较的表名
     * targetTableFields 要比较的表字段
     *
     * @return sql 语句
     * @throws DoValidException 异常
     */
    public String getEquivalentConsistencySql(MetadataDataModel model) throws DoValidException {
        //SELECT * FROM my_user s,d1_role_copy1 t WHERE (s.id = t.user_id) AND ((s.name != t.name) OR (s.age != t.age));

        TableModel source = SqlTemplateValidate.validateTableModelIsNull(model, "元数据不可为空");
        TableModel target = SqlTemplateValidate.validateTargetTableModelIsNull(model, "比较元数据不可为空");
        List<FieldModel> tableFields = SqlTemplateValidate.validateFieldModelIsNull(source, "元数据字段不可为空");
        List<FieldModel> targetTableFields = SqlTemplateValidate.validateFieldModelIsNull(target, "比较元数据中字段不可为空");
        String tableName = SqlTemplateValidate.validateTableNameIsNull(source, "表名不可为空");
        String targetTableName = SqlTemplateValidate.validateTableNameIsNull(target, "比较元数据中表名不可为空");

        if (tableFields.size() < 2) {
            throw new DoValidException("字段数量必须大于2个");
        }

        if (tableFields.size() != targetTableFields.size()) {
            throw new DoValidException("原字段与比较字段数量不一致");
        }

        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName + " s," + targetTableName + " t WHERE ");
        for (int i = 0; i < tableFields.size(); i++) {
            if (i == 0) {
                sql.append("(s." + tableFields.get(i).getName() + " = t." + targetTableFields.get(i).getName() + ")");
                sql.append(" AND (");
            } else {
                sql.append("(s." + tableFields.get(i).getName() + " != t." + targetTableFields.get(i).getName() + ")");
                if (i != tableFields.size() - 1) {
                    sql.append(" OR ");
                } else {
                    sql.append(");");
                }
            }
        }
        return sql.toString();
    }

    /**
     * 逻辑一致性依赖约束
     * <p>
     * tableName         表名
     * tableFields       表字段
     * targetTableName   要比较的表名
     * targetTableFields 要比较的表字段
     * operators         操作符
     *
     * @return sql 语句
     * @throws DoValidException 异常
     */
    public String getLogicalConsistencySql(MetadataDataModel model) throws DoValidException {
        //SELECT * FROM my_user s,d1_role_copy1 t WHERE (s.id = t.user_id) AND ((s.id > t.id) OR (s.age < t.age));

        TableModel source = SqlTemplateValidate.validateTableModelIsNull(model, "元数据不可为空");
        TableModel target = SqlTemplateValidate.validateTargetTableModelIsNull(model, "比较元数据不可为空");
        List<FieldModel> tableFields = SqlTemplateValidate.validateFieldModelIsNull(source, "元数据字段不可为空");
        List<FieldModel> targetTableFields = SqlTemplateValidate.validateFieldModelIsNull(target, "比较元数据中字段不可为空");
        String tableName = SqlTemplateValidate.validateTableNameIsNull(source, "表名不可为空");
        String targetTableName = SqlTemplateValidate.validateTableNameIsNull(target, "比较元数据中表名不可为空");
        List<String> operators = SqlTemplateValidate.validateOperatorIsNull(model, "运算符不可为空");

        if (tableFields.size() != targetTableFields.size()) {
            throw new DoValidException("原字段与比较字段数量不一致");
        }

        if (tableFields.size() < 2) {
            throw new DoValidException("字段数量必须大于2个");
        }

        if (tableFields.size() != operators.size()) {
            throw new DoValidException("字段与操作符数量不一致");
        }

        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName + " s," + targetTableName + " t WHERE ");
        for (int i = 0; i < tableFields.size(); i++) {
            sql.append("(s." + tableFields.get(i).getName() + " " + operators.get(i) + " t." + targetTableFields.get(i).getName() + ")");
            if (i == 0) {
                sql.append(" AND (");
            } else {
                if (i != tableFields.size() - 1) {
                    sql.append(" OR ");
                } else {
                    sql.append(");");
                }
            }
        }
        return sql.toString();
    }

    /**
     * 取值准确性约束
     * <p>
     * tableName   表名
     * tableFields 表字段
     * fieldValues 字段值
     *
     * @return sql 语句
     * @throws DoValidException 异常
     */
    public String getValueAccuracySql(MetadataDataModel model) throws DoValidException {
        //SELECT * FROM my_user WHERE (id = '1') AND ((name != 'fengzi') OR (age != '13'));

        TableModel source = SqlTemplateValidate.validateTableModelIsNull(model, "元数据不可为空");
        List<FieldModel> tableFields = SqlTemplateValidate.validateFieldModelIsNull(source, "元数据字段不可为空");
        String tableName = SqlTemplateValidate.validateTableNameIsNull(source, "表名不可为空");
        List<String> fieldValues = SqlTemplateValidate.validateValueIsNull(model, "字段值不可为空");

        if (tableFields.size() != fieldValues.size()) {
            throw new DoValidException("字段与值数量不一致");
        }

        if (tableFields.size() < 2) {
            throw new DoValidException("字段数量必须大于2个");
        }

        StringBuffer sql = new StringBuffer("SELECT * FROM " + tableName + " WHERE ");
        for (int i = 0; i < tableFields.size(); i++) {
            if (i == 0) {
                sql.append("(" + tableFields.get(i).getName() + " = '" + fieldValues.get(i) + "')");
                sql.append(" AND (");
            } else {
                sql.append("(" + tableFields.get(i).getName() + " != '" + fieldValues.get(i) + "')");
                if (i != tableFields.size() - 1) {
                    sql.append(" OR ");
                } else {
                    sql.append(");");
                }
            }
        }
        return sql.toString();
    }


    //////////////
    protected String joinFields(String delimiter, List<FieldModel> tableFields) {
        StringBuffer fields = new StringBuffer();
        for (int i = 0; i < tableFields.size(); i++) {
            fields.append(tableFields.get(i).getName());
            if (i != tableFields.size() - 1) {
                fields.append(delimiter);
            }
        }
        return fields.toString();
    }
}
