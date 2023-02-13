package d1.framework.permission.impl;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.entity.BaseEntity;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//service的基类，封装一些通用方法curd
public abstract class DoServiceImpBase<T> {

    protected abstract JpaRepository<T, String> getDao();

    public List<T> findAll() {
        List<T> all = getDao().findAll();
        return all;
    }

    @Deprecated
    /**
     * 建议使用新的SpecificationBuilder来替代
     * 模糊或精准条件查询所有数据，前提是dao必须继承JpaSpecificationExecutor接口
     **/
    public Page<T> findAll(String[] condition, Integer page, Integer size, Sort sort) {
        Specification<T> noteSpecification = new Specification<T>() {
            @Override
            public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                for (String aum : condition) {
                    String[] datas = aum.split(",");
                    //模糊查询
                    if (datas[datas.length - 1].equals("like") && !StringUtils.isEmpty(datas[1]) && !"null".equals(datas[1])) {
                        predicates.add(criteriaBuilder.like(root.get(datas[0]), "%" + datas[1] + "%"));
                    }
                    //精确查询
                    if (datas[datas.length - 1].equals("exact") && !StringUtils.isEmpty(datas[1]) && !"null".equals(datas[1])) {
                        predicates.add(criteriaBuilder.equal(root.get(datas[0]), datas[1]));
                    }
                    //以XXX开始的查询
                    if (datas[datas.length - 1].toLowerCase().equals("startswith") && !StringUtils.isEmpty(datas[1]) && !"null".equals(datas[1])) {
                        predicates.add(criteriaBuilder.like(root.get(datas[0]), datas[1] + "%"));
                    }
                    //以XXX结尾的查询
                    if (datas[datas.length - 1].toLowerCase().equals("endswith") && !StringUtils.isEmpty(datas[1]) && !"null".equals(datas[1])) {
                        predicates.add(criteriaBuilder.like(root.get(datas[0]), "%" + datas[1]));
                    }
                    //时间范围查询
                    if (datas[datas.length - 1].equals("range")) {
                        if (StringUtils.isEmpty(datas[1]) || "null".equals(datas[1])) {
                            datas[1] = "1990-01-01";
                        }
                        if (StringUtils.isEmpty(datas[2]) || "null".equals(datas[2])) {
                            datas[2] = "2999-12-30";
                        }
                        String startTime = datas[1] + " 00:00:00";
                        String endTime = datas[2] + " 23:59:59";
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date finalStartTime = null;
                        Date finalEndTime = null;
                        try {
                            finalStartTime = df.parse(startTime);
                            finalEndTime = df.parse(endTime);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        predicates.add(criteriaBuilder.between(root.get(datas[0]), finalStartTime, finalEndTime));
                    }
                }
                Predicate[] pre = new Predicate[predicates.size()];

                return query.where(predicates.toArray(pre)).getRestriction();
            }
        };
        Pageable pageable = initPageable(page, size, sort);
        return ((JpaSpecificationExecutor) getDao()).findAll(noteSpecification, pageable);
    }

    /***
     * 模糊、精准、以XXX开头、以XXX结尾条件查询所有数据，前提是dao必须继承JpaSpecificationExecutor接口
     * @param condition 条件
     * @param page 页数
     * @param size 每页条数
     * @param sort 排序
     * @return 分页列表
     */
    public Page<T> findAll(List<String> condition, Integer page, Integer size, Sort sort) {

        String[] conditionArray = condition.toArray(new String[condition.size()]);

        return this.findAll(conditionArray, page, size, sort);
    }


    /***
     * 查找所有数据，分页,排序
     * @param page 页数
     * @param size 每页条数
     * @param sort 排序
     * @param t 实体类
     * @return 分页列表
     */
    public Page<T> findAllWithPage(Integer page, Integer size, Sort sort, T t) {
        if (page == null && size == null && t == null) {
            if (sort == null)
                return new PageImpl<T>(getDao().findAll());
            else
                return new PageImpl<T>(getDao().findAll(sort));
        }
        Example<T> example = null;
        if (t != null) {
            ExampleMatcher matcher = ExampleMatcher.matching();
            String[] paths = withIgnorePaths();
            for (int i = 0; i < paths.length; i++) {
                matcher = matcher.withIgnorePaths(paths[i]);
            }
            matcher = matcher.withIgnoreNullValues();
            example = Example.of(t, matcher);
        }

        Pageable pageable = null;
        if (page != null || size != null) {
            pageable = initPageable(page, size, sort);
        }
        if (example != null && pageable != null) {
            return getDao().findAll(example, pageable);
        }
        if (example != null && pageable == null) {
            return new PageImpl<T>(getDao().findAll(example));
        }
        if (example == null && pageable != null) {
            return getDao().findAll(pageable);
        }
        return null;
    }

