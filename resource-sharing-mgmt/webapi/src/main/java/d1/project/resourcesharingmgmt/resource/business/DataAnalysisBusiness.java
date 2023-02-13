package d1.project.resourcesharingmgmt.resource.business;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.entity.AssetDictEntity;
import d1.project.resourcesharingmgmt.resource.entity.KeywordSearchEntity;
import d1.project.resourcesharingmgmt.resource.entity.ResourceUseInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.AssetDict.AssetDictFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedAnalysisVm;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedByReasonVm;
import d1.project.resourcesharingmgmt.resource.model.DemandedInfo.DemandedByRequestTypeVm;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseInfoByProvOrgIdExport;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseInfoByProvOrgIdVm;
import d1.project.resourcesharingmgmt.resource.model.ResourceUseInfo.ResourceUseInfoFindByProvOrgIdVm;
import d1.project.resourcesharingmgmt.resource.model.Screen.*;
import d1.project.resourcesharingmgmt.resource.service.*;
import d1.project.resourcesharingmgmt.system.entity.OrganizationEntity;
import d1.project.resourcesharingmgmt.system.entity.SystemSettingsEntity;
import d1.project.resourcesharingmgmt.system.service.OrganizationService;
import d1.project.resourcesharingmgmt.system.service.SystemSettingsService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 首页
 *
 * @author maoyuying
 */
@Service
public class DataAnalysisBusiness {
    @Value("${frontdatabase.url}")
    private String frontUrl;

    @Value("${frontdatabase.username}")
    private String frontUserName;

    @Value("${frontdatabase.password}")
    private String frontPass;

    private final JdbcTemplate jdbcTemplate;
    private final OrganizationService organizationService;
    private final ResourceUseInfoService resourceUseInfoService;
    private final DemandedInfoService demandedInfoService;
    private final ArchBusiUviewExService archBusiUviewExService;
    private final AssetDictService assetDictService;
    private final FileDownLogService fileDownLogService;
    private final ApiVisitLogService apiVisitLogService;
    private final KeywordSearchService keywordSearchService;
    private final DataPushLogService dataPushLogService;
    private final AssetFileExService assetFileExService;
    private final AssetApiExService assetApiExService;
    private final AssetDataExService assetDataExService;
    private final SystemSettingsService systemSettingsService;

    public DataAnalysisBusiness(JdbcTemplate jdbcTemplate, OrganizationService organizationService, ResourceUseInfoService resourceUseInfoService,
                                DemandedInfoService demandedInfoService, ArchBusiUviewExService archBusiUviewExService,
                                AssetDictService assetDictService, FileDownLogService fileDownLogService, ApiVisitLogService apiVisitLogService,
                                KeywordSearchService keywordSearchService, DataPushLogService dataPushLogService, AssetFileExService assetFileExService,
                                AssetApiExService assetApiExService, AssetDataExService assetDataExService, SystemSettingsService systemSettingsService) {
        this.jdbcTemplate = jdbcTemplate;
        this.organizationService = organizationService;
        this.resourceUseInfoService = resourceUseInfoService;
        this.demandedInfoService = demandedInfoService;
        this.archBusiUviewExService = archBusiUviewExService;
        this.assetDictService = assetDictService;
        this.fileDownLogService = fileDownLogService;
        this.apiVisitLogService = apiVisitLogService;
        this.keywordSearchService = keywordSearchService;
        this.dataPushLogService = dataPushLogService;
        this.assetFileExService = assetFileExService;
        this.assetApiExService = assetApiExService;
        this.assetDataExService = assetDataExService;
        this.systemSettingsService = systemSettingsService;
    }

    /**
     * 需求统计
     *
     * @return
     */
    public Map<String, Object> getDemandInfoDataAnalysis(HttpServletRequest request) throws Exception {
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        String id = user.getString("id");
        String organizationId = user.getString("organizationId");
        String sql = "SELECT count(1) as totals, " +
                "count(case when status='0' then status end) as notAuditCount, " +
                "count(case when status!='0' then status end) as auditCount " +
                "FROM d1_demanded_info";
        if (!"admin".equals(id)) {
            sql += " where accept_dept_id = '" + organizationId + "'";
        }
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        return map;
    }

