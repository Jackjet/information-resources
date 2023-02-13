package d1.project.container.api.factory;

import net.dcrun.component.security.HmacSignService;

/**
 * @author maorui
 */
public class ServiceFactory {

    public static HmacSignService hmacSignService;

    public synchronized static HmacSignService getHmacSignService() {
        if (hmacSignService == null) {
            hmacSignService = new HmacSignService();
        }
        return hmacSignService;
    }


}
