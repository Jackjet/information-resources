package d1.project.sample.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;

/**
 * @author Buter
 */
public class ResultUtil {
    private static final Logger logger = LoggerFactory.getLogger(ResultUtil.class);

    /**
     * 成功且带数据
     **/
    public static <T> Result<T> success(String msg, Object object) {
        return result(ResultCode.SUCCESS.code, msg, object);
    }

    public static <T> Result<T> success(String msg) {
        return success(msg, null);
    }

    public static <T> Result<T> result(Integer code) {
        return result(code, "");
    }

    public static <T> Result<T> result(Integer code, String msg) {
        return result(code, msg, null);
    }

    public static <T> Result<T> result(Integer code, String msg, Object object) {
        Result result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        if (object != null) {
            if (object instanceof Throwable) {
                StackTraceElement[] es = ((Throwable) object).getStackTrace();
                if (es != null && es.length > 0) {
                    result.setData(es[0]);
                }
            } else {
                result.setData(object);
            }
        }
        errorLog(code, msg, object);
        return result;
    }


    /**
     * 成功但不带数据
     **/
    public static <T> Result<T> success() {
        return success("", null);
    }

    public static <T> Result<T> fail() {
        return fail("", null);
    }

    /**
     * 失败
     **/
    public static <T> Result<T> fail(String msg, Object object) {
        return result(ResultCode.FAIL.code, msg, object);
    }

    public static <T> Result<T> fail(String msg) {
        return result(ResultCode.FAIL.code, msg, null);
    }

    public static <T> Result<T> fail(Exception object) {
        return fail(object.getMessage(), object);
    }

//---------------------------------------------------我没啥用------------------------------------------------------------//

    /**
     * 那些校验异常并不写到error.txt,其它的异常才写到error.txt，error.txt里需要经常review，确保解决里面的异常
     **/
    private static void errorLog(Integer code, String msg, Object object) {
        if (code == ResultCode.FAIL.code || code == ResultCode.UN_CATCH_ERROR.code) {
            if (object instanceof MethodArgumentNotValidException || object instanceof BindException) {
                logger.warn(msg, object);
            } else if (object instanceof Throwable) {
                logger.error(msg, (Throwable) object);
            } else {
                logger.error(msg, object);
            }
        }
    }
}
