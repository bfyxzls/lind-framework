package com.lind.common.tree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lind
 * @date 2023/5/8 14:28
 * @since 1.0.0
 */
public class TreeTest {

	@Test
	public void sons() {
		List<NormalTree> normalTreeList = new ArrayList<>();
		normalTreeList.add(NormalTree.builder().id("1").parentId("0").title("1").type(0).build());
		normalTreeList.add(NormalTree.builder().id("2").parentId("1").title("2").type(0).build());
		normalTreeList.add(NormalTree.builder().id("3").parentId("1").title("3").type(0).build());
		normalTreeList.add(NormalTree.builder().id("4").parentId("2").title("4").type(0).build());
		normalTreeList.add(NormalTree.builder().id("5").parentId("2").title("5").type(0).build());

		List<NormalTree> ones = normalTreeList.stream().filter(o -> o.parentId.equals("0"))
				.collect(Collectors.toList());
		new NormalTree().fillTreeRzSons(ones, normalTreeList);

		System.out.println(ones);

		List<NormalTree> oneLevelRecords = normalTreeList.stream().filter(item -> item.getParentId().equals("0"))
				.map(item -> item.covert(normalTreeList)).collect(Collectors.toList());

		List<NormalTree> oneLevelRecords2 = normalTreeList.stream().filter(item -> item.getParentId().equals("0"))
				.map(item -> covert(item, normalTreeList)).collect(Collectors.toList());
		System.out.println(oneLevelRecords);
	}

	/**
	 * 初始对象转化为节点对象
	 */
	private NormalTree covert(NormalTree item, List<NormalTree> allList) {
		NormalTree node = new NormalTree();
		BeanUtils.copyProperties(item, node);
		List<NormalTree> children = allList.stream().filter(subItem -> subItem.getParentId().equals(item.getId()))
				.map(subItem -> covert(subItem, allList)).collect(Collectors.toList());
		node.setSons(children);
		return node;
	}

	@Data
	@Builder(toBuilder = true)
	@AllArgsConstructor
	@NoArgsConstructor
	static class NormalTree implements Tree {

		private String id;

		private String parentId;

		private String title;

		private Integer type;

		private List<? extends Tree> sons;

	}

}
