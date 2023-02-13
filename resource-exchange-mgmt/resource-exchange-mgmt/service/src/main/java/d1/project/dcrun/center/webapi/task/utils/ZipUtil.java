package d1.project.dcrun.center.webapi.task.utils;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    /**
     * @Author AlphaJunS
     * @Date 11:32 2020/3/8
     * @Description
     * @param zip 压缩目的地址
     * @param srcFiles 压缩的源文件
     * @return void
     */
    public static void zipFile(String zip , List<File> srcFiles ) {
        try {
            if( zip.endsWith(".zip") || zip.endsWith(".ZIP") ){
                FileOutputStream fos = new FileOutputStream(new File(zip));
                ZipOutputStream _zipOut = new ZipOutputStream(fos) ;
                //_zipOut.setEncoding("GBK");
                for( File _f : srcFiles ){
                    handlerFile(zip , _zipOut , _f , "");
                }
                _zipOut.close();
                fos.close();
            }else{
                System.out.println("target file[" + zip + "] is not .zip type file");
            }
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * @Author AlphaJunS
     * @Date 11:33 2020/3/8
     * @Description
     * @param zip 压缩的目的地址
     * @param zipOut
     * @param srcFile 被压缩的文件信息
     * @param path 在zip中的相对路径
     * @return void
     */
    private static void handlerFile(String zip , ZipOutputStream zipOut , File srcFile , String path) throws IOException {
        System.out.println(" begin to compression file[" + srcFile.getName() + "]");
        if( !"".equals(path) && ! path.endsWith(File.separator)){
            path += File.separator ;
        }
        if( ! srcFile.getPath().equals(zip) ){
            if( srcFile.isDirectory() ){
                File[] _files = srcFile.listFiles() ;
                if( _files.length == 0 ){
                    zipOut.putNextEntry(new ZipEntry( path + srcFile.getName() + File.separator));
                    zipOut.closeEntry();
                }else{
                    for( File _f : _files ){
                        handlerFile( zip ,zipOut , _f , path + srcFile.getName() );
                    }
                }
            }else{
                InputStream _in = new FileInputStream(srcFile) ;
                zipOut.putNextEntry(new ZipEntry(path + srcFile.getName()));
                int len = 0 ;
                byte[] _byte = new byte[1024];
                while( (len = _in.read(_byte)) > 0  ){
                    zipOut.write(_byte, 0, len);
                }
                _in.close();
                zipOut.closeEntry();
            }
        }
    }
}
