package d1.project.container.api.configuration;


import d1.project.container.api.http.Result;
import d1.project.container.api.http.ResultUtil;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一拦截http请求返回的异常，统一处理
 *
 * @author Buter
 */
@ControllerAdvice
public class MyControllerAdvice {

    /**
     * 如果是未处理的异常，返回500错误
     *
     * @param e 未处理的Exception
     * @return 返回http的500错误
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result all(Exception e) {
//        if (e instanceof DoValidException) {
//            return ResultUtil.fail(e.getMessage(), e);
//        }
        return ResultUtil.fail("未处理异常", e);
    }


    /**
     * 通过@Valid来标识一个Post请求的 @RequestBody的对象，通过@NotBlank（message="xx")等注解来标识一个属性
     *
     * @param e 捕获的Valid异常
     * @return 直接返回注解里的message信息
     */
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result errorNotValidHandler(MethodArgumentNotValidException e) {
        String errorMsg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResultUtil.fail(errorMsg);
    }

    /**
     * 通过@Valid来标识一个Get请求的参数对象，通过@NotBlank（message="xx")等注解来标识一个属性
     *
     * @param e 捕获的Valid异常
     * @return 直接返回注解里的message信息
     */
    @ResponseBody
    @ExceptionHandler(value = BindException.class)
    public Result errorBindHandler(BindException e) {
        String errorMsg = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return ResultUtil.fail(errorMsg);
    }
}
