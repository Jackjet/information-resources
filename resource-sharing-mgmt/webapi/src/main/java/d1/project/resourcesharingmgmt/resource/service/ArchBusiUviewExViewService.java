package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.resourcesharingmgmt.resource.dao.ArchBusiUviewExViewDao;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.view.ArchBusiUviewExView;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Optional;

/**
 * @author zhengyang
 */
@Service
public class ArchBusiUviewExViewService {

    private final ArchBusiUviewExViewDao archBusiUviewExViewDao;

    public ArchBusiUviewExViewService(ArchBusiUviewExViewDao archBusiUviewExViewDao) {
        this.archBusiUviewExViewDao = archBusiUviewExViewDao;
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<ArchBusiUviewExView> find(String id) {
        return archBusiUviewExViewDao.findById(id);
    }

    /**
     * 查询所有
     *
     * @param model
     * @return
     * @throws Exception
     */
    public Page<ArchBusiUviewExView> findAll(ArchBusiUviewExFindAllVm model) throws Exception {
        String order = model.getOrder();
        if (StringUtils.isEmpty(order)) {
            order = "createTime";
        }
        SpecificationBuilder<ArchBusiUviewExView> builder = new SpecificationBuilder<>();
        Specification<ArchBusiUviewExView> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sContain("uviewNm", "uviewNm")
                .sContain("uviewNo", "uviewNo")
                .sEqual("shareLv", "shareLv")
                .sEqual("provOrgId", "provOrgId")
                .sEqual("typId", "typId")
                .sEqual("hookStatus", "hookStatus")
                .sEqual("status", "status")
                .dOrder(order)
                .build();
        return archBusiUviewExViewDao.findAll(specification, builder.getPageable());
    }
}
