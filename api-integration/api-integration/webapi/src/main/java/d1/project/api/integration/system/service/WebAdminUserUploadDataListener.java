package d1.project.api.integration.system.service;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.api.integration.system.model.WebAdminUserExcelImport;
import net.dcrun.component.ehcache.IEhcacheService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-11 17:36
 */
public class WebAdminUserUploadDataListener extends AnalysisEventListener<WebAdminUserExcelImport> {
    private static final Logger logger = LoggerFactory.getLogger(WebAdminUserUploadDataListener.class);
    /**
     * 每隔5条存储数据库，实际使用中可以3000条，然后清理list ，方便内存回收
     */
    private static final int batchCount = 1;
    /**
     * 假设这个是一个DAO，当然有业务逻辑这个也可以是一个service。当然如果不用存储这个对象没用。
     */
    private final IWebAdminUserService webAdminUserService;
    private final HttpServletRequest request;
    private final String uuId;
    private final IEhcacheService ehcacheService;

    List<WebAdminUserExcelImport> list = new ArrayList<>();

    /**
     * 如果使用了spring,请使用这个构造方法。每次创建Listener的时候需要把spring管理的类传进来
     *
     * @param webAdminUserService
     */
    public WebAdminUserUploadDataListener(IEhcacheService ehcacheService, IWebAdminUserService webAdminUserService, HttpServletRequest request, String uuId) {
        this.ehcacheService = ehcacheService;
        this.webAdminUserService = webAdminUserService;
        this.request = request;
        this.uuId = uuId;
    }

    /**
     * 这个每一条数据解析都会来调用
     *
     * @param data    one row value. Is is same as {@link AnalysisContext#readRowHolder()}
     * @param context
     */
    @Override
    public void invoke(WebAdminUserExcelImport data, AnalysisContext context) {
//        logger.info("解析到一条数据:{}", JSON.toJSONString(data));
        list.add(data);
        // 达到BATCH_COUNT了，需要去存储一次数据库，防止数据几万条数据在内存，容易OOM
        if (list.size() >= batchCount) {
            saveData();
            // 存储完成清理 list
            list.clear();
        }
    }

    /**
     * 所有数据解析完成了 都会来调用
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context)  {
        // 这里也要保存数据，确保最后遗留的数据也存储到数据库
        saveData();
//        throw new ExcelAnalysisException();
        logger.info("所有数据解析完成！");
    }


    /**
     * 在转换异常 获取其他异常下会调用本接口。抛出异常则停止读取。如果这里不抛出异常则 继续读取下一行。
     *
     * @param exception
     * @param context
     * @throws Exception
     */
    @Override
    public void onException(Exception exception, AnalysisContext context) throws DoValidException {
        // 如果是某一个单元格的转换异常 能获取到具体行号
        // 如果要获取头的信息 配合invokeHeadMap使用
        throw new DoValidException(exception.getMessage());
    }

    /**
     * 加上存储数据库
     */
    private void saveData() {
        logger.info("{}条数据，开始存储数据库！", list.size());
        try {
            webAdminUserService.insertAll(list, request);
        } catch (DoValidException e) {
            if (ehcacheService.containsKey(uuId)) {
                String string = ehcacheService.getString(uuId);
                JSONObject jsonObject = JSONObject.parseObject(string);
                JSONObject jsonObject1 = JSONObject.parseObject(e.getMessage());
                for (Map.Entry<String, Object> stringObjectEntry : jsonObject.entrySet()) {
                    String string1 = jsonObject1.getString(stringObjectEntry.getKey());
                    if (!StringUtils.isEmpty(string1)) {
                        stringObjectEntry.setValue(stringObjectEntry.getValue() + "," + string1);
                    }

                }
                ehcacheService.setExpireString(uuId, jsonObject.toString(), 20 * 60);

            } else {
                ehcacheService.setExpireString(uuId, e.getMessage(), 20 * 60);
            }
        }
        logger.info("存储数据库成功！");
    }

}
