package d1.project.resource.group.mapper;

import d1.project.resource.group.entity.GroupInfo;
import d1.project.resource.group.model.GroupVm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * @author baozh
 */
@Mapper
public interface GroupMapper {

    /**
     * 标签添加转换实体
     *
     * @param source 标签添加数据
     * @param target 标签数据
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    void groupInsert(GroupVm source, @MappingTarget GroupInfo target);


    /**
     * 标签编辑转换实体
     *
     * @param source 标签编辑数据
     * @param target 标签数据
     */
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    @Mapping(target = "parentId", ignore = true)
    void groupUpdate(GroupVm source, @MappingTarget GroupInfo target);
}
