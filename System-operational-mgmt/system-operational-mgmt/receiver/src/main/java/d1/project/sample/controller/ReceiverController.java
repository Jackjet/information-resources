package d1.project.sample.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.project.sample.http.Result;
import d1.project.sample.http.ResultUtil;
import d1.project.sample.model.ReceiverVm;
import d1.project.sample.service.ReceiverService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author lin
 */
@RestController
@RequestMapping("/receiver")
public class ReceiverController {
    @Autowired
    private ReceiverService receiverService;

    @RequestMapping(value = "/write", method = RequestMethod.POST)
    public Result<String> write(@RequestBody ReceiverVm model) {
        try {
            receiverService.writeFile(model);
            return ResultUtil.success("成功");
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }

    @RequestMapping(value = "/read", method = RequestMethod.GET)
    public Result<List<String>> read() {
        try {
            return ResultUtil.success("成功",receiverService.readFile());
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(), e);
        }
    }
}
