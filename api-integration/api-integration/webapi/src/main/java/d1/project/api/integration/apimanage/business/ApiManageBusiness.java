package d1.project.api.integration.apimanage.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.api.integration.apimanage.entity.*;
import d1.project.api.integration.apimanage.model.ApiInsertVm;
import d1.project.api.integration.apimanage.model.ApiUpdateVm;
import d1.project.api.integration.apimanage.service.*;
import d1.project.api.integration.apimanage.view.entity.ApiWithSourceApi;
import d1.project.api.integration.apimanage.view.entity.SourceApiList;
import d1.project.api.integration.apimanage.view.service.ApiWithSourceApiService;
import d1.project.api.integration.apimanage.view.service.SourceApiListService;
import d1.project.api.integration.common.Constants;
import d1.project.api.integration.common.model.OperationLog;
import d1.project.api.integration.common.service.IOperationLogService;
import d1.project.api.integration.common.utils.BaseUtils;
import d1.project.api.integration.common.utils.JsonFormatTool;
import d1.project.api.integration.kong.business.KongBusiness;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author baozh
 */
@Service
public class ApiManageBusiness {
    private final ApiWithSourceApiService apiWithSourceApiService;
    private final ApiBaseInfoService apiBaseInfoService;
    private final KongBusiness kongBusiness;
    private final SourceApiListService sourceApiListService;
    private final IOperationLogService iOperationLogService;

    private final KongApiService kongApiService;

    private final MetaService metaService;
    private final SourceApiService sourceApiService;

    private final KongSourceApiService kongSourceApiService;

    private final ApiAuthManageService apiAuthManageService;

    @Value("${kong.base.http}")
    private String kongHttp;
    @Value("${kong.base.https}")
    private String kongHttps;

    public ApiManageBusiness(ApiWithSourceApiService apiWithSourceApiService, ApiBaseInfoService apiBaseInfoService, KongBusiness kongBusiness, SourceApiListService sourceApiListService, IOperationLogService iOperationLogService, KongApiService kongApiService, MetaService metaService, SourceApiService sourceApiService, KongSourceApiService kongSourceApiService, ApiAuthManageService apiAuthManageService) {
        this.apiWithSourceApiService = apiWithSourceApiService;
        this.apiBaseInfoService = apiBaseInfoService;
        this.kongBusiness = kongBusiness;
        this.sourceApiListService = sourceApiListService;
        this.iOperationLogService = iOperationLogService;
        this.kongApiService = kongApiService;
        this.metaService = metaService;
        this.sourceApiService = sourceApiService;
        this.kongSourceApiService = kongSourceApiService;
        this.apiAuthManageService = apiAuthManageService;
    }

    /**
     * API列表
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 异常处理
     */
    public JSONObject findApiList(JSONObject params) throws Exception {
        //查询API
        Page<ApiWithSourceApi> data = apiWithSourceApiService.findAll(params);

        JSONObject object = (JSONObject) JSON.toJSON(data);
        return object;
    }

    public JSONObject findById(String id) throws Exception {
        ApiBaseInfo apiBaseInfo = apiBaseInfoService.findById(id);
        List<Meta> metas = metaService.findAllByAssociationsId(apiBaseInfo.getId());
        SourceApi sourceApi = sourceApiService.findFirstByApiId(id);

        JSONObject data = (JSONObject) JSON.toJSON(apiBaseInfo);
        data.put("metas", metas);
        data.put("sourceApi", sourceApi);

        return data;
    }

    /**
     * 添加API信息 (在KONG注册route信息返回routeId)
     *
     * @param insertVm 添加信息
     * @param request  请求信息
     * @throws Exception 异常处理
     */
    @Transactional(rollbackOn = Exception.class)
    public void insertApiBaseInfo(ApiInsertVm insertVm, HttpServletRequest request) throws Exception {
        ApiBaseInfo apiBaseInfo = new ApiBaseInfo();
        BeanUtils.copyProperties(insertVm, apiBaseInfo);

        String userId = BaseUtils.getUserId(request);

        if (apiBaseInfoService.existsAllByName(apiBaseInfo.getName())) {
            throw new Exception(Constants.DATA_REPEAT);
        }

        checkApiLimitNum(apiBaseInfo);
        apiBaseInfo.setCreateById(userId);
        apiBaseInfo.setCreateTime(Calendar.getInstance());
        apiBaseInfo.setUpdateById(userId);
        apiBaseInfo.setUpdateTime(Calendar.getInstance());
        apiBaseInfo.setId(BaseUtils.generate32Id());
        apiBaseInfo.setMethods(insertVm.getSourceApi().getMethod());

        apiBaseInfo.setStatus(Constants.API_STATUS_TO_BE_RELEASED);
        apiBaseInfoService.saveInfo(apiBaseInfo);

        insertMetas(JSON.parseArray(insertVm.getMetas()), apiBaseInfo.getId());
        insertSourceApi(insertVm.getSourceApi(), apiBaseInfo.getId());
        iOperationLogService.insert(new OperationLog("API管理", "API添加", "API添加", "添加API" + insertVm.getName(), 1), request);
    }

