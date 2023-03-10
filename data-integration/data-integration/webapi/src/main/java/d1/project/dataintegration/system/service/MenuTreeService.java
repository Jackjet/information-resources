package d1.project.dataintegration.system.service;

import com.alibaba.fastjson.JSON;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.entity.BaseEntity;
import d1.framework.webapi.utils.TokenManager;
import d1.project.dataintegration.common.Constants;
import d1.project.dataintegration.common.model.OperationLog;
import d1.project.dataintegration.common.service.IOperationLogService;
import d1.project.dataintegration.common.utils.BaseUtils;
import d1.project.dataintegration.system.dao.MenuTreeDao;
import d1.project.dataintegration.system.dao.RoleMenuTreeDao;
import d1.project.dataintegration.system.entity.MenuTreeEntity;
import d1.project.dataintegration.system.mapper.MenuTreeMapper;
import d1.project.dataintegration.system.model.MenuTreeTree;
import d1.project.dataintegration.system.model.vm.MenuTreeInsertVm;
import d1.project.dataintegration.system.model.vm.MenuTreeUpdateSeqVm;
import d1.project.dataintegration.system.model.vm.MenuTreeUpdateVm;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-10 14:40
 */
@Service
public class MenuTreeService {

    private final MenuTreeDao menuTreeDao;
    private final RoleMenuTreeDao roleMenuTreeDao;
    private final IOperationLogService iOperationLogService;

    private final MenuTreeMapper mapper;

    public MenuTreeService(MenuTreeDao menuTreeDao, RoleMenuTreeDao roleMenuTreeDao, IOperationLogService iOperationLogService) {
        this.menuTreeDao = menuTreeDao;
        this.mapper = Mappers.getMapper(MenuTreeMapper.class);
        this.roleMenuTreeDao = roleMenuTreeDao;
        this.iOperationLogService = iOperationLogService;
    }


    /**
     * ??????
     *
     * @param model ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(MenuTreeInsertVm model, HttpServletRequest request) throws DoValidException {
        MenuTreeEntity menuTree = mapper.dtoFormatIntoInsertEntity(model);
        TokenManager.getInstance().updateCreateIdAndTime(request, menuTree);
        menuTree.setId(BaseUtils.generate32Id());
        String parentId =StringUtils.isEmpty(menuTree.getParentId()) ? "" : menuTree.getParentId();
        menuTree.setParentId(parentId);
        /*?????????0??????*/
        menuTree.setLevel(0);
        /*??????????????????????????????*/
        menuTree.setLevelMsg(BaseUtils.toHanStr(1) + "???");
        menuTree.setIdLink(menuTree.getId());

        MenuTreeEntity menuTree1 = menuTreeDao.findFirstByParentIdOrderBySeqDesc(parentId).orElse(null);
        menuTree.setSeq(0);
        if (menuTree1 != null) {
            menuTree.setSeq(menuTree1.getSeq() + 1);
        }

        if (!StringUtils.isEmpty(parentId)) {
            MenuTreeEntity parentIdMenu = menuTreeDao.findById(parentId).orElseThrow(() -> new DoValidException("?????????????????????"));
            int level = parentIdMenu.getLevel() + 1;
            /*?????????0??????*/
            menuTree.setLevel(level);
            /*??????????????????????????????*/
            menuTree.setLevelMsg(BaseUtils.toHanStr(level + 1) + "???");
            menuTree.setParentName(parentIdMenu.getName());
            menuTree.setIdLink(parentIdMenu.getIdLink() + "_" + menuTree.getId());
        } else {
            /*0 ?????????1 ?????????2 ??????*/
            if (model.getType() != 0) {
                throw new DoValidException("????????????????????????????????????????????????");
            }

        }

