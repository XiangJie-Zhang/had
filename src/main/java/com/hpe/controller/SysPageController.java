package com.hpe.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


/**   
 * @ClassName:  SysPageController   
 * @Description:系统页面视图  
 * @author: lilong
 * @date:   2018年1月19日 下午3:12:17       
 */  
@Controller
public class SysPageController {
	
	@RequestMapping("{module}/{url}.html")
	public String module(@PathVariable("module") String module, @PathVariable("url") String url){
		return  module + "/" + url + ".html";
	}
	@RequestMapping("{url}.html")
	public String url(@PathVariable("url") String url){
		return url + ".html";
	}
	@RequestMapping("/")
	public String index(){
		return "redirect:index.html";
	}

	@RequestMapping("{url}.tjsp")
	public String login(@PathVariable("url") String url){
		System.out.println(url);
		return url;
	}
}
