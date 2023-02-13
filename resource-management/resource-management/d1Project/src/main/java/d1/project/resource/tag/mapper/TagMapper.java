package d1.project.resource.tag.mapper;

import d1.project.resource.tag.entity.TagInfo;
import d1.project.resource.tag.model.TagVm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author baozh
 */
@Mapper
public interface TagMapper {

    /**
     * 标签添加转换实体
     *
     * @param source 标签添加数据
     * @param target 标签数据
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateName", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    @Mapping(target = "createName", ignore = true)
    void tagInsert(TagVm source, @MappingTarget TagInfo target);


    /**
     * 标签编辑转换实体
     *
     * @param source 标签编辑数据
     * @param target 标签数据
     */
    @Mapping(target = "updateName", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    @Mapping(target = "createName", ignore = true)
    void tagUpdate(TagVm source, @MappingTarget TagInfo target);
}
