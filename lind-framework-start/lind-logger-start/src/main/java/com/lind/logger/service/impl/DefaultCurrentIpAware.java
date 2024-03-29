package com.lind.logger.service.impl;

import com.lind.common.core.util.IpInfoUtils;
import com.lind.logger.service.CurrentIpAware;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;

public class DefaultCurrentIpAware implements CurrentIpAware {

	@Autowired
	IpInfoUtils ipInfoUtil;

	@Autowired(required = false)
	private HttpServletRequest request;

	@Override
	public String address() {
		if (request != null)
			return ipInfoUtil.getIpAddr(request);
		return "127.0.0.1";
	}

}