    /**
     * 更新Api信息 （路由信息更新需要更新KONG的路由信息）
     *
     * @param updateVm 更新信息
     * @param request  请求信息
     * @throws Exception 抛出异常
     */

    @Transactional(rollbackOn = Exception.class)
    public void updateApiBaseInfo(ApiUpdateVm updateVm, HttpServletRequest request) throws Exception {
        metaService.deleteByAssociationsId(updateVm.getId());
        insertMetas(JSON.parseArray(updateVm.getMetas()), updateVm.getId());

        sourceApiService.deleteAllByApiId(updateVm.getId());
        insertSourceApi(updateVm.getSourceApi(), updateVm.getId());

        String userId = BaseUtils.getUserId(request);
        String id = updateVm.getId();
        String name = updateVm.getName();
        if (apiBaseInfoService.existsAllByNameAndIdNot(name, id)) {
            throw new Exception(Constants.DATA_REPEAT);
        }
        ApiBaseInfo apiBaseInfo = apiBaseInfoService.findById(id);
        BeanUtils.copyProperties(updateVm, apiBaseInfo);

        apiBaseInfo.setMethods(updateVm.getSourceApi().getMethod());
        checkApiLimitNum(apiBaseInfo);
        apiBaseInfo.setUpdateById(userId);
        apiBaseInfo.setUpdateTime(Calendar.getInstance());

        apiBaseInfo.setStatus(Constants.API_STATUS_TO_BE_RELEASED);
        apiBaseInfoService.saveInfo(apiBaseInfo);
        iOperationLogService.insert(new OperationLog("API管理", "API编辑", "API编辑", "编辑API" + name, 1), request);
    }

    /**
     * 复制API信息
     *
     * @param id      数据ID
     * @param request 请求信息
     * @throws Exception 抛出异常
     */

    @Transactional(rollbackOn = Exception.class)
    public void copyApiInfo(String id, HttpServletRequest request) throws Exception {
        //复制api信息
        String userId = BaseUtils.getUserId(request);
        ApiBaseInfo apiBaseInfo = apiBaseInfoService.findById(id);
        String copy_name = apiBaseInfo.getName() + "_copy_api";

        if (apiBaseInfoService.existsAllByName(copy_name)) {
            throw new DoValidException(copy_name + "已存在");
        }

        ApiBaseInfo info = new ApiBaseInfo();
        BeanUtils.copyProperties(apiBaseInfo, info);
        info.setName(copy_name);
        info.setCreateById(userId);
        info.setCreateTime(Calendar.getInstance());
        info.setUpdateTime(Calendar.getInstance());
        info.setUpdateById(userId);
        info.setId(BaseUtils.generate32Id());

        info.setStatus(Constants.API_STATUS_TO_BE_RELEASED);
        apiBaseInfoService.saveInfo(info);

        //复制标签信息
        List<Meta> metas = metaService.findAllByAssociationsId(apiBaseInfo.getId());

        List<Meta> newMetas = new Vector<>();
        for (Meta meta : metas) {
            Meta newMeta = new Meta();
            BeanUtils.copyProperties(meta, newMeta);
            newMeta.setId(BaseUtils.generate32Id());
            newMeta.setAssociationsId(info.getId());
            newMetas.add(newMeta);
        }

        metaService.saveAll(newMetas);

        SourceApi sourceApi = sourceApiService.findFirstByApiId(id);

        SourceApi newSourceApi = new SourceApi();
        BeanUtils.copyProperties(sourceApi, newSourceApi);
        newSourceApi.setId(BaseUtils.generate32Id());
        newSourceApi.setApiId(info.getId());
        sourceApiService.save(newSourceApi);

        iOperationLogService.insert(new OperationLog("API管理", "API复制", "API复制", "复制API" + apiBaseInfo.getName(), 1), request);
    }

    /**
     * 删除API
     *
     * @param id 数据ID
     */
    @Transactional(rollbackOn = Exception.class)
    public void deleteApi(String id, HttpServletRequest request) throws Exception {
        if (!apiBaseInfoService.existById(id)) {
            throw new DoValidException("当前API信息不存在");
        }

        if (kongApiService.existsByApiId(id)) {
            throw new DoValidException("当前API已发布，请撤回后再删除");
        }
        apiBaseInfoService.deleteById(id);

        //删除标签信息
        metaService.deleteByAssociationsId(id);

        //删除授权关系
        apiAuthManageService.deleteAllByApiId(id);

        iOperationLogService.insert(new OperationLog("API管理", "API删除", "API删除", "删除API" + id, 1), request);
    }

