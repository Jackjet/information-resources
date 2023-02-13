package d1.project.dcrun.center.webapi.basesys.model.tree;

import java.util.List;

/**
 * 文件/目录 结构
 *
 * @author maorui
 */
public class FileTree {

    /**
     * 目录/文件 名称
     */
    private String name;

    /**
     * 目录相对路径 ./files/services/{integrationId}/webapi/{appid}/{path}
     */
    private String path;

    /**
     * 文件类型 {@link FileType}
     */
    private String fileType;

    /**
     * 是否隐藏
     */
    private boolean isHidden;

    /**
     * 权限列表：新建(文件/目录),重命名,删除
     * 0：表示不允许，1 表示允许
     */
    private String permission;

    /**
     * 子目录/文件
     */
    private List<FileTree> children;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean hidden) {
        isHidden = hidden;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public List<FileTree> getChildren() {
        return children;
    }

    public void setChildren(List<FileTree> children) {
        this.children = children;
    }


}
