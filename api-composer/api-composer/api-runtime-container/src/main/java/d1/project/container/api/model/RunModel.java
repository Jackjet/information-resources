package d1.project.container.api.model;

import d1.project.container.api.base.bean.Node;

import javax.validation.constraints.NotBlank;
import java.util.List;

public class RunModel {
    @NotBlank(message = "id不能为空")
    private String id;

    private String name;

    @NotBlank(message = "api路径不能为空")
    private String path;

    private String method;

    private String contentType;

//    private List<Object> params;
//
//    private List<Object> headers;
//
//    private List<Object> body;

    private List<Node> nodes;


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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

//    public List<Object> getParams() {
//        return params;
//    }
//
//    public void setParams(List<Object> params) {
//        this.params = params;
//    }
//
//    public List<Object> getHeaders() {
//        return headers;
//    }
//
//    public void setHeaders(List<Object> headers) {
//        this.headers = headers;
//    }
//
//    public List<Object> getBody() {
//        return body;
//    }
//
//    public void setBody(List<Object> body) {
//        this.body = body;
//    }
}
