package org.lili.common.web;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.support.WebBindingInitializer;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

/**
 * 数据绑定初始化类
 * 
 */
public class BindingInitializer implements WebBindingInitializer {
	@Override
	public void initBinder(WebDataBinder binder, WebRequest request) {
		binder.registerCustomEditor(Date.class, new DateTypeEditor());
	}
}
