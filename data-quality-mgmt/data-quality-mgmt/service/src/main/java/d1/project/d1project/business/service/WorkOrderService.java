package d1.project.d1project.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.d1project.business.dao.WorkOrderDao;
import d1.project.d1project.business.entity.WorkOrder;
import d1.project.d1project.business.model.WorkOrderFindAllByMyVm;
import d1.project.d1project.business.model.WorkOrderFindAllVm;
import d1.project.d1project.business.model.WorkOrderHandleVm;
import d1.project.d1project.business.model.WorkOrderInsertVm;
import d1.project.d1project.business.utils.Utils;
import d1.project.d1project.common.utils.BaseUtils;
import d1.project.d1project.common.utils.StringUtils;
import org.apache.http.util.TextUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class WorkOrderService {

    private final WorkOrderDao workOrderDao;
    private final JdbcTemplate jdbcTemplate;

    public WorkOrderService(WorkOrderDao workOrderDao, JdbcTemplate jdbcTemplate) {
        this.workOrderDao = workOrderDao;
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * 分页查询所有
     *
     * @param model 查询参数
     */
    public Page<WorkOrder> findAll(WorkOrderFindAllVm model, HttpServletRequest request) throws Exception {

        SpecificationBuilder<WorkOrder> builder = new SpecificationBuilder<>();
        if (!Utils.getCurrentUserId(request).equals("admin")) {
            model.setHandlerId(Utils.getCurrentUserId(request));
        }
        builder.init((JSONObject) JSON.toJSON(model));
        Specification<WorkOrder> specification = builder.sContain("title", "title")
                .sEqual("level", "level")
                .sEqual("handlerId", "handlerId")
                .dOrder("createTime")
                .build();
        return workOrderDao.findAll(specification, builder.getPageable());
    }

    /**
     * 分页查询所有关于我的工单
     *
     * @param model 查询参数
     */
    public Page<WorkOrder> findAllByMy(WorkOrderFindAllByMyVm model, HttpServletRequest request) throws Exception {
        JSONObject paramObject = new JSONObject();
        paramObject.put("handlerId", Utils.getCurrentUserId(request));

        SpecificationBuilder<WorkOrder> builder = new SpecificationBuilder<>();
        builder.init(paramObject);
        builder.sEqual("handlerId", "handlerId");

        String status = model.getStatus();
        if (TextUtils.isEmpty(status) || status.equals("-1")) { //表示查询所有
            //do nothing

        } else if (status.equals("0")) { //我的代办
            paramObject.put("status", 0);
            builder.sEqual("status", "status");

        } else if (status.equals("1")) { //我已处理
            paramObject.put("status", 0);
            builder.sNotEqual("status", "status");
        }
        builder.dOrder("createTime");
        Specification<WorkOrder> specification = builder.build();
        return workOrderDao.findAll(specification, builder.getPageable());
    }

    public Map<String, Object> getCount(HttpServletRequest request) throws DoValidException {
        Map<String, Object> result = new HashMap<>();
        result.put("total", workOrderDao.countByHandlerId(Utils.getCurrentUserId(request)));
        result.put("unfinished", workOrderDao.countByHandlerIdAndStatus(Utils.getCurrentUserId(request), 0));
        result.put("finished", workOrderDao.countByHandlerIdAndStatusNot(Utils.getCurrentUserId(request), 0));
        return result;
    }

    public WorkOrder find(String id) throws DoValidException {
        return workOrderDao.findById(id).orElseThrow(() -> new DoValidException("工单不存在"));
    }

    public WorkOrder findById(String id) throws DoValidException {
        String sql = "select a.*,b.metadata_data from d1_work_order a left join d1_verify_rule b on a.verify_rule_id = b.id where a.id = " + "'" + id + "'";
        WorkOrder workOrder = jdbcTemplate.query(sql, new Object[]{}, new BeanPropertyRowMapper<WorkOrder>(WorkOrder.class)).get(0);
        if (workOrder != null && !StringUtils.isNull(workOrder.getMetadataData())) {
            JSONObject jo = JSON.parseObject(workOrder.getMetadataData());
            workOrder.setDataBasesName(jo.getString("sourceName"));
            workOrder.setTableName(jo.getJSONObject("source").getString("name"));
        }
        return workOrder;
    }

    /**
     * 新增
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(WorkOrderInsertVm model, HttpServletRequest request) throws DoValidException {
        //判断规则名称是否重复
        Optional<WorkOrder> workOrderOpt = workOrderDao.findFirstByTitle(model.getTitle());
        if (workOrderOpt.isPresent()) {
            throw new DoValidException("名称:“" + model.getTitle() + "”已经存在!");
        }

        WorkOrder entity = new WorkOrder();
        BeanUtils.copyProperties(model, entity);

        entity.setId(BaseUtils.generate32Id());
        entity.setStatus(0);
        entity.setCreateTime(Calendar.getInstance());
        entity.setCreateById(Utils.getCurrentUserId(request));
        entity.setCreateByName(Utils.getCurrentUserName(request));
        workOrderDao.save(entity);
    }


    /**
     * 工单处理
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void handle(WorkOrderHandleVm model, HttpServletRequest request) throws DoValidException {
        WorkOrder entity = workOrderDao.findById(model.getId()).orElseThrow(() -> new DoValidException("工单不存在"));

        //不属于自己的工单不能处理
        if (!entity.getHandlerId().equals(Utils.getCurrentUserId(request))) {
            throw new DoValidException("此工单处理人是“" + entity.getHandlerName() + "”,您无权限处理");
        }

        entity.setStatus(model.getStatus());
        entity.setHandlerOpinion(model.getHandlerOpinion());
        entity.setHandlerTime(Calendar.getInstance());
        entity.setHandlerPeriod(calculateHandlerPeriod(entity.getCreateTime(), entity.getHandlerTime()));

        entity.setUpdateTime(Calendar.getInstance());
        entity.setUpdateById(Utils.getCurrentUserId(request));
        workOrderDao.save(entity);
    }

    ////////////////////

    /**
     * 处理周期
     *
     * @param createTime  创建时间
     * @param handlerName 处理完时间
     * @return 处理时长
     */
    private String calculateHandlerPeriod(Calendar createTime, Calendar handlerName) {
        long ms = handlerName.getTimeInMillis() - createTime.getTimeInMillis();
        return Utils.formatTime(ms);
    }

}
