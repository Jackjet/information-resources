package d1.project.container.task.service;

import d1.project.container.task.model.NodeInsertDetailVm;
import net.dcrun.component.osinfo.OsInfoService;
import net.dcrun.component.osinfo.model.CPUInfo;
import net.dcrun.component.osinfo.model.RamInfo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author zhengyang
 */
@Service
public class NodeService {
    @Value("${d1.project.access.url}")
    private String accessUrl;

    @Value("${d1.project.node.ip}")
    private String nodeIp;

    public void getNodeDetail() throws Exception {
        OsInfoService osInfoService = new OsInfoService();
        RamInfo ramInfo = (RamInfo) osInfoService.getOsInfoByType("RAM");
        CPUInfo cpuInfo = (CPUInfo) osInfoService.getOsInfoByType("CPU");
        NodeInsertDetailVm model = new NodeInsertDetailVm();
        model.setIp(nodeIp);
        model.setMemoryTotal(ramInfo.getMemTotal());
        model.setMemoryFree(ramInfo.getMemFree());
        model.setMemoryUsed(ramInfo.getMemUsed());
        model.setCpuUsed(cpuInfo.getCombined());
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.postForObject(accessUrl + "/webadmin/nodeApi/insertDetail", model, NodeInsertDetailVm.class);
    }
}
