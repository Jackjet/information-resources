package d1.component.choice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.project.container.api.base.Context;
import d1.project.container.api.base.bean.Input;
import d1.project.container.api.base.factory.ScriptEngineFactory;
import d1.project.container.api.base.service.BaseNodeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.script.ScriptEngine;
import java.util.Map;

public class ChoiceService extends BaseNodeService {
    private static final Logger logger = LoggerFactory.getLogger(ChoiceService.class);

    private ScriptEngine scriptEngine;
    private JSONArray conditions;

    @Override
    public void init(Map<String, Object> properties) throws Exception {
        super.init(properties);
        scriptEngine = ScriptEngineFactory.getInstance().getScriptEngine();
        conditions = JSON.parseArray(JSON.toJSONString(properties.get("condition")));
    }

    @Override
    public Object run(Context context, Input input) throws Exception {
        super.run(context, input);

        BaseNodeService nextNode = null;
        BaseNodeService defaultNode = null;
        for (int i = 0; i < conditions.size(); i++) {
            JSONObject jsonObject = conditions.getJSONObject(i);
            String expression = jsonObject.getString("expression");
            String next = jsonObject.getString("next");
            if ("default".equals(expression)) {
                defaultNode = context.getNode(next);
            } else {
                Object result = scriptEngine.eval(processExpressions(context, input.getInput(), expression));
                if ("true".equals(result.toString())) {
                    nextNode = context.getNode(next);
                    break;
                }
            }
        }

        //如果都不符合就走default
        if (nextNode == null) {
            nextNode = defaultNode;
        }

        //终止执行 TODO
        if (nextNode == null) {
            return null;
        }
        return nextNode.run(context, new Input());
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
    }
}
