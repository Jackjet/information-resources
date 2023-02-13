package d1.project.dcrun.center.webapi.resource.business;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.TokenService;
import d1.project.dcrun.center.webapi.common.Constants;
import d1.project.dcrun.center.webapi.common.model.OperationLog;
import d1.project.dcrun.center.webapi.common.service.IOperationLogService;
import d1.project.dcrun.center.webapi.common.utils.BaseUtils;
import d1.project.dcrun.center.webapi.resource.entity.ContainerInfo;
import d1.project.dcrun.center.webapi.resource.entity.DataSourceInfo;
import d1.project.dcrun.center.webapi.resource.mapper.ResourceMapper;
import d1.project.dcrun.center.webapi.resource.model.ContainerInsertVm;
import d1.project.dcrun.center.webapi.resource.model.ContainerUpdateVm;
import d1.project.dcrun.center.webapi.resource.model.DataSourceInsertVm;
import d1.project.dcrun.center.webapi.resource.model.DataSourceUpdateVm;
import d1.project.dcrun.center.webapi.resource.service.ContainerInfoService;
import d1.project.dcrun.center.webapi.resource.service.DataSourceInfoService;
import d1.project.dcrun.center.webapi.task.dao.TaskDao;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Calendar;
import java.util.Objects;

/**
 * @author baozh
 */
@Service
public class ResourceBusiness {

    private final DataSourceInfoService dataSourceInfoService;
    private final ContainerInfoService containerInfoService;
    private final ResourceMapper mapper;
    private final TokenService tokenService;
    private final IOperationLogService iOperationLogService;
    private final TaskDao taskDao;

    @Value("${d1.project.container.test.url}")
    private String testContainerUrl;

    public ResourceBusiness(DataSourceInfoService dataSourceInfoService, ContainerInfoService containerInfoService, TokenService tokenService, IOperationLogService iOperationLogService, TaskDao taskDao) {
        this.dataSourceInfoService = dataSourceInfoService;
        this.containerInfoService = containerInfoService;
        this.tokenService = tokenService;
        this.iOperationLogService = iOperationLogService;
        this.taskDao = taskDao;
        this.mapper = Mappers.getMapper(ResourceMapper.class);
    }

    /**
     * 数据源列表查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 查询异常
     */
    public Page<DataSourceInfo> findAllDataSource(JSONObject params) throws Exception {
        return dataSourceInfoService.findAll(params);
    }

    /**
     * 数据源详情查询
     *
     * @param id 数据ID
     * @return 查询结果
     * @throws Exception 手动异常处理
     */
    public DataSourceInfo findDataSource(String id) throws Exception {
        DataSourceInfo dataSourceInfo = dataSourceInfoService.findById(id);
        if (dataSourceInfo == null) {
            throw new Exception(Constants.DATA_NULL);
        }
        return dataSourceInfo;
    }

    /**
     * 添加数据源数据
     *
     * @param insertVm 添加实体
     * @param request  请求信息
     * @throws Exception 异常处理
     */
    public void insertDataSource(DataSourceInsertVm insertVm, HttpServletRequest request) throws Exception {
        String name = insertVm.getName();
        String host = insertVm.getHost();
        String port = insertVm.getPort();
        String dataName = insertVm.getDataName();
        if (dataSourceInfoService.existsAllByName(name)) {
            throw new Exception(Constants.DATA_SOURCE_NAME_REPEAT);
        }
        if (dataSourceInfoService.existsAllByHostAndPortAndDataName(host, port, dataName)) {
            throw new Exception(Constants.DATA_SOURCE_JDBC_REPEAT);
        }
        JSONObject userVm = tokenService.getUserByToken(request);
        String userId = userVm.getString("id");
        String userName = userVm.getString("name");
        DataSourceInfo dataSourceInfo = new DataSourceInfo();
        mapper.dataSourceInsert(insertVm, dataSourceInfo);
        dataSourceInfo.setId(BaseUtils.generate32Id());
        dataSourceInfo.setCreateById(userId);
        dataSourceInfo.setUpdateName(userName);
        dataSourceInfo.setCreateName(userName);
        dataSourceInfo.setUpdateById(userId);
        dataSourceInfo.setCreateTime(Calendar.getInstance());
        dataSourceInfo.setUpdateTime(Calendar.getInstance());
        dataSourceInfoService.insert(dataSourceInfo);
        iOperationLogService.insertLog(new OperationLog("数据源管理", "数据源添加", "数据源添加", "添加数据源" + dataSourceInfo.getName(), 1), userVm);
    }


