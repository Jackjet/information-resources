package d1.project.container.api.utils;

import com.alibaba.fastjson.JSONObject;
import org.hibernate.query.criteria.internal.expression.LiteralExpression;
import org.hibernate.query.criteria.internal.predicate.ComparisonPredicate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.text.SimpleDateFormat;
import java.util.*;

enum PredicateType {
    //字符串相等
    STRING_EQUAL,
    //字符串相等
    STRING_NOT_EQUAL,
    //字符串在一个列表里
    STRING_IN,
    //字符串在不在一个列表里
    STRING_NOT_IN,
    //字符串包含
    STRING_CONTAIN,
    //数字等于
    NUMBER_EQUAL,
    //数字不等于
    NUMBER_NOT_EQUAL,
    //数字大于
    NUMBER_THAN,
    //数字小于
    NUMBER_LESS,
    //日期大于
    DATETIME_THAN,
    //日期小于
    DATETIME_LESS,
    //日期区间
    DATETIME_BETWEEN,
    //日期大于等于
    DATETIME_THAN_EQUAL,
    //日期小于等于
    DATETIME_LESS_EQUAL,
    //1=1
    IDENTICALLY_EQUAL,
    //=null
    IS_NULL,
    //!=null
    IS_NOT_NULL,
    //trim之后=空字符串
    IS_BLANK,
    //指定某一个为空的参数查询
    IS_BLANK_EQUAL,
    //trim之后!=空字符串
    IS_NOT_BLANK,
    //指定一个不为空的参数查询
    IS_NOT_BLANK_EQUAL
}

/**
 * 生成jpa Specification的工具类
 *
 * @author Buter
 */
public class SpecificationBuilder<T> {
    private static Logger logger = LoggerFactory.getLogger(SpecificationBuilder.class);

    private JSONObject paramObject;
    private List<SpecificationCondition> conditions = new ArrayList<>();
    private Map<String, String> orders = new HashMap<>();

