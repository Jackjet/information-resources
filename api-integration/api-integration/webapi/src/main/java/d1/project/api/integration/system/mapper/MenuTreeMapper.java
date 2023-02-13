package d1.project.api.integration.system.mapper;

import d1.project.api.integration.system.entity.MenuTreeEntity;
import d1.project.api.integration.system.model.MenuTreeTree;
import d1.project.api.integration.system.model.vm.MenuTreeInsertVm;
import d1.project.api.integration.system.model.vm.MenuTreeUpdateVm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-10 14:41
 */
@Mapper
public interface MenuTreeMapper {

    @Mapping(target = "parentName", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "seq", ignore = true)
    @Mapping(target = "levelMsg", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "idLink", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    @Mapping(target = "hasSystem", ignore = true)
    MenuTreeEntity dtoFormatIntoInsertEntity(MenuTreeInsertVm insertVm);

    @Mapping(target = "children", ignore = true)
    MenuTreeTree entityFormatIntoDtoTree(MenuTreeEntity menuTree);

    @Mapping(target = "parentName", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "seq", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    @Mapping(target = "levelMsg", ignore = true)
    @Mapping(target = "level", ignore = true)
    @Mapping(target = "idLink", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    @Mapping(target = "hasSystem", ignore = true)
    void copyProperties(MenuTreeUpdateVm source, @MappingTarget MenuTreeEntity target);
}
