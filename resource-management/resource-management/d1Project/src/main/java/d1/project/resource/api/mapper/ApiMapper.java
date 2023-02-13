package d1.project.resource.api.mapper;

import d1.project.resource.api.entity.SourceApi;
import d1.project.resource.api.model.DesignInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * 资源管理映射
 *
 * @author missoul
 * @date 2020-09-08 14:28
 */
@Mapper
public interface ApiMapper {

    /**
     * 数据源数据添加转换实体
     *
     * @param source 数据源添加数据
     * @return API数据
     */
    @Mapping(target = "host", ignore = true)
    @Mapping(target = "loadBalancing", ignore = true)
    @Mapping(target = "groupId", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    SourceApi dataSourceInsert(DesignInfo source);
}
