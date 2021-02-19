package com.meme;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RedirectSwagger {
	
	@RequestMapping (value = "/swagger-ui" , method = RequestMethod.GET)
    public String home() {
	return "redirect:/swagger-ui.html";
    }

}