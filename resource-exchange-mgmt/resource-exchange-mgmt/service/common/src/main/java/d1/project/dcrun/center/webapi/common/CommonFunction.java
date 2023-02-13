package d1.project.dcrun.center.webapi.common;

import com.alibaba.fastjson.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author Buter
 * @date 2020/3/8 9:01
 */
@Component
public class CommonFunction {
    private Function<String, String> getAppkeyByAppidFunction;
    private BiFunction<JpaSpecificationExecutor, JSONObject, Page> findAllControlCenterFunction;
    private Supplier<String> getHostsSupplier;

    private Function<String, JSONObject> getByAppidFunction;

    public Supplier<String> getGetHostsSupplier() {
        return getHostsSupplier;
    }

    public void setGetHostsSupplier(Supplier<String> getHostsSupplier) {
        this.getHostsSupplier = getHostsSupplier;
    }

    public Function<String, String> getGetAppkeyByAppidFunction() {
        return getAppkeyByAppidFunction;
    }

    public void setGetAppkeyByAppidFunction(Function<String, String> getAppkeyByAppidFunction) {
        this.getAppkeyByAppidFunction = getAppkeyByAppidFunction;
    }

    public BiFunction<JpaSpecificationExecutor, JSONObject, Page> getFindAllControlCenterFunction() {
        return findAllControlCenterFunction;
    }

    public void setFindAllControlCenterFunction(BiFunction<JpaSpecificationExecutor, JSONObject, Page> findAllControlCenterFunction) {
        this.findAllControlCenterFunction = findAllControlCenterFunction;
    }

    public Function<String, JSONObject> getGetByAppidFunction() {
        return getByAppidFunction;
    }

    public void setGetByAppidFunction(Function<String, JSONObject> getByAppidFunction) {
        this.getByAppidFunction = getByAppidFunction;
    }
}
