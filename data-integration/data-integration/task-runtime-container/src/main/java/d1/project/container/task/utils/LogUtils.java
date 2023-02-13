package d1.project.container.task.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils {
    private static String fileSeparator = File.separator;

    public static void wirteLogs(String taskId, String logs){
        if(logs==null){
            return;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String logsPath = System.getProperty("user.dir") + fileSeparator + "static" + fileSeparator + "kettleLogs" +
                fileSeparator + sdf.format(new Date()) + fileSeparator + taskId + ".log";
        File file = new File(logsPath);
        FileOutputStream fos = null;
        try {
            if (!file.exists()) {
                file.createNewFile();
//                在文件系统中根据保存在文件中的路径信息，创建一个新的空文件。如果创建成功就会返回true，如果文件存在返回false。注意：如果他已经存在但不是文件，可能是目录也会返回false。
                boolean hasFile = file.createNewFile();
                if (!hasFile) {
                    fos = new FileOutputStream(file);
                }
            } else {
//                以追加的方式创建一个文件输出流
                fos = new FileOutputStream(file, true);
            }
            fos.write(logs.getBytes());
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