        if (menuTreeDao.existsByNameAndParentId(menuTree.getName(), parentId)) {
            throw new DoValidException("??????????????????????????????????????????????????????");
        }
        menuTree.setHasSystem("0");
        menuTreeDao.save(menuTree);
        iOperationLogService.insert(new OperationLog("????????????_????????????", "??????", "????????????" + menuTree.getName(), JSON.toJSONString(menuTree), 1), request);
    }

    /**
     * ??????
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id, HttpServletRequest request) throws DoValidException {
        if (Constants.CANNOT_DELETED_MENU_TREE.contains(id)) {
            throw new DoValidException("???????????????????????????");
        }
        MenuTreeEntity menuTree = menuTreeDao.findById(id).orElseThrow(() -> new DoValidException("???????????????"));
        //???????????????????????????????????????
        List<MenuTreeEntity> subTrees = menuTreeDao.findByParentId(menuTree.getId());
        if (subTrees != null && subTrees.size() > 0) {
            throw new DoValidException("????????????????????????????????????");
        }
        menuTreeDao.deleteById(menuTree.getId());
        roleMenuTreeDao.deleteByMenuTreeId(menuTree.getId());
        List<MenuTreeEntity> byParentIdOrderBySeqAsc = menuTreeDao.findByParentIdOrderBySeqAsc(menuTree.getParentId());
        for (int i = 0; i < byParentIdOrderBySeqAsc.size(); i++) {
            byParentIdOrderBySeqAsc.get(i).setSeq(i);
        }
        menuTreeDao.saveAll(byParentIdOrderBySeqAsc);

        iOperationLogService.insert(new OperationLog("????????????_????????????", "??????", "????????????" + menuTree.getName(), JSON.toJSONString(menuTree), 1), request);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Collection<String> ids, HttpServletRequest request) throws DoValidException {
        for (String id : ids) {
            delete(id, request);
        }
    }

    /**
     * ??????
     *
     * @param model ????????????
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(MenuTreeUpdateVm model, HttpServletRequest request) throws DoValidException {
        String id = model.getId();
        if (Constants.CANNOT_DELETED_ORGANIZATION.equals(id)) {
            throw new DoValidException("???????????????????????????");
        }
        MenuTreeEntity menuTree = menuTreeDao.findById(id).orElseThrow(() -> new DoValidException("???????????????"));
        String oldName = menuTree.getName();
        String newName = model.getName();
        String parentId = menuTree.getParentId();
        String parentId1 = StringUtils.isEmpty(parentId) ? "" : parentId;
        if (menuTreeDao.existsByNameAndParentIdAndIdNot(model.getName(), parentId1, menuTree.getId())) {
            throw new DoValidException("??????????????????????????????????????????????????????");
        }

        if (StringUtils.isEmpty(parentId1)) {
            /*0?????????1?????????2????????????3?????????4??????????????????*/
            if (model.getType() != 0) {
                throw new DoValidException("????????????????????????????????????????????????");
            }
        }

        mapper.copyProperties(model, menuTree);
        TokenManager.getInstance().updateUpdateIdAndTime(request, menuTree);

        menuTreeDao.save(menuTree);
        if (!newName.equals(oldName)) {
            List<MenuTreeEntity> collect = menuTreeDao.findByParentId(id).stream().peek(menuTreeEntity -> menuTreeEntity.setParentName(newName)).collect(Collectors.toList());
            menuTreeDao.saveAll(collect);
        }

        iOperationLogService.insert(new OperationLog("????????????_????????????", "??????", "????????????" + menuTree.getName(), JSON.toJSONString(menuTree), 1), request);
    }


    /**
     * ????????????
     *
     * @param model ??????
     */
    public void updateSeq(MenuTreeUpdateSeqVm model, HttpServletRequest request) throws DoValidException {
        MenuTreeEntity menuTree = menuTreeDao.findById(model.getId()).orElseThrow(() -> new DoValidException("???????????????"));
        int seq = menuTree.getSeq();
        int i;
        /*??????:0->??????->1??????*/
        String action;
        String parentId = menuTree.getParentId();
        parentId = StringUtils.isEmpty(parentId) ? "" : menuTree.getParentId();
        if (model.getAction() == 0) {
            i = seq - 1;
            if (i < 0) {
                return;
            }

            MenuTreeEntity byParentIdAndSeq = menuTreeDao.findByParentIdAndSeq(parentId, i).orElse(null);
            if (byParentIdAndSeq == null) {
                return;
            }
            byParentIdAndSeq.setSeq(byParentIdAndSeq.getSeq() + 1);
            menuTreeDao.save(byParentIdAndSeq);
            action = "??????";
        } else {
            i = seq + 1;
            MenuTreeEntity byParentIdAndSeq = menuTreeDao.findByParentIdAndSeq(parentId, i).orElse(null);
            if (byParentIdAndSeq == null) {
                return;
            }
            byParentIdAndSeq.setSeq(byParentIdAndSeq.getSeq() - 1);
            menuTreeDao.save(byParentIdAndSeq);
            action = "??????";
        }

        menuTree.setSeq(i);
        menuTreeDao.save(menuTree);

        iOperationLogService.insert(new OperationLog("????????????_????????????", "????????????", "????????????" + menuTree.getName() + action, JSON.toJSONString(menuTree), 1), request);
    }

    /**
     * ????????????
     */
    public List<MenuTreeTree> findAllFormatIntoVm() {
        List<MenuTreeEntity> seq = menuTreeDao.findAll(Sort.by(Sort.Direction.ASC, "seq"));
        return getMenuTreeTreeVm(seq, false);
    }

    /**
     * ????????????
     */
    public List<MenuTreeEntity> findAll() {
        return menuTreeDao.findAll(Sort.by(Sort.Direction.ASC, "seq"));
    }

    public List<String> findAllAsLongAsId() {
        return menuTreeDao.findAll(Sort.by(Sort.Direction.ASC, "seq")).stream().filter(menuTreeEntity -> !StringUtils.isEmpty(menuTreeEntity.getParentId())).map(BaseEntity::getId).collect(Collectors.toList());
    }

    /**
     * ?????????????????????????????????ID
     */
    public List<String> findAllNoChildrenAsLongAsId(List<String> collect) {
        List<String> menuTreeIds = new ArrayList<>();
        List<MenuTreeTree> allByIdIn = findAllByIdIn(collect, false);
        recursionNoChildrenTheAsLongAsId(menuTreeIds, allByIdIn);
        return menuTreeIds;
    }

    /**
     * ????????????????????????????????????ID
     */
    private void recursionNoChildrenTheAsLongAsId(List<String> menuTreeIds, List<MenuTreeTree> allByIdIn) {
        for (MenuTreeTree menuTreeTree : allByIdIn) {
            List<MenuTreeTree> children = menuTreeTree.getChildren();
            if (children.isEmpty()) {
                menuTreeIds.add(menuTreeTree.getId());
            } else {
                recursionNoChildrenTheAsLongAsId(menuTreeIds, children);
            }

        }
    }


    /**
     * ??????id
     */
    public MenuTreeTree find(String id) {
        List<MenuTreeEntity> byLevelAsc = menuTreeDao.findByIdLinkLikeOrderBySeqAsc(id);
        return getOrganizationTreeVm(byLevelAsc);
    }

    /**
     * ????????????id?????????????????????
     *
     * @param id        id??????
     * @param isSidebar ??????????????????
     */
    public List<MenuTreeTree> findAllByIdIn(Collection<String> id, boolean isSidebar) {
        List<MenuTreeEntity> seq;
        if (isSidebar) {
            seq = menuTreeDao.findAllByIdInAndTypeInOrderBySeqAsc(id, Arrays.asList(0, 1));
        } else {
            seq = menuTreeDao.findAllByIdInOrderBySeqAsc(id);
        }
        return getMenuTreeTreeVm(seq, isSidebar);
    }

    public List<MenuTreeEntity> findAllByIdInOrderBySeqAsc(Collection<String> id) {
        return menuTreeDao.findAllByIdInOrderBySeqAsc(id);
    }


    /**
     * ?????????????????????????????????
     *
     * @param byLevelAsc ??????????????????
     */
    private MenuTreeTree getOrganizationTreeVm(List<MenuTreeEntity> byLevelAsc) {
        MenuTreeTree menuTreeTreeVm = new MenuTreeTree();
        for (MenuTreeEntity treeTreeVm : byLevelAsc) {
            if (StringUtils.isEmpty(treeTreeVm.getParentId())) {
                menuTreeTreeVm = mapper.entityFormatIntoDtoTree(treeTreeVm);
                recursion(byLevelAsc, menuTreeTreeVm, false);
            }
        }
        return menuTreeTreeVm;
    }

    /**
     * ???????????????????????????????????????
     *
     * @param byLevelAsc ???????????????
     */
    private List<MenuTreeTree> getMenuTreeTreeVm(List<MenuTreeEntity> byLevelAsc, boolean isSidebar) {
        List<MenuTreeTree> menuTreeTreeVms = new ArrayList<>();
        for (MenuTreeEntity menuTree : byLevelAsc) {
            if (StringUtils.isEmpty(menuTree.getParentId())) {
                MenuTreeTree menuTreeTreeVm = mapper.entityFormatIntoDtoTree(menuTree);
//                menuTreeTreeVm.setRedirect(menuTreeTreeVm.getPath());
                recursion(byLevelAsc, menuTreeTreeVm, isSidebar);
                menuTreeTreeVms.add(menuTreeTreeVm);
            }
        }
        return menuTreeTreeVms;
    }

    /**
     * ??????
     *
     * @param menuTreeList   ????????????
     * @param menuTreeTreeVm ?????????
     */
    private void recursion(List<MenuTreeEntity> menuTreeList, MenuTreeTree menuTreeTreeVm, boolean isSidebar) {

        for (MenuTreeEntity organization1 : menuTreeList) {
            if (menuTreeTreeVm.getId().equals(organization1.getParentId())) {
                List<MenuTreeTree> children = menuTreeTreeVm.getChildren();
                MenuTreeTree organizationTreeVm2 = mapper.entityFormatIntoDtoTree(organization1);
                children.add(organizationTreeVm2);
                recursion(menuTreeList, organizationTreeVm2, isSidebar);
            }
        }
    }


}
