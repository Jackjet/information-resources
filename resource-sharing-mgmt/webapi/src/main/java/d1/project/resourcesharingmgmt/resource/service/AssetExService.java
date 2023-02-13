package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.google.common.base.Joiner;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.common.Constants;
import d1.project.resourcesharingmgmt.common.model.OperationLog;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.business.ArchBusiUviewExBusiness;
import d1.project.resourcesharingmgmt.resource.dao.*;
import d1.project.resourcesharingmgmt.resource.entity.*;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExUpdateVm;
import d1.project.resourcesharingmgmt.resource.model.AssetApiEx.*;
import d1.project.resourcesharingmgmt.system.model.vm.IdBaseVm;
import net.dcrun.component.file.server.IFileServerService;
import net.dcrun.component.file.server.UploadResult;
import net.dcrun.component.http.HttpService;
import net.dcrun.component.security.HmacSignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 信息资源目录与云数据关联表
 *
 * @author zhengyang
 */
@Service
public class AssetExService {
    @Value("${daas.apiKey}")
    private String api_Key;
    @Value("${dass.url}")
    private String dass_url;

    private final AssetExDao assetExDao;
    private final AssetExLogDao assetExLogDao;
    private final AssetFileExDao assetFileExDao;
    private final AssetDataExDao assetDataExDao;
    private final IOperationLogService iOperationLogService;
    private final ArchBusiUviewExService archBusiUviewExService;
    private final IFileServerService fileServerService;
    private final OrgExService orgExService;

    public AssetExService(AssetExDao assetExDao, AssetExLogDao assetExLogDao, AssetFileExDao assetFileExDao,
                          AssetDataExDao assetDataExDao, IOperationLogService iOperationLogService,
                          ArchBusiUviewExService archBusiUviewExService, IFileServerService fileServerService,
                          OrgExService orgExService) {
        this.assetExDao = assetExDao;
        this.assetExLogDao = assetExLogDao;
        this.assetFileExDao = assetFileExDao;
        this.assetDataExDao = assetDataExDao;
        this.iOperationLogService = iOperationLogService;
        this.archBusiUviewExService = archBusiUviewExService;
        this.fileServerService = fileServerService;
        this.orgExService = orgExService;
    }

    /**
     * 新建申请
     *
     * @param model
     * @param request
     */
    public void insert(AssetExInsertVm model, int state, HttpServletRequest request) throws DoValidException {
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        if (model.getType() == 0 && assetFileExDao.existsByUviewIdAndName(model.getUviewId(), model.getName())) {
            throw new DoValidException("文件已经挂接，请到资源管理模块中删除此文件后再挂接");
        }

        AssetExEntity entity = new AssetExEntity();
        BeanUtils.copyProperties(model, entity);
        entity.setId(BaseUtils.generate32Id());
        entity.setStatus(state);

        if (model.getType() == 0) {
            entity.setResourceName(model.getName());
        } else {
            entity.setResourceName(model.getTableName());
        }
        TokenManager.getInstance().updateCreateIdAndTime(request, entity);

        if (state == 1) {
            addLog(entity, request);
        }

        assetExDao.save(entity);
        iOperationLogService.insert(new OperationLog("资源管理_数据挂接_申请", "数据挂接_申请", "数据挂接_申请：" + entity.getId(), JSON.toJSONString(entity), 1), request);
    }

    /**
     * 编辑
     *
     * @param model
     * @param request
     */
    public void update(AssetExUpdateVm model, int state, HttpServletRequest request) throws DoValidException {
        AssetExEntity entity = assetExDao.findById(model.getId()).orElseThrow(() -> new DoValidException("申请不存在"));

        if (entity.getStatus() != 0 && entity.getStatus() != 3) {
            throw new DoValidException("申请状态异常");
        }
        BeanUtils.copyProperties(model, entity);
        entity.setStatus(state);
        if (model.getType() == 0) {
            entity.setResourceName(model.getName());
        } else {
            entity.setResourceName(model.getTableName());
        }
        TokenManager.getInstance().updateCreateIdAndTime(request, entity);

        if (state == 1) {
            addLog(entity, request);
        }

        assetExDao.save(entity);
        iOperationLogService.insert(new OperationLog("资源管理_数据挂接_编辑", "数据挂接_编辑", "数据挂接_编辑：" + entity.getId(), JSON.toJSONString(entity), 1), request);
    }

    /**
     * 查询资源目录下所有云数据信息
     *
     * @param model
     * @return
     * @throws Exception
     */
    public Page<AssetExEntity> findAll(AssetExFindAllVm model) throws Exception {
        SpecificationBuilder<AssetExEntity> builder = new SpecificationBuilder<>();
        Specification<AssetExEntity> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sContain("resourceName", "resourceName")
                .sEqual("uviewId", "uviewId")
                .sContain("uviewNm", "uviewNm")
                .sContain("uviewNo", "uviewNo")
                .sEqual("orgId", "orgId")
                .sContain("name", "name")
                .nEqual("type", "type")
                .nEqual("status", "status")
                .dOrder("createTime")
                .build();
        return assetExDao.findAll(specification, builder.getPageable());
    }

