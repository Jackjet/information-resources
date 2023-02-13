package d1.project.resourcesharingmgmt.resource.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.DataAggregationBusiness;
import d1.project.resourcesharingmgmt.resource.model.DataAggregationVm;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 门户端_首页_数据汇聚
 *
 * @author zhengyang
 */
@Auth("noauth")
@RestController("/web/dataAggregation")
@RequestMapping("/web/dataAggregation")
public class DataAggregationController {
    private final DataAggregationBusiness dataAggregationBusiness;



    public DataAggregationController(DataAggregationBusiness dataAggregationBusiness) {
        this.dataAggregationBusiness = dataAggregationBusiness;
    }

    /**
     * 数据汇聚
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<DataAggregationVm> find() throws DoValidException {
        return ResultUtil.success("", dataAggregationBusiness.find());
    }
}
