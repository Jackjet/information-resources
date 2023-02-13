package d1.project.d1project.system.mapper;

import d1.project.d1project.common.utils.MapperFormatStrategy;
import d1.project.d1project.system.entity.WebAdminUserEntity;
import d1.project.d1project.system.model.WebAdminUserExcelExport;
import d1.project.d1project.system.model.WebAdminUserExcelImport;
import d1.project.d1project.system.model.WebAdminUserVm;
import d1.project.d1project.system.model.vm.WebAdminUserInsertVm;
import d1.project.d1project.system.model.vm.WebAdminUserUpdateVm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 14:28
 */
@Mapper(uses = MapperFormatStrategy.class)
public interface WebAdminUserMapper {

    @Mapping(target = "ssoId", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "roleName", ignore = true)
    @Mapping(target = "pinyin", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "organizationName", ignore = true)
    @Mapping(target = "lastSignInTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enable", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    WebAdminUserEntity dtoFormatIntoInsertEntity(WebAdminUserInsertVm insertVm);

    @Mapping(target = "ssoId", ignore = true)
    @Mapping(target = "avatar", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "pinyin", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "lastSignInTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enable", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    WebAdminUserEntity dtoFormatIntoInsertEntity(WebAdminUserExcelImport insertVm);

    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "organizationId", ignore = true)
    WebAdminUserVm entityFormatIntoDto(WebAdminUserEntity insertVm);

    List<WebAdminUserVm> entityListFormatIntoDtoList(List<WebAdminUserEntity> insertVm);

    List<WebAdminUserExcelExport> entityListFormatIntoExcelDtoList(List<WebAdminUserEntity> insertVm);

    WebAdminUserExcelExport entityFormatIntoExcelDto(WebAdminUserEntity insertVm);

    @Mapping(target = "ssoId", ignore = true)
    @Mapping(target = "roleName", ignore = true)
    @Mapping(target = "organizationName", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "pinyin", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "lastSignInTime", ignore = true)
    @Mapping(target = "enable", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    @Mapping(target = "account", ignore = true)
    void copyProperties(WebAdminUserUpdateVm source, @MappingTarget WebAdminUserEntity target);
}
