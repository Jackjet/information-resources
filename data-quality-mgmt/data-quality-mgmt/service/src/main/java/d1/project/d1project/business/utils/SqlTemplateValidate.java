package d1.project.d1project.business.utils;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.business.model.template.FieldModel;
import d1.project.d1project.business.model.template.MetadataDataModel;
import d1.project.d1project.business.model.template.TableModel;
import org.apache.http.util.TextUtils;

import java.util.List;

public class SqlTemplateValidate {


    public static TableModel validateTableModelIsNull(MetadataDataModel model, String message) throws DoValidException {
        TableModel source = model.getSource();
        if (source == null) {
            throw new DoValidException(message);
        }
        return source;
    }

    public static TableModel validateTargetTableModelIsNull(MetadataDataModel model, String message) throws DoValidException {
        TableModel target = model.getTarget();
        if (target == null) {
            throw new DoValidException(message);
        }
        return target;
    }

    public static List<FieldModel> validateFieldModelIsNull(TableModel model, String message) throws DoValidException {
        List<FieldModel> tableFields = model.getFields();
        if (tableFields == null || tableFields.size() == 0) {
            throw new DoValidException(message);
        }
        return tableFields;
    }

    public static String validateTableNameIsNull(TableModel model, String message) throws DoValidException {
        String tableName = model.getName();
        if (TextUtils.isEmpty(tableName)) {
            throw new DoValidException(message);
        }
        return tableName;
    }

    public static List<String> validateOperatorIsNull(MetadataDataModel model, String message) throws DoValidException {
        List<String> operators = model.getOperators();
        if (operators == null || operators.size() == 0) {
            throw new DoValidException(message);
        }
        return operators;
    }

    public static List<String> validateValueIsNull(MetadataDataModel model, String message) throws DoValidException {
        List<String> values = model.getValues();
        if (values == null || values.size() == 0) {
            throw new DoValidException(message);
        }
        return values;
    }


}
