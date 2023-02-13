package d1.project.d1project.business.utils;

/**
 * 等于
 * 不等于
 * 大于
 * 大于等于
 * 小于
 * 小于等于
 * 在...和...之间
 * 不在...和...之间
 */
public enum Operator {
    EQUAL("=", "等于"),
    NOT_EQUAL("<>", "不等于"),
    MORE_THAN(">", "大于"),
    MORE_THAN_OR_EQUAL(">=", "大于等于"),
    LESS_THAN("<", "小于"),
    LESS_THAN_OR_EQUAL("<=", "小于等于"),
    BETWEEN("between", "在...和...之间"),
    NOT_BETWEEN("not_between", "不在...和...之间");

    private String id;
    /**
     * 中文名
     */
    private String name;

    Operator(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Operator getOperatorById(String id) {
        for (Operator operator : Operator.values()) {
            if (operator.getId().equals(id)) {
                return operator;
            }
        }
        return null;
    }

    public static Operator getOperatorByName(String name) {
        for (Operator operator : Operator.values()) {
            if (operator.getName().equals(name)) {
                return operator;
            }
        }
        return null;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return name;
    }

}
