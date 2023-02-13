package d1.component.excel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.component.excel.util.ExcelExportUtil;
import d1.project.container.api.base.Context;
import d1.project.container.api.base.bean.Input;
import d1.project.container.api.base.service.BaseNodeService;
import d1.project.container.api.base.utils.Constants;
import d1.project.container.api.base.utils.Utils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

public class ExcelService extends BaseNodeService {
    private static final Logger logger = LoggerFactory.getLogger(ExcelService.class);

    //excel文件名称
    private String name;

    //数据路径
    private String path;

    @Override
    public void init(Map<String, Object> properties) throws Exception {
        super.init(properties);

        //获取属性
        name = properties.get("name").toString();
        path = properties.get("path").toString();
    }

    @Override
    public Object run(Context context, Input input) throws Exception {
        super.run(context, input);

        JSONObject object = new JSONObject();
        object.put("input", input.getInput());

        Object values = "[]";
        if (!Utils.isEmpty(path)) {
            List<String> pathExpressions = Utils.getPathExpressions(path);
            if (pathExpressions.size() > 0) {
                values = Utils.getValueByJsonPath(object, pathExpressions.get(0));
            }
        }

        if (values instanceof JSONObject) {
            values = JSON.toJSON(((JSONObject) values).values());
        }

        HSSFWorkbook wb = new ExcelExportUtil().exportExcel(JSON.parseArray(values.toString()));
        if (wb == null) {
            throw new Exception("生成Excel失败");
        }

        //文件输出
        File file = new File(getOutDir(Constants.EXCEL), name + ".xls");
        wb.write(new FileOutputStream(file));

        Input nextInput = new Input();
        nextInput.setInput("api/download/excel/" + file.getName());
        return getNextNode(context).run(context, nextInput);
    }


    private File getOutDir(String path) throws Exception {
        File outDir = new File(path);
        if (!outDir.exists()) {
            if (!outDir.mkdirs()) {
                throw new Exception("创建目录失败");
            }
        }
        return outDir;
    }

    @Override
    public void destroy() throws Exception {
        super.destroy();
    }


    ////////////////

}