    /**
     * 查询资源目录下所有云数据信息
     *
     * @param model
     * @return
     * @throws Exception
     */
    public Page<AssetExEntity> findAllByExamine(JSONObject model) throws Exception {
        SpecificationBuilder<AssetExEntity> builder = new SpecificationBuilder<>();
        Specification<AssetExEntity> specification = builder.init(model)
                .sContain("resourceName", "resourceName")
                .sEqual("uviewId", "uviewId")
                .sContain("uviewNm", "uviewNm")
                .sContain("uviewNo", "uviewNo")
                .sEqual("orgId", "orgId")
                .sContain("name", "name")
                .nEqual("type", "type")
                .nNotEqual("status", "notStatus")
                .nEqual("status", "status")
                .dOrder("createTime")
                .build();
        return assetExDao.findAll(specification, builder.getPageable());
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<AssetExEntity> find(String id) {
        return assetExDao.findById(id);
    }

    /**
     * 新建申请
     *
     * @param model
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    public void submit(IdBaseVm model, HttpServletRequest request) throws DoValidException {
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        AssetExEntity entity = assetExDao.findById(model.getId()).orElseThrow(() -> new DoValidException("申请不存在"));
        if (entity.getStatus() != 0) {
            throw new DoValidException("申请状态异常");
        }
        entity.setStatus(1);
        assetExDao.save(entity);

        addLog(entity, request);

        iOperationLogService.insert(new OperationLog("资源管理_数据挂接_提交审核", "数据挂接_提交审核", "数据挂接_提交审核：" + entity.getId(), JSON.toJSONString(entity), 1), request);
    }

    /**
     * 新建申请
     *
     * @param model
     * @param request
     */
    @Transactional(rollbackFor = Exception.class)
    public void examine(AssetExExamineVm model, HttpServletRequest request) throws DoValidException {
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        AssetExEntity entity = assetExDao.findById(model.getId()).orElseThrow(() -> new DoValidException("申请不存在"));
        ArchBusiUviewExEntity archBusiUviewExEntity = archBusiUviewExService.find(entity.getUviewId()).orElseThrow(() -> new DoValidException("资源目录信息不存在"));

        if (entity.getStatus() != 1) {
            throw new DoValidException("申请状态异常");
        }

        //添加审批记录
        AssetExLogEntity log = new AssetExLogEntity();
        log.setId(BaseUtils.generate32Id());
        log.setUviewId(entity.getUviewId());
        log.setUviewNm(entity.getUviewNm());
        log.setAssetExId(entity.getId());
        log.setDetail(model.getDetail());
        log.setType(entity.getType());
        log.setCreateByName(user.getString("name"));
        TokenManager.getInstance().updateCreateIdAndTime(request, log);

        //0通过，1驳回
        if (model.getStatus() == 0) {
            entity.setStatus(2);
            log.setStatusDetail("终审通过");
            //资源目录设置为已挂接并添加挂接后的信息
            if (entity.getType() == 0) {
                archBusiUviewExEntity.setFileHookStatus("1");
                AssetFileExEntity fileExEntity = new AssetFileExEntity();
                BeanUtils.copyProperties(entity, fileExEntity);
                fileExEntity.setId(BaseUtils.generate32Id());
                TokenManager.getInstance().updateCreateIdAndTime(request, fileExEntity);
                assetFileExDao.save(fileExEntity);
            } else {
                //云数据挂接后只允许保留一条挂接
                assetDataExDao.deleteByUviewId(entity.getUviewId());

                archBusiUviewExEntity.setDataHookStatus("1");
                AssetDataExEntity dataExEntity = new AssetDataExEntity();
                BeanUtils.copyProperties(entity, dataExEntity);
                dataExEntity.setId(BaseUtils.generate32Id());
                TokenManager.getInstance().updateCreateIdAndTime(request, dataExEntity);
                assetDataExDao.save(dataExEntity);
            }
            archBusiUviewExEntity.setHookTime(Calendar.getInstance());
        } else {
            //如果是文件挂接，驳回后删除文件
            if (entity.getType() == 0) {
                String fileDownPath = entity.getFileDownloadUri();
                String fileName = fileDownPath.replace("webadmin/file/download/", "");

                File file = new File(Constants.FILE + File.separator + fileName);
                if (file.exists()) {
                    file.delete();
                }
            }
            entity.setStatus(3);
            log.setStatusDetail("驳回");
        }

        assetExDao.save(entity);
        assetExLogDao.save(log);
        archBusiUviewExService.save(archBusiUviewExEntity);

        iOperationLogService.insert(new OperationLog("资源管理_数据挂接_审核", "数据挂接_审核", "数据挂接_审核：" + entity.getId(), JSON.toJSONString(entity), 1), request);
    }

    public UploadResult upload(String uviewId, MultipartFile file) throws Exception {
        ArchBusiUviewExEntity archBusiUviewExEntity = archBusiUviewExService.find(uviewId).orElseThrow(() -> new DoValidException("资源目录信息不存在"));
        OrgExEntity orgExEntity = orgExService.find(archBusiUviewExEntity.getProvOrgId()).orElseThrow(() -> new DoValidException("机构文件夹不存在"));
        String uviewNo = archBusiUviewExEntity.getUviewNo().replace("/", "_");
        String orgCode = orgExEntity.getOrgCode();
        return fileServerService.uploadFile(file, Constants.FILE + "/" + orgCode + "/" + uviewNo, "webadmin/file/download/" + orgCode + "/" + uviewNo);
    }

    /**
     * 添加提交申请记录
     *
     * @param entity
     * @param request
     * @throws DoValidException
     */
    private void addLog(AssetExEntity entity, HttpServletRequest request) throws DoValidException {
        JSONObject user = TokenManager.getInstance().getUserByToken(request);
        //添加审批记录
        AssetExLogEntity log = new AssetExLogEntity();
        log.setId(BaseUtils.generate32Id());
        log.setUviewId(entity.getUviewId());
        log.setUviewNm(entity.getUviewNm());
        log.setAssetExId(entity.getId());
        log.setDetail("提交审核");
        log.setType(entity.getType());
        log.setStatusDetail("待审核");
        log.setCreateByName(user.getString("name"));
        TokenManager.getInstance().updateCreateIdAndTime(request, log);
        assetExLogDao.save(log);
    }
}
