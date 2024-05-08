package com.lind.common.othertest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.testng.Assert;

/**
 * @author lind
 * @date 2022/8/12 14:38
 * @since 1.0.0
 */
@Slf4j
public class StarTest {

	public static String replaceExtraStr(String value) {
		String regexPersonName = "周永康|薄熙来|徐才厚|令计划|郭伯雄|孙政才|鲁炜|苏荣|杨晶|王保安|苏树林|李崇禧|卢恩光|曹白隽|申维辰|朱明国|杨栋梁|周本顺|奚晓明|秦玉海|魏宏|苏宏章|田修思|吴天君|项俊波|吴爱英|杨崇勇|陈安众|李刚|王晓光|孙怀山|许前飞|杨焕宁|王三运|李立国|黄兴国|苏宏章|魏宏|王珉|房峰辉|项俊波|蒋洁敏|李春城|刘铁男|倪发科|郭永祥|王素毅|李达球|季建业|廖少华|陈柏槐|郭有明|陈安众|童名谦|李东生|杨刚|冀文林|祝作利|金道铭|沈培平|姚木根|毛小兵|谭栖伟|阳宝华|赵智勇|杜善学|令政策|万庆良|谭力|韩先聪|张田欣|武长顺|陈铁新|陈川平|聂春玉|白云|白恩培|任润厚|潘逸阳|秦玉海|何家成|赵少麟|杨金山|梁滨|隋凤富|王敏|韩学键|孙鸿志|杨卫泽|叶万勇|方文平|马建|陆武成|斯鑫良|许爱民|苑世军|景春华|栗智|仇和|徐建一|徐钢|郭伯雄|余远辉|韩志然|肖天|乐大克|张力军|赵黎平|谷春立|白雪山|艾宝俊|吕锡文|孙清云|姚刚|盖如垠|颜世元|刘志勇|曹建方|陈雪枫|龚清概|刘礼祖|贺家铁|刘志庚|卢子跃|王阳|李嘉|张力夫|杨鲁豫|李成云|张越|孔令中|杨振超|李云峰|赖德荣|尹海林|郑玉焯|陈树隆|张文雄|吴天君|卢恩光|虞海燕|窦玉沛|李文科|陈旭|杨崇勇|张化为|陈传书|周春雨|魏民洲|刘新齐|曲淑辉|刘善桥|张喜武|王宏江|周化辰|莫建成|沐华平|何挺|刘强|张杰辉|冯新柱|季缃绮|李贻煌|刘君|白向群|蒲波|努尔·白克力|靳绥东|李士祥|邱大明|王尔智|王铁|吴浈涉|艾文礼|曾志权|张少春|王晓光|陈质枫|王晓林|周部长|薄书记|徐副主席|总政徐|军区徐政委|总政治部徐|令部长|郭副主席|总参郭|军区郭司令|总参谋部郭|黄松有";

		if (StringUtils.isBlank(value)) {
			return "";
		}
		return value.replaceAll(regexPersonName, "***");// replaceAll支持|这种匹配,它支持正则表达式
	}

	public static String fgfToDh(String str) {
		return str.replaceAll(";|；|，|/", ",");
	}

	public static String removeEndFH(String str) {
		if (StringUtils.isBlank(str)) {
			return "";
		}
		else {
			do {
				if (StringUtils.isNotBlank(str) && str.charAt(str.length() - 1) == ';') {
					str = str.substring(0, str.length() - 1);
				}
			}
			while (str.length() > 0 && str.charAt(str.length() - 1) == ';');

			return str;
		}
	}

	@Test
	public void replaceExtraStrTest() {
		log.info(replaceExtraStr("孙政才不错"));
		Assert.assertEquals("***不错", replaceExtraStr("孙政才不错"));
		Assert.assertEquals("中国,强大了", fgfToDh("中国，强大了"));
		log.info(removeEndFH("孙政才不错;;;"));

	}

}
