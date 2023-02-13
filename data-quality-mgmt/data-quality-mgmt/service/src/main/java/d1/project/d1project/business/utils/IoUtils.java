package d1.project.d1project.business.utils;

import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.charset.StandardCharsets;

/**
 * @author maorui
 */
public class IoUtils {

    public static void createDirectory(String directory) throws Exception {
        createDirectory(new File(directory));
    }

    public static void createDirectory(File directory) throws Exception {
        if (directory.exists()) {
            return;
        }
        if (!directory.mkdirs()) {
            throw new Exception("目录创建失败！" + directory.getAbsolutePath());
        }
    }

    public static void createFile(String file) throws Exception {
        if (existFile(file)) {
            return;
        }
        File newFile = new File(file);
        if (!newFile.getParentFile().exists()) {
            createDirectory(newFile.getParent());
        }
        if (!newFile.createNewFile()) {
            throw new Exception("文件创建失败！" + newFile.getAbsolutePath());
        }
    }

    public static boolean existFile(String fileFullName) {
        File file = new File(fileFullName);
        if (file.isFile()) {
            return file.exists();
        }
        return false;
    }


    public static String readFile(File file) throws IOException {
        return readFile(new FileInputStream(file));
    }

    public static String readFile(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        StringBuffer sb = new StringBuffer();
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            sb.append(str + "\n");
        }

        bufferedReader.close();

        String content = sb.toString();
        if (!Utils.isEmpty(content)) {
            return content.substring(0, content.length() - 1);
        }
        return content;
    }

    public static void writeFile(String content, File file, boolean isAppend) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, isAppend), StandardCharsets.UTF_8));
        bufferedWriter.write(content);
        bufferedWriter.close();
    }

    public static boolean delete(File file) {
        return FileUtils.deleteQuietly(file);
    }
}
