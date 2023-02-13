package d1.project.resourcesharingmgmt.resource.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.entity.WayEntity;
import d1.project.resourcesharingmgmt.resource.service.WayService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author dy
 * 服务指引
 */
@Auth("webadmin")
@RestController("/web/guide")
@RequestMapping("/web/guide")
public class GuideController {
    private final WayService wayService;

    public GuideController(WayService wayService) {
        this.wayService = wayService;
    }

    /**
     * 门户获取当前用户服务指引
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAll")
    public Result<List<WayEntity>> guideFindAllWeb() throws DoValidException {
        return ResultUtil.success("成功", wayService.guideFindAllWeb());
    }
}
