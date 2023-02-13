package d1.project.resourcesharingmgmt.resource.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.ChargeBusiness;
import d1.project.resourcesharingmgmt.resource.model.way.WayChargeWebVm;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author dy
 * 代办
 */
@Auth("webadmin")
@RestController("/web/charge")
@RequestMapping("/web/charge")
public class ChargeController {
    private final ChargeBusiness chargeBusiness;

    public ChargeController(ChargeBusiness chargeBusiness) {
        this.chargeBusiness = chargeBusiness;
    }

    /**
     * 门户获取当前用户代办
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAll")
    public Result<List<WayChargeWebVm>> findAll() throws DoValidException {
        return ResultUtil.success("成功",chargeBusiness.findAll());
    }
}
