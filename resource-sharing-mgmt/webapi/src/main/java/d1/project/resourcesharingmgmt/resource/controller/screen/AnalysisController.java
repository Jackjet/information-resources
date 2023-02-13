package d1.project.resourcesharingmgmt.resource.controller.screen;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.DataAnalysisBusiness;
import d1.project.resourcesharingmgmt.resource.entity.KeywordSearchEntity;
import d1.project.resourcesharingmgmt.resource.model.Screen.*;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 大屏展示
 * @author zhengyang
 */
@Auth("noauth")
@RestController("/screen/analysis")
@RequestMapping("/screen/analysis")
public class AnalysisController {
    private final DataAnalysisBusiness dataAnalysisBusiness;

    public AnalysisController(DataAnalysisBusiness dataAnalysisBusiness) {
        this.dataAnalysisBusiness = dataAnalysisBusiness;
    }

    /**
     * 目录统计
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getArchCount")
    public Result<ArchCountVm> getArchCount() throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getArchCount());
    }

    /**
     * 数据类型分布
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getArchMediaFmtCount")
    public Result<List<ArchMediaFmtCountVm>> getArchMediaFmtCount() throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getArchMediaFmtCount());
    }

    /**
     * 更新周期类别占比
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getArchUpdateCycCount")
    public Result<List<ArchUpdateCycCountVm>> getArchUpdateCycCount() throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getArchUpdateCycCount());
    }

    /**
     * 文件下载和API调用次数
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getFileAndApiUseCount")
    public Result<FileAndApiUseCountVm> getFileAndApiUseCount() throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getFileAndApiUseCount());
    }

    /**
     * 当月服务调用次数（文件和API）每天
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getFileAndApiUseCountByMonth")
    public Result<List<FileAndApiUseCountByMonthVm>> getFileAndApiUseCountByMonth() throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getFileAndApiUseCountByMonth());
    }

    /**
     * 部门使用top10
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getOrgUseTop")
    public Result<List<OrgUseCount>> getOrgUseTop() throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getOrgUseTop());
    }

    /**
     * 数据提供部门top10
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getOrgGiveTop")
    public Result<OrgGiveOrDemandedTop> getOrgGiveTop() throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getOrgGiveTop());
    }

    /**
     * 部门需求top10
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getOrgDemandedTop")
    public Result<OrgGiveOrDemandedTop> getOrgDemandedTop() throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getOrgDemandedTop());
    }

    /**
     * 部门积极度TOP10
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getOrgApplyTop")
    public Result<List<OrgApplyCount>> getOrgApplyTop() throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getOrgApplyTop());
    }

    /**
     * 支持业务
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getSupportBusinessTList")
    public Result<List<SupportBusinessCountVm>> getSupportBusinessTList() throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getSupportBusinessTList());
    }

    /**
     * 关键词检索频度
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getKeywordSearchTList")
    public Result<List<KeywordSearchEntity>> getKeywordSearchTList() throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getKeywordSearchTList());
    }

    /**
     * 数据归集最多部门TOP10
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getDataCountTList")
    public Result<List<OrgDataUseCount>> getDataCountTList() throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getDataCountTList());
    }

    /**
     * 资源目录数与数据提供量提供最完整的TOP10部门
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getArchAndDataCountTList")
    public Result<List<OrgArchAndUseCount>> getArchAndDataCountTList() throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getArchAndDataCountTList());
    }

    /**
     * 前置机动态交换图
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/getArchAndCenterCount")
    public Result<OrgAndCenterCount> getArchAndCenterCount() throws Exception {
        return ResultUtil.success("",dataAnalysisBusiness.getArchAndCenterCount());
    }
}
