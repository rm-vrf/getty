package com.iflytek.demo

import javax.annotation.*
import javax.servlet.http.*

import org.springframework.stereotype.*
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.*

@Controller
public class TripController {
	
	@Resource(name = "tripService")
	def tripService;
	
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@ResponseBody
	def test() {
		def map = new HashMap<String, Object>();
		map.put("ok", true);
		map.put("code", 200);
		map.put("message", tripService.execute(100));
		return map;
	}
}
