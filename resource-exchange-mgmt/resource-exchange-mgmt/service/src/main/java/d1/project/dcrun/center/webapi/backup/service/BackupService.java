package d1.project.dcrun.center.webapi.backup.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.backup.dao.BackupDao;
import d1.project.dcrun.center.webapi.backup.entity.Backup;
import d1.project.dcrun.center.webapi.common.util.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author xuaa
 * @date 2020-06-28-17:55
 */
@Service
public class BackupService {

    @Autowired
    private BackupDao backupDao;
    @Autowired
    private TokenService tokenService;


    /**
     * 查询列表
     *
     * @param params 请求参数
     * @return 返回参数
     * @throws Exception 抛出异常
     */
    public Page<Backup> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<Backup> builder = new SpecificationBuilder<>();
        Specification<Backup> specification = builder.init(params)
                .sContain("name", "name")
                .dOrder("createTime").build();

        return this.backupDao.findAll(specification, builder.getPageable());
    }

    public Backup findById(String id) {
        Optional<Backup> emqUserOptional = backupDao.findById(id);
        return emqUserOptional.orElse(null);
    }
    /**
     * 添加
     *
     * @param request 请求
     * @param params  请求实体
     * @throws Exception 抛出异常
     */
    public void insert(HttpServletRequest request, JSONObject params) throws Exception {

        Backup entity = JSONObject.parseObject(params.toJSONString(), Backup.class);

        if (backupDao.existsByName(entity.getName())) {
            throw new ValidException("该备份名称已存在");
        }

        entity.setId(MyUtils.generatePrimaryKeyId());
        tokenService.updateCreateIdAndTime(request, entity);
        backupDao.save(entity);
    }

    /**
     * 删除
     * @param id 任务id
     */
    public void delete(String id) {
        backupDao.deleteById(id);
    }
}
