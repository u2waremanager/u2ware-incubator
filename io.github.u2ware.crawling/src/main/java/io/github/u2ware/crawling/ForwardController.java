package io.github.u2ware.crawling;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ForwardController {

	@RequestMapping(value = "{path:[^\\\\.]*}")
	public String html5Forwarding() {
		return "forward:/index.html";
	}
	
}
