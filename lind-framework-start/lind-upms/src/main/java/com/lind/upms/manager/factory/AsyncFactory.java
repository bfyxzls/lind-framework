package com.lind.upms.manager.factory;

import com.lind.upms.constant.Constants;
import com.lind.upms.domain.SysLogininfor;
import com.lind.upms.domain.SysOperLog;
import com.lind.upms.service.ISysLogininforService;
import com.lind.upms.service.ISysOperLogService;
import com.lind.upms.utils.LogUtils;
import com.lind.upms.utils.ServletUtils;
import com.lind.upms.utils.StringUtils;
import com.lind.upms.utils.ip.AddressUtils;
import com.lind.upms.utils.ip.IpUtils;
import com.lind.upms.utils.spring.SpringUtils;
import eu.bitwalker.useragentutils.UserAgent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.TimerTask;

/**
 * 异步工厂（产生任务用）
 *
 * @author ruoyi
 */
public class AsyncFactory {

	private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");

	/**
	 * 记录登录信息
	 * @param username 用户名
	 * @param status 状态
	 * @param message 消息
	 * @param args 列表
	 * @return 任务task
	 */
	public static TimerTask recordLogininfor(final String username, final String status, final String message,
			final Object... args) {
		final UserAgent userAgent = UserAgent.parseUserAgentString(ServletUtils.getRequest().getHeader("User-Agent"));
		final String ip = IpUtils.getIpAddr();
		return new TimerTask() {
			@Override
			public void run() {
				String address = AddressUtils.getRealAddressByIP(ip);
				String s = LogUtils.getBlock(ip) + address + LogUtils.getBlock(username) + LogUtils.getBlock(status)
						+ LogUtils.getBlock(message);
				// 打印信息到日志
				sys_user_logger.info(s, args);
				// 获取客户端操作系统
				String os = userAgent.getOperatingSystem().getName();
				// 获取客户端浏览器
				String browser = userAgent.getBrowser().getName();
				// 封装对象
				SysLogininfor logininfor = new SysLogininfor();
				logininfor.setUserName(username);
				logininfor.setIpaddr(ip);
				logininfor.setLoginLocation(address);
				logininfor.setBrowser(browser);
				logininfor.setOs(os);
				logininfor.setMsg(message);
				// 日志状态
				if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
					logininfor.setStatus(Constants.SUCCESS);
				}
				else if (Constants.LOGIN_FAIL.equals(status)) {
					logininfor.setStatus(Constants.FAIL);
				}
				// 插入数据
				SpringUtils.getBean(ISysLogininforService.class).insertLogininfor(logininfor);
			}
		};
	}

	/**
	 * 操作日志记录
	 * @param operLog 操作日志信息
	 * @return 任务task
	 */
	public static TimerTask recordOper(final SysOperLog operLog) {
		return new TimerTask() {
			@Override
			public void run() {
				// 远程查询操作地点
				operLog.setOperLocation(AddressUtils.getRealAddressByIP(operLog.getOperIp()));
				SpringUtils.getBean(ISysOperLogService.class).insertSysOperLog(operLog);
			}
		};
	}

}
