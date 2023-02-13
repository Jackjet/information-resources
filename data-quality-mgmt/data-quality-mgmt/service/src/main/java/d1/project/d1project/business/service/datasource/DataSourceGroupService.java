package d1.project.d1project.business.service.datasource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.business.dao.DataSourceDao;
import d1.project.d1project.business.dao.DataSourceGroupDao;
import d1.project.d1project.business.entity.DataSourceGroup;
import d1.project.d1project.business.model.datasource.group.DataSourceManageGroupInsertPostVm;
import d1.project.d1project.business.model.datasource.group.DataSourceManageGroupUpdatePutVm;
import d1.project.d1project.common.utils.BaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author libin
 */
@Service
public class DataSourceGroupService {
    private final DataSourceGroupDao dataSourceGroupDao;
    private final DataSourceDao dataSourceDao;

    public DataSourceGroupService(DataSourceGroupDao dataSourceGroupDao, DataSourceDao dataSourceDao) {
        this.dataSourceGroupDao = dataSourceGroupDao;
        this.dataSourceDao = dataSourceDao;
    }

    public JSONArray getDataSourceGroup(){
        List<DataSourceGroup> dataSourceGroups = dataSourceGroupDao.findAll();
        Map<String,List<DataSourceGroup>> maps = dataSourceGroups.stream().collect(Collectors.groupingBy(DataSourceGroup::getParentId));
        return generateForest(maps);
    }

    public List<DataSourceGroup> findAll(){
        return dataSourceGroupDao.findAll();
    }

    public void insert(DataSourceManageGroupInsertPostVm params) throws DoValidException {
        DataSourceGroup dataSourceGroup = new DataSourceGroup();
        BeanUtils.copyProperties(params,dataSourceGroup);

        if(dataSourceGroupDao.existsByName(dataSourceGroup.getName())){
            throw new DoValidException("已存在同名组");
        }

        dataSourceGroup.setId(BaseUtils.generate32Id());

        if(StringUtils.isEmpty(dataSourceGroup.getParentId())){
            dataSourceGroup.setParentId("0");
        }
        dataSourceGroupDao.save(dataSourceGroup);
    }

    public void update(DataSourceManageGroupUpdatePutVm params) throws DoValidException {
        DataSourceGroup dataSourceGroup = new DataSourceGroup();
        BeanUtils.copyProperties(params,dataSourceGroup);

        if(dataSourceGroupDao.existsByNameAndIdNot(dataSourceGroup.getName(),dataSourceGroup.getId())){
            throw new DoValidException("已存在同名组");
        }

        DataSourceGroup data = dataSourceGroupDao.findById(dataSourceGroup.getId()).orElseThrow(() -> new DoValidException("组信息不存在"));
        data.setName(dataSourceGroup.getName());

        dataSourceGroupDao.save(data);
    }

    public void delete(String id) throws DoValidException {
        if(dataSourceGroupDao.existsByParentId(id)){
            throw new DoValidException("节点下存在子节点，请先删除子节点");
        }

        if(dataSourceDao.existsByGroupId(id)){
            throw new DoValidException("节点下存在数据源，请将数据源删除或转移至其它分组");
        }

        dataSourceGroupDao.deleteById(id);
    }

    //================================================================
    private JSONArray generateForest(Map<String,List<DataSourceGroup>> maps){
        JSONArray forest = generateRoots(maps);
        return generateChildren(maps,forest);
    }

    private JSONArray generateRoots(Map<String,List<DataSourceGroup>> maps){
        List<DataSourceGroup> roots = maps.get("0");
        if(roots == null) {
            return new JSONArray();
        }
        return  (JSONArray) JSON.toJSON(roots);
    }

    private JSONArray generateChildren(Map<String,List<DataSourceGroup>> maps,JSONArray forest){
        for(Object obj : forest){
            JSONObject node = (JSONObject) obj;
            List<DataSourceGroup> children = maps.get(node.get("id"));
            if(children != null){
                node.put("children", generateChildren(maps,(JSONArray)JSON.toJSON(children)));
            } else {
                node.put("children",new JSONArray());
            }
        }

        return forest;
    }
}
