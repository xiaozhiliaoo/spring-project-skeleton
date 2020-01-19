package org.lili.api;

import com.alibaba.fastjson.JSON;
import com.google.common.io.CharStreams;
import org.apache.commons.lang3.StringUtils;
import org.lili.common.result.CommonResult;
import org.lili.common.result.WebApiResultType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author lili
 * @date 2020/1/18 20:02
 * @description
 */
@Controller
public class BaseApi {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String POST = "POST";

    private static final String GET = "GET";

    @Autowired
    private HttpServletRequest request;

    @ModelAttribute
    public void initBase(HttpServletRequest request, HttpServletResponse response, ModelMap model) {

    }


    public CommonResult success() {
        return CommonResult.Builder.SUCC().initSuccCodeAndMsg(WebApiResultType.Success.code,
                "");
    }

    public CommonResult success(Object object) {
        return CommonResult.Builder.SUCC().initSuccCodeAndMsg(WebApiResultType.Success.code, WebApiResultType.Success.message).initSuccData(object);
    }

    public CommonResult fail() {
        return CommonResult.Builder.FAIL().initErrCodeAndMsg(WebApiResultType.SystemError.getCode(),
                WebApiResultType.SystemError.getMessage());
    }


    /**
     * 出错时候打印错误参数，以及错误日志，正常情况不需要打印日志
     *
     * @param e 异常
     */
    protected void errorWithParams(Exception e) {
        String method = request.getMethod();
        String params = "";
        if (GET.equalsIgnoreCase(method)) {
            params = JSON.toJSONString(request.getParameterMap());
        } else if (POST.equalsIgnoreCase(method)) {
            try {
                params = CharStreams.toString(request.getReader());
                if (StringUtils.isBlank(params)) {
                    params = JSON.toJSONString(request.getParameterMap());
                }
            } catch (Exception convertException) {
                //ignore the exception
            }
        }
        String requestURI = request.getRequestURI();
        logger.error("uri is:{}, params:{}, error:{}", requestURI, params, e);
    }


}