    /**
     * 添加数据源数据
     *
     * @param updateVm 修改实体
     * @param request  请求信息
     * @throws Exception 异常处理
     */
    public void updateDataSource(DataSourceUpdateVm updateVm, HttpServletRequest request) throws Exception {
        String id = updateVm.getId();
        String name = updateVm.getName();
        String host = updateVm.getHost();
        String port = updateVm.getPort();
        String dataName = updateVm.getDataName();
        if (dataSourceInfoService.existsAllByNameAndIdNot(name, id)) {
            throw new Exception(Constants.DATA_SOURCE_NAME_REPEAT);
        }
        if (dataSourceInfoService.existsAllByHostAndPortAndDataNameAndIdNot(host, port, dataName, id)) {
            throw new Exception(Constants.DATA_SOURCE_JDBC_REPEAT);
        }
        JSONObject userVm = tokenService.getUserByToken(request);
        String userId = userVm.getString("id");
        String userName = userVm.getString("name");
        DataSourceInfo dataSourceInfo = findDataSource(id);
        mapper.dataSourceUpdate(updateVm, dataSourceInfo);
        dataSourceInfo.setUpdateById(userId);
        dataSourceInfo.setUpdateName(userName);
        dataSourceInfo.setUpdateTime(Calendar.getInstance());
        dataSourceInfoService.insert(dataSourceInfo);
        iOperationLogService.insertLog(new OperationLog("数据源管理", "数据源编辑", "数据源编辑", "编辑数据源" + dataSourceInfo.getName(), 1), userVm);
    }

    /**
     * 删除数据源
     *
     * @param id 数据源ID
     */
    public void deleteDataSource(String id) {
        dataSourceInfoService.deleteById(id);
    }


    /**
     * 测试数据库连接
     *
     * @param id 数据ID
     * @throws Exception 抛出异常
     */
    public void testConnection(String id, HttpServletRequest request) throws Exception {

        DataSourceInfo data = dataSourceInfoService.findById(id);
        String status = testSqlConnection(data, request);
        data.setStatus(status);
        dataSourceInfoService.insert(data);
        String errorCode = "0";
        if (errorCode.equals(status)) {
            throw new Exception(Constants.DATA_INFO_ERROR);
        }
    }

    /**
     * 测试数据库连接2
     *
     * @param data 数据ID
     * @throws Exception 抛出异常
     */
    public void testConnectionButton(DataSourceInfo data, HttpServletRequest request) throws Exception {
        String status = testSqlConnection(data, request);
        testSqlConnection(data, request);
        String errorCode = "0";
        if (errorCode.equals(status)) {
            throw new Exception(Constants.DATA_INFO_ERROR);
        }
    }

    private String testSqlConnection(DataSourceInfo data, HttpServletRequest request) throws Exception {
        String type = data.getType();
        String host = data.getHost();
        String port = data.getPort();
        String dataName = data.getDataName();
        String userName = data.getUserName();
        String password = data.getPassword();
        String url;
        String status;
        Connection connection;
        try {
            switch (type) {
                case "MySQL":
                    url = "jdbc:mysql://" + host + ":" + port + "/" + dataName + "?characterEncoding=utf8&useSSL=false";
                    Class.forName("com.mysql.jdbc.Driver");
                    connection = DriverManager.getConnection(url, userName, password);
                    connection.close();
                    break;
                case "PostgreSQL":
                    url = "jdbc:postgresql://" + host + ":" + port + "/" + dataName + "?characterEncoding=utf8&useSSL=false";
                    Class.forName("org.postgresql.Driver");
                    connection = DriverManager.getConnection(url, userName, password);
                    connection.close();
                    break;
                case "Oracle":
                    url = "jdbc:oracle:thin:@//" + host + ":" + port + "/" + dataName;
                    Class.forName("oracle.jdbc.OracleDriver");
                    connection = DriverManager.getConnection(url, userName, password);
                    connection.close();
                    break;
                case "SQLserver":
                    url = "jdbc:sqlserver://" + host + ":" + port + ";databaseName=" + dataName + ";user=" + userName + ";password=" + password;
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    connection = DriverManager.getConnection(url);
                    connection.close();
                    break;
                case "dameng":
                    Class.forName("dm.jdbc.driver.DmDriver");
                    url = "jdbc:oracle:thin:@//" + host + ":" + port + "/" + dataName;
                    connection = DriverManager.getConnection(url, userName, password);
                    connection.close();
                    break;
                default:
                    throw new Exception(Constants.DATA_TYPE_ERROR);
            }
            status = "1";
        } catch (Exception e) {
            status = "0";
        }
        iOperationLogService.insert(new OperationLog("数据源管理", "数据源连接测试", "数据源连接测试", "测试数据源" + dataName, Integer.parseInt(status)), request);
        return status;
    }


    /**
     * 容器列表查询
     *
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 查询异常
     */
    public Page<ContainerInfo> findAllContainerInfo(JSONObject params) throws Exception {
        return containerInfoService.findAll(params);
    }

    /**
     * 容器详情
     *
     * @param id 数据id
     * @return 查询结果
     * @throws Exception 异常处理
     */
    public ContainerInfo findContainerInfo(String id) throws Exception {
        ContainerInfo containerInfo = containerInfoService.findById(id);
        if (containerInfo == null) {
            throw new Exception(Constants.DATA_NULL);
        }
        return containerInfo;
    }

