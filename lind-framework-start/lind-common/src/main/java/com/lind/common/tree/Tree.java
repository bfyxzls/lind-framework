package com.lind.common.tree;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public interface Tree {

	String getId();

	String getParentId();

	String getTitle();

	Integer getType();

	List<? extends Tree> getSons();

	void setSons(List<? extends Tree> sons);

	/**
	 * 填充一级节点的子节点.
	 * @param ones 一级节点
	 * @param all 所有节点
	 */
	default void fillTreeRzSons(List<? extends Tree> ones, List<? extends Tree> all) {
		for (Tree tree : ones) {
			if (tree != null) {
				List<Tree> sons = all.stream().filter(
						o -> o.getParentId() != null && o.getParentId().equals(tree.getId()) && o.getType() == 0)
						.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(sons)) {
					tree.setSons(sons);
					fillTreeRzSons(sons, all);
				}
			}
		}
	}

}