    public Specification<T> build() {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();
            for (SpecificationCondition condition : conditions) {
                Predicate predicate = buildPredicate(condition, root, criteriaBuilder);
                if (!isLiteralTrue(predicate)) {
                    //如果是1=1就不加进到数组里了，去掉sql语句中的1=1
                    predicates.add(buildPredicate(condition, root, criteriaBuilder));
                }
            }
            Predicate[] pre = new Predicate[predicates.size()];
            return query.where(predicates.toArray(pre)).orderBy(toCriteriaOrder(root, criteriaBuilder)).getRestriction();

        };
    }

    public Specification<T> build(String expression) throws Exception {
        //解析类似表达式 10 and 1 or 2 and (4 or 15)
        SpecificationExpression specificationExpression = new SpecificationExpression(expression);
        List<String> list = specificationExpression.transfer();
        return (root, query, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.equal(criteriaBuilder.literal(1), 1);
            Stack<Predicate> stack = new Stack<>();
            try {
                for (String s : list) {
                    // 普通数值的处理
                    if (s.matches("\\d+")) {
                        int i = Integer.parseInt(s);
                        if (i >= conditions.size()) {
                            throw new Exception("表达式里的数据越界:" + i);
                        }
                        stack.push(buildPredicate(conditions.get(i), root, criteriaBuilder));
                    }
                    // & | 运算符的处理
                    else if (s.equals("&") || s.equals("|")) {
                        Predicate k1 = stack.pop();
                        Predicate k2 = stack.pop();
                        if (s.equals("&")) {
                            //如果是1=1，可以合并
                            if (isLiteralTrue(k1)) {
                                predicate = k2;
                            } else if (isLiteralTrue(k2)) {
                                predicate = k1;
                            } else {
                                predicate = criteriaBuilder.and(k1, k2);
                            }
                        } else {
                            //如果是1=1，可以合并,这里虽然是or，但是这里的1=1就表示常false，可以和and一个逻辑
                            if (isLiteralTrue(k1)) {
                                predicate = k2;
                            } else if (isLiteralTrue(k2)) {
                                predicate = k1;
                            } else {
                                predicate = criteriaBuilder.or(k1, k2);
                            }
                        }
                        stack.push(predicate);
                    }
                }
            } catch (Exception e) {
                logger.error(expression + "解析表达式错误", e);
                return null;
            }
            return query.where(predicate).orderBy(toCriteriaOrder(root, criteriaBuilder)).getRestriction();
        };
    }

    public Pageable getPageable() {
        return getPageable(null);
    }

    /**
     * 查找出的数据列表转换成Page对象
     *
     * @param list     全数据列表
     * @param pageable pageable
     * @return Page对象
     */
    public Page<T> listToPage(List<T> list, Pageable pageable) {
        if (list == null || pageable == null) {
            logger.error("SpecificationBuilder.listToPage的list或pageable为null");
            return null;
        }
        int start = (int) pageable.getOffset();
        int end = (start + pageable.getPageSize()) > list.size() ? list.size() : (start + pageable.getPageSize());
        return new PageImpl<T>(list.subList(start, end), pageable, list.size());
    }

    /**
     * 写死了page和size2个字段名，缺省是0和10
     */
    public Pageable getPageable(Sort sort) {
        Integer page = paramObject.getInteger("Page");
        page = page != null ? page : paramObject.getInteger("page");
        Integer size = paramObject.getInteger("Size");
        size = size != null ? size : paramObject.getInteger("size");
        page = (page == null || page < 1) ? 0 : page - 1;
        if (size == null || size < 1) {
            size = 10;
        }

        return sort != null ? PageRequest.of(page, size, sort) : PageRequest.of(page, size);
    }

    /**
     * ASC Order
     */
    public SpecificationBuilder<T> aOrder(String column) {
        this.orders.put(column, "asc");
        return this;
    }

    /**
     * DESC Order
     */
    public SpecificationBuilder<T> dOrder(String column) {
        this.orders.put(column, "desc");
        return this;
    }

    /**
     * 初始化SpecificationBuilder，paramObject不能为空
     */
    public SpecificationBuilder<T> init(JSONObject paramObject) throws Exception {
        if (paramObject == null) {
            throw new Exception("参数对象不能为空");
        }
        this.paramObject = paramObject;
        return this;
    }

    /**
     * 数字等于判定
     */
    public SpecificationBuilder<T> nEqual(String rootKey, String paramKey) {
        conditions.add(new SpecificationCondition(rootKey, paramKey, PredicateType.NUMBER_EQUAL));
        return this;
    }

    /**
     * 数字不等于判定
     */

    public SpecificationBuilder<T> nNotEqual(String rootKey, String paramKey) {
        conditions.add(new SpecificationCondition(rootKey, paramKey, PredicateType.NUMBER_NOT_EQUAL));
        return this;
    }

    /**
     * 数字大于判定
     */
    public SpecificationBuilder<T> nThan(String rootKey, String paramKey) {
        conditions.add(new SpecificationCondition(rootKey, paramKey, PredicateType.NUMBER_THAN));
        return this;
    }

    /**
     * 数字小于判定
     */

    public SpecificationBuilder<T> nLess(String rootKey, String paramKey) {
        conditions.add(new SpecificationCondition(rootKey, paramKey, PredicateType.NUMBER_LESS));
        return this;
    }

    /**
     * 字符串等于判定
     */

    public SpecificationBuilder<T> sEqual(String rootKey, String paramKey) {
        conditions.add(new SpecificationCondition(rootKey, paramKey, PredicateType.STRING_EQUAL));
        return this;
    }

    /**
     * 字符串不等于判定
     */

    public SpecificationBuilder<T> sNotEqual(String rootKey, String paramKey) {
        conditions.add(new SpecificationCondition(rootKey, paramKey, PredicateType.STRING_NOT_EQUAL));
        return this;
    }

    /**
     * 字符串在一个列表里
     */

    public SpecificationBuilder<T> sIn(String rootKey, String paramKey) {
        conditions.add(new SpecificationCondition(rootKey, paramKey, PredicateType.STRING_IN));
        return this;
    }

    /**
     * 字符串在不在一个列表里
     */
    public SpecificationBuilder<T> sNotIn(String rootKey, String paramKey) {
        conditions.add(new SpecificationCondition(rootKey, paramKey, PredicateType.STRING_NOT_IN));
        return this;
    }

    /**
     * 字符串包含判定
     */

    public SpecificationBuilder<T> sContain(String rootKey, String paramKey) {
        conditions.add(new SpecificationCondition(rootKey, paramKey, PredicateType.STRING_CONTAIN));
        return this;
    }

    /**
     * 时间区间判定
     */
    public SpecificationBuilder<T> tBetween(String rootKey, String startTimeKey, String endTimeKey) {
        return tBetween(rootKey, startTimeKey, endTimeKey, "yyyy-MM-dd");
    }


    public SpecificationBuilder<T> tBetween(String rootKey, String startTimeKey, String endTimeKey, String format) {
        //这里用`-`来拼接开始和结束时间参数
        conditions.add(new SpecificationCondition(rootKey, startTimeKey + "-" + endTimeKey, PredicateType.DATETIME_BETWEEN, format));
        return this;
    }

    /**
     * 时间大于判定
     */
    public SpecificationBuilder<T> tThan(String rootKey, String timeKey) {
        return tThan(rootKey, timeKey, "yyyy-MM-dd");
    }

    public SpecificationBuilder<T> tThan(String rootKey, String timeKey, String format) {
        conditions.add(new SpecificationCondition(rootKey, timeKey, PredicateType.DATETIME_THAN, format));
        return this;
    }

    /**
     * 时间小于判定
     */

    public SpecificationBuilder<T> tLess(String rootKey, String timeKey) {
        return tLess(rootKey, timeKey, "yyyy-MM-dd");
    }

    public SpecificationBuilder<T> tLess(String rootKey, String timeKey, String format) {
        conditions.add(new SpecificationCondition(rootKey, timeKey, PredicateType.DATETIME_LESS, format));
        return this;
    }

    /**
     * 时间大于判定
     */
    public SpecificationBuilder<T> tThanEqual(String rootKey, String timeKey) {
        return tThanEqual(rootKey, timeKey, "yyyy-MM-dd");
    }

    public SpecificationBuilder<T> tThanEqual(String rootKey, String timeKey, String format) {
        conditions.add(new SpecificationCondition(rootKey, timeKey, PredicateType.DATETIME_THAN_EQUAL, format));
        return this;
    }

    /**
     * 时间小于等于判定
     */
    public SpecificationBuilder<T> tLessEqual(String rootKey, String timeKey) {
        return tLessEqual(rootKey, timeKey, "yyyy-MM-dd");
    }

    public SpecificationBuilder<T> tLessEqual(String rootKey, String timeKey, String format) {
        conditions.add(new SpecificationCondition(rootKey, timeKey, PredicateType.DATETIME_LESS_EQUAL, format));
        return this;
    }


    /**
     * =null
     */
    public SpecificationBuilder<T> isNull(String rootKey) {
        conditions.add(new SpecificationCondition(rootKey, null, PredicateType.IS_NULL));
        return this;
    }

    /**
     * !=null
     */
    public SpecificationBuilder<T> isNotNull(String rootKey) {
        conditions.add(new SpecificationCondition(rootKey, null, PredicateType.IS_NOT_NULL));
        return this;
    }

    /**
     * trim后=空字符串
     */
    public SpecificationBuilder<T> isBlank(String rootKey) {
        conditions.add(new SpecificationCondition(rootKey, null, PredicateType.IS_BLANK));
        return this;
    }

    public SpecificationBuilder<T> isBlankEqual(String rootKey, String paramKey) {
        conditions.add(new SpecificationCondition(rootKey, paramKey, PredicateType.IS_BLANK_EQUAL));
        return this;
    }


    /**
     * trim后=空字符串
     */
    public SpecificationBuilder<T> isNotBlank(String rootKey) {
        conditions.add(new SpecificationCondition(rootKey, null, PredicateType.IS_NOT_BLANK));
        return this;
    }

    /**
     * trim后=空字符串
     */
    public SpecificationBuilder<T> isNotBlankEqual(String rootKey, String paramKey) {
        conditions.add(new SpecificationCondition(rootKey, paramKey, PredicateType.IS_NOT_BLANK_EQUAL));
        return this;
    }