    /**
     * 资源使用统计
     *
     * @return
     */
    public Map<String, Object> getResourceUseDataAnalysis(HttpServletRequest request) throws Exception {
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        String id = user.getString("id");
        String organizationId = user.getString("organizationId");
        String sql = "SELECT count(case when status='0' then status end) as notAuditCount, " +
                "count(case when status!='0' then status end) as auditCount " +
                "from d1_resource_use_info";
        if (!"admin".equals(id)) {
            sql += " where prov_org_id = '" + organizationId + "'";
        }
        Map<String, Object> map = jdbcTemplate.queryForMap(sql);
        return map;
    }

    /**
     * 按照时间统计资源使用
     *
     * @param typ 1:近一个月  2：近半年  3：年份
     * @return
     */
    public List<Map<String, Object>> getResourceUseInfoByTime(String typ, HttpServletRequest request) throws Exception {
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        String id = user.getString("id");
        String organizationId = user.getString("organizationId");

        Date edate = new Date();
        Calendar cal = Calendar.getInstance();

        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("SELECT * from (SELECT ");
        // 1 :天 2：月 3：年份
        if ("1".equals(typ)) {
            stringBuffer.append("date(create_time) as date,");
        }
        if ("2".equals(typ)) {
            stringBuffer.append("extract(month from create_time) as month,");
        }
        if (!"1".equals(typ) && !"2".equals(typ)) {
            stringBuffer.append("extract(year from create_time) as year,extract(month from create_time)  as month,");
        }
        stringBuffer.append("count(case when status='0' then status end) as notAuditCount,");
        stringBuffer.append("count(case when status='2' then status end) as auditCount ");
        stringBuffer.append("FROM d1_resource_use_info ");
        stringBuffer.append("WHERE 1=1 ");

        if (!"admin".equals(id)) {
            stringBuffer.append(" and prov_org_id = '" + organizationId + "'");
        }

        if ("1".equals(typ)) {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            cal.setTime(edate);
            cal.add(Calendar.DATE, -30);// 24小时制
            Date sdate = cal.getTime();

            Calendar eCal = Calendar.getInstance();
            eCal.setTime(edate);
            eCal.add(Calendar.DATE, 1);
            edate = eCal.getTime();
            stringBuffer.append("and date(create_time) ");
            stringBuffer.append("BETWEEN '" + df.format(sdate) + "' and '" + df.format(edate) + "'");
            stringBuffer.append("GROUP BY date(create_time) ");
            stringBuffer.append("order by date(create_time) ");
        }
        if ("2".equals(typ)) {
            cal.setTime(edate);
            cal.add(Calendar.MONTH, -6);// 24小时制
            Date sdate = cal.getTime();

            Calendar eCal = Calendar.getInstance();
            eCal.setTime(edate);
            eCal.add(Calendar.MONTH, 1);
            edate = eCal.getTime();
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM");
            stringBuffer.append("and create_time ");
            stringBuffer.append("BETWEEN to_date('" + df1.format(sdate) + "', 'yyyy-MM')  and to_date('" + df1.format(edate) + "', 'yyyy-MM') ");
            stringBuffer.append("GROUP BY extract(month from create_time) ");
            stringBuffer.append("order by extract(month from create_time) ");
        }
        if (!"1".equals(typ) && !"2".equals(typ)) {
            stringBuffer.append("and extract(year from create_time)='" + typ + "' ");
            stringBuffer.append("GROUP BY extract(year from create_time),extract(month from create_time) ");
            stringBuffer.append("order by extract(month from create_time) ");
        }
        stringBuffer.append(" ) as a where a.notAuditCount<>0 or a.auditCount<>0 ");

        List<Map<String, Object>> maps = jdbcTemplate.queryForList(stringBuffer.toString());
        return maps;
    }

