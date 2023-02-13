package d1.component.webservice;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.project.container.api.base.Context;
import d1.project.container.api.base.bean.Input;
import d1.project.container.api.base.service.BaseNodeService;
import d1.project.container.api.base.utils.Utils;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class WebserviceService extends BaseNodeService {
    private static final Logger logger = LoggerFactory.getLogger(WebserviceService.class);

    private JSONObject propertiesObj;

    @Override
    public void init(Map<String, Object> properties) throws Exception {
        super.init(properties);
        propertiesObj = new JSONObject(properties);
    }

    @Override
    public Object run(Context context, Input input) throws Exception {
        super.run(context, input);

        //替换带有表达式的字符串
        String properties = processExpressions(context, input.getInput(), propertiesObj.toJSONString());

        //转换成JSON对象
        propertiesObj = JSON.parseObject(properties);

        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();

        String wsdlUrl = propertiesObj.getString("url");
        String method = propertiesObj.getString("method");
        String params = propertiesObj.getString("params");

        // 获取CXF客户端
        Client client = dcf.createClient(wsdlUrl);
        client.getEndpoint().getEndpointInfo().setAddress(wsdlUrl);
        // 调用Web Service方法
        Object[] result = client.invoke(method, convertToArray(params));

        Input nextInput = new Input();
        nextInput.setInput(JSON.toJSON(result));
        return getNextNode(context).run(context, nextInput);
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
    }

    ////////

    /**
     * JSONArray 字符串转换成数组
     *
     * @param params SONArray 字符串
     * @return 数组
     */
    private Object[] convertToArray(String params) {
        if (Utils.isEmpty(params)) {
            return null;
        }

        JSONArray array = JSON.parseArray(params);
        if (array == null) {
            return null;
        }

        Object[] newArray = new Object[array.size()];
        for (int i = 0; i < array.size(); i++) {
            newArray[i] = array.get(i);
        }
        return newArray;
    }

}
