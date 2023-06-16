package tw.niq.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(HomeController.REQUEST_PATH)
public class HomeController {
	
	public static final String REQUEST_PATH = "/home";
	public static final String TEMPLATE_PATH = "home/";

	@GetMapping
	public String home() {
		return TEMPLATE_PATH + "home";
	}
	
}
