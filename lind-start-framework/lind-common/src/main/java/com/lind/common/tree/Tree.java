package com.lind.common.tree;

import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

public interface Tree {

	String getId();

	String getParentId();

	String getTitle();

	Integer getType();

	List<List<? extends Tree>> getSons();

	void setSons(List<? extends Tree> sons);

	default void getTreeRzSons(List<? extends Tree> ones, List<? extends Tree> all) {
		for (Tree tree : ones) {
			if (tree != null) {
				List<Tree> sons = all.stream().filter(
						o -> o.getParentId() != null && o.getParentId().equals(tree.getId()) && o.getType() == 0)
						.collect(Collectors.toList());
				if (!CollectionUtils.isEmpty(sons)) {
					tree.setSons(sons);
					getTreeRzSons(sons, all);
				}
			}
		}
	}

}
