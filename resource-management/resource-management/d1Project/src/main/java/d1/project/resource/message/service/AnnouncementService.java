package d1.project.resource.message.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resource.common.Constants;
import d1.project.resource.common.model.OperationLog;
import d1.project.resource.common.service.IOperationLogService;
import d1.project.resource.common.utils.BaseUtils;
import d1.project.resource.message.dao.AnnouncementDao;
import d1.project.resource.message.entity.AnnouncementEntity;
import d1.project.resource.message.model.AnnouncementFindAllVm;
import d1.project.resource.message.model.AnnouncementInsertVm;
import d1.project.resource.message.model.AnnouncementUpdateVm;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * MY_JAR_NAME
 *
 * @author kikki
 * @date 2020-11-19 16:05
 */
@Service
public class AnnouncementService {
    private final AnnouncementDao announcementDao;
    private final IOperationLogService iOperationLogService;

    public AnnouncementService(AnnouncementDao announcementDao, IOperationLogService iOperationLogService) {
        this.announcementDao = announcementDao;
        this.iOperationLogService = iOperationLogService;
    }

    /**
     * 新增
     */
    public void insert(AnnouncementInsertVm model, HttpServletRequest request) throws DoValidException {
        AnnouncementEntity announcementEntity = new AnnouncementEntity();
        announcementEntity.setId(BaseUtils.generate32Id());
        announcementEntity.setTitle(model.getTitle());
        announcementEntity.setContent(model.getContent());
        TokenManager.getInstance().updateCreateIdAndTime(request, announcementEntity);
        announcementDao.save(announcementEntity);
        iOperationLogService.insert(new OperationLog("消息管理_通知公告", "新增", "新增通知公告" + announcementEntity.getTitle(), JSON.toJSONString(announcementEntity), 1), request);
    }

    /**
     * 删除
     */
    public void delete(String id, HttpServletRequest request) throws DoValidException {
        AnnouncementEntity announcementEntity = announcementDao.findById(id).orElseThrow(() -> new DoValidException("通知公告不存在"));
        announcementDao.delete(announcementEntity);
        iOperationLogService.insert(new OperationLog("消息管理_通知公告", "删除", "删除通知公告" + announcementEntity.getTitle(), JSON.toJSONString(announcementEntity), 1), request);
    }

    /**
     * 更新
     */
    public void update(AnnouncementUpdateVm model, HttpServletRequest request) throws DoValidException {
        AnnouncementEntity announcementEntity = announcementDao.findById(model.getId()).orElseThrow(() -> new DoValidException("通知公告不存在"));
        announcementEntity.setTitle(model.getTitle());
        announcementEntity.setContent(model.getContent());
        TokenManager.getInstance().updateUpdateIdAndTime(request, announcementEntity);
        announcementDao.save(announcementEntity);
        iOperationLogService.insert(new OperationLog("消息管理_通知公告", "编辑", "编辑通知公告" + announcementEntity.getTitle(), JSON.toJSONString(announcementEntity), 1), request);
    }


    /**
     * 查询所有
     */
    public Page<AnnouncementEntity> findAll(AnnouncementFindAllVm model) throws Exception {
        SpecificationBuilder<AnnouncementEntity> builder = new SpecificationBuilder<>();
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        Specification<AnnouncementEntity> specification = builder.init(paramObject)
                .sContain("title", "title")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime")
                .build();
        return announcementDao.findAll(specification, builder.getPageable());
    }

    /**
     * 详情
     */
    public Optional<AnnouncementEntity> find(String id) {
        return announcementDao.findById(id);
    }
}
