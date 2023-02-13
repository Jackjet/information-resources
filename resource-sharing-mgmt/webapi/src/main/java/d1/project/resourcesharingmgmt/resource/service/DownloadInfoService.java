package d1.project.resourcesharingmgmt.resource.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.common.model.OperationLog;
import d1.project.resourcesharingmgmt.common.service.IOperationLogService;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.dao.DownloadInfoDao;
import d1.project.resourcesharingmgmt.resource.entity.DownloadInfoEntity;
import d1.project.resourcesharingmgmt.resource.model.DownloadInfo.DownloadInfoFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.DownloadInfo.DownloadInfoInsertVm;
import d1.project.resourcesharingmgmt.resource.model.DownloadInfo.DownloadInfoUpdateVm;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.DecimalFormat;
import java.util.Optional;

/**
 * 下载中心
 *
 * @author zhengyang
 */
@Service
public class DownloadInfoService {
    private final DownloadInfoDao downloadInfoDao;
    private final IOperationLogService operationLogService;

    public DownloadInfoService(DownloadInfoDao downloadInfoDao, IOperationLogService operationLogService) {
        this.downloadInfoDao = downloadInfoDao;
        this.operationLogService = operationLogService;
    }

    /**
     * 删除文件数据
     *
     * @param id
     * @throws DoValidException
     */

    public void delete(String id) throws DoValidException {
        DownloadInfoEntity downloadInfoEntity = downloadInfoDao.findById(id).orElseThrow(() -> new DoValidException("文件资源不存在"));
        downloadInfoDao.delete(downloadInfoEntity);
    }

    /**
     * 新增文件
     *
     * @param model
     * @param request
     * @throws DoValidException
     */
    public void insert(DownloadInfoInsertVm model, HttpServletRequest request) throws DoValidException {
        DownloadInfoEntity entity = new DownloadInfoEntity();
        BeanUtils.copyProperties(model, entity);
        entity.setId(BaseUtils.generate32Id());
        entity.setFileSize(formatFileSize(Long.valueOf(model.getFileSize())));
        TokenManager.getInstance().updateCreateIdAndTime(request, entity);
        downloadInfoDao.save(entity);
        operationLogService.insert(new OperationLog("文件管理_新增", "新增", "文件管理新增" + entity.getId(), JSON.toJSONString(entity), 1), request);
    }

    /**
     * 查询所有
     *
     * @param model
     * @return
     * @throws Exception
     */
    public Page<DownloadInfoEntity> findAll(DownloadInfoFindAllVm model) throws Exception {
        SpecificationBuilder<DownloadInfoEntity> builder = new SpecificationBuilder<>();
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        Specification<DownloadInfoEntity> specification = builder.init(paramObject)
                .sContain("title", "title")
                .nEqual("type","type")
                .dOrder("createTime")
                .build();
        return downloadInfoDao.findAll(specification, builder.getPageable());
    }

    /**
     * 详情
     *
     * @param id id
     */
    public Optional<DownloadInfoEntity> find(String id) {
        return downloadInfoDao.findById(id);
    }

    /**
     * 文件下载
     *
     * @param id
     * @return
     */
    public DownloadInfoEntity download(String id) throws DoValidException {
        DownloadInfoEntity downloadInfoEntity = downloadInfoDao.findById(id).orElseThrow(() -> new DoValidException("文件不存在"));
        downloadInfoEntity.setDownCount(downloadInfoEntity.getDownCount()+1);
        downloadInfoDao.save(downloadInfoEntity);
        return downloadInfoEntity;
    }

    /**
     * 审核
     *
     * @param model
     * @param request
     * @throws DoValidException
     */
    public void update(DownloadInfoUpdateVm model, HttpServletRequest request) throws DoValidException {
        DownloadInfoEntity downloadInfoEntity = downloadInfoDao.findById(model.getId()).orElseThrow(() -> new DoValidException("文件不存在"));
        BeanUtils.copyProperties(model, downloadInfoEntity);
        if(!model.getFileSize().contains("KB")) {
            downloadInfoEntity.setFileSize(formatFileSize(Long.valueOf(model.getFileSize())));
        }
        TokenManager.getInstance().updateUpdateIdAndTime(request, downloadInfoEntity);
        downloadInfoDao.save(downloadInfoEntity);
        operationLogService.insert(new OperationLog("文件管理_文件修改", "文件修改", "文件修改" + downloadInfoEntity.getTitle(), JSON.toJSONString(downloadInfoEntity), 1), request);
    }

    /**
     * 转换文件大小
     */
    public static String formatFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }
}
