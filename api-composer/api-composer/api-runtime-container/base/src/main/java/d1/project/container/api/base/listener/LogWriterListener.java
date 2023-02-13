package d1.project.container.api.base.listener;

public interface LogWriterListener {
    void info(String apiId, String message);

    void error(String apiId, Throwable e);
}
