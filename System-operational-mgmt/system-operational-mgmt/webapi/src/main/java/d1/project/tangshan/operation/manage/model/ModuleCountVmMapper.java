package d1.project.tangshan.operation.manage.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author lin
 */
public class ModuleCountVmMapper implements RowMapper<ModuleCountVm> {
    @Override
    public ModuleCountVm mapRow(ResultSet rs, int rowNum) throws SQLException {
        //从结果集中得到数据
        String date = rs.getString("date");
        Long number = rs.getLong("number");
        String name = rs.getString("name");
        String moduleName = rs.getString("moduleName");
        //把得到的数据封装到对象里面
        ModuleCountVm moduleCountVm = new ModuleCountVm();
        moduleCountVm.setDate(date);
        moduleCountVm.setNumber(number);
        moduleCountVm.setModuleName(moduleName);
        moduleCountVm.setName(name);
        return moduleCountVm;
    }
}
