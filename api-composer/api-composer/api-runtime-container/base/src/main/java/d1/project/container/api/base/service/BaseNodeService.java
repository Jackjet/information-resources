package d1.project.container.api.base.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.project.container.api.base.Context;
import d1.project.container.api.base.bean.Input;
import d1.project.container.api.base.utils.Utils;

import java.util.List;
import java.util.Map;

public class BaseNodeService implements INodeService {
    /**
     * 节点id
     */
    private String id;

    /**
     * 节点名称
     */
    private String name;

    /**
     * 节点类型（组件类型）
     */
    private String type;

    /**
     * 下一个节点id
     */
    private String next;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNext() {
        return next;
    }

    public void setNext(String next) {
        this.next = next;
    }

    public BaseNodeService getNextNode(Context context) {
        if (context == null) {
            return null;
        }
        return context.getNode(next);
    }

    @Override
    public void init(Map<String, Object> properties) throws Exception {

    }

    @Override
    public Object run(Context context, Input input) throws Exception {
        context.setCurrentNode(this);
        LogService.info(context.getId(), "exec node, info:" + JSON.toJSONString(this) + ",input:" + JSON.toJSONString(input));
        return null;
    }

    /**
     * 处理properties中的变量
     *
     * @param context 上下文
     * @param input   输入值
     * @param source  表达式
     * @return 替换后的值
     * @throws Exception 异常
     */
    protected String processExpressions(Context context, Object input, String source) throws Exception {
        List<String> pathExpressions = Utils.getPathExpressions(source);

        String payload = context.getPayloadJson().toJSONString();
        //copy一份，添加 input节点方便查找 ${input.xxx}
        JSONObject object = JSON.parseObject(payload);
        object.put("input", input);

        for (String pathExpression : pathExpressions) {
            Object value = Utils.getValueByJsonPath(object, pathExpression);
            if (Utils.isEmpty(value)) {
                throw new Exception("表达式错误${" + pathExpression + "},没找到对应的变量值");
            }

            //根据值类型判断，如果不是String类型就需要去掉值旁边的""
            if (value instanceof String) {
                source = source.replace("${" + pathExpression + "}", value.toString());
            } else {
                source = source.replace("\"${" + pathExpression + "}\"", value.toString());
            }
        }

        return source;
    }

    @Override
    public void destroy() throws Exception {

    }
}
