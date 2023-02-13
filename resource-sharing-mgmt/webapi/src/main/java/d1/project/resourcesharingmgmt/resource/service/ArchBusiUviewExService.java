package d1.project.resourcesharingmgmt.resource.service;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.resourcesharingmgmt.resource.dao.ArchBusiUviewExDao;
import d1.project.resourcesharingmgmt.resource.dao.AssetApiExDao;
import d1.project.resourcesharingmgmt.resource.dao.AssetDataExDao;
import d1.project.resourcesharingmgmt.resource.dao.AssetFileExDao;
import d1.project.resourcesharingmgmt.resource.entity.ArchBusiUviewExEntity;
import d1.project.resourcesharingmgmt.resource.model.ArchBusiUviewEx.ArchBusiUviewExFindAllVm;
import d1.project.resourcesharingmgmt.resource.model.Screen.OrgUseCount;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 * 信息资源目录
 *
 * @author zhengyang
 */
@Service
public class ArchBusiUviewExService {
    private final ArchBusiUviewExDao archBusiUviewExDao;
    private final AssetApiExDao assetApiExDao;
    private final AssetFileExDao assetFileExDao;
    private final AssetDataExDao assetDataExDao;

    public ArchBusiUviewExService(ArchBusiUviewExDao archBusiUviewExDao, AssetApiExDao assetApiExDao, AssetFileExDao assetFileExDao, AssetDataExDao assetDataExDao) {
        this.archBusiUviewExDao = archBusiUviewExDao;
        this.assetApiExDao = assetApiExDao;
        this.assetFileExDao = assetFileExDao;
        this.assetDataExDao = assetDataExDao;
    }

    public String findAllSql(ArchBusiUviewExFindAllVm model, String userId, List<String> orgs) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select a.* ");
        if (!StringUtils.isEmpty(userId)) {
            stringBuffer.append(",CASE WHEN b.uview_id is Null THEN 0 ELSE 1 END isFocus ");
        }
        stringBuffer.append("from d1_arch_busi_uview_ex a ");
        if (!StringUtils.isEmpty(userId)) {
            stringBuffer.append("LEFT JOIN d1_my_focus_info b on a.uview_id=b.uview_id and b.create_by_id='" + userId + "' ");
        }
        stringBuffer.append("where 1=1 ");
        if (!StringUtils.isEmpty(userId)) {
            stringBuffer.append("and a.status='0' ");
        }
        if (model.getTypId() != null && !StringUtils.isEmpty(model.getTypId())) {
            stringBuffer.append("and a.uview_id in(select DISTINCT info_id from d1_asset_cate_ex b where b.typ_id in(with RECURSIVE r as ");
            stringBuffer.append("( select t1.typ_id from d1_dict_asset_cate t1 where t1.typ_id = '" + model.getTypId() + "' union all ");
            stringBuffer.append("select t2.typ_id from d1_dict_asset_cate t2 inner join r on r.typ_id = t2.par_typ_id) ");
            stringBuffer.append("select * from r)) ");
        }
        if (!StringUtils.isEmpty(model.getUviewNo())) {
            stringBuffer.append("and a.uview_no like '%" + model.getUviewNo() + "%' ");
        }
        if (!StringUtils.isEmpty(model.getUviewNm())) {
            stringBuffer.append("and a.uview_nm like '%" + model.getUviewNm().replace(" ", "%") + "%' ");
        }
        if (!StringUtils.isEmpty(model.getShareLv())) {
            stringBuffer.append("and a.share_lv ='" + model.getShareLv() + "' ");
        }
        if (!StringUtils.isEmpty(model.getStatus())) {
            stringBuffer.append("and a.status ='" + model.getStatus() + "' ");
        }
        if (!StringUtils.isEmpty(model.getIsHook())) {
            //0未挂接，表示所有都未挂接，1已挂接表示有一种挂接就行
            if ("0".equals(model.getIsHook())) {
                if (StringUtils.isEmpty(model.getHookType())) {
                    stringBuffer.append("and a.hook_status ='0' and a.file_hook_status ='0' and a.data_hook_status ='0' ");
                } else {
                    if ("1".equals(model.getHookType())) {
                        stringBuffer.append("and a.hook_status ='0' ");
                    }
                    if ("2".equals(model.getHookType())) {
                        stringBuffer.append("and a.file_hook_status ='0' ");
                    }
                    if ("3".equals(model.getHookType())) {
                        stringBuffer.append("and a.data_hook_status ='0' ");
                    }
                }
            } else {
                if (StringUtils.isEmpty(model.getHookType())) {
                    stringBuffer.append("and (a.hook_status ='1' or a.file_hook_status ='1' or a.data_hook_status ='1') ");
                } else {
                    if ("1".equals(model.getHookType())) {
                        stringBuffer.append("and a.hook_status ='1' ");
                    }
                    if ("2".equals(model.getHookType())) {
                        stringBuffer.append("and a.file_hook_status ='1' ");
                    }
                    if ("3".equals(model.getHookType())) {
                        stringBuffer.append("and a.data_hook_status ='1' ");
                    }
                }
            }
        }
        if (orgs.size() > 0) {
            stringBuffer.append(" and a.prov_org_id in ('");
            String orgStr = String.join(",", orgs);
            orgStr = orgStr.replace(",", "','");
            stringBuffer.append(orgStr);
            stringBuffer.append("') ");
        }
        if (model.getOrder() == null || StringUtils.isEmpty(model.getOrder())) {
            stringBuffer.append("order by a.update_time desc");
        } else {
            stringBuffer.append("order by a." + model.getOrder());
            if ("0".equals(model.getDesc())) {
                stringBuffer.append(" asc ");
            } else {
                stringBuffer.append(" desc ");
            }
        }

