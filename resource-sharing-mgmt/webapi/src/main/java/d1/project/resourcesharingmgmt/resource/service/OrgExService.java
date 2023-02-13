package d1.project.resourcesharingmgmt.resource.service;

import d1.project.resourcesharingmgmt.resource.dao.OrgExDao;
import d1.project.resourcesharingmgmt.resource.entity.OrgExEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * 机构对照
 *
 * @author zhengyang
 */
@Service
public class OrgExService {
    private final OrgExDao orgExDao;

    public OrgExService(OrgExDao orgExDao) {
        this.orgExDao = orgExDao;
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<OrgExEntity> find(String id) {
        return orgExDao.findById(id);
    }

}