    /**
     * 资源统计
     *
     * @return
     */
    public Map<String, Object> getArchBusiUviewExDataAnalysis(HttpServletRequest request) throws Exception {
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        String id = user.getString("id");
        String organizationId = user.getString("organizationId");
        String sql = "select count(1) as totalUview,count(case when hook_status='1' or data_hook_status='1' or file_hook_status='1' then hook_status end) as hookCount from d1_arch_busi_uview_ex";
        if (!"admin".equals(id)) {
            sql += " where prov_org_id = '" + organizationId + "'";
        }
        return jdbcTemplate.queryForMap(sql);
    }

    /**
     * 按照部门统计挂接资源目录
     *
     * @return
     */
    public List<Map<String, Object>> getArchBusiUviewExByProvOrgId() {
        String sql = "SELECT a.prov_org_id,b.name," +
                "count(case when a.hook_status='1' or a.file_hook_status='1' or a.data_hook_status='1' then 1 else 0 end) as hookCount," +
                "concat(round(count(case when a.hook_status='1' or a.file_hook_status='1' or a.data_hook_status='1' then 1 else 0 end)::numeric/(select count(1) as totalHookStatus from d1_arch_busi_uview_ex where hook_status='1' or file_hook_status='1' or data_hook_status='1')*100),'%') as hookPercent " +
                "FROM d1_arch_busi_uview_ex a " +
                "LEFT JOIN d1_organization b on a.prov_org_id=b.id " +
                "WHERE a.hook_status='1' or a.file_hook_status='1' or a.data_hook_status='1' " +
                "GROUP BY a.prov_org_id,b.name ORDER BY hookCount desc LIMIT 10";
        return jdbcTemplate.queryForList(sql);
    }

    public Page<ResourceUseInfoByProvOrgIdVm> findResourceUseInfoByProvOrgId(ResourceUseInfoFindByProvOrgIdVm model) throws Exception {
        JSONObject orgParam = new JSONObject();
        orgParam.put("id", model.getCreateDeptId());
        orgParam.put("page", model.getPage());
        orgParam.put("size", model.getSize());
        orgParam.put("level", 1);
        Page<OrganizationEntity> orgPage = organizationService.findAll(orgParam);
        List<ResourceUseInfoByProvOrgIdVm> list = new ArrayList<>();
        for (OrganizationEntity org : orgPage.getContent()) {
            JSONObject resourceUseParam = new JSONObject();
            if (!org.getId().equals("headquarters")) {
                resourceUseParam.put("createDeptId", org.getId());
            }
            resourceUseParam.put("startTime", model.getStartTime());
            resourceUseParam.put("endTime", model.getEndTime());
            resourceUseParam.put("status", "2");
            List<ResourceUseInfoEntity> resourceUseInfoList = resourceUseInfoService.findAllList(resourceUseParam);

            ResourceUseInfoByProvOrgIdVm orgVm = new ResourceUseInfoByProvOrgIdVm();
            orgVm.setCreateDeptId(org.getId());
            orgVm.setCreateDeptName(org.getName());
            orgVm.setArchNum(resourceUseInfoList.stream().filter(BaseUtils.distinctByKey(ResourceUseInfoEntity::getUviewId)).count());
            orgVm.setApiNum(resourceUseInfoList.stream().filter(i -> i.getType().equals("1")).collect(Collectors.toList()).size());
            orgVm.setFileNum(resourceUseInfoList.stream().filter(i -> i.getType().equals("2")).collect(Collectors.toList()).size());
            orgVm.setDataNum(resourceUseInfoList.stream().filter(i -> i.getType().equals("3")).collect(Collectors.toList()).size());

            list.add(orgVm);
        }

        return new PageImpl<>(list, orgPage.getPageable(), orgPage.getTotalElements());
    }

