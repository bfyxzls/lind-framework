package com.lind.common;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static com.lind.common.util.HtmlUtils.delHTMLTag;
import static com.lind.common.util.HtmlUtils.getTextFromHtml;

@Slf4j
public class HtmlUtilsTest {

	@Test
	public void htmlDel() {
		String str = "<div style='text-align:center;'> 整治“四风”   清弊<fdafd32除>垢<br/><span style='font-size:14px;'> </span><span style='font-size:18px;'>公司召开党的群众路线教育实践活动动员大会</span><br/></div>";
		System.out.println(getTextFromHtml(str));

		String str2 = "<p align='center'><font class='MTitle'>蔡某某开设赌场一审刑事判决书</font></p><br><div align=center>上海市嘉定区人民法院<br>刑事判决书<br></div><br><br><div align=right>(2016)沪0114刑初992号</div><br><br>公诉机关上海市嘉定区人民检察院。<br>被告人蔡某X，女，1983年1月20日出生，汉族，户籍所在地河南省，暂住上海市嘉定区。<br>上海市嘉定区人民检察院以沪嘉检诉刑诉[2016]979号起诉书指控被告人蔡某X犯开设赌场罪，于2016年6月20日向本院提起公诉。<br>本院受理后，依法适用简易程序，实行独任审判，公开开庭审理了本案。<br>上海市嘉定区人民检察院指派代理检察员陈某某出庭支持公诉，被告人蔡某X到庭参加诉讼。<br>现已审理终结。<br>上海市嘉定区人民检察院指控，2014年7月起，被告人蔡某X租赁嘉定区江桥镇金耀南路XXX号，后为牟取非法利益，购置9台“晃晃麻将”机在上址，开设棋牌室，以“晃晃麻将”的形式招揽他人赌博，并从每桌每副抽头人民币2元渔利。<br>2015年6月18日下午，被告人蔡某X再次在上址以“晃晃麻将”形式先后聚集王某某等24人赌博。<br>至当日16时30分许，公安机关至该处抓获被告人蔡某X及参赌人员，缴获营业款人民币1900元、赌具“晃晃麻将”专用机9台、充卡机1台及IC卡100张等。<br>被告人蔡某X到案后如实供述了犯罪事实。<br>上述事实，被告人蔡某X在开庭审理过程中亦无异议，并有证人程某某、严某某、王某某等人的证言，相关辨认笔录及照片，公安机关检查笔录、清点记录、扣押清单、工作情况，有关现场照片，房屋租赁合同，出生医学证明，被告人蔡某X的户籍资料及有关供述等证据证实，足以认定。<br>本院认为，公诉机关指控被告人蔡某X以营利为目的，开设赌场，聚众赌博，其行为已触犯刑律，构成开设赌场罪的事实清楚，证据确实、充分，所控罪名成立，应依法惩处。<br>公诉机关关于被告人蔡某X到案后能如实供述自己的罪行，可从轻处罚的意见，合法有据，本院予以支持。<br>据此，依照《中华人民共和国刑法》第三百零三条第二款、第六十七条第三款、第七十二条、第七十三条第二款、第三款、第六十四条之规定，判决如下：一、被告人蔡某X犯开设赌场罪，判处有期徒刑一年，缓刑一年，罚金人民币一万元；(缓刑考验期限，从判决确定之日起计算。<br>)(罚金应于本判决生效之日起十日内缴纳。<br>)二、赌资、赌具予以没收。<br>被告人蔡某X回到社区后，应当遵守法律、法规，服从监督管理，接受教育，完成公益劳动，做一名有益社会的公民。<br>如不服本判决，可在接到判决书的第二日起十日内，通过本院或者直接向上海市第二中级人民法院提出上诉。<br>书面上诉的，应当提交上诉状正本一份，副本两份。<br><div align=right><br>审判员　　项永明<br>二〇一六年七月七日<br>书记员　　姚布依<br></div>附：相关法律条文《中华人民共和国刑法》第三百零三条以营利为目的，聚众赌博或者以赌博为业的，处三年以下有期徒刑、拘役或者管制，并处罚金。<br>开设赌场的，处三年以下有期徒刑、拘役或者管制，并处罚金；情节严重的，处三年以上十年以下有期徒刑，并处罚金。<br>第六十七条……犯罪嫌疑人虽不具有前两款规定的自首情节，但是如实供述自己罪行的，可以从轻处罚；因其如实供述自己罪行，避免特别严重后果发生的，可以减轻处罚。<br>第七十二条对于被判处拘役、三年以下有期徒刑的犯罪分子，同时符合下列条件的，可以宣告缓刑，对其中不满十八周岁的人、怀孕的妇女和已满七十五周岁的人，应当宣告缓刑：（一）犯罪情节较轻；（二）有悔罪表现；（三）没有再犯罪的危险；（四）宣告缓刑对所居住社区没有重大不良影响。<br>宣告缓刑，可以根据犯罪情况，同时禁止犯罪分子在缓刑考验期限内从事特定活动，进入特定区域、场所，接触特定的人。<br>被宣告缓刑的犯罪分子，如果被判处附加刑，附加刑仍须执行。<br>第七十三条拘役的缓刑考验期限为原判刑期以上一年以下，但是不能少于二个月。<br>有期徒刑的缓刑考验期限为原判刑期以上五年以下，但是不能少于一年。<br>缓刑考验期限，从判决确定之日起计算。<br>第五十三条罚金在判决指定的期限内一次或者分期缴纳。<br>期满不缴纳的，强制缴纳。<br>对于不能全部缴纳罚金的，人民法院在任何时候发现被执行人有可以执行的财产，应当随时追缴。<br>由于遭遇不能抗拒的灾祸等原因缴纳确实有困难的，经人民法院裁定，可以延期缴纳、酌情减少或者免除。<br>第六十四条犯罪分子违法所得的一切财物，应当予以追缴或者责令退赔；对被害人的合法财产，应当及时返还；违禁品和供犯罪所用的本人财物，应当予以没收。<br>没收的财物和罚金，一律上缴国库，不得挪用和自行处理。<br>";
		str2 = str2.replaceAll("<br>", "\r\n").replaceAll("</br>", "\r\n");
		String s = delHTMLTag(str2);

		System.out.println(s);
	}

}