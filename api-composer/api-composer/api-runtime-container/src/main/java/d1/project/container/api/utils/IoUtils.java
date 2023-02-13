package d1.project.container.api.utils;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class IoUtils {
    public static String readFile(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));

        StringBuffer sb = new StringBuffer();
        String str;
        while ((str = bufferedReader.readLine()) != null) {
            sb.append(str);
        }

        bufferedReader.close();
        return sb.toString();
    }

    public static void writeFile(String content, File file, boolean isAppend) throws IOException {
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, isAppend), StandardCharsets.UTF_8));
        bufferedWriter.write(content);
        bufferedWriter.close();
    }

    public static void createDirectory(String directory) throws Exception {
        File path = new File(directory);
        if (path.exists()) {
            return;
        }
        if (!path.mkdirs()) {
            throw new Exception("目录创建失败！" + path.getAbsolutePath());
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
}
