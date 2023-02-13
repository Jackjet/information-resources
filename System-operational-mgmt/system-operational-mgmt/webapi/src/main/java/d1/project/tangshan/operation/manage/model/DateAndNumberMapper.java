package d1.project.tangshan.operation.manage.model;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DateAndNumberMapper implements RowMapper<DateAndNumber> {
        @Override
        public DateAndNumber mapRow(ResultSet rs, int num) throws SQLException {
            //从结果集中得到数据
            String date = rs.getString("date");
            Long number = rs.getLong("number");
            //把得到的数据封装到对象里面
            DateAndNumber dateAndNumber = new DateAndNumber();
            dateAndNumber.setDate(date);
            dateAndNumber.setNumber(number);
            return dateAndNumber;
        }

}
