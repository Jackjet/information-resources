package d1.project.dcrun.center.webapi.log.mapper;

import d1.project.dcrun.center.webapi.common.model.OperationLog;
import d1.project.dcrun.center.webapi.log.entity.OperationLogEntity;
import d1.project.dcrun.center.webapi.log.model.OperationLogExcel;
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
public interface OperationLogMapper {


    @Mapping(target = "type", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createByPhone", ignore = true)
    @Mapping(target = "createByName", ignore = true)
    @Mapping(target = "createById", ignore = true)
    @Mapping(target = "createByAccount", ignore = true)
    OperationLogEntity dtoFormatIntoRequestInsertEntity(OperationLog insertVm);

    List<OperationLogExcel> entityListFormatIntoExcelDtoList(List<OperationLogEntity> insertVm);
}
