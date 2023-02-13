package d1.component.end;

import d1.project.container.api.base.Context;
import d1.project.container.api.base.bean.Input;
import d1.project.container.api.base.service.BaseNodeService;

import java.util.Map;

public class EndService extends BaseNodeService {
    @Override
    public void init(Map<String, Object> properties) throws Exception {
        super.init(properties);
    }

    @Override
    public Object run(Context context, Input input) throws Exception {
        super.run(context, input);
        //最后一个就是结果
        return input.getInput();
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
    }
}
