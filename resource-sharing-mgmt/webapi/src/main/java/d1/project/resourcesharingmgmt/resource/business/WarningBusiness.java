package d1.project.resourcesharingmgmt.resource.business;

import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExWarningVm;
import d1.project.resourcesharingmgmt.resource.model.WarningVm;
import d1.project.resourcesharingmgmt.resource.service.ArchBusiUviewExService;
import org.springframework.data.domain.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 目录资源更新警告
 *
 * @author JungYoung
 */
@Service
public class WarningBusiness {
    private final JdbcTemplate jdbcTemplate;

    public WarningBusiness(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Page<ArchBusiUviewExWarningVm> findAll(WarningVm model, HttpServletRequest request) throws Exception {
        String warningSql = "select a.*,c.\"name\" provOrgName " + getWarningSql(model)+ " limit " + model.getSize() + " OFFSET " + ((model.getPage() - 1) * model.getSize());
        String warningCountSql = "select count(*) " + getWarningSql(model);
        List<ArchBusiUviewExWarningVm> listVm = jdbcTemplate.query(warningSql, new Object[]{}, new BeanPropertyRowMapper<ArchBusiUviewExWarningVm>(ArchBusiUviewExWarningVm.class));
        int count = jdbcTemplate.queryForObject(warningCountSql, Integer.class);
        return new PageImpl<>(listVm, getPageable(null, model.getPage(), model.getSize()), count);
    }


    private String getWarningSql(WarningVm model) {
        StringBuffer stringBuffer = new StringBuffer();

        LocalDateTime day = LocalDateTime.now();
        day = day.minusDays(1);

        LocalDateTime week = LocalDateTime.now();
        week = week.minusDays(1 + 7);

        LocalDateTime month = LocalDateTime.now();
        month = month.minusDays(1).minusMonths(1);

        LocalDateTime quarter = LocalDateTime.now();
        quarter = quarter.minusDays(1).minusMonths(3);

        LocalDateTime halfYear = LocalDateTime.now();
        halfYear = halfYear.minusDays(1).minusMonths(6);

        LocalDateTime year = LocalDateTime.now();
        year = year.minusDays(1).minusYears(1);

        stringBuffer.append(" from ( ");
        stringBuffer.append(getJoinSql() + " where update_cyc = '02' and deleted = 0 and (update_t < '" + Timestamp.valueOf(day) + "' or update_t is null) UNION ");
        stringBuffer.append(getJoinSql() + " where update_cyc = '03' and deleted = 0 and (update_t < '" + Timestamp.valueOf(week) + "' or update_t is null) UNION ");
        stringBuffer.append(getJoinSql() + " where update_cyc = '04' and deleted = 0 and (update_t < '" + Timestamp.valueOf(month) + "' or update_t is null) UNION ");
        stringBuffer.append(getJoinSql() + " where update_cyc = '05' and deleted = 0 and (update_t < '" + Timestamp.valueOf(quarter) + "' or update_t is null) UNION ");
        stringBuffer.append(getJoinSql() + " where update_cyc = '06' and deleted = 0 and (update_t < '" + Timestamp.valueOf(halfYear) + "' or update_t is null) UNION ");
        stringBuffer.append(getJoinSql() + " where update_cyc = '07' and deleted = 0 and (update_t < '" + Timestamp.valueOf(year) + "' or update_t is null) ");
        stringBuffer.append(" ) as a left join d1_organization c on c.id=a.prov_org_id ");
        stringBuffer.append(" where a.audit_status = '5'");
        if (!StringUtils.isEmpty(model.getOrgId())) {
            stringBuffer.append(" and a.prov_org_id ='" + model.getOrgId()+"'");
        }
        if (!StringUtils.isEmpty(model.getUviewNo())) {
            stringBuffer.append(" and a.uview_no like CONCAT(CONCAT('%','" + model.getUviewNo() + "','%'))");
        }
        if (!StringUtils.isEmpty(model.getUviewNm())) {
            stringBuffer.append(" and a.uview_nm like CONCAT(CONCAT('%','" + model.getUviewNm() + "','%'))");
        }
        if (!StringUtils.isEmpty(model.getUpdateCyc())) {
            stringBuffer.append(" and a.update_cyc ='" + model.getUpdateCyc()+"'");
        }
        return stringBuffer.toString();
    }

    private String getJoinSql() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select * from (select a.*,f.create_t update_t from d1_arch_busi_uview_ex as a " +
                "left join (" +
                "select uview_id,max(create_time) create_t from d1_asset_file_ex GROUP BY uview_id)as f " +
                "on a.uview_id = f.uview_id ) t ");
        return stringBuffer.toString();
    }

    private Pageable getPageable(Sort sort, Integer page, Integer size) {
        page = page != null ? page : 1;
        size = size != null ? size : 10;
        page = page != null && page >= 1 ? page - 1 : 0;
        if (size == null || size < 1) {
            size = 10;
        }

        return sort != null ? PageRequest.of(page, size, sort) : PageRequest.of(page, size);
    }
}
