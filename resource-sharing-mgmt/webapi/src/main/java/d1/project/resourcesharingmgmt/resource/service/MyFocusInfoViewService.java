package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.resourcesharingmgmt.resource.dao.MyFocusInfoViewDao;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.MyFocusInfoFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.view.MyFocusInfoView;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author zhengyang
 */
@Service
public class MyFocusInfoViewService {

    private final MyFocusInfoViewDao myFocusInfoViewDao;

    public MyFocusInfoViewService(MyFocusInfoViewDao myFocusInfoViewDao){
        this.myFocusInfoViewDao=myFocusInfoViewDao;
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<MyFocusInfoView> find(String id) {
        return myFocusInfoViewDao.findById(id);
    }

    /**
     * 查询所有
     * @param model
     * @return
     * @throws Exception
     */
    public Page<MyFocusInfoView> findAll(MyFocusInfoFindAllVm model) throws Exception {
        SpecificationBuilder<MyFocusInfoView> builder = new SpecificationBuilder<>();
        Specification<MyFocusInfoView> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sContain("uviewNm", "uviewNm")
                .sContain("uviewNo", "uviewNo")
                .sEqual("shareLv", "shareLv")
                .sEqual("createById", "createById")
                .dOrder("createTime")
                .build();
        return myFocusInfoViewDao.findAll(specification, builder.getPageable());
    }
}
