package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	/**
	 * 	/page/item-add'			item-add.jsp
	 * 	/page/item-list			item-list.jsp
	 * 	/page/item-param-list'	item-param-list.jsp
	 * 	需求: 能否利用一个方法实现通用的页面处理.
	 * 	方法: restFul风格(一)
	 * 	语法:
	 * 		1.利用/方式进行参数分割
	 * 		2.利用{}括号的形式动态接受参数
	 * 		3.利用@PathVariable 动态接受参数
	 *
	 * 	方法: restFul风格(二)
	 * 		利用不同的请求类型,实现不同的业务功能
	 * 		查询业务		GET
	 * 		更新业务		PUT
	 * 		删除业务		DELETE
	 * 		新增业务		POST
	 */
	@RequestMapping("/page/{modelName}")
	public String itemAdd(@PathVariable String modelName){

		return modelName;
	}

}
