package d1.project.d1project.business.workers.bean;

import d1.project.d1project.business.entity.Task;

import java.util.List;

/**
 * @author Buter
 * @date 2021/4/13 13:35
 */
public class RunTask extends Task {

    private boolean isRunning;

    private List<RunRule> runRules;

    public boolean isRunning() {
        return isRunning;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public List<RunRule> getRunRules() {
        return runRules;
    }

    public void setRunRules(List<RunRule> runRules) {
        this.runRules = runRules;
    }
}
