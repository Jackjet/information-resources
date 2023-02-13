package d1.project.resourcesharingmgmt.resource.configuration;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.component.file.GenericFileMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.RandomAccessFile;

/**
 * @author JungYoung
 */
@Component
public class LocationFileProcessor  implements Processor {
    private static Logger logger = LoggerFactory.getLogger( LocationFileProcessor.class );

    @Value("${ftp.local.dir}")
    private String fileDir;

//    @Autowired
//    OrderService orderService;

    @Override
    public void process(Exchange exchange) throws Exception {
        GenericFileMessage<RandomAccessFile> inFileMessage = (GenericFileMessage<RandomAccessFile>) exchange.getIn();
        //文件名
        String fileName = inFileMessage.getGenericFile().getFileName();
        //文件的绝对路径
        logger.info(fileDir + fileName);
        //解析入库等操作
//        orderService.process(fileDir + fileName);
    }
}
