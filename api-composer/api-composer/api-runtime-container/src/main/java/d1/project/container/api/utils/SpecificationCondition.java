package d1.project.container.api.utils;

/**
 * @author Buter
 * @date 2020/4/3 19:56
 */
public class SpecificationCondition {

    private String rootKey;
    private String paramKey;
    private PredicateType type;
    private String timeFormat;

    public SpecificationCondition(String rootKey, String paramKey, PredicateType type) {
        this(rootKey, paramKey, type, null);
    }

    public SpecificationCondition(String rootKey, String paramKey, PredicateType type, String timeFormat) {
        setRootKey(rootKey);
        setParamKey(paramKey);
        setType(type);
        setTimeFormat(timeFormat);
    }

    String getRootKey() {
        return rootKey;
    }

    private void setRootKey(String rootKey) {
        this.rootKey = rootKey;
    }

    String getParamKey() {
        return paramKey;
    }

    private void setParamKey(String paramKey) {
        this.paramKey = paramKey;
    }

    PredicateType getType() {
        return type;
    }

    private void setType(PredicateType type) {
        this.type = type;
    }

    String getTimeFormat() {
        return timeFormat;
    }

    private void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }

}
