package d1.project.dcrun.center.webapi.resource.mapper;

import d1.project.dcrun.center.webapi.resource.entity.ContainerInfo;
import d1.project.dcrun.center.webapi.resource.entity.DataSourceInfo;
import d1.project.dcrun.center.webapi.resource.model.ContainerInsertVm;
import d1.project.dcrun.center.webapi.resource.model.ContainerUpdateVm;
import d1.project.dcrun.center.webapi.resource.model.DataSourceInsertVm;
import d1.project.dcrun.center.webapi.resource.model.DataSourceUpdateVm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/**
 * 资源管理映射
 *
 * @author missoul
 * @date 2020-09-08 14:28
 */
@Mapper
public interface ResourceMapper {
    /**
     * 数据源数据添加转换实体
     *
     * @param source 数据源添加数据
     * @param target 数据源数据
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateName", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    @Mapping(target = "createName", ignore = true)
    void dataSourceInsert(DataSourceInsertVm source, @MappingTarget DataSourceInfo target);

    /**
     * 数据源数据添加转换实体
     *
     * @param source 数据源添加数据
     * @param target 数据源数据
     */
    @Mapping(target = "updateName", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    @Mapping(target = "createName", ignore = true)
    void dataSourceUpdate(DataSourceUpdateVm source, @MappingTarget DataSourceInfo target);

    /**
     * 数据源数据添加转换实体
     *
     * @param source 数据源添加数据
     * @param target 数据源数据
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "updateName", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    @Mapping(target = "createName", ignore = true)
    void containerInsert(ContainerInsertVm source, @MappingTarget ContainerInfo target);

    /**
     * 数据源数据添加转换实体
     *
     * @param source 数据源添加数据
     * @param target 数据源数据
     */
    @Mapping(target = "updateName", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    @Mapping(target = "createName", ignore = true)
    void containerUpdate(ContainerUpdateVm source, @MappingTarget ContainerInfo target);

}