        stringBuffer.append(" limit " + model.getSize() + " OFFSET " + ((model.getPage() - 1) * model.getSize()));
        return stringBuffer.toString();
    }

    public String findAllCountSql(ArchBusiUviewExFindAllVm model, String userId, List<String> orgs) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("select count(*) from d1_arch_busi_uview_ex a ");
        if (!StringUtils.isEmpty(userId)) {
            stringBuffer.append("LEFT JOIN d1_my_focus_info b on a.uview_id=b.uview_id and b.create_by_id='" + userId + "' ");
        }
        stringBuffer.append("where 1=1 ");
        if (!StringUtils.isEmpty(userId)) {
            stringBuffer.append("and a.status='0' ");
        }
        if (model.getTypId() != null && !StringUtils.isEmpty(model.getTypId())) {
            stringBuffer.append("and a.uview_id in(select DISTINCT info_id from d1_asset_cate_ex b where b.typ_id in(with RECURSIVE r as ");
            stringBuffer.append("( select t1.typ_id from d1_dict_asset_cate t1 where t1.typ_id = '" + model.getTypId() + "' union all ");
            stringBuffer.append("select t2.typ_id from d1_dict_asset_cate t2 inner join r on r.typ_id = t2.par_typ_id) ");
            stringBuffer.append("select * from r)) ");
        }
        if (!StringUtils.isEmpty(model.getUviewNo())) {
            stringBuffer.append("and a.uview_no like '%" + model.getUviewNo() + "%' ");
        }
        if (!StringUtils.isEmpty(model.getUviewNm())) {
            stringBuffer.append("and a.uview_nm like '%" + model.getUviewNm().replace(" ", "%") + "%' ");
        }
        if (!StringUtils.isEmpty(model.getShareLv())) {
            stringBuffer.append("and a.share_lv ='" + model.getShareLv() + "' ");
        }
        if (!StringUtils.isEmpty(model.getStatus())) {
            stringBuffer.append("and a.status ='" + model.getStatus() + "' ");
        }
        if (!StringUtils.isEmpty(model.getIsHook())) {
            //0未挂接，表示所有都未挂接，1已挂接表示有一种挂接就行
            if ("0".equals(model.getIsHook())) {
                if (StringUtils.isEmpty(model.getHookType())) {
                    stringBuffer.append("and a.hook_status ='0' and a.file_hook_status ='0' and a.data_hook_status ='0' ");
                } else {
                    if ("1".equals(model.getHookType())) {
                        stringBuffer.append("and a.hook_status ='0' ");
                    }
                    if ("2".equals(model.getHookType())) {
                        stringBuffer.append("and a.file_hook_status ='0' ");
                    }
                    if ("3".equals(model.getHookType())) {
                        stringBuffer.append("and a.data_hook_status ='0' ");
                    }
                }
            } else {
                if (StringUtils.isEmpty(model.getHookType())) {
                    stringBuffer.append("and (a.hook_status ='1' or a.file_hook_status ='1' or a.data_hook_status ='1') ");
                } else {
                    if ("1".equals(model.getHookType())) {
                        stringBuffer.append("and a.hook_status ='1' ");
                    }
                    if ("2".equals(model.getHookType())) {
                        stringBuffer.append("and a.file_hook_status ='1' ");
                    }
                    if ("3".equals(model.getHookType())) {
                        stringBuffer.append("and a.data_hook_status ='1' ");
                    }
                }
            }
        }
        if (orgs.size() > 0) {
            stringBuffer.append(" and a.prov_org_id in ('");
            String orgStr = String.join(",", orgs);
            orgStr = orgStr.replace(",", "','");
            stringBuffer.append(orgStr);
            stringBuffer.append("') ");
        }
        return stringBuffer.toString();
    }


    /**
     * 详情
     *
     * @param id id
     */
    public Optional<ArchBusiUviewExEntity> find(String id) {
        return archBusiUviewExDao.findById(id);
    }

    /**
     * 根据资源id查询详情
     *
     * @param uviewId
     * @return
     */
    public Optional<ArchBusiUviewExEntity> findByUviewId(String uviewId) {
        return archBusiUviewExDao.findByUviewId(uviewId);
    }

    /**
     * 根据资源编号查询详情
     *
     * @param uviewNo
     * @return
     */
    public Optional<ArchBusiUviewExEntity> findByUviewNo(String uviewNo) {
        return archBusiUviewExDao.findByUviewNo(uviewNo);
    }

    /**
     * 查找最后更新的一条数据
     *
     * @return
     */
    public Optional<ArchBusiUviewExEntity> findFirstByOrderByUpdateTimeDesc() {
        return archBusiUviewExDao.findFirstByOrderByUpdateTimeDesc();
    }

    public void update(ArchBusiUviewExEntity archBusiUviewExEntity) throws DoValidException {
        archBusiUviewExDao.save(archBusiUviewExEntity);
    }

    /**
     * 统计
     *
     * @return
     */
    public long count() {
        return archBusiUviewExDao.count();
    }

    /**
     * 统计
     *
     * @return
     */
    public long countByStatus(String status) {
        return archBusiUviewExDao.countByStatus(status);
    }

    /**
     * 统计媒体格式
     *
     * @return
     */
    public long countByMediaFmt(String mediaFmt) {
        return archBusiUviewExDao.countByMediaFmt(mediaFmt);
    }

    /**
     * 统计更新周期
     *
     * @return
     */
    public long countByUpdateCyc(String updateCyc) {
        return archBusiUviewExDao.countByUpdateCyc(updateCyc);
    }

    /**
     * 统计是否挂接
     *
     * @return
     */
    public long countByHookStatusOrFileHookStatusOrDataHookStatus(String hookStatus, String fileHookStatus, String dataHookStatus) {
        return archBusiUviewExDao.countByHookStatusOrFileHookStatusOrDataHookStatus(hookStatus, fileHookStatus, dataHookStatus);
    }

    /**
     * 根据资源目录id,判断是否存在资源目录
     *
     * @param UviewId
     * @return
     */
    public boolean existsByUviewId(String UviewId) {
        return archBusiUviewExDao.existsByUviewId(UviewId);
    }

    /**
     * 根据资源目录编号,判断是否存在资源目录
     *
     * @param UviewNo
     * @return
     */
    public boolean existsByUviewNo(String UviewNo) {
        return archBusiUviewExDao.existsByUviewNo(UviewNo);
    }

    /**
     * 单条保存资源目录
     *
     * @param exEntity
     */
    public void save(ArchBusiUviewExEntity exEntity) {
        archBusiUviewExDao.save(exEntity);
    }

    /**
     * 批量保存资源目录
     *
     * @param list
     */
    public void insertAll(List<ArchBusiUviewExEntity> list) {
        archBusiUviewExDao.saveAll(list);
    }

    public void deleteById(String id) {
        archBusiUviewExDao.deleteById(id);
        assetApiExDao.deleteByUviewId(id);
        assetFileExDao.deleteByUviewId(id);
        assetDataExDao.deleteByUviewId(id);
    }

    /**
     * 统计挂接数
     *
     * @param orgId orgId
     */
    public long countByOrgId(String orgId) {
        return archBusiUviewExDao.countByProvOrgId(orgId);
    }

    /**
     * 统计机构挂接数
     *
     * @param orgId orgId
     */
    public long countExByOrgId(String orgId) {
        return archBusiUviewExDao.countExByOrgId(orgId);
    }

    /**
     * 统计机构挂接了多少目录
     *
     */
    public List<OrgUseCount> countOrgAndCenterCount() {
        return archBusiUviewExDao.countOrgAndCenterCount();
    }
}
