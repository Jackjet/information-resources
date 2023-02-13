package d1.project.container.task.service;

import d1.project.container.task.utils.JobGroupIdCache;
import d1.project.container.task.utils.JobIdCache;
import d1.project.container.task.controller.WebSocketServer;
import org.pentaho.di.core.Result;
import org.pentaho.di.job.Job;
import org.pentaho.di.job.JobEntryListener;
import org.pentaho.di.job.entry.JobEntryCopy;
import org.pentaho.di.job.entry.JobEntryInterface;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMetaDataCombi;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

/**
 * KETTLE 作业中的转换监听
 *
 * @author baozh
 */
public class MyJobEntryListener implements JobEntryListener {

    private final JobEntityLogService jobEntityLogService;

    public MyJobEntryListener(JobEntityLogService jobEntityLogService) {
        this.jobEntityLogService = jobEntityLogService;
    }

    @Override
    public void afterExecution(Job arg0, JobEntryCopy arg1, JobEntryInterface arg2, Result arg3) {
        String channelId = arg0.getLogChannelId();
        String taskId = JobIdCache.getInstance().getCacheData(channelId);
        try {
            WebSocketServer.sendInfo(taskId, arg3.getLogText());
        }catch (Exception e){

        }
        String uuId = JobGroupIdCache.getInstance().getCacheData(channelId);
        if (arg2.isStart()) {
            uuId = UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.ROOT);
            JobGroupIdCache.getInstance().addCacheData(channelId, uuId);
        }
        String success = "成功";
        if (success.equals(arg2.getName())) {
            jobEntityLogService.insertLog(taskId, channelId, uuId, arg2.getName());
            jobEntityLogService.sendEnd(taskId, channelId, uuId);
            JobGroupIdCache.getInstance().removeCacheData(channelId);
        }
        //转换步骤处理
        if (arg2.isTransformation()) {
            Class cla = arg2.getClass();
            Field[] fs = cla.getDeclaredFields();
            Field field = fs[32];
            field.setAccessible(true);
            Trans trans = null;
            try {
                trans = (Trans) field.get(arg2);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            if (trans != null) {
                String tranName = trans.getName();
                List<StepMetaDataCombi> list = trans.getSteps();
                for (StepMetaDataCombi stepMetaDataCombi : list) {
                    Class cla01 = stepMetaDataCombi.getClass();
                    Field[] tempFs = cla01.getDeclaredFields();
                    Field f = tempFs[3];
                    f.setAccessible(true);
                    try {
                        StepInterface step = (StepInterface) f.get(stepMetaDataCombi);
                        jobEntityLogService.insert(taskId, step, channelId, uuId, tranName);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else{
            //非转换步骤数据处理
            jobEntityLogService.insertLog(taskId, channelId, uuId, arg2.getName());
        }
    }

    @Override
    public void beforeExecution(Job arg0, JobEntryCopy arg1, JobEntryInterface arg2) {
    }

}