//---------------------------------------------------动名词状语从句------------------------------------------------------------

    private List<Order> toCriteriaOrder(Root<T> root, CriteriaBuilder criteriaBuilder) {
        List<Order> criteriaOrders = new ArrayList<>();
        for (Map.Entry<String, String> entry : this.orders.entrySet()) {
            String value = entry.getValue();
            String key = entry.getKey();
            if ("asc".equals(value)) {
                criteriaOrders.add(criteriaBuilder.asc(root.get(key)));
            } else if ("desc".equals(value)) {
                criteriaOrders.add(criteriaBuilder.desc(root.get(key)));
            }
        }
        return criteriaOrders;
    }

    private Predicate buildPredicate(SpecificationCondition condition, Root<T> root, CriteriaBuilder criteriaBuilder) {
        Path rootKey = root.get(condition.getRootKey());
        //缺省是 1 = 1
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.literal(1), 1);
        String param = paramObject.getString(condition.getParamKey());
        if (rootKey == null) {
            return predicate;
        }
        switch (condition.getType()) {
            case STRING_EQUAL:
                if (!StringUtils.isEmpty(param)) {
                    predicate = criteriaBuilder.equal(rootKey, param);
                }
                break;
            case STRING_NOT_EQUAL:
                if (!StringUtils.isEmpty(param)) {
                    predicate = criteriaBuilder.notEqual(rootKey, param);
                }
                break;
            case STRING_IN:
                Object list = paramObject.get(condition.getParamKey());
                if (list instanceof Collection) {
                    if (((Collection) list).size() > 0) {
                        predicate = rootKey.in(list);
                    }
                }
                break;
            case STRING_NOT_IN:
                Object list1 = paramObject.get(condition.getParamKey());
                if (list1 instanceof Collection) {
                    if (((Collection) list1).size() > 0) {
                        predicate = criteriaBuilder.not(rootKey.in(list1));

                    }
                }
                break;
            case STRING_CONTAIN:
                if (!StringUtils.isEmpty(param)) {
                    predicate = criteriaBuilder.like(rootKey, "%" + param + "%");
                }
                break;
            case NUMBER_EQUAL:
                if (!StringUtils.isEmpty(param)) {
                    try {
                        Integer type = Integer.parseInt(param);
                        predicate = criteriaBuilder.equal(rootKey, type);
                    } catch (NumberFormatException e) {
                        //无需处理异常
                        e.printStackTrace();
                    }
                }
                break;
            case NUMBER_NOT_EQUAL:
                if (!StringUtils.isEmpty(param)) {
                    try {
                        Integer type = Integer.parseInt(param);
                        predicate = criteriaBuilder.notEqual(rootKey, type);
                    } catch (NumberFormatException e) {
                        //无需处理异常
                        e.printStackTrace();
                    }
                }
                break;
            case NUMBER_THAN:
                if (!StringUtils.isEmpty(param)) {
                    try {
                        Integer type = Integer.parseInt(param);
                        predicate = criteriaBuilder.gt(rootKey, type);
                    } catch (NumberFormatException e) {
                        //无需处理异常
                        e.printStackTrace();
                    }
                }
                break;
            case NUMBER_LESS:
                if (!StringUtils.isEmpty(param)) {
                    try {
                        Integer type = Integer.parseInt(param);
                        predicate = criteriaBuilder.le(rootKey, type);
                    } catch (NumberFormatException e) {
                        //无需处理异常
                        e.printStackTrace();
                    }
                }
                break;
            case DATETIME_THAN:
                if (!StringUtils.isEmpty(param)) {
                    try {
                        SimpleDateFormat timeFormat = new SimpleDateFormat(condition.getTimeFormat());
                        Date time = timeFormat.parse(param);
                        if (time != null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(time);
                            predicate = criteriaBuilder.greaterThan(rootKey, calendar);
                        }
                    } catch (Exception e) {
                        //无需处理异常
                        e.printStackTrace();
                    }
                }
                break;
            case DATETIME_LESS:
                if (!StringUtils.isEmpty(param)) {
                    try {
                        SimpleDateFormat timeFormat = new SimpleDateFormat(condition.getTimeFormat());
                        Date time = timeFormat.parse(param);
                        if (time != null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(time);
                            predicate = criteriaBuilder.lessThan(rootKey, calendar);
                        }
                    } catch (Exception e) {
                        //无需处理异常
                        e.printStackTrace();
                    }
                }
                break;
            case DATETIME_BETWEEN:
                predicate = datetimeBetween(criteriaBuilder, condition, rootKey);
                break;
            case DATETIME_THAN_EQUAL:
                if (!StringUtils.isEmpty(param)) {
                    try {
                        SimpleDateFormat timeFormat = new SimpleDateFormat(condition.getTimeFormat());
                        Date time = timeFormat.parse(param);
                        if (time != null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(time);
                            predicate = criteriaBuilder.greaterThanOrEqualTo(rootKey, calendar);
                        }
                    } catch (Exception e) {
                        //无需处理异常
                        e.printStackTrace();
                    }
                }
                break;
            case DATETIME_LESS_EQUAL:
                if (!StringUtils.isEmpty(param)) {
                    try {
                        SimpleDateFormat timeFormat = new SimpleDateFormat(condition.getTimeFormat());
                        Date time = timeFormat.parse(param);
                        if (time != null) {
                            Calendar calendar = Calendar.getInstance();
                            calendar.setTime(time);
                            predicate = criteriaBuilder.lessThanOrEqualTo(rootKey, calendar);
                        }
                    } catch (Exception e) {
                        //无需处理异常
                        e.printStackTrace();
                    }
                }
                break;
            case IS_BLANK:
                predicate = criteriaBuilder.equal(criteriaBuilder.trim(rootKey), "");
                break;
            case IS_BLANK_EQUAL:
                if (!StringUtils.isEmpty(param)) {
                    predicate = criteriaBuilder.equal(criteriaBuilder.trim(rootKey), "");
                }
                break;
            case IS_NOT_BLANK_EQUAL:
                if (!StringUtils.isEmpty(param)) {
                    predicate = criteriaBuilder.notEqual(criteriaBuilder.trim(rootKey), "");
                }
                break;
            case IS_NOT_BLANK:
                predicate = criteriaBuilder.notEqual(criteriaBuilder.trim(rootKey), "");
                break;
            case IS_NULL:
                predicate = criteriaBuilder.isNull(rootKey);
                break;
            case IS_NOT_NULL:
                predicate = criteriaBuilder.isNotNull(rootKey);
                break;
            default:
                break;
        }
        return predicate;
    }

    /**
     * 传递的起始时间和结束时间，可以都为空，可以单个为空，可以都不为空
     */
    private Predicate datetimeBetween(CriteriaBuilder criteriaBuilder, SpecificationCondition condition, Path rootKey) {
        String[] times = condition.getParamKey().split("-");
        SimpleDateFormat timeFormat = new SimpleDateFormat(condition.getTimeFormat());
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.literal(1), 1);
        Date startTime = null;
        Date endTime = null;
        try {
            startTime = timeFormat.parse(paramObject.getString(times[0]));
        } catch (Exception e) {
            // 这里无需打印错误日志，允许二个time为null
        }
        try {
            if (times.length > 1) {
                endTime = timeFormat.parse(paramObject.getString(times[1]));
            }
        } catch (Exception e) {
            // 这里无需打印错误日志，允许二个time为null
        }
        Calendar sCalendar = Calendar.getInstance();
        Calendar eCalendar = Calendar.getInstance();
        if (startTime == null) {
            if (endTime != null) {
                eCalendar.setTime(endTime);
                predicate = criteriaBuilder.lessThan(rootKey, eCalendar);
            }
        } else {
            sCalendar.setTime(startTime);
            if (endTime == null) {
                predicate = criteriaBuilder.greaterThanOrEqualTo(rootKey, sCalendar);
            } else {
                eCalendar.setTime(endTime);
                predicate = criteriaBuilder.between(rootKey, sCalendar, eCalendar);

            }
        }
        return predicate;
    }

    /**
     * 判断断言是否是1=1
     */
    private boolean isLiteralTrue(Predicate predicate) {
        boolean leftIsNumber1 = false, rightIsNumber1 = false;
        if (predicate instanceof ComparisonPredicate) {
            Expression expression = ((ComparisonPredicate) predicate).getLeftHandOperand();
            if (expression instanceof LiteralExpression) {
                Object literal = ((LiteralExpression) expression).getLiteral();
                if (literal instanceof Integer) {
                    leftIsNumber1 = ((Integer) literal == 1);
                }
            }
            expression = ((ComparisonPredicate) predicate).getRightHandOperand();
            if (expression instanceof LiteralExpression) {
                Object literal = ((LiteralExpression) expression).getLiteral();
                if (literal instanceof Integer) {
                    rightIsNumber1 = ((Integer) literal == 1);
                }
            }
        }
        return leftIsNumber1 && rightIsNumber1;
    }
}