    public List<ResourceUseInfoByProvOrgIdExport> exportResourceUseInfoByProvOrgId(ResourceUseInfoFindByProvOrgIdVm model) throws Exception {
        JSONObject orgParam = new JSONObject();
        orgParam.put("level", 1);
        List<OrganizationEntity> orgPage = organizationService.findAllList(orgParam);
        List<ResourceUseInfoByProvOrgIdExport> list = new ArrayList<>();
        for (OrganizationEntity org : orgPage) {
            JSONObject resourceUseParam = new JSONObject();
            resourceUseParam.put("createDeptId", org.getId());
            resourceUseParam.put("startTime", model.getStartTime());
            resourceUseParam.put("endTime", model.getEndTime());
            resourceUseParam.put("status", "2");
            List<ResourceUseInfoEntity> resourceUseInfoList = resourceUseInfoService.findAllList(resourceUseParam);

            ResourceUseInfoByProvOrgIdExport orgVm = new ResourceUseInfoByProvOrgIdExport();
            orgVm.setCreateDeptName(org.getName());
            orgVm.setArchNum(resourceUseInfoList.stream().filter(BaseUtils.distinctByKey(ResourceUseInfoEntity::getUviewId)).count());
            orgVm.setApiNum(resourceUseInfoList.stream().filter(i -> i.getType().equals("1")).collect(Collectors.toList()).size());
            orgVm.setFileNum(resourceUseInfoList.stream().filter(i -> i.getType().equals("2")).collect(Collectors.toList()).size());
            orgVm.setDataNum(resourceUseInfoList.stream().filter(i -> i.getType().equals("3")).collect(Collectors.toList()).size());

            list.add(orgVm);
        }

        return list;
    }

    /**
     * 返回excel
     */
    public void easyExcelWrite(HttpServletResponse response, List<ResourceUseInfoByProvOrgIdExport> allList) throws IOException {
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("申请方统计", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ResourceUseInfoByProvOrgIdExport.class).sheet("申请方统计").doWrite(allList);
    }

    /**
     * 需求汇总统计数量
     */
    public DemandedAnalysisVm getDemandCountAnalysis(HttpServletRequest request) {
        DemandedAnalysisVm vm = new DemandedAnalysisVm();
        vm.setOrg(demandedInfoService.countByOrgNumDistinct());
        vm.setNum(demandedInfoService.count());
        vm.setProcessed(demandedInfoService.countByStatus("1"));
        vm.setReject(demandedInfoService.countByStatus("2"));

        return vm;
    }

    /**
     * 需求汇总需求缘由
     */
    public DemandedByReasonVm getDemandByReason(HttpServletRequest request) {
        DemandedByReasonVm vm = new DemandedByReasonVm();
        vm.setRelated(demandedInfoService.countByReason("政策相关"));
        vm.setUnrelated(demandedInfoService.countByReason("非政策相关"));

        return vm;
    }

    /**
     * 需求汇总需求类型
     */
    public DemandedByRequestTypeVm getDemandByRequestType(HttpServletRequest request) {
        DemandedByRequestTypeVm vm = new DemandedByRequestTypeVm();
        vm.setArchUpdate(demandedInfoService.countByRequestType("0"));
        vm.setArchAdd(demandedInfoService.countByRequestType("1"));
        vm.setDataUpdate(demandedInfoService.countByRequestType("2"));
        vm.setDataAdd(demandedInfoService.countByRequestType("3"));
        return vm;
    }

    /**
     * 需求汇总通过率
     */
    public String getDemandByPassRate(HttpServletRequest request) {
        long count = demandedInfoService.count();
        long pass = demandedInfoService.countByStatus("1");
        if (pass == 0) {
            return "0%";
        }
        return BaseUtils.getPercent(pass, count);
    }

    /**
     * 大屏目录统计
     *
     * @return
     * @throws Exception
     */
    public ArchCountVm getArchCount() throws Exception {
        ArchCountVm vm = new ArchCountVm();
        vm.setOrg(organizationService.countByParentId("headquarters"));
        vm.setUview(archBusiUviewExService.count());
        vm.setUviewForData(archBusiUviewExService.countByHookStatusOrFileHookStatusOrDataHookStatus("1", "1", "1"));
        return vm;
    }

    /**
     * 大屏数据类型分布
     *
     * @return
     * @throws Exception
     */
    public List<ArchMediaFmtCountVm> getArchMediaFmtCount() throws Exception {
        AssetDictFindAllVm findVm = new AssetDictFindAllVm();
        findVm.setType("format_type");
        List<AssetDictEntity> list = assetDictService.findAll(findVm);
        List<ArchMediaFmtCountVm> vms = new ArrayList<>();
        for (AssetDictEntity entity : list) {
            ArchMediaFmtCountVm vm = new ArchMediaFmtCountVm();
            vm.setName(entity.getName());
            vm.setNum(archBusiUviewExService.countByMediaFmt(entity.getValue()));
            vms.add(vm);
        }
        return vms;
    }

