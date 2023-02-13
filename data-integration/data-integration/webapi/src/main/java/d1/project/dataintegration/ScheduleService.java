package d1.project.dataintegration;

import d1.project.dataintegration.task.service.TaskAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时任务示例，项目如果没有定时任务请删除这个文件
 * 如果有多个Schedule文件，可以把@EnableScheduling 这个注解加在主Application类上
 *
 * @author liuyi
 */
@Component
@EnableScheduling
public class ScheduleService {
    private final TaskAnalysisService taskAnalysisService;

    @Autowired
    public ScheduleService(TaskAnalysisService taskAnalysisService){
        this.taskAnalysisService = taskAnalysisService;
    }
    /*
    一个cron表达式有至少6个（也可能7个）有空格分隔的时间元素。
    corn表达式格式：
    秒 分 时 日 月 周 年(可选)。
    “?”字符：表示不确定的值
    “,”字符：指定数个值
    “-”字符：指定一个值的范围
    “/”字符：指定一个值的增加幅度。n/m表示从n开始，每次增加m
    “L”字符：用在日表示一个月中的最后一天，用在周表示该月最后一个星期X
    “W”字符：指定离给定日期最近的工作日(周一到周五)
    “#”字符：表示该月第几个周X。6#3表示该月第3个周五

    例子：
    每隔5秒执行一次：                      /5 * * * ?
    每隔1分钟执行一次：                    0 /1 * * ?
    每天23点执行一次：                     0 0 23 * * ?
    每天凌晨1点执行一次：                  0 0 1 * * ?
    每月1号凌晨1点执行一次：               0 0 1 1 * ?
    每月最后一天23点执行一次：             0 0 23 L * ?
    每周星期天凌晨1点实行一次：            0 0 1 ? * L
    在26分、29分、33分执行一次：           0 26,29,33 * * * ?
    每天的0点、13点、18点、21点都执行一次：0 0 0,13,18,21 * * ?
    每天的8点开始，2小时执行一次：         0 0 8/2 * * ?
     */

    /**
     * 任务运行日志
     */
//    @Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(cron = "0 1 0 * * ?")
    public void TaskAnalysisDay() {
        //每天凌晨0点过1分执行任务一次
        this.taskAnalysisService.taskAnalysisByDay();
    }

    /**
     * 任务分析
     */
//    @Scheduled(cron = "0/50 * * * * ?")
    @Scheduled(cron = "0 1 0 1 * ?")
    public void TaskAnalysis() {
        //每月1号凌晨0点过1分执行任务
        this.taskAnalysisService.taskAnalysisByMonth();
    }
}
