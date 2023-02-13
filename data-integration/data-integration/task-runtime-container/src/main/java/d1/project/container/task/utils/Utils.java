package d1.project.container.task.utils;

import java.util.Locale;
import java.util.UUID;

public class Utils {
    public static String getUid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.ROOT);
    }
}