    /**
     * 大屏更新周期类别占比
     *
     * @return
     * @throws Exception
     */
    public List<ArchUpdateCycCountVm> getArchUpdateCycCount() throws Exception {
        long archCount = archBusiUviewExService.count();

        AssetDictFindAllVm findVm = new AssetDictFindAllVm();
        findVm.setType("update_cycle");
        List<AssetDictEntity> list = assetDictService.findAll(findVm);
        List<ArchUpdateCycCountVm> vms = new ArrayList<>();
        for (AssetDictEntity entity : list) {
            ArchUpdateCycCountVm vm = new ArchUpdateCycCountVm();
            vm.setName(entity.getName());
            if (archCount == 0) {
                vm.setNum("0%");
            } else {
                vm.setNum(BaseUtils.getPercent(archBusiUviewExService.countByUpdateCyc(entity.getValue()), archCount));
            }
            vms.add(vm);
        }
        return vms;
    }

    /**
     * 数据推送和文件下载和API调用次数
     *
     * @return
     * @throws Exception
     */
    public FileAndApiUseCountVm getFileAndApiUseCount() throws Exception {
        FileAndApiUseCountVm vm = new FileAndApiUseCountVm();
        vm.setDataPushCount(dataPushLogService.countSum());
        vm.setFileDownCount(fileDownLogService.count());
        vm.setApiUseCount(apiVisitLogService.count());
        return vm;
    }

    /**
     * 当月数据推送量和服务调用次数（文件和API）每天
     *
     * @return
     * @throws Exception
     */
    public List<FileAndApiUseCountByMonthVm> getFileAndApiUseCountByMonth() throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd");
        List<FileAndApiUseCountByMonthVm> vms = new ArrayList<>();

        Calendar calendar = Calendar.getInstance();
        String month = sdf.format(calendar.getTime());
        String sDay = sdf1.format(calendar.getTime());
        int day = Integer.parseInt(sDay);
        for (int i = 1; i <= day; i++) {
            long dataPushCount = dataPushLogService.countByDay(month + "-" + String.format("%02d", i));
            long fileUseCount = fileDownLogService.countByDay(month + "-" + String.format("%02d", i));
            long apiUseCount = apiVisitLogService.countByDay(month + "-" + String.format("%02d", i));

            FileAndApiUseCountByMonthVm vm = new FileAndApiUseCountByMonthVm();
            vm.setDay(i);
            vm.setUseCount(dataPushCount + fileUseCount + apiUseCount);
            vms.add(vm);
        }

