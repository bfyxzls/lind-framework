package com.lind.common.tree;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

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
