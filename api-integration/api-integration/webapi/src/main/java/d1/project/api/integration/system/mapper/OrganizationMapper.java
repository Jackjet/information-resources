package d1.project.api.integration.system.mapper;

import d1.project.api.integration.system.entity.OrganizationEntity;
import d1.project.api.integration.system.model.OrganizationTree;
import d1.project.api.integration.system.model.vm.OrganizationInsertVm;
import d1.project.api.integration.system.model.vm.OrganizationUpdateVm;
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
public interface OrganizationMapper {

    @Mapping(target = "nameLink", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "seq", ignore = true)
    @Mapping(target = "levelMsg", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "idLink", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    OrganizationEntity dtoFormatIntoInsertEntity(OrganizationInsertVm insertVm);

    @Mapping(target = "children", ignore = true)
    OrganizationTree entityFormatIntoDtoTree(OrganizationEntity organization);

    @Mapping(target = "nameLink", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "seq", ignore = true)
    @Mapping(target = "parentName", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "levelMsg", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "idLink", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    void copyProperties(OrganizationUpdateVm source, @MappingTarget OrganizationEntity target);
}
