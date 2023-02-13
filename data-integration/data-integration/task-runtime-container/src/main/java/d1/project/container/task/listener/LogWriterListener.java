package d1.project.container.task.listener;

public interface LogWriterListener {
    void info(String taskId, String message);

    void error(String taskId, Throwable e);
}