    /**
     * API列表
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 异常处理
     */
    public Page<SourceApiList> sourceApiList(JSONObject params) throws Exception {
        return sourceApiListService.findAll(params);
    }

    //TODO
    @Transactional(rollbackOn = Exception.class)
    public void publish(String id, String container) throws Exception {
        ApiBaseInfo apiBaseInfo = this.apiBaseInfoService.findById(id);
        if (apiBaseInfo == null) {
            throw new DoValidException("Api信息不存在");
        }

        apiBaseInfo.setContainer(container);

        //kong添加upstream,target,service
        SourceApi sourceApi = sourceApiService.findFirstByApiId(id);
        if (sourceApi == null) {
            throw new DoValidException("接口资源信息不存在");
        }

        KongSourceApi kongSourceApi = kongSourceApiService.findByApiId(sourceApi.getId());
        if (kongSourceApi == null) {
            kongSourceApi = new KongSourceApi();
            kongSourceApi.setId(BaseUtils.generate32Id());
            kongSourceApi.setSourceId(sourceApi.getId());
        }

        BeanUtils.copyProperties(sourceApi, kongSourceApi);

        String[] host = sourceApi.getHost().split(",");
        JSONArray array = new JSONArray();
        for (String item : host) {
            JSONObject obj = new JSONObject();
            obj.put("target", item);

            array.add(obj);
        }

        kongSourceApi.setHost(array.toJSONString());
        kongBusiness.saveSourceApi(container, kongSourceApi);

        kongSourceApiService.save(kongSourceApi);

        //kong添加route和插件
        KongApi kongApi = kongApiService.findByApiId(id);
        if (kongApi == null) {
            kongApi = new KongApi();
            kongApi.setId(BaseUtils.generate32Id());
        }

        BeanUtils.copyProperties(apiBaseInfo, kongApi);

        kongApi.setServiceId(kongSourceApi.getServiceId());

        String method = sourceApi.getMethod();
        if (StringUtils.isEmpty(method)) {
            method = "OPTIONS";
        } else if (!method.contains("OPTIONS")) {
            method = method + ",OPTIONS";
        }
        kongApi.setMethods(method);
        kongApi.setApiId(id);
        //保存kong配置
        kongBusiness.saveApi(apiBaseInfo.getContainer(), kongApi);

        kongApiService.save(kongApi);
        //修改发布状态
        apiBaseInfo.setStatus(Constants.API_STATUS_RELEASE);
        apiBaseInfoService.saveInfo(apiBaseInfo);
    }

    @Transactional(rollbackOn = Exception.class)
    public void revocation(String id) throws Exception {
        ApiBaseInfo apiBaseInfo = this.apiBaseInfoService.findById(id);
        if (apiBaseInfo == null) {
            throw new DoValidException("Api信息不存在");
        }

        KongApi kongApi = this.kongApiService.findByApiId(id);
        if (kongApi != null) {
            this.kongBusiness.deleteApi(apiBaseInfo.getContainer(), kongApi);
            this.kongApiService.delete(kongApi);
        }

        //删除接口资源相关数据
        SourceApi sourceApi = sourceApiService.findFirstByApiId(id);
        if (sourceApi != null) {
            KongSourceApi kongSourceApi = this.kongSourceApiService.findByApiId(sourceApi.getId());
            if (kongSourceApi != null) {
                kongBusiness.deleteSourceApi(apiBaseInfo.getContainer(), kongSourceApi);
                kongSourceApiService.delete(kongSourceApi);
            }
        }

        apiBaseInfo.setStatus(Constants.API_STATUS_TO_BE_RELEASED);
        apiBaseInfoService.saveInfo(apiBaseInfo);
    }

