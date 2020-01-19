package org.lili.api;

import org.lili.cache.RedisClientTemplate;
import org.lili.common.entity.BaseUser;
import org.lili.common.result.CommonResult;
import org.lili.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author lili
 * @date 2020/1/18 20:02
 * @description
 */
@Controller
@RequestMapping(value = "/user")
public class UserApi extends BaseApi {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisClientTemplate cache;

    @RequestMapping(value = "/user_name", method = RequestMethod.POST, produces = "application/json;charset=utf-8")
    @ResponseBody
    public CommonResult user_name(@RequestBody Map<String, Object> requestMap,
                                  HttpServletRequest request, HttpServletResponse response, ModelMap model) {
        BaseUser user = userService.findById(1);
        cache.set("lili", "lili");
        return success(user);
    }

}