    /**
     * 添加容器
     *
     * @param insertVm 添加信息实体
     * @param request  请求信息
     * @throws Exception 异常处理
     */
    public void insertContainerInfo(ContainerInsertVm insertVm, HttpServletRequest request) throws Exception {
        String name = insertVm.getName();
        if (containerInfoService.existsAllByName(name)) {
            throw new Exception(Constants.CONTAINER_NAME_REPEAT);
        }
        String ip = insertVm.getIp();
        String port = insertVm.getPort();
        if (containerInfoService.existsAllByIpAndPort(ip, port)) {
            throw new Exception(Constants.CONTAINER_URL_REPEAT);
        }
        JSONObject userVm = tokenService.getUserByToken(request);
        String userId = userVm.getString("id");
        String userName = userVm.getString("name");
        ContainerInfo containerInfo = new ContainerInfo();
        mapper.containerInsert(insertVm, containerInfo);
        containerInfo.setId(BaseUtils.generate32Id());
        containerInfo.setCreateTime(Calendar.getInstance());
        containerInfo.setCreateName(userName);
        containerInfo.setUpdateById(userId);
        containerInfo.setUpdateName(userName);
        containerInfo.setCreateById(userId);
        containerInfo.setUpdateTime(Calendar.getInstance());
        containerInfoService.insert(containerInfo);
        iOperationLogService.insertLog(new OperationLog("容器管理", "容器添加", "容器添加", "添加容器" + containerInfo.getName(), 1), userVm);
    }


    /**
     * 修改容器
     *
     * @param updateVm 修改信息实体
     * @param request  请求信息
     * @throws Exception 异常处理
     */
    public void updateContainerInfo(ContainerUpdateVm updateVm, HttpServletRequest request) throws Exception {
        String name = updateVm.getName();
        String id = updateVm.getId();
        String ip = updateVm.getIp();
        String port = updateVm.getPort();
        if (containerInfoService.existsAllByNameAndIdNot(name, id)) {
            throw new Exception(Constants.CONTAINER_NAME_REPEAT);
        }
        if (containerInfoService.existsAllByIpAndPortAndIdNot(ip, port, id)) {
            throw new Exception(Constants.CONTAINER_URL_REPEAT);
        }
        JSONObject userVm = tokenService.getUserByToken(request);
        String userId = userVm.getString("id");
        String userName = userVm.getString("name");
        ContainerInfo containerInfo = findContainerInfo(id);
        //ip和端口相同且不存在任务就允许修改
        if (!updateVm.getIp().equals(containerInfo.getIp()) || !updateVm.getPort().equals(containerInfo.getPort())){
            if(taskDao.existsAllByContainer(id)) {
                throw new Exception(Constants.CONTAINER_IS_TASK);
            }
        }
        mapper.containerUpdate(updateVm, containerInfo);
        containerInfo.setUpdateById(userId);
        containerInfo.setUpdateName(userName);
        containerInfo.setUpdateTime(Calendar.getInstance());
        containerInfoService.insert(containerInfo);
        iOperationLogService.insertLog(new OperationLog("容器管理", "容器编辑", "容器编辑", "编辑容器" + containerInfo.getName(), 1), userVm);
    }

    /**
     * 删除容器
     *
     * @param id 数据ID
     */
    public void deleteContainerInfo(String id, HttpServletRequest request) throws Exception {
        ContainerInfo containerInfo = containerInfoService.findById(id);
        if (containerInfo == null) {
            throw new Exception(Constants.DATA_NULL);
        }
        if (taskDao.existsAllByContainer(id)){
            throw new Exception(Constants.CONTAINER_IS_TASK);
        }
        containerInfoService.deleteById(id);
        iOperationLogService.insert(new OperationLog("容器管理", "容器删除", "容器删除", "删除容器" + containerInfo.getName(), 1), request);
    }

    /**
     * 测试容器连接
     *
     * @param id 容器ID
     * @throws Exception 异常处理
     */
    public void testContainer(String id, HttpServletRequest request) throws Exception {
        ContainerInfo containerInfo = findContainerInfo(id);
        String relUrl = "http://" + containerInfo.getIp() + ":" + containerInfo.getPort() + testContainerUrl;
        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<JSONObject> res = restTemplate.getForEntity(relUrl, JSONObject.class);
            HttpStatus httpStatus = res.getStatusCode();
            if (httpStatus.is2xxSuccessful()) {
                containerInfo.setStatus("1");
                containerInfoService.insert(containerInfo);
            } else {
                containerInfo.setStatus("0");
                containerInfoService.insert(containerInfo);
                throw new Exception(Objects.requireNonNull(res.getBody()).getString("msg"));
            }
        } catch (RestClientException e) {
            containerInfo.setStatus("0");
            containerInfoService.insert(containerInfo);
            throw new Exception(Constants.NET_ERROR);
        }
        iOperationLogService.insert(new OperationLog("容器管理", "容器测试", "测试容器连接", "测试容器" + containerInfo.getName(), 1), request);
    }
}
