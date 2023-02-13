package d1.project.container.api.factory;

import d1.project.container.api.base.service.BaseNodeService;
import d1.project.container.api.utils.MyUtils;

public class NodeServiceFactory {
    private final static NodeServiceFactory nodeServiceFactory = new NodeServiceFactory();

    private NodeServiceFactory() {
    }

    public static NodeServiceFactory getInstance() {
        return nodeServiceFactory;
    }

    public BaseNodeService createNodeService(String typeId) throws Exception {
        try {
            return (BaseNodeService) Class.forName(getClass(typeId)).newInstance();
        } catch (Exception e) {
            throw new Exception("未知的节点类型:" + typeId);
        }
    }

    public String getClass(String typeId) {
        return "d1.component." + typeId + "." + MyUtils.upperFirstCase(typeId) + "Service";
    }
}
