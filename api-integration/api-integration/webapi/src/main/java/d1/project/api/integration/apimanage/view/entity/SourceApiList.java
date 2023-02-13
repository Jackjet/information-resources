package d1.project.api.integration.apimanage.view.entity;

import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Calendar;

/**
 * @author baozh
 */
@Entity
@Immutable
@Subselect("SELECT " +
        "d1_source_api.id AS id," +
        "d1_source_api.name AS source_api_name," +
        "d1_source_api.method AS method," +
        "d1_source_api.service_id AS service_id," +
        "d1_supplier.name AS supplier_name," +
        "d1_source_api.create_time AS create_time " +
        "FROM d1_source_api LEFT JOIN d1_supplier ON d1_source_api.supplier_id = d1_supplier.id")
public class SourceApiList {
    @Id
    private String id;

    private String method;

    private String serviceId;

    private String sourceApiName;

    private String supplierName;

    private Calendar createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSourceApiName() {
        return sourceApiName;
    }

    public void setSourceApiName(String sourceApiName) {
        this.sourceApiName = sourceApiName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }
}
