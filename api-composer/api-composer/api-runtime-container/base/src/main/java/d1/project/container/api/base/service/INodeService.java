package d1.project.container.api.base.service;

import d1.project.container.api.base.Context;
import d1.project.container.api.base.bean.Input;

import java.util.Map;

public interface INodeService {

    void init(Map<String, Object> properties) throws Exception;

    Object run(Context context, Input input) throws Exception;

    void destroy() throws Exception;
}
