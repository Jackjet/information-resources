package d1.project.resourcesharingmgmt.resource.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.resourcesharingmgmt.resource.business.MyFocusInfoBusiness;
import d1.project.resourcesharingmgmt.resource.entity.MyFocusInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.MyFocusInfoFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.MyFocusInfoInsertVm;
import d1.project.resourcesharingmgmt.resource.model.view.MyFocusInfoView;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 门户端_我的关注
 *
 * @author zhengyang
 */
@Auth("webadmin")
@RestController("/web/myFocusInfo")
@RequestMapping("/web/myFocusInfo")
public class MyFocusInfoController {

    private final MyFocusInfoBusiness myFocusInfoBusiness;

    public MyFocusInfoController(MyFocusInfoBusiness myFocusInfoBusiness) {
        this.myFocusInfoBusiness = myFocusInfoBusiness;
    }

    /**
     * 添加收藏关注
     *
     * @param model
     * @param request
     * @return
     * @throws DoValidException
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody MyFocusInfoInsertVm model, HttpServletRequest request) throws DoValidException {
        myFocusInfoBusiness.insert(model, request);
        return ResultUtil.success("");
    }

    /**
     * 我关注的目录
     *
     * @param model
     * @param request
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/findAll")
    public Result<Page<MyFocusInfoView>> findAll(MyFocusInfoFindAllVm model, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", myFocusInfoBusiness.findAll(model, request));
    }

    /**
     * 关注详情
     *
     * @param id
     * @return
     * @throws DoValidException
     */
    @GetMapping(value = "/findById")
    public Result<MyFocusInfoEntity> find(String id) throws DoValidException {
        return ResultUtil.success("", myFocusInfoBusiness.find(id));
    }

    /**
     * 资源目录被收藏次数
     * @param uviewId
     * @return
     * @throws DoValidException
     */
    @Auth("noauth")
    @GetMapping(value = "/countByUviewId")
    public Result<Long> countByUviewId(String uviewId) throws DoValidException {
        return ResultUtil.success("", myFocusInfoBusiness.countAllByUviewId(uviewId));
    }

    /**
     * 取消关注
     * @param uviewId
     * @param request
     * @return
     * @throws DoValidException
     */
    @DeleteMapping(value = "/delete")
    public Result<String> delete(String uviewId, HttpServletRequest request) throws DoValidException {
        myFocusInfoBusiness.delete(uviewId, request);
        return ResultUtil.success("");
    }
}
