package d1.project.tangshan.operation.manage.model;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author lin
 */
public class UtilizationVm {
    @ApiModelProperty("配置")
    private String configuration;
    @ApiModelProperty("ram")
    private Long ram;
    @ApiModelProperty("rom")
    private Long rom;
    @ApiModelProperty("cpu")
    private Integer cpu;

    public String getConfiguration() {
        return configuration;
    }

    public void setConfiguration(String configuration) {
        this.configuration = configuration;
    }

    public Long getRam() {
        return ram;
    }

    public void setRam(Long ram) {
        this.ram = ram;
    }

    public Long getRom() {
        return rom;
    }

    public void setRom(Long rom) {
        this.rom = rom;
    }

    public Integer getCpu() {
        return cpu;
    }

    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }
}
