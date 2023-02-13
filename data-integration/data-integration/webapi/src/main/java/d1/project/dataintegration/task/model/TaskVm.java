package d1.project.dataintegration.task.model;


import d1.project.dataintegration.task.entity.Task;

import java.util.List;

/**
 * API设计信息
 *
 * @author zhengyang
 */
public class TaskVm extends Task {
    List<MetasReturnVm> metas;

    public List<MetasReturnVm> getMetas() {
        return metas;
    }

    public void setMetas(List<MetasReturnVm> metas) {
        this.metas = metas;
    }
}
