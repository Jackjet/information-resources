package d1.project.dcrun.center.webapi.basesys.model.tree;


/**
 * script 脚本文件类型：dir 目录、js、html、kjb、css、json
 *
 * @author maorui
 */

public enum FileType {

    /**
     * 目录类型
     */
    DIR("dir", "目录类型"),

    /**
     * js文件类型
     */
    JS("js", "js文件类型"),

    /**
     * html文件类型
     */
    HTML("html", "html文件类型"),

    /**
     * css文件类型
     */
    CSS("css", "css文件类型"),

    /**
     * kettle运行脚本文件类型
     */
    KJB("kjb", "kettle运行脚本文件类型"),


    /**
     * json文件类型
     */
    JSON("json", "json文件类型");


    /**
     * 英文名
     */
    private String name;
    /**
     * 中文名
     */
    private String chineseName;

    FileType(String name, String chineseName) {
        this.name = name;
        this.chineseName = chineseName;
    }

    /**
     * 根据属性中文名，获取英文名
     *
     * @param chineseName 中文名
     * @return
     */
    public static String getEnglishName(String chineseName) {
        for (FileType operation : FileType.values()) {
            if (operation.getChineseName().equals(chineseName)) {
                return operation.getName();
            }
        }
        return null;
    }

    /**
     * 根据属性英文名，获取中文名
     *
     * @param englishName 英文文名
     * @return
     */
    public static String getChineseName(String englishName) {
        for (FileType operation : FileType.values()) {
            if (operation.getName().equals(englishName)) {
                return operation.getChineseName();
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public String getChineseName() {
        return chineseName;
    }

}
