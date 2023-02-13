package d1.project.sample.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/**
 * @author lin
 */
@Component
@EnableScheduling
public class ScheduleService {
    @Autowired
    private SenderService senderService;

    /**
     * 每3分钟获取通讯信息并发送
     */
    @Scheduled(cron = "0/20 * * * * ?")
    public void syncSsoUser() {
        senderService.send();
    }

}
