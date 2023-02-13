package d1.project.container.api.base;

import com.alibaba.fastjson.JSONObject;
import d1.project.container.api.base.bean.Payload;
import d1.project.container.api.base.service.BaseNodeService;

import java.util.HashMap;
import java.util.Map;

/**
 * 上下文
 */
public class Context {
    private String id;

    private Map<String, BaseNodeService> nodes;

    private BaseNodeService firstNode;

    private BaseNodeService currentNode;

    private Payload payload;

    public void setNodes(Map<String, BaseNodeService> nodes) {
        if (nodes == null) {
            nodes = new HashMap<>();
        }
        this.nodes = nodes;
    }

    public BaseNodeService getCurrentNode() {
        return currentNode;
    }

    public void setCurrentNode(BaseNodeService currentNode) {
        this.currentNode = currentNode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(Payload payload) {
        this.payload = payload;
    }

    public JSONObject getPayloadJson() {
        JSONObject requestObj = new JSONObject();

        requestObj.put("headers", payload.getHeaders());
        requestObj.put("params", payload.getParams());
        requestObj.put("body", payload.getBody());

        JSONObject payloadObj = new JSONObject();
        payloadObj.put("request", requestObj);
        return payloadObj;
    }

    public BaseNodeService getNode(String nodeId) {
        return nodes.get(nodeId);
    }

    public BaseNodeService getFirstNode() {
        return firstNode;
    }

    public void setFirstNode(BaseNodeService firstNode) {
        this.firstNode = firstNode;
    }

    /**
     * 释放所有组件资源
     */
    public void destroy() throws Exception {
        for (Map.Entry<String, BaseNodeService> node : nodes.entrySet()) {
            if (node != null && node.getValue() != null) {
                node.getValue().destroy();
            }
        }
        nodes.clear();
    }

}