    /**
     * 导出API文档
     *
     * @param response 响应头
     * @param ids      集合ID
     * @throws Exception 向上抛出异常
     */
    public void doc(HttpServletResponse response, List<String> ids) throws Exception {
        XWPFDocument doc = new XWPFDocument();

        XWPFParagraph p = doc.createParagraph();
        p.setAlignment(ParagraphAlignment.BOTH);
        XWPFRun r = p.createRun();
        setXWPFRunStyle(r, "000000", true, 18);
        r.setText("1.概述");

        XWPFParagraph p2 = doc.createParagraph();
        p2.setAlignment(ParagraphAlignment.BOTH);
        XWPFRun c2 = p2.createRun();
        c2.setText("1.1 接口使用说明");
        setXWPFRunStyle(c2, "000000", true, 16);

        XWPFParagraph p3 = doc.createParagraph();
        p3.setAlignment(ParagraphAlignment.BOTH);
        XWPFRun c3 = p3.createRun();
        c3.addTab();
        c3.setText("本文档的应用场景是通过API集成平台授权的接口服务。");
        setXWPFRunStyle(c3, "000000", false, 10);

        XWPFParagraph p4 = doc.createParagraph();
        p4.setAlignment(ParagraphAlignment.BOTH);
        XWPFRun c4 = p4.createRun();
        c4.setText("1.2 请求结构");
        setXWPFRunStyle(c4, "000000", true, 16);

        XWPFParagraph p5 = doc.createParagraph();
        p4.setAlignment(ParagraphAlignment.BOTH);
        XWPFRun c5 = p5.createRun();
        c5.addTab();
        c5.setText("1) 服务地址API的服务接入地址格式为：http://xxx.xxx.xxx/", 0);
        c5.addCarriageReturn();
        c5.addTab();
        c5.setText("2) 请求方法支持HTTP GET、POST方法发送请求", 1);
        setXWPFRunStyle(c5, "000000", false, 10);

        XWPFParagraph p6 = doc.createParagraph();
        p6.setAlignment(ParagraphAlignment.BOTH);
        XWPFRun c6 = p6.createRun();
        c6.setText("1.3 返回结果");
        setXWPFRunStyle(c6, "000000", true, 16);

        XWPFParagraph p7 = doc.createParagraph();
        p7.setAlignment(ParagraphAlignment.BOTH);
        XWPFRun c7 = p7.createRun();
        c7.addTab();
        c7.setText("1) 同步返回数据格式示例：");
        setXWPFRunStyle(c6, "000000", false, 10);

        XWPFTable tb1 = createTable(doc);
        XWPFTableRow tr1 = tb1.getRow(0);
        XWPFParagraph tr01 = tr1.getCell(0).addParagraph();
        XWPFRun ctr = tr01.createRun();
        ctr.setText("{");
        ctr.addBreak();
        ctr.setText("   \"code\":1,");
        ctr.addBreak();
        ctr.setText("   \"data\":{},");
        ctr.addBreak();
        ctr.setText("   \"msg\":\"String\"");
        ctr.addBreak();
        ctr.setText("}");

        XWPFParagraph p8 = doc.createParagraph();
        p8.setAlignment(ParagraphAlignment.BOTH);
        XWPFRun c8 = p8.createRun();
        c8.addTab();
        c8.setText("说明:");
        setXWPFRunStyle(c8, "000000", false, 10);

        XWPFTable tb2 = createTable(doc);
        XWPFTableRow tr201 = tb2.getRow(0);
        tr201.getCell(0).setText("参数名");
        tr201.addNewTableCell().setText("说明");
        tr201.addNewTableCell().setText("类型");

        XWPFTableRow tr202 = tb2.createRow();
        tr202.getCell(0).setText("code");
        tr202.getCell(1).setText("表示请求成功并返回正确的业务数据,0接口请求失败返回错误提示信息");
        tr202.getCell(2).setText("Integer");

        XWPFTableRow tr203 = tb2.createRow();
        tr203.getCell(0).setText("data");
        tr203.getCell(1).setText("接口返回的具体响应数据，以下接口文档只定义这个部分");
        tr203.getCell(2).setText("Object");

        XWPFTableRow tr204 = tb2.createRow();
        tr204.getCell(0).setText("msg");
        tr204.getCell(1).setText("接口请求成功或失败的提示信息");
        tr204.getCell(2).setText("String");

        XWPFParagraph p9 = doc.createParagraph();
        p9.setAlignment(ParagraphAlignment.BOTH);
        XWPFRun c9 = p9.createRun();
        c9.setText("2 接口文档");
        setXWPFRunStyle(c9, "000000", true, 16);
        c5.addCarriageReturn();
        insertApiData(doc, ids);

        String fileNameUrl = URLEncoder.encode("myWord.doc", "UTF-8");
        exportDoc(doc, response, fileNameUrl);
    }

