package io.github.u2ware.spring.oauth2.resource.sample;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {

    @RequestMapping("/resource") @ResponseBody
    public String resource(){
        return "resource: "+System.currentTimeMillis();
    }
}
