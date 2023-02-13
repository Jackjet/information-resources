package d1.project.d1project.log.mapper;

import d1.project.d1project.log.entity.UserLogEntity;
import d1.project.d1project.log.model.UserLogExcel;
import d1.project.d1project.log.model.vm.UserLogRequestInsertVm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 14:28
 */
@Mapper
public interface UserLogMapper {

    @Mapping(target = "type", ignore = true)
    @Mapping(target = "sourceIp", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createByPhone", ignore = true)
    @Mapping(target = "createByName", ignore = true)
    @Mapping(target = "createById", ignore = true)
    @Mapping(target = "createByAccount", ignore = true)
    UserLogEntity dtoFormatIntoRequestInsertEntity(UserLogRequestInsertVm insertVm);

    List<UserLogExcel> entityListFormatIntoExcelDtoList(List<UserLogEntity> insertVm);
}
