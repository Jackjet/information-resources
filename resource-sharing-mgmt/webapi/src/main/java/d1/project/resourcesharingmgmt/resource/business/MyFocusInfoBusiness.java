package d1.project.resourcesharingmgmt.resource.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.common.model.OperationLog;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.entity.ArchBusiUviewExEntity;
import d1.project.resourcesharingmgmt.resource.entity.MyFocusInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.MyFocusInfoFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.MyFocusInfoInsertVm;
import d1.project.resourcesharingmgmt.resource.model.view.MyFocusInfoView;
import d1.project.resourcesharingmgmt.resource.service.ArchBusiUviewExService;
import d1.project.resourcesharingmgmt.resource.service.MyFocusInfoService;
import d1.project.resourcesharingmgmt.resource.service.MyFocusInfoViewService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * 我的关注
 *
 * @author JungYoung
 */
@Service
public class MyFocusInfoBusiness {
    private final MyFocusInfoService myFocusInfoService;
    private final MyFocusInfoViewService myFocusInfoViewService;
    private final ArchBusiUviewExService archBusiUviewExService;
    private final IOperationLogService iOperationLogService;

    public MyFocusInfoBusiness(MyFocusInfoService myFocusInfoService, MyFocusInfoViewService myFocusInfoViewService, ArchBusiUviewExService archBusiUviewExService, IOperationLogService iOperationLogService) {
        this.myFocusInfoService = myFocusInfoService;
        this.myFocusInfoViewService = myFocusInfoViewService;
        this.archBusiUviewExService = archBusiUviewExService;
        this.iOperationLogService = iOperationLogService;
    }

    public void insert(MyFocusInfoInsertVm model, HttpServletRequest request) throws DoValidException {
        JSONObject webAdminUserVm = TokenManager.getInstance().getUserByToken(request);
        String uviewId = model.getUviewId();
        ArchBusiUviewExEntity archBusiUviewExEntity = archBusiUviewExService.findByUviewId(uviewId).orElseThrow(() -> new DoValidException("信息资源目录不存在"));
        if (myFocusInfoService.existsAllByUviewIdAndCreateById(uviewId, webAdminUserVm.getString("id"))) {
            throw new DoValidException("已关注该资源目录");
        }
        MyFocusInfoEntity myFocusInfoEntity = new MyFocusInfoEntity();
        BeanUtils.copyProperties(archBusiUviewExEntity, myFocusInfoEntity);
        myFocusInfoEntity.setId(BaseUtils.generate32Id());
        TokenManager.getInstance().updateCreateIdAndTime(request, myFocusInfoEntity);
        myFocusInfoService.insert(myFocusInfoEntity);
        iOperationLogService.insert(new OperationLog("个人中心_我的关注", "新增", "新增我的关注" + myFocusInfoEntity.getUviewNm(), JSON.toJSONString(myFocusInfoEntity), 1), request);
    }

    public void delete(String uviewId, HttpServletRequest request) throws DoValidException {
        JSONObject webAdminUserVm = TokenManager.getInstance().getUserByToken(request);
        MyFocusInfoEntity myFocusInfoEntity = myFocusInfoService.findFirstByUviewIdAndCreateById(uviewId, webAdminUserVm.getString("id")).orElseThrow(() -> new DoValidException("我的关注不存在"));
        myFocusInfoService.delete(myFocusInfoEntity.getId());
        iOperationLogService.insert(new OperationLog("个人中心_我的关注", "删除", "取消关注" + myFocusInfoEntity.getUviewNm(), JSON.toJSONString(myFocusInfoEntity), 1), request);
    }

    public Page<MyFocusInfoView> findAll(MyFocusInfoFindAllVm model, HttpServletRequest request) throws Exception {
        JSONObject webAdminUserVm = TokenManager.getInstance().getUserByToken(request);
        model.setCreateById(webAdminUserVm.getString("id"));
        return myFocusInfoViewService.findAll(model);
    }

    public MyFocusInfoEntity find(String id) throws DoValidException {
        MyFocusInfoEntity myFocusInfoEntity = myFocusInfoService.find(id).orElseThrow(() -> new DoValidException("我的关注不存在"));
        return myFocusInfoEntity;
    }


    public long countAllByUviewId(String uviewId) throws DoValidException {
        return myFocusInfoService.countAllByUviewId(uviewId);
    }
}
