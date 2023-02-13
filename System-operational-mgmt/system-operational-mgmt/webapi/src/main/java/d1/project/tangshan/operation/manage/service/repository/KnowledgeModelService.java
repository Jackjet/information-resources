package d1.project.tangshan.operation.manage.service.repository;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.repository.KnowledgeModelDao;
import d1.project.tangshan.operation.manage.entity.repository.KnowledgeModel;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lin
 */
@Service
public class KnowledgeModelService {
    private final KnowledgeModelDao modelDao;
    private final TokenService tokenService;
    private final OperationLogService operationLogService;

    public KnowledgeModelService(KnowledgeModelDao modelDao, TokenService tokenService, OperationLogService operationLogService) {
        this.modelDao = modelDao;
        this.tokenService = tokenService;
        this.operationLogService = operationLogService;
    }

    public KnowledgeModel findById(String id) throws Exception {
        return modelDao.findById(id).orElseThrow(() -> new ValidException("该模型不存在"));
    }

    public void insert(JSONObject model, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            KnowledgeModel model1 = MyUtils.model2Entity(model, KnowledgeModel.class);
            MyUtils.throwMsg(modelDao.existsByName(model1.getName()), "模型名称已存在");
            model1.setId(MyUtils.generate32Id());
            name = model1.getName();
            tokenService.updateCreateIdAndTime(request, model1);

            modelDao.save(model1);
            operationLogService.setLog("模型管理—添加", userName, "知识库-模型管理", "添加了一个模型:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("模型管理—添加", userName, "知识库-模型管理", "添加了一个模型:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            MyUtils.throwMsg(StringUtils.isEmpty(id), "id不能为空");
            KnowledgeModel model = modelDao.findById(id).orElseThrow(() -> new ValidException("该模型不存在"));
            name = model.getName();
            modelDao.deleteById(id);
            operationLogService.setLog("模型管理—删除", userName, "知识库-模型管理", "删除了一个模型:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("模型管理—删除", userName, "知识库-模型管理", "删除了一个模型:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }

    public void update(HttpServletRequest request, JSONObject model) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            KnowledgeModel model1 = MyUtils.model2Entity(model, KnowledgeModel.class);

            KnowledgeModel oldModel1 = modelDao.findById(model1.getId()).orElseThrow(() -> new ValidException("该模型不存在"));
            tokenService.updateUpdateIdAndTime(request, oldModel1);
            name = oldModel1.getName();
            if (!oldModel1.getName().equals(model1.getName())) {
                MyUtils.throwMsg(modelDao.existsByName(model1.getName()), "模型名称已存在");
                oldModel1.setName(model1.getName());
            }
            oldModel1.setContent(model1.getContent());
            oldModel1.setRemark(model1.getRemark());
            modelDao.save(oldModel1);
            operationLogService.setLog("模型管理—编辑", userName, "知识库-模型管理", "编辑了一个模型:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("模型管理—编辑", userName, "知识库-模型管理", "编辑了一个模型:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }

    }


    public Page<KnowledgeModel> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<KnowledgeModel> builder = new SpecificationBuilder<>();
        Specification<KnowledgeModel> specification = builder.init(params)
                .sContain("name", "name")
                .sEqual("type", "type")
                .dOrder("createTime")
                .build();
        return modelDao.findAll(specification, builder.getPageable());
    }


}
