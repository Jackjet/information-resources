package d1.project.d1project.business.dao;

import d1.project.d1project.business.entity.DataQualityReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface DataQualityReportDao extends JpaRepository<DataQualityReport, String>, JpaSpecificationExecutor<DataQualityReport> {
}
