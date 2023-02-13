package d1.project.resourcesharingmgmt.resource.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.ArchBusiUviewExBusiness;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExResVm;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExVm;
import d1.project.resourcesharingmgmt.resource.service.KeywordSearchService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * 门户端_信息资源目录管理
 *
 * @author zhengyang
 */
@Auth("noauth")
@RestController("/web/archBusiUviewEx")
@RequestMapping("/web/archBusiUviewEx")
public class ArchBusiUviewExController {
    private final ArchBusiUviewExBusiness archBusiUviewExBusiness;
    private final KeywordSearchService keywordSearchService;

    public ArchBusiUviewExController(ArchBusiUviewExBusiness archBusiUviewExBusiness, KeywordSearchService keywordSearchService) {
        this.archBusiUviewExBusiness = archBusiUviewExBusiness;
        this.keywordSearchService = keywordSearchService;
    }

    /**
     * 信息资源目录分页查询
     * @param model
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findAll")
    public Result<Page<ArchBusiUviewExResVm>> findAll(ArchBusiUviewExFindAllVm model, HttpServletRequest request) throws Exception {
        //因为门户端和管理端共用Business，所以其他业务加在这里
        keywordSearchService.save(model.getUviewNm(), request);
        return ResultUtil.success("", archBusiUviewExBusiness.findAll(model, request));
    }

    /**
     * 信息资源目录详情
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/find")
    public Result<ArchBusiUviewExVm> find(String id, HttpServletRequest request) throws DoValidException {
        return ResultUtil.success("", archBusiUviewExBusiness.find(id, request));
    }

    /**
     * 信息资源目录访问次数
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findVisits")
    public Result<String> findVisits(String id, HttpServletRequest request) throws DoValidException {
        return ResultUtil.success("", archBusiUviewExBusiness.findVisits(id, request));
    }
}
