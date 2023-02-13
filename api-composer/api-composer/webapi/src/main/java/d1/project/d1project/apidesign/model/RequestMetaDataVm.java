package d1.project.d1project.apidesign.model;

import java.util.List;

/**
 * http request 请求的元数据
 */
public class RequestMetaDataVm {


    /**
     * request携带的数据，包含 header、parameter、body
     */
    private List<RequestMetaDataItemVm> payload;

    public List<RequestMetaDataItemVm> getPayload() {
        return payload;
    }

    public void setPayload(List<RequestMetaDataItemVm> payload) {
        this.payload = payload;
    }
}
