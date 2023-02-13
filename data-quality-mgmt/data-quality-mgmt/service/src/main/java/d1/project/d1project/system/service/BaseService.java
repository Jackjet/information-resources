package d1.project.d1project.system.service;

import com.github.houbb.heaven.util.lang.ObjectUtil;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.CacheManger;
import d1.project.d1project.common.utils.BaseUtils;
import d1.project.d1project.system.utils.VerifyImageUtil;
import net.dcrun.component.ehcache.IEhcacheService;
import net.dcrun.component.shell.ShellService;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.io.File;
import java.io.InputStream;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-14 10:17
 */
@Service
public class BaseService {

    private static final String IMG_PATH = "/image/picture/*.*";
    private static final String TEMP_IMG_PATH = "/image/temp/temp.png";
    private static final int IMG_CACHE_EX_TIME = 120;
    private final DataSourceProperties dataSourceProperties;
    private final DataSource dataSource;

    public BaseService(DataSourceProperties dataSourceProperties, DataSource dataSource) {
        this.dataSourceProperties = dataSourceProperties;
        this.dataSource = dataSource;
    }

    private static void handleMsg(String log) {
        System.out.println("java收到消息: " + log);
    }

    public Map<String, String> findDatabaseInfo() {
        String url = dataSourceProperties.getUrl();
        String[] split = url.split(":");
        switch (split[1]) {
            case "postgresql":
                return getPostgresInfo(url);
            case "mysql":
                break;
            default:

        }
        return null;
    }

    /**
     * 获取数据库基础信息
     */
    private Map<String, String> getPostgresInfo(String url) {
        String group = getSubUtilSimple(url, "\\d/(.*?)[?]");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Map<String, String> stringMap = new HashMap<>();
        try {
            String sql = "select pg_database.datname, pg_size_pretty (pg_database_size(pg_database.datname)) from pg_database where datname=?";

            connection = dataSource.getConnection();
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, group);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()) {
                String gender = resultSet.getString("pg_size_pretty");
                stringMap.put("size", gender);
            }
            stringMap.put("name", group);
            stringMap.put("type", "postgresql");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (preparedStatement != null) {
                try {
                    preparedStatement.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        }
        return stringMap;
    }

    /**
     * 备份数据库
     */
    public void backupPostgres(String path) throws Exception {
        if (StringUtils.isEmpty(path)) {
            path = "/root/";
        }
//        FileUtilService fileUtilService = new FileUtilService();
//        if (!fileUtilService.fileExists(path)) {
//            fileUtilService.createDir(path);
//        }
        String urlAll = dataSourceProperties.getUrl();
        String url = getSubUtilSimple(urlAll, "//(.*?)[:]");
        String port = getSubUtilSimple(urlAll, "[0-9][:](.*?)[/][a-zA-Z]");
        String database = getSubUtilSimple(urlAll, "\\d/(.*?)[?]");
        String password1 = dataSourceProperties.getPassword();
        String username = dataSourceProperties.getUsername();
        ShellService shellService = new ShellService();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        String format = simpleDateFormat.format(new Date());
        String str = "PGPASSWORD=" + password1 + " pg_dump -d " + database + " -h " + url + " -p " + (StringUtils.isEmpty(port) ? "5432" : port) + " -U " + username + " -f " + path + File.separator + format + ".sql";
        shellService.run(str, BaseService::handleMsg);
    }

    public String getSubUtilSimple(String soap, String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher m = pattern.matcher(soap);
        if (m.find()) {
            return m.group(1);
        }
        return "";

    }


    public Map<String, Object> getImageVerifyCode() throws Exception {
        /*读取图库目录*/
        List<File> imgList = queryFileList(IMG_PATH);
        int randNum = new SecureRandom().nextInt(imgList.size());
        File targetFile = imgList.get(randNum);
        File tempImgFile = queryFileList(TEMP_IMG_PATH).get(0);

        /*根据模板裁剪图片*/
        Map<String, Object> resultMap = VerifyImageUtil.pictureTemplatesCut(tempImgFile, targetFile);
        int xWidth = (int) resultMap.remove("xWidth");

        String checkMoveId = "ImageVerifyCode/" + BaseUtils.generate32Id();
        CacheManger.getInstance().getCache().setExpireString(checkMoveId, xWidth + "", IMG_CACHE_EX_TIME);
        resultMap.put("checkMoveId", checkMoveId);

        return resultMap;
    }


    /**
     * 检查移动
     */
    public void checkMove(String xWidth, String checkMoveId) throws DoValidException {
        if (ObjectUtil.isNull(xWidth) || ObjectUtil.isNull(checkMoveId)) {
            throw new DoValidException("请使用滑块验证码");
        }

        // 获取cache中缓存的随机宽度
        IEhcacheService cache = CacheManger.getInstance().getCache();
        String xWidthOriginal = cache.getString(checkMoveId);
        if (ObjectUtil.isNull(xWidthOriginal)) {
            throw new DoValidException("请刷新滑块验证码");
        }

        if (Math.abs(Double.parseDouble(xWidth) - Double.parseDouble(xWidthOriginal)) > 10) {
            throw new DoValidException("请拖动滑块到正确的位置");
        }
        cache.removeData(checkMoveId);
    }

    /**
     * 获取图片，由于spring boot打包成jar之后，获取到获取不到resources里头的图片，对此进行处理
     */
    public List<File> queryFileList(String path) {
        /*获取容器资源解析器*/
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<File> filelist = new ArrayList<>();
        /*获取远程服务器IP和端口*/
        try {
            /*获取所有匹配的文件*/
            Resource[] resources = resolver.getResources(path);
            for (Resource resource : resources) {
                /*获得文件流，因为在jar文件中，不能直接通过文件资源路径拿到文件，但是可以在jar包中拿到文件流*/
                InputStream stream = resource.getInputStream();
                String targetFilePath = resource.getFilename();
                if (StringUtils.isEmpty(targetFilePath)) {
                    throw new DoValidException("验证码获取异常");
                }
                File ttfFile = new File(targetFilePath);
                FileUtils.copyInputStreamToFile(stream, ttfFile);
                filelist.add(ttfFile);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filelist;
    }

}
