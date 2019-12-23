package sg.edu.nus.catest2.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("home")
public class MainController {
	
	@RequestMapping("/")
	public String home() {
		return "homepage";
	}
	
	@RequestMapping("/calendar")
	public String calendar() {
		return "homecalendar";
	}
	
	@RequestMapping("/news")
	public String news() {
		return "homenews";
	}
	
	@RequestMapping("/aboutus")
	public String aboutus() {
		return "homeaboutus";
	}
	
	@RequestMapping("/contactus")
	public String contactus() {
		return "homecontactus";
	}
}
