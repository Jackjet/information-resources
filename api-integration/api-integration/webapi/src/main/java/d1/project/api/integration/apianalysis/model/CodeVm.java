package d1.project.api.integration.apianalysis.model;

import java.util.List;

/**
 * @author baozh
 */
public class CodeVm {
    private List<NameNumVm> codeNum;
    private VisitResultVm visitNum;

    public List<NameNumVm> getCodeNum() {
        return codeNum;
    }

    public void setCodeNum(List<NameNumVm> codeNum) {
        this.codeNum = codeNum;
    }

    public VisitResultVm getVisitNum() {
        return visitNum;
    }

    public void setVisitNum(VisitResultVm visitNum) {
        this.visitNum = visitNum;
    }
}
