package d1.project.tangshan.operation.manage.service.operations.module;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.tangshan.operation.manage.dao.operations.module.NodeDao;
import d1.project.tangshan.operation.manage.entity.operations.module.Node;
import d1.project.tangshan.operation.manage.service.operations.log.OperationLogService;
import d1.project.tangshan.operation.manage.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author lin
 */
@Service
public class NodeService {
    private final NodeDao nodeDao;
    private final OperationLogService operationLogService;
    private final TokenService tokenService;

    public NodeService(NodeDao nodeDao, OperationLogService operationLogService, TokenService tokenService) {
        this.nodeDao = nodeDao;
        this.operationLogService = operationLogService;
        this.tokenService = tokenService;
    }

    public void insert(JSONObject model, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        Node node = MyUtils.model2Entity(model, Node.class);
        String name = node.getName();
        try {
            MyUtils.throwMsg(nodeDao.existsByName(node.getName()), "节点名称已存在");
            if (!StringUtils.isEmpty(node.getIntranetIp())) {
                MyUtils.throwMsg(nodeDao.existsByIntranetIp(node.getIntranetIp()), "内网IP已存在");
            }
            if (!StringUtils.isEmpty(node.getPublicIp())) {
                MyUtils.throwMsg(nodeDao.existsByPublicIp(node.getPublicIp()), "公网IP已存在");
            }
            node.setId(MyUtils.generate32Id());
            tokenService.updateCreateIdAndTime(request, node);
            nodeDao.save(node);

            operationLogService.setLog("节点管理—添加", userName, "运维行为管理-组件管理-节点管理", "添加了一个节点:" + name, "1", request);
        } catch (Exception e) {

            operationLogService.setLog("节点管理—添加", userName, "运维行为管理-组件管理-节点管理", "添加了一个节点:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }


    public void delete(String id, HttpServletRequest request) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            MyUtils.throwMsg(StringUtils.isEmpty(id), "id不能为空");
            Node node = nodeDao.findById(id).orElseThrow(() -> new ValidException("该节点不存在"));
            name = node.getName();
            nodeDao.deleteById(id);
            operationLogService.setLog("节点管理—删除", userName, "运维行为管理-组件管理-节点管理", "删除了一个节点:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("节点管理—删除", userName, "运维行为管理-组件管理-节点管理", "删除了一个节点:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }
    }


    public void update(HttpServletRequest request, JSONObject model) throws Exception {
        String userName = tokenService.getUserByToken(request).getString("name");
        String name = "未知";
        try {
            Node node = MyUtils.model2Entity(model, Node.class);

            Node oldNode = nodeDao.findById(node.getId()).orElseThrow(() -> new ValidException("该节点不存在"));
            tokenService.updateUpdateIdAndTime(request, oldNode);
            name = oldNode.getName();
            if (!StringUtils.isEmpty(node.getIntranetIp())) {
                if (!node.getIntranetIp().equals(oldNode.getIntranetIp())) {
                    MyUtils.throwMsg(nodeDao.existsByIntranetIp(node.getIntranetIp()), "公网IP已存在");
                }
            }
            if (!StringUtils.isEmpty(node.getPublicIp())) {
                if (!node.getPublicIp().equals(oldNode.getPublicIp())) {
                    MyUtils.throwMsg(nodeDao.existsByPublicIp(node.getPublicIp()), "内网IP已存在");
                }
            }

            oldNode.setPublicIp(node.getPublicIp());
            oldNode.setIntranetIp(node.getIntranetIp());
            oldNode.setConfiguration(node.getConfiguration());
            oldNode.setPurpose(node.getPurpose());
            oldNode.setRemark(node.getRemark());
            nodeDao.save(oldNode);
            operationLogService.setLog("节点管理—编辑", userName, "运维行为管理-组件管理-节点管理", "编辑了一个节点:" + name, "1", request);
        } catch (Exception e) {
            operationLogService.setLog("节点管理—编辑", userName, "运维行为管理-组件管理-节点管理", "编辑了一个节点:" + name, "0", request);
            throw new ValidException(e.getMessage());
        }

    }


    public Page<Node> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<Node> builder = new SpecificationBuilder<>();
        Specification<Node> specification = builder.init(params)
                .sContain("name", "name")
                .sContain("publicIp", "publicIp")
                .sContain("intranetIp", "intranetIp")
                .tBetween("createTime", "startTime", "endTime", "yyyy-MM-dd HH:mm:ss")
                .dOrder("createTime")
                .build();
        return nodeDao.findAll(specification, builder.getPageable());
    }

    public List<Node> findAllNodes() {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        return nodeDao.findAll(sort);
    }

    public Node findById(String id) throws Exception {
        return nodeDao.findById(id).orElseThrow(() -> new ValidException("该节点不存在"));
    }

    public Node findByIntranetIp(String intranetIp) {
        return this.nodeDao.findByIntranetIp(intranetIp);
    }

    public Node findByPublicIp(String publicIp) {
        return this.nodeDao.findByPublicIp(publicIp);
    }
}