    public void exportDoc(XWPFDocument doc, HttpServletResponse response, String fileNameUrl) throws Exception {

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameUrl + ";" + "filename*=utf-8''" + fileNameUrl);
        response.setContentType("application/msword");
        //刷新缓冲
        response.flushBuffer();
        OutputStream outputStream = response.getOutputStream();
        doc.write(outputStream);
        outputStream.flush();
        outputStream.close();
    }

    /**
     * DOC填充数据
     *
     * @param doc doc文档
     * @param ids API ID集合
     * @throws Exception 抛出异常
     */
    public void insertApiData(XWPFDocument doc, List<String> ids) throws Exception {
        List<ApiBaseInfo> apiList = apiBaseInfoService.findAllByIdIn(ids);
        if (apiList.size() > 0) {
            for (ApiBaseInfo apiBaseInfo : apiList) {
                SourceApi sourceApi = sourceApiService.findFirstByApiId(apiBaseInfo.getId());
                int i = apiList.indexOf(apiBaseInfo);
                i = i + 1;
                XWPFParagraph p1 = doc.createParagraph();
                p1.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun c1 = p1.createRun();
                setXWPFRunStyle(c1, "000000", true, 12);
                c1.setText("2." + i + " " + apiBaseInfo.getName());
                c1.addBreak();
                XWPFRun c4 = p1.createRun();
                setXWPFRunStyle(c4, "000000", true, 10);
                c4.setText("路由:" + apiBaseInfo.getRouteInfo());
                c4.addBreak();
                c4.setText("描述:" + apiBaseInfo.getDetail());
                c4.addBreak();
                c4.setText("接口方法:" + sourceApi.getMethod());
                c4.addBreak();
                c4.setText("请求参数数据部分说明:");

                String params = sourceApi.getParams();
                String protocol = sourceApi.getProtocol();
                protocol = protocol.toLowerCase(Locale.ROOT);
                String url = getDocUrl(protocol, params, apiBaseInfo);
                XWPFTable tb = createTable(doc);
                XWPFTableRow tr = tb.getRow(0);
                XWPFParagraph tr01 = tr.getCell(0).addParagraph();
                XWPFRun ctr1 = tr01.createRun();
                ctr1.setText(url);
                String body = sourceApi.getBody();
                if (!StringUtils.isEmpty(body)) {
                    JSONArray bodyParams = JSONArray.parseArray(body);
                    if (bodyParams != null && bodyParams.size() > 0) {
                        XWPFTable tb2 = createTable(doc);
                        XWPFTableRow tr2 = tb2.getRow(0);
                        XWPFParagraph tr02 = tr2.getCell(0).addParagraph();
                        XWPFRun ctr2 = tr02.createRun();
                        formatJson2(bodyParams, ctr2);
                    }
                }
                XWPFParagraph p6 = doc.createParagraph();
                p6.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun c6 = p6.createRun();
                setXWPFRunStyle(c6, "000000", true, 10);
                c6.setText("数据参数说明:");

                XWPFTable tb6 = doc.createTable();
                CTTblWidth tblWidth6 = tb6.getCTTbl().addNewTblPr().addNewTblW();
                tblWidth6.setType(STTblWidth.DXA);
                tblWidth6.setW(BigInteger.valueOf(9072));
                XWPFTableRow tr206 = tb6.getRow(0);
                String[] paramArray = {"说明", "类型", "参数位置"};
                createTitleCell("参数名", paramArray, tr206);
                if (!StringUtils.isEmpty(params)) {
                    JSONArray param = JSONArray.parseArray(params);
                    if (param != null && param.size() > 0) {
                        formatRes(param, tb6);
                    }
                    JSONArray bp = JSONArray.parseArray(body);
                    if (bp != null && bp.size() > 0) {
                        formatRes2(bp, tb6);
                    }
                }

                XWPFParagraph p2 = doc.createParagraph();
                p2.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun c2 = p2.createRun();
                setXWPFRunStyle(c2, "000000", true, 10);
                c2.setText("返回参数数据说明:");
                c2.addBreak();
                c2.setText("返回示例:");
                String response = sourceApi.getResponse();
                if (!StringUtils.isEmpty(response)) {
                    JSONArray responseData = JSONArray.parseArray(response);
                    XWPFTable tb3 = createTable(doc);
                    XWPFTableRow tr3 = tb3.getRow(0);
                    XWPFParagraph tr03 = tr3.getCell(0).addParagraph();
                    XWPFRun ctr3 = tr03.createRun();
                    ctr3.setText("{");
                    ctr3.addBreak();
                    ctr3.addTab();
                    ctr3.setText("code:200,");
                    ctr3.addBreak();
                    ctr3.addTab();
                    ctr3.setText("msg:'',");
                    ctr3.addBreak();
                    ctr3.addTab();
                    ctr3.setText("data:");
                    if (responseData != null && responseData.size() > 0) {
                        formatJson(responseData, ctr3);
                    }
                    ctr3.addBreak();
                    ctr3.setText("}");
                }

                XWPFParagraph p3 = doc.createParagraph();
                p3.setAlignment(ParagraphAlignment.LEFT);
                XWPFRun c3 = p3.createRun();
                setXWPFRunStyle(c3, "000000", true, 10);
                c3.setText("数据参数说明:");

                XWPFTable tb4 = doc.createTable();
                CTTblWidth tblWidth4 = tb4.getCTTbl().addNewTblPr().addNewTblW();
                tblWidth4.setType(STTblWidth.DXA);
                tblWidth4.setW(BigInteger.valueOf(9072));
                XWPFTableRow tr204 = tb4.getRow(0);
                String[] returnArray = {"说明", "类型"};
                createTitleCell("参数名", returnArray, tr204);
                if (!StringUtils.isEmpty(response)) {
                    JSONArray responseData = JSONArray.parseArray(response);
                    if (responseData != null && responseData.size() > 0) {
                        formatRes3(responseData, tb4);
                    }
                }

                String constants = sourceApi.getConstants();
                if (!StringUtils.isEmpty(constants)) {
                    XWPFParagraph p4 = doc.createParagraph();
                    p4.setAlignment(ParagraphAlignment.LEFT);
                    XWPFRun c = p4.createRun();
                    setXWPFRunStyle(c, "000000", true, 10);
                    c.setText("常量参数说明:");

                    XWPFTable tb7 = createTable(doc);
                    XWPFTableRow tr207 = tb7.getRow(0);
                    String[] strArray = {"参数位置", "参数值", "说明"};
                    createTitleCell("参数名", strArray, tr207);
                    JSONArray responseData = JSONArray.parseArray(constants);
                    if (responseData != null && responseData.size() > 0) {
                        formatResConstant(responseData, tb7);
                    }
                }
            }
        }
    }

    public void exportTxt(HttpServletResponse response, List<String> ids, String name) throws IOException {
        String fileNameUrl = URLEncoder.encode("myTxt.txt", "UTF-8");

        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-disposition", "attachment;filename=" + fileNameUrl + ";" + "filename*=utf-8''" + fileNameUrl);
        response.setContentType("text/plain");
        //刷新缓冲
        response.flushBuffer();
        OutputStream outputStream = response.getOutputStream();

        outputStream.write(generateText(name, ids).getBytes(StandardCharsets.UTF_8));

        outputStream.flush();
        outputStream.close();
    }

    //============================================================================
    private void formatJson(JSONArray body, XWPFRun ctr2) {
        ctr2.setText("{");
        ctr2.addBreak();
        for (Object o : body) {
            JSONObject dataBody = JSONObject.parseObject(o.toString());
            String dataName = dataBody.getString("name");
            String defaultValue = dataBody.getString("defaultValue");
            JSONArray jsonArray = dataBody.getJSONArray("children");
            if (jsonArray != null && jsonArray.size() > 0) {
                ctr2.addBreak();
                ctr2.addTab();
                ctr2.addTab();
                ctr2.setText("   " + "'" + dataName + "'" + ":[{");
                formatJson(jsonArray, ctr2);
                ctr2.setText("   }]");
            } else {
                ctr2.addTab();
                ctr2.addTab();
                if (StringUtils.isEmpty(defaultValue)) {
                    ctr2.setText("'" + dataName + "'" + ":'',");
                } else {
                    ctr2.setText("'" + dataName + "'" + ":'" + defaultValue + "',");
                }

            }
            ctr2.addBreak();
        }
        ctr2.addTab();
        ctr2.addTab();
        ctr2.setText("}");
    }

    private void formatJson2(JSONArray body, XWPFRun ctr2) {
        ctr2.setText("{");
        ctr2.addBreak();
        for (Object o : body) {
            JSONObject dataBody = JSONObject.parseObject(o.toString());
            String dataName = dataBody.getString("name");
            String defaultValue = dataBody.getString("defaultValue");
            JSONArray jsonArray = dataBody.getJSONArray("children");
            if (jsonArray != null && jsonArray.size() > 0) {
                ctr2.addBreak();
                ctr2.addTab();
                ctr2.setText("   " + "'" + dataName + "'" + ":[{");
                formatJson(jsonArray, ctr2);
                ctr2.setText("   }]");
            } else {
                ctr2.addTab();
                if (StringUtils.isEmpty(defaultValue)) {
                    ctr2.setText("'" + dataName + "'" + ":'',");
                } else {
                    ctr2.setText("'" + dataName + "'" + ":'" + defaultValue + "',");
                }
            }
            ctr2.addBreak();
        }
        ctr2.setText("}");
    }

    private void formatRes(JSONArray responseData, XWPFTable tb) {
        for (Object res : responseData) {
            JSONObject resData = JSONObject.parseObject(res.toString());
            XWPFTableRow tr = tb.createRow();
            tr.getCell(0).setText(resData.getString("name"));
            tr.getCell(1).setText(resData.getString("description"));
            tr.getCell(3).setText("");
            tr.getCell(3).setText(resData.getString("type"));
            JSONArray child = resData.getJSONArray("children");
            if (child != null && child.size() > 0) {
                formatRes(child, tb);
            }
        }
    }

    private void formatRes2(JSONArray responseData, XWPFTable tb) {
        for (Object res : responseData) {
            JSONObject resData = JSONObject.parseObject(res.toString());
            XWPFTableRow tr = tb.createRow();
            tr.getCell(0).setText(resData.getString("name"));
            tr.getCell(1).setText(resData.getString("description"));
            tr.getCell(2).setText(resData.getString("type"));
            tr.getCell(3).setText("Body");
            JSONArray child = resData.getJSONArray("children");
            if (child != null && child.size() > 0) {
                formatRes2(child, tb);
            }
        }
    }

    private void formatRes3(JSONArray responseData, XWPFTable tb) {
        for (Object res : responseData) {
            JSONObject resData = JSONObject.parseObject(res.toString());
            XWPFTableRow tr = tb.createRow();
            tr.getCell(0).setText(resData.getString("name"));
            tr.getCell(1).setText(resData.getString("description"));
            tr.getCell(2).setText(resData.getString("type"));
            JSONArray child = resData.getJSONArray("children");
            if (child != null && child.size() > 0) {
                formatRes3(child, tb);
            }
        }
    }

    /**
     * 常量用
     *
     * @param responseData 请求数据
     * @param tb           表格
     */
    private void formatResConstant(JSONArray responseData, XWPFTable tb) {
        for (Object res : responseData) {
            JSONObject resData = JSONObject.parseObject(res.toString());
            XWPFTableRow tr = tb.createRow();
            tr.getCell(0).setText(resData.getString("name"));
            tr.getCell(1).setText(resData.getString("type"));
            tr.getCell(2).setText(resData.getString("value"));
            String description = resData.getString("description");
            if (!StringUtils.isEmpty(description)) {
                tr.getCell(3).setText(description);
            } else {
                tr.getCell(3).setText("");
            }
            JSONArray child = resData.getJSONArray("children");
            if (child != null && child.size() > 0) {
                formatResConstant(child, tb);
            }
        }
    }

    private void setXWPFRunStyle(XWPFRun c, String color, boolean bold, int fontSize) {
        c.setColor(color);
        c.setBold(bold);
        c.setFontSize(fontSize);
    }

    private void checkApiLimitNum(ApiBaseInfo info) throws Exception {
        long minute = info.getPerMinute();
        long hour = info.getEveryHour();
        long day = info.getEveryDay();
        if (minute > hour) {
            throw new Exception(Constants.HOURS_ERROR);
        }
        if (hour > day) {
            throw new Exception(Constants.DAY_ERROR);
        }
    }

    private XWPFTable createTable(XWPFDocument doc) {
        XWPFTable tb = doc.createTable();
        CTTblWidth tblWidth = tb.getCTTbl().addNewTblPr().addNewTblW();
        tblWidth.setType(STTblWidth.DXA);
        tblWidth.setW(BigInteger.valueOf(9072));
        return tb;
    }

    private void createTitleCell(String str, String[] list, XWPFTableRow tr207) {
        tr207.getCell(0).setText(str);
        for (String s : list) {
            tr207.addNewTableCell().setText(s);
        }
    }

    private String getDocUrl(String protocol, String params, ApiBaseInfo apiBaseInfo) {
        String url = null;
        if ("http".equals(protocol)) {
            url = protocol + "://" + kongHttp + apiBaseInfo.getRouteInfo();
        }
        if ("https".equals(protocol)) {
            url = protocol + "://" + kongHttps + apiBaseInfo.getRouteInfo();
        }
        if (!StringUtils.isEmpty(params)) {
            JSONArray param = JSONArray.parseArray(params);
            if (param != null && param.size() > 0) {
                StringBuilder sbf = new StringBuilder();
                for (Object o : param) {
                    JSONObject dataParam = JSONObject.parseObject(o.toString());
                    String descType = dataParam.getString("type");
                    if ("Parameter".equals(descType)) {
                        String paramName = dataParam.getString("name");
                        sbf.append(paramName).append("=");
                        String defaultValue = dataParam.getString("defaultValue");
                        if (!StringUtils.isEmpty(defaultValue)) {
                            sbf.append(defaultValue);
                        }
                        sbf.append("&");
                    }
                }
                String str = sbf.toString();
                if (!StringUtils.isEmpty(str)) {
                    url = url + "?" + str.substring(0, str.lastIndexOf("&"));
                }
            }
        }
        return url;
    }

    private void insertMetas(JSONArray metas, String id) {
        List<Meta> metaList = new Vector<>();

        int seq = 0;
        for (Object obj : metas) {
            JSONObject metaItem = (JSONObject) obj;
            Meta meta = new Meta();
            meta.setId(BaseUtils.generate32Id());
            meta.setAssociationsId(id);
            meta.setKey(metaItem.getString("key"));
            meta.setValue(metaItem.getString("value"));

            meta.setSeq(seq);
            metaList.add(meta);

            seq++;
        }

        metaService.saveAll(metaList);
    }

    private void insertSourceApi(SourceApi sourceApi, String apiId) {
        sourceApi.setId(BaseUtils.generate32Id());
        sourceApi.setApiId(apiId);
        sourceApiService.save(sourceApi);
    }

    private String generateText(String name, List<String> ids) {
        List<ApiBaseInfo> apiBaseInfos = apiBaseInfoService.findAllByIdIn(ids);
        List<SourceApi> sourceApis = sourceApiService.findAllByApiId(ids);

        return JsonFormatTool.formatJson(formatJson(name, apiBaseInfos, sourceApis).toJSONString());
    }

    private JSONObject formatJson(String name, List<ApiBaseInfo> apiBaseInfos, List<SourceApi> sourceApis) {
        JSONObject result = new JSONObject();

        JSONObject info = new JSONObject();
        info.put("_postman_id", BaseUtils.generate32Id());
        info.put("name", name);
        info.put("schema", "https://schema.getpostman.com/json/collection/v2.1.0/collection.json");

        JSONArray item = new JSONArray();

        for (ApiBaseInfo apiBaseInfo : apiBaseInfos) {
            for (SourceApi sourceApi : sourceApis) {
                if (apiBaseInfo.getId().equals(sourceApi.getApiId())) {
                    item.add(parseItem(apiBaseInfo.getContainer().split(":")[0], sourceApi));
                    break;
                }
            }
        }

        result.put("info", info);
        result.put("item", item);

        return result;
    }

    private JSONObject parseItem(String ip, SourceApi sourceApi) {
        JSONObject item = new JSONObject();
        item.put("name", sourceApi.getName());
        item.put("request", parseRequest(ip, sourceApi));
        item.put("response", parseResponse(sourceApi));
        return item;
    }

    private JSONObject parseRequest(String ip, SourceApi sourceApi) {
        JSONObject request = new JSONObject();
        request.put("method", sourceApi.getMethod());
        request.put("header", parseHeader(sourceApi));
        request.put("body", parseBody(sourceApi));
        request.put("url", parseUrl(ip, sourceApi));
        return request;
    }

    private JSONArray parseHeader(SourceApi sourceApi) {
        JSONArray header = new JSONArray();

        JSONObject defaultItem = new JSONObject();
        defaultItem.put("key", "Content-Type");
        defaultItem.put("value", sourceApi.getFormatType());
        defaultItem.put("type", "text");

        header.add(defaultItem);

        return header;
    }

    private JSONObject parseBody(SourceApi sourceApi) {
        JSONObject body = new JSONObject();
        body.put("mode", "raw");
        body.put("raw", sourceApi.getBody());

        return body;
    }


    private JSONObject parseUrl(String ip, SourceApi sourceApi) {
        JSONObject url = new JSONObject();
        String raw = sourceApi.getProtocol().toLowerCase() + "://" + ip + sourceApi.getPath();
        url.put("raw", raw);
        url.put("protocol", sourceApi.getProtocol().toLowerCase());
        url.put("host", ip.split("."));
        url.put("path", sourceApi.getPath().split("/"));


        JSONArray params = JSON.parseArray(sourceApi.getParams());
        JSONArray query = new JSONArray();
        for (Object item : params) {
            JSONObject paramItem = (JSONObject) item;

            JSONObject queryItem = new JSONObject();
            queryItem.put("key", paramItem.getString("name"));
            queryItem.put("value", paramItem.getString("defaultValue"));

            query.add(queryItem);
        }

        url.put("query", query);

        return url;
    }

    private JSONArray parseResponse(SourceApi sourceApi) {
        JSONArray response = new JSONArray();

        JSONArray array = JSON.parseArray(sourceApi.getResponse());
        for (Object item : array) {
            JSONObject json = (JSONObject) item;

            JSONObject tmp = new JSONObject();
            tmp.put("key", json.getString("name"));
            tmp.put("value", json.getString("defaultValue"));
            response.add(tmp);
        }

        return response;
    }

}
