package com.lind.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class RegexTest {

	private static final Logger logger = LoggerFactory.getLogger(RegexTest.class);

	private static final Pattern DOT = Pattern.compile("\\.");

	private static final Pattern AUTHORIZATION_PATTERN = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-:._~+/]+=*)$",
			Pattern.CASE_INSENSITIVE);

	@Test
	public void authCode() {
		String[] parsed = DOT.split("abc.123.400.aa", 3);// 限制3位，这个数组被分为abc,123,400.aa，最后一位.就不进行拆分了
		logger.info("{}", parsed);
	}

	@Test
	public void bearer() {
		String token = "Bearer eyJhbGciOiJIUzI1NiIsInR5c+";
		Matcher matcher = AUTHORIZATION_PATTERN.matcher(token);
		if (!matcher.matches()) {
			log.error("Bearer token is malformed");
		}
		else {
			log.info("Bearer token={}", matcher.group("token"));
		}
	}

	@Test
	public void trombone() {
		String result = "现决定废止2006年3月7日公布的《中华人民共和国上海海关公告2006年第4号》（上海海关关于对承运跨关区公路转关运输货物的监管车辆驾驶员实施IC卡管理试点的公告）、2007年3月22日公布的《中华人民共和国上海海关公告2007年第5号》（上海海关关于办理上海地区出境展览品海关业务的公告）、2011年12月31日公布的《中华人民共和国上海海关公告2011年第3号》（上海海关关于试行进口货物价格预审核管理的公告）、2012年4月17日公布的《中华人民共和国上海海关公告2012年第2号》（上海海关上海出入境检验检疫局关于实施通关单无纸化试点的公告）、2012年6月19日公布的《中华人民共和国上海海关公告2012年第5号》（上海海关关于实施原产地预确定的公告）、2014年4月21日公布的《中华人民共和国上海海关公告2014年第13号》（上海海关关于开展跨境贸易电子商务进口业务的公告）、2014年6月26日公布的《中华人民共和国上海海关公告2014年第27号》（上海海关关于在中国（上海）自由贸易试验区内推进海关AEO互认工作的公告）、2014年12月1日公布的《中华人民共和国上海海关公告2014年第43号》（上海海关关于实施《中华人民共和国海关企业信用管理暂行办法》相关事项的公告）、2014年12月5日公布的《中华人民共和国上海海关公告2014年第44号》（上海海关关于在中国（上海）自由贸易试验区开展“自主报税、自助通关、自动审放、重点稽核\\\"改革项目试点的公告）、2015年5月6日公布的《中华人民共和国上海海关公告2015年第3号》（上海海关关于企业报送《报关单位注册信息年度报告》相关事项的公告）、2015年5月25日公布的《中华人民共和国上海海关公告2015年第4号》（上海海关关于优化海运口岸出口货物夜间、节假日加急查验工作的公告）、2015年6月16日公布的《中华人民共和国上海海关公告2015年第5号》（上海海关关于发布《中国（上海）自由贸易试验区海关行政权力清单（2015版）》、《中国（上海）自由贸易试验区海关行政责任清单（2015版）》的公告）、2015年7月23日公布的《中华人民共和国上海海关公告2015年第12号》（中华人民共和国上海海关中华人民共和国上海出入境检验检疫局上海市口岸服务办公室公告）、2015年6月10日公布的《上海出入境检验检疫局公告2015年第4号》（上海出入境检验检疫局上海海关关于自贸区实施通关单无纸化的公告）、2015年7月20日公布的《上海出入境检验检疫局公告2015年第11号》（关于支持上海国际旅游度假区及迪士尼项目建设的公告）、2016年5月30日公布的《上海出入境检验检疫局公告2016年第5号》（上海出入境检验检疫局上海海关关于出口加工区实施通关单无纸化的公告）、2016年9月20日公布的《上海出入境检验检疫局公告2016年第12号》（关于免于核查输出国家或地区动植物检疫证书的清单（第二版）的公告）";
		String regex = "、(?![^\\（]*\\）)";
		String[] split = result.split(regex);
		for (String splitStr : split) {
			logger.info("{}", splitStr);
		}
	}

}
