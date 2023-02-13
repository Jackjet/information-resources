package d1.project.dataintegration.system.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.model.PageableVm;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.dataintegration.common.utils.BaseUtils;
import d1.project.dataintegration.system.dao.OrganizationUserDao;
import d1.project.dataintegration.system.dao.WebAdminUserDao;
import d1.project.dataintegration.system.entity.OrganizationUserEntity;
import d1.project.dataintegration.system.entity.WebAdminUserEntity;
import d1.project.dataintegration.system.model.vm.OrganizationUserInsertAllVm;
import d1.project.dataintegration.system.model.vm.WebAdminUserBaseVm;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 组织机构用户
 * d1Project
 *
 * @author kikki
 * @date 2020-09-09 11:27
 */
@Service
public class OrganizationUserService {


    private final OrganizationUserDao organizationUserDao;

    private final WebAdminUserDao webAdminUserDao;

    public OrganizationUserService(OrganizationUserDao organizationUserDao, WebAdminUserDao webAdminUserDao) {
        this.organizationUserDao = organizationUserDao;
        this.webAdminUserDao = webAdminUserDao;
    }

    /**
     * 新增
     *
     * @param model 传输模型
     */
    public void insert(OrganizationUserEntity model, HttpServletRequest request) throws DoValidException {
        model.setId(BaseUtils.generate32Id());
        JSONObject userByToken = TokenManager.getInstance().getUserByToken(request);
        if (userByToken!=null) {
            TokenManager.getInstance().updateCreateIdAndTime(request, model);
        }
        organizationUserDao.save(model);
    }

    @Transactional(rollbackFor = Exception.class)
    public void insertAll(OrganizationUserInsertAllVm model, HttpServletRequest request) throws DoValidException {
        List<WebAdminUserEntity> webAdminUserEntities = new ArrayList<>();
        List<OrganizationUserEntity> organizationUserEntities = new ArrayList<>();
        List<String> userIds = model.getUserIds();
        if (userIds.contains("admin")) {
            throw new DoValidException("不可为系统管理员分配角色");
        }
        for (WebAdminUserEntity webAdminUserEntity : webAdminUserDao.findAllById(userIds)) {
            /*新增关联关系*/
            OrganizationUserEntity organizationUserEntity = new OrganizationUserEntity(webAdminUserEntity.getId(), model.getOrganizationId());
            TokenManager.getInstance().updateCreateIdAndTime(request, organizationUserEntity);
            organizationUserEntity.setId(BaseUtils.generate32Id());
            organizationUserEntities.add(organizationUserEntity);
            webAdminUserEntity.setOrganizationName(model.getOrganizationName());
            webAdminUserEntities.add(webAdminUserEntity);
        }
        organizationUserDao.deleteByUserIdIn(userIds);
        organizationUserDao.saveAll(organizationUserEntities);
        webAdminUserDao.saveAll(webAdminUserEntities);
    }

    void deleteByUserId(String userId) {
        organizationUserDao.deleteByUserId(userId);
    }

    void deleteByUserIdIn(Collection<String> userId) {
        organizationUserDao.deleteByUserIdIn(userId);
    }

    boolean existsByOrganizationIdIn(Collection<String> organizationId) {
        return organizationUserDao.existsByOrganizationIdIn(organizationId);
    }

    public List<OrganizationUserEntity> findByOrganizationId(String organizationId) {
        return organizationUserDao.findByOrganizationId(organizationId);
    }

    public Page<WebAdminUserBaseVm> findAllByOrganizationId(boolean selected, String roleName, String userName, String organizationId, PageableVm pageableVm) throws Exception {
        SpecificationBuilder<WebAdminUserEntity> builder = new SpecificationBuilder<>();
        builder.init((JSONObject) JSON.toJSON(pageableVm));
        if (selected) {
            return organizationUserDao.findAllByOrganizationIdIsNotNullAndUserNameAndRoleName(organizationId, userName, roleName, builder.getPageable());
        } else {
            return organizationUserDao.findAllByOrganizationIdIsNullAndUserNameAndRoleName(organizationId, userName, roleName, builder.getPageable());
        }
    }


    public List<OrganizationUserEntity> findByUserId(String userId) {
        return organizationUserDao.findByUserId(userId);
    }
}
