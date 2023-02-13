package d1.project.resourcesharingmgmt.resource.dao;

import d1.project.resourcesharingmgmt.resource.model.view.MyFocusInfoView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 信息资源目录视图
 *
 * @author zhengyang
 */
public interface MyFocusInfoViewDao extends JpaRepository<MyFocusInfoView, String>, JpaSpecificationExecutor<MyFocusInfoView> {

}
