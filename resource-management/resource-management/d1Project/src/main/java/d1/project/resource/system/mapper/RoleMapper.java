package d1.project.resource.system.mapper;

import d1.project.resource.system.entity.RoleEntity;
import d1.project.resource.system.model.vm.RoleInsertVm;
import d1.project.resource.system.model.vm.RoleUpdateVm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 14:28
 */
@Mapper
public interface RoleMapper {


    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    RoleEntity dtoFormatIntoInsertEntity(RoleInsertVm insertVm);

//    @Mapping(target = "updateTime", ignore = true)
//    @Mapping(target = "updateById", ignore = true)
//    @Mapping(target = "createTime", ignore = true)
//    @Mapping(target = "createById", ignore = true)
//    Role dtoFormatIntoUpdateEntity(RoleUpdateVm insertVm);

//    WebAdminUser dtoFormatIntoUpdateEntity(WebAdminUserUpdateVm userUpdateVm);
//

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    void copyProperties(RoleUpdateVm source, @MappingTarget RoleEntity target);
}