        return vms;
    }

    /**
     * 部门使用top10
     *
     * @return
     * @throws Exception
     */
    public List<OrgUseCount> getOrgUseTop() throws Exception {
        List<OrgUseCount> vms = new ArrayList<>();
        List<OrganizationEntity> orgs = organizationService.findByLevel(1);

        for (OrganizationEntity org : orgs) {
            long dataPushCount = dataPushLogService.countUseByOrgId(org.getId());
            long fileUseCount = fileDownLogService.countUseByOrgId(org.getId());
            long apiUseCount = apiVisitLogService.countUseByOrgId(org.getId());

            OrgUseCount vm = new OrgUseCount();
            vm.setOrgName(org.getAlias());
            vm.setCount(dataPushCount + fileUseCount + apiUseCount);
            vms.add(vm);
        }

        List<OrgUseCount> vmsSort = vms.stream().sorted(Comparator.comparing(OrgUseCount::getCount).reversed()).collect(Collectors.toList());

        return vmsSort.stream().limit(10).collect(Collectors.toList());
    }

    /**
     * 数据提供部门top10
     *
     * @return
     * @throws Exception
     */
    public OrgGiveOrDemandedTop getOrgGiveTop() throws Exception {
        OrgGiveOrDemandedTop top = new OrgGiveOrDemandedTop();
        List<OrgUseCount> vms = new ArrayList<>();
        List<OrganizationEntity> orgs = organizationService.findByLevel(1);

        for (OrganizationEntity org : orgs) {
            long dataPushCount = dataPushLogService.countGiveByOrgId(org.getId());
            long fileUseCount = assetFileExService.countByOrgId(org.getId());
            long apiUseCount = assetApiExService.countByOrgId(org.getId());

            OrgUseCount vm = new OrgUseCount();
            vm.setOrgName(org.getAlias());
            vm.setCount(dataPushCount + fileUseCount + apiUseCount);
            vms.add(vm);
        }

        List<OrgUseCount> vmsSort = vms.stream().sorted(Comparator.comparing(OrgUseCount::getCount).reversed()).collect(Collectors.toList());

        top.setCount(orgs.size());
        top.setTop(vmsSort.stream().limit(10).collect(Collectors.toList()));
        return top;
    }

    /**
     * 部门需求top10
     *
     * @return
     * @throws Exception
     */
    public OrgGiveOrDemandedTop getOrgDemandedTop() throws Exception {
        OrgGiveOrDemandedTop top = new OrgGiveOrDemandedTop();
        List<OrgUseCount> vms = new ArrayList<>();
        List<OrganizationEntity> orgs = organizationService.findByLevel(1);

        for (OrganizationEntity org : orgs) {
            long demandedCount = demandedInfoService.countByCreateDeptId(String.valueOf(org.getId()));

            OrgUseCount vm = new OrgUseCount();
            vm.setOrgName(org.getAlias());
            vm.setCount(demandedCount);
            vms.add(vm);
        }

        List<OrgUseCount> vmsSort = vms.stream().sorted(Comparator.comparing(OrgUseCount::getCount).reversed()).collect(Collectors.toList());

        top.setCount(vmsSort.stream().filter(t -> t.getCount() > 0).count());
        top.setTop(vmsSort.stream().limit(10).collect(Collectors.toList()));
        return top;
    }

    /**
     * 部门积极度TOP10
     *
     * @return
     * @throws Exception
     */
    public List<OrgApplyCount> getOrgApplyTop() throws Exception {
        List<OrgApplyCount> vms = new ArrayList<>();
        List<OrganizationEntity> orgs = organizationService.findByLevel(1);

        for (OrganizationEntity org : orgs) {
            long init = resourceUseInfoService.countByCreateDeptIdAndStatus(String.valueOf(org.getId()), "0");
            long first = resourceUseInfoService.countByCreateDeptIdAndStatus(String.valueOf(org.getId()), "1");
            long approved = resourceUseInfoService.countByCreateDeptIdAndStatus(String.valueOf(org.getId()), "2");
            long reject = resourceUseInfoService.countByCreateDeptIdAndStatus(String.valueOf(org.getId()), "3");
            long fail = resourceUseInfoService.countByCreateDeptIdAndStatus(String.valueOf(org.getId()), "4");

            OrgApplyCount vm = new OrgApplyCount();
            vm.setOrgName(org.getAlias());
            vm.setCount(init + first + approved + reject + fail);
            vm.setInit(init);
            vm.setFirst(first);
            vm.setApproved(approved);
            vm.setReject(reject);
            vm.setFail(fail);
            vms.add(vm);
        }

        List<OrgApplyCount> vmsSort = vms.stream().sorted(Comparator.comparing(OrgApplyCount::getCount).reversed()).collect(Collectors.toList());

        return vmsSort.stream().limit(10).collect(Collectors.toList());
    }

    public List<SupportBusinessCountVm> getSupportBusinessTList() {
        return resourceUseInfoService.findSupportBusinessCount();
    }

    public List<KeywordSearchEntity> getKeywordSearchTList() throws Exception {
        return keywordSearchService.findTopList();
    }

    /**
     * 数据归集最多部门TOP10
     *
     * @return
     * @throws Exception
     */
    public List<OrgDataUseCount> getDataCountTList() throws Exception {
        List<OrgDataUseCount> vms = new ArrayList<>();
        List<OrganizationEntity> orgs = organizationService.findByLevel(1);

        for (OrganizationEntity org : orgs) {
            long dataExCount = assetDataExService.countByOrgId(org.getId());
            long fileExCount = assetFileExService.countByOrgId(org.getId());
            long apiExCount = assetApiExService.countByOrgId(org.getId());

            OrgDataUseCount vm = new OrgDataUseCount();
            vm.setOrgName(org.getAlias());
            vm.setCount(dataExCount + fileExCount + apiExCount);
            vm.setApi(apiExCount);
            vm.setData(dataExCount);
            vm.setFile(fileExCount);
            vms.add(vm);
        }

        List<OrgDataUseCount> vmsSort = vms.stream().sorted(Comparator.comparing(OrgDataUseCount::getCount).reversed()).collect(Collectors.toList());

        return vmsSort.stream().limit(10).collect(Collectors.toList());
    }

    /**
     * 资源目录数与数据提供量提供最完整的TOP10部门
     *
     * @return
     * @throws Exception
     */
    public List<OrgArchAndUseCount> getArchAndDataCountTList() throws Exception {
        List<OrgArchAndUseCount> vms = new ArrayList<>();
        List<OrganizationEntity> orgs = organizationService.findByLevel(1);

        for (OrganizationEntity org : orgs) {
            long count = archBusiUviewExService.countByOrgId(org.getId());
            long exCount = archBusiUviewExService.countExByOrgId(org.getId());

            OrgArchAndUseCount vm = new OrgArchAndUseCount();
            vm.setOrgName(org.getAlias());
            vm.setCount(count);
            vm.setData(exCount);
            vm.setPercent(BaseUtils.getPercentFloat(exCount, count));
            vms.add(vm);
        }

        List<OrgArchAndUseCount> vmsSort = vms.stream().sorted(Comparator.comparing(OrgArchAndUseCount::getPercent, Comparator.reverseOrder()).thenComparing(OrgArchAndUseCount::getCount, Comparator.reverseOrder())).collect(Collectors.toList());

        return vmsSort.stream().limit(10).collect(Collectors.toList());
    }

    /**
     * 前置机动态交换图
     *
     * @return
     * @throws Exception
     */
    public OrgAndCenterCount getArchAndCenterCount() throws Exception {
        OrgAndCenterCount vm = new OrgAndCenterCount();
        List<OrganizationEntity> orgs = organizationService.findByLevel(1);
        List<OrgUseCount> orgUseCountList = archBusiUviewExService.countOrgAndCenterCount();
        Optional<SystemSettingsEntity> entityOptional = systemSettingsService.findByType("frontDataBaseCount");
        if (entityOptional.isPresent()) {
            vm.setDataBase(Long.parseLong(entityOptional.get().getValue()));
        } else {
            vm.setDataBase(0L);
        }
        vm.setApi(assetApiExService.count());
        vm.setFile(assetFileExService.count());
        vm.setArch(orgUseCountList.stream().mapToLong(OrgUseCount::getCount).sum());
        vm.setOrg(orgs.size());
        vm.setOrgs(orgs.stream().map(OrganizationEntity::getAlias).collect(Collectors.toList()));
        return vm;
    }

    public void syncFrontDataBaseCount() throws Exception {
        Optional<SystemSettingsEntity> entityOptional = systemSettingsService.findByType("frontDataBaseCount");
        SystemSettingsEntity entity = new SystemSettingsEntity();
        if (entityOptional.isPresent()) {
            entity = entityOptional.get();
        } else {
            entity.setId(BaseUtils.generate32Id());
            entity.setType("frontDataBaseCount");
            entity.setCreateTime(Calendar.getInstance());
            entity.setUpdateTime(Calendar.getInstance());
        }

        Connection connection = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection(frontUrl, frontUserName, frontPass);
            Statement statement = connection.createStatement();
            String sql = "select sum(table_rows) from information_schema.tables";
            ResultSet resultSet = statement.executeQuery(sql);
            if (resultSet.next()) {
                long count = resultSet.getLong(1);
                entity.setValue(String.valueOf(count));
            }
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        systemSettingsService.save(entity);
    }
}
