package d1.project.container.api.http;


/**
 * 响应码枚举，参考HTTP状态码的语义
 *
 * @author Buter
 */
public enum ResultCode {
    /**
     * 成功
     */
    SUCCESS(1),
    /**
     * 失败
     */
    FAIL(0);
    public final int code;

    ResultCode(int code) {
        this.code = code;
    }
}