    /**
     * 如果需要忽略一些类型的字段，由子类来覆盖实现
     */
    protected String[] withIgnorePaths() {
        return new String[]{};
    }

    public T findById(String id) {
        if (StringUtils.isEmpty(id)) return null;
        Optional<T> t = getDao().findById(id);
        if (!t.isPresent()) return null;
        return getDao().findById(id).get();
    }

    public void insert(T opt) throws Exception {
        if (opt instanceof BaseEntity) {
            String id = ((BaseEntity) opt).getId();
            if (id == null) {
                ((BaseEntity) opt).setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            } else {
                if (getDao().existsById(id)) throw new Exception("id=" + id + "的资源已存在");
            }
        }
        beforeInsert(opt);
        getDao().save(opt);
    }

    /**
     * 验证插入的参数是否有效
     *
     * @param opt
     * @throws Exception
     */
    protected void beforeInsert(T opt) throws Exception {

    }

    public void update(T opt) throws Exception {
        beforeUpdate(opt);
        if (opt instanceof BaseEntity) {
            String id = ((BaseEntity) opt).getId();
            T t = getDao().findById(id).get();
            List<Field> declaredFields = getParentAndMyFields(t.getClass());
            List<Field> optFields = getParentAndMyFields(opt.getClass());
            for (Field field : declaredFields) {
                field.setAccessible(true);
                Field optField = getFieldByName(optFields, field.getName());
                if (optField == null) continue;
                optField.setAccessible(true);
                if (field.get(t) != null && optField.get(opt) == null) {
                    optField.set(opt, field.get(t));
                }
            }
        }

        getDao().save(opt);
    }

    /**
     * 验证更新的参数是否有效
     *
     * @param opt
     * @throws Exception
     */
    protected void beforeUpdate(T opt) throws Exception {
        if (opt instanceof BaseEntity) {
            String id = ((BaseEntity) opt).getId();
            if (id == null || !getDao().existsById(id)) throw new Exception("id不能为空或找不到这个id对应的资源");
        }
    }


    private Field getFieldByName(List<Field> fields, String name) {
        for (Field field : fields) {
            if (field.getName().equals(name))
                return field;
        }
        return null;
    }

    private List<Field> getParentAndMyFields(Class tempClass) {
        List<Field> fieldList = new ArrayList<>();
        while (tempClass != null && !tempClass.getName().toLowerCase().equals("java.lang.object")) {
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); //得到父类,然后赋给自己
            if (tempClass.getName().toLowerCase().equals("java.lang.object")) break;
        }
        return fieldList;
    }

    public void delete(String id) throws Exception {
        beforeDelete(id);
        getDao().deleteById(id);
    }

    /**
     * 验证删除的参数是否有效
     *
     * @param id id
     * @throws Exception 未找到id=" + id + "的资源
     */
    protected void beforeDelete(String id) throws Exception {
        if (!getDao().existsById(id)) throw new Exception("未找到id=" + id + "的资源");
    }


    public static Pageable initPageable(Integer page, Integer size, Sort sort) {
        Pageable pageable = null;
        page = (page == null || page < 1) ? 0 : page - 1;
        size = (size == null || size < 1) ? 10 : size;
        if (sort != null)
            pageable = PageRequest.of(page, size, sort);
        else
            pageable = PageRequest.of(page, size);
        return pageable;
    }

    public String[] initStartEndTime(String startTime, String endTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startTime = startTime == null || startTime.equals("") ? "1970-01-01 00:00:00" : startTime + " 00:00:00";
        endTime = endTime == null || endTime.equals("") ? sdf.format(new Date()) : endTime + " 23:59:59";
        String[] times = {startTime, endTime};
        return times;
    }

    /**
     * model类转换成实体类
     *
     * @param object      转换类
     * @param entityClass 转换类型
     * @return 转换后的类
     */
    public static <T> T model2Entity(Object object, Class<T> entityClass) {
        String jsonObject = JSONObject.toJSON(object).toString();
        return JSONObject.parseObject(jsonObject, entityClass);
    }

    /**
     * 联合查询返回List<Map<String,Object>> ,把这个值转换成实体类对应的list
     *
     * @param lists 转换列表
     * @param cls   转换类
     * @param <S>
     * @return 转换后的列表
     */
    public static <S> List<S> listMap2Model(List<Map<String, Object>> lists, Class<S> cls) {
        String json = JSONObject.toJSON(lists).toString();
        List<S> noteOut = JSONObject.parseArray(json, cls);
        return noteOut;
    }

}
