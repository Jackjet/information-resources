package d1.component.script;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.project.container.api.base.Context;
import d1.project.container.api.base.bean.Input;
import d1.project.container.api.base.factory.ScriptEngineFactory;
import d1.project.container.api.base.service.BaseNodeService;
import d1.project.container.api.base.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.Invocable;
import javax.script.ScriptEngine;
import java.util.List;
import java.util.Map;

public class ScriptService extends BaseNodeService {
    private static final Logger logger = LoggerFactory.getLogger(ScriptService.class);

    private ScriptEngine scriptEngine;
    private String code;

    @Override
    public void init(Map<String, Object> properties) throws Exception {
        super.init(properties);

        scriptEngine = ScriptEngineFactory.getInstance().getScriptEngine();
        code = properties.get("code").toString();
    }

    @Override
    public Object run(Context context, Input input) throws Exception {
        super.run(context, input);

        if (Utils.isEmpty(code)) {
            throw new Exception("code属性值不能为空");
        }

        //替换方法体中的变量
        String fun = processExpressions(context, input.getInput(), code);

        //加载方法
        scriptEngine.eval(fun);

        //执行方法
        Invocable invocable = (Invocable) scriptEngine;
        Object result = invocable.invokeFunction("run", input.getInput());

        Input nextInput = new Input();
        nextInput.setInput(JSON.toJSON(result));
        return getNextNode(context).run(context, nextInput);
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
    @Override
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

            //解决如果是脚本的话，字符串没有加""会报错
            if (value instanceof String) {
                source = source.replace("${" + pathExpression + "}", "\"" + value.toString() + "\"");
            } else {
                source = source.replace("${" + pathExpression + "}", value.toString());
            }
        }

        return source;
    }


    @Override
    public void destroy() throws Exception {
        super.destroy();
    }
}
