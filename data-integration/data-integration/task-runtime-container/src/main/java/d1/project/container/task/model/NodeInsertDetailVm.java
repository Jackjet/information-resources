package d1.project.container.task.model;


/**
 * 新增节点详情
 */
public class NodeInsertDetailVm {

    /**
     * IP地址
     */
    private String ip;

    /**
     * 总内存
     */
    private String memoryTotal;

    /**
     * 空闲内存
     */
    private String memoryFree;

    /**
     * 已用内存
     */
    private String memoryUsed;

    /**
     * CPU使用（百分比）
     */
    private String cpuUsed;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getMemoryTotal() {
        return memoryTotal;
    }

    public void setMemoryTotal(String memoryTotal) {
        this.memoryTotal = memoryTotal;
    }

    public String getMemoryFree() {
        return memoryFree;
    }

    public void setMemoryFree(String memoryFree) {
        this.memoryFree = memoryFree;
    }

    public String getMemoryUsed() {
        return memoryUsed;
    }

    public void setMemoryUsed(String memoryUsed) {
        this.memoryUsed = memoryUsed;
    }

    public String getCpuUsed() {
        return cpuUsed;
    }

    public void setCpuUsed(String cpuUsed) {
        this.cpuUsed = cpuUsed;
    }
}
