package com.yuantek.myblog.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/test")
public class HelloController {

	@GetMapping("/bs")
	public String bootstrap() {
		return "/test/bs";
	}

}
