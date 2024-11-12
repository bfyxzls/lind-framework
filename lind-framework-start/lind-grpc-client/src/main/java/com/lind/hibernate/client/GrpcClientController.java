package com.lind.hibernate.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Michael (yidongnan@gmail.com)
 * @since 2016/12/4
 */
@RestController
public class GrpcClientController {

	@Autowired
	private GrpcClientService grpcClientService;

	@RequestMapping("/")
	public String printMessage(@RequestParam(defaultValue = "lind") String name) {
		return grpcClientService.sendMessage(name);
	}

}
