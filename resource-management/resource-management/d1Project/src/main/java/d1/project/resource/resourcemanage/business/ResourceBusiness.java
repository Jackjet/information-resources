package d1.project.resource.resourcemanage.business;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.resource.common.Constants;
import d1.project.resource.common.model.OperationLog;
import d1.project.resource.common.service.IOperationLogService;
import d1.project.resource.common.utils.BaseUtils;
import d1.project.resource.resourcemanage.entity.ContainerInfo;
import d1.project.resource.resourcemanage.entity.DataSourceInfo;
import d1.project.resource.resourcemanage.mapper.ResourceMapper;
import d1.project.resource.resourcemanage.model.ContainerInsertVm;
import d1.project.resource.resourcemanage.model.ContainerUpdateVm;
import d1.project.resource.resourcemanage.model.DataSourceInsertVm;
import d1.project.resource.resourcemanage.model.DataSourceUpdateVm;
import d1.project.resource.resourcemanage.service.ContainerInfoService;
import d1.project.resource.resourcemanage.service.DataSourceInfoService;
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
    private final IOperationLogService iOperationLogService;

    @Value("${d1.project.container.test.url}")
    private String testContainerUrl;

    public ResourceBusiness(DataSourceInfoService dataSourceInfoService, ContainerInfoService containerInfoService, IOperationLogService iOperationLogService) {
        this.dataSourceInfoService = dataSourceInfoService;
        this.containerInfoService = containerInfoService;
        this.iOperationLogService = iOperationLogService;
        this.mapper = Mappers.getMapper(ResourceMapper.class);
    }

    /**
     * ?????????????????????
     *
     * @param params ????????????
     * @return ????????????
     * @throws Exception ????????????
     */
    public Page<DataSourceInfo> findAllDataSource(JSONObject params) throws Exception {
        return dataSourceInfoService.findAll(params);
    }

    /**
     * ?????????????????????
     *
     * @param id ??????ID
     * @return ????????????
     * @throws DoValidException ??????????????????
     */
    public DataSourceInfo findDataSource(String id) throws DoValidException {
        DataSourceInfo dataSourceInfo = dataSourceInfoService.findById(id);
        if (dataSourceInfo == null) {
            throw new DoValidException(Constants.DATA_NULL);
        }
        return dataSourceInfo;
    }

    /**
     * ?????????????????????
     *
     * @param insertVm ????????????
     * @param request  ????????????
     * @throws Exception ????????????
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
        JSONObject userVm = BaseUtils.getUserInfo(request);
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
        iOperationLogService.insertLog(new OperationLog("???????????????", "???????????????", "???????????????", "???????????????" + dataSourceInfo.getName(), 1), userVm);
    }


    /**
     * ?????????????????????
     *
     * @param updateVm ????????????
     * @param request  ????????????
     * @throws Exception ????????????
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
        JSONObject userVm = BaseUtils.getUserInfo(request);
        String userId = userVm.getString("id");
        String userName = userVm.getString("name");
        DataSourceInfo dataSourceInfo = findDataSource(id);
        mapper.dataSourceUpdate(updateVm, dataSourceInfo);
        dataSourceInfo.setUpdateById(userId);
        dataSourceInfo.setUpdateName(userName);
        dataSourceInfo.setUpdateTime(Calendar.getInstance());
        dataSourceInfoService.insert(dataSourceInfo);
        iOperationLogService.insertLog(new OperationLog("???????????????", "???????????????", "???????????????", "???????????????" + dataSourceInfo.getName(), 1), userVm);
    }

    /**
     * ???????????????
     *
     * @param id ?????????ID
     */
    public void deleteDataSource(String id) {
        dataSourceInfoService.deleteById(id);
    }


    /**
     * ?????????????????????
     *
     * @param id ??????ID
     * @throws Exception ????????????
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
     * ?????????????????????2
     *
     * @param data ??????ID
     * @throws Exception ????????????
     */
    public void testConnectionButton(DataSourceInfo data, HttpServletRequest request) throws Exception {
        String status = testSqlConnection(data, request);
        testSqlConnection(data, request);
        String errorCode = "0";
        if (errorCode.equals(status)) {
            throw new Exception(Constants.DATA_INFO_ERROR);
        }
    }

    /**
     * ???????????????????????????
     * @param id
     * @throws DoValidException
     */
    public void updateInit(String id) throws DoValidException {
        ContainerInfo containerInfo = containerInfoService.findById(id);
        if(containerInfo == null) {
            throw new DoValidException("???????????????");
        }

        containerInfo.setIsInit("1");
        containerInfoService.save(containerInfo);
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
        iOperationLogService.insert(new OperationLog("???????????????", "?????????????????????", "?????????????????????", "???????????????" + dataName, Integer.parseInt(status)), request);
        return status;
    }


    /**
     * ??????????????????
     *
     * @param params ????????????
     * @return ????????????
     * @throws Exception ????????????
     */
    public Page<ContainerInfo> findAllContainerInfo(JSONObject params) throws Exception {
        return containerInfoService.findAll(params);
    }

    /**
     * ????????????
     *
     * @param id ??????id
     * @return ????????????
     * @throws Exception ????????????
     */
    public ContainerInfo findContainerInfo(String id) throws Exception {
        ContainerInfo containerInfo = containerInfoService.findById(id);
        if (containerInfo == null) {
            throw new DoValidException(Constants.DATA_NULL);
        }
        return containerInfo;
    }

    /**
     * ????????????
     *
     * @param insertVm ??????????????????
     * @param request  ????????????
     * @throws Exception ????????????
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
        JSONObject userVm = BaseUtils.getUserInfo(request);
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
        iOperationLogService.insertLog(new OperationLog("????????????", "????????????", "????????????", "????????????" + containerInfo.getName(), 1), userVm);
    }


    /**
     * ????????????
     *
     * @param updateVm ??????????????????
     * @param request  ????????????
     * @throws Exception ????????????
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
        JSONObject userVm = BaseUtils.getUserInfo(request);
        String userId = userVm.getString("id");
        String userName = userVm.getString("name");
        ContainerInfo containerInfo = findContainerInfo(id);
        mapper.containerUpdate(updateVm, containerInfo);
        containerInfo.setUpdateById(userId);
        containerInfo.setUpdateName(userName);
        containerInfo.setUpdateTime(Calendar.getInstance());
        containerInfoService.insert(containerInfo);
        iOperationLogService.insertLog(new OperationLog("????????????", "????????????", "????????????", "????????????" + containerInfo.getName(), 1), userVm);
    }

    /**
     * ????????????
     *
     * @param id ??????ID
     */
    public void deleteContainerInfo(String id, HttpServletRequest request) throws Exception {
        ContainerInfo containerInfo = containerInfoService.findById(id);
        if (containerInfo == null) {
            throw new Exception(Constants.DATA_NULL);
        }
        containerInfoService.deleteById(id);
        iOperationLogService.insert(new OperationLog("????????????", "????????????", "????????????", "????????????" + containerInfo.getName(), 1), request);
    }

    /**
     * ??????????????????
     *
     * @param id ??????ID
     * @throws Exception ????????????
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
        iOperationLogService.insert(new OperationLog("????????????", "????????????", "??????????????????", "????????????" + containerInfo.getName(), 1), request);
    }
}
