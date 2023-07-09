package com.format.management.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;


@RestController
@RequestMapping("/")
public class HomeController {
	
	static String frontEnd = "http://localhost:8000/";
	RedirectView redirectview = new RedirectView();
	
	@GetMapping
	public RedirectView home() {
		redirectview.setUrl(frontEnd);
		return redirectview;
	}
	
}
