package d1.project.resourcesharingmgmt.resource.business;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;
import d1.project.resourcesharingmgmt.common.utils.BaseUtils;
import d1.project.resourcesharingmgmt.resource.entity.FileDownLogEntity;
import d1.project.resourcesharingmgmt.resource.entity.ResourceUseInfoEntity;
import d1.project.resourcesharingmgmt.resource.service.FileDownLogService;
import d1.project.resourcesharingmgmt.resource.service.ResourceUseInfoService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;

/**
 * 信息资源目录与信息类型关联表
 * @author JungYoung
 */
@Service
public class ArchExFileBusiness {
    private final ResourceUseInfoService resourceUseInfoService;
    private final FileDownLogService fileDownLogService;

    public ArchExFileBusiness(ResourceUseInfoService resourceUseInfoService, FileDownLogService fileDownLogService) {
        this.resourceUseInfoService = resourceUseInfoService;
        this.fileDownLogService = fileDownLogService;
    }

    public void insertDownLog(String orgId, String filePath, HttpServletRequest request) throws Exception {
        if(StringUtils.isEmpty(orgId)){
            return;
        }

        ResourceUseInfoEntity resourceUseInfoEntity = resourceUseInfoService.findFirstByCreateDeptIdAndFileDownloadUri(orgId, filePath).orElseThrow(() -> new DoValidException("资源使用申请信息不存在"));

        FileDownLogEntity entity = new FileDownLogEntity();
        entity.setId(BaseUtils.generate32Id());
        entity.setResourceUseInfoId(resourceUseInfoEntity.getId());
        entity.setFileDownloadUri(filePath);
        entity.setDownloadTime(Calendar.getInstance());
        entity.setCreateTime(Calendar.getInstance());

        fileDownLogService.save(entity);
    }
}
