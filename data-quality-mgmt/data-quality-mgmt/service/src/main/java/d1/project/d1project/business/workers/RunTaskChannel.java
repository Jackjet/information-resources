package d1.project.d1project.business.workers;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.d1project.business.dao.TaskDao;
import d1.project.d1project.business.dao.TaskLogDao;
import d1.project.d1project.business.dao.TaskRuleLogDao;
import d1.project.d1project.business.workers.bean.RunTask;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 记录规则检查任务的队列，可以被添加规则任务，可以被worker获取任务去执行
 *
 * @author Buter
 * @date 2021/4/13 11:43
 */
@Service
public class RunTaskChannel {

    private final Object mapLock = new Object();

    //最大任务数
    private final static int maxTaskCount = 10000;

    //最多工作线程数
    private final static int workerCount = 5;

    private final ConcurrentHashMap<String, RunTask> ruleMap = new ConcurrentHashMap<>();

    public RunTaskChannel(TaskDao taskDao, TaskLogDao taskLogDao, TaskRuleLogDao taskRuleLogDao) {
        WorkerThread[] workerThreads = new WorkerThread[workerCount];
        for (int i = 0; i < workerThreads.length; i++) {
            workerThreads[i] = new WorkerThread(this, taskDao, taskLogDao, taskRuleLogDao);
            workerThreads[i].start();
        }
    }


    public void addTask(RunTask runTask) throws DoValidException {
        synchronized (mapLock) {
            if (ruleMap.size() >= maxTaskCount) {
                throw new DoValidException("任务数量超出最大限制");
            }

            if (runTask == null || StringUtils.isEmpty(runTask.getId())) {
                throw new DoValidException("任务不能为空");
            }

            if (ruleMap.containsKey(runTask.getId())) {
                throw new DoValidException("当前任务正在运行，不能重复启动");
            }

            ruleMap.put(runTask.getId(), runTask);
            mapLock.notifyAll();
        }
    }

    public RunTask getTask() {
        RunTask runTask = null;
        synchronized (mapLock) {
            for (RunTask value : ruleMap.values()) {
                if (!value.isRunning()) {
                    value.setRunning(true);
                    runTask = value;
                }
            }
            try {
                if (runTask == null) {
                    mapLock.wait();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return runTask;
    }

    public void removeTask(String id) {
        synchronized (mapLock) {
            //任务结束后再删除队列里的任务
            ruleMap.remove(id);
        }
    }
}
