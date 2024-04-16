package com.lind.common.tree;

import lombok.SneakyThrows;
import org.springframework.beans.BeanUtils;
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

	/**
	 * 构建当前元素的孩子节点
	 * @param allList
	 * @return
	 * @param <T>
	 */
	@SneakyThrows
	default <T extends Tree> T covert(List<T> allList) {
		Class<T> clazz = (Class<T>) this.getClass();
		T node = clazz.newInstance(); // 初始化新的T对象
		BeanUtils.copyProperties(this, node);
		List<Tree> children = allList.stream().filter(subItem -> subItem.getParentId().equals(this.getId()))
				.map(subItem -> subItem.covert(allList)).collect(Collectors.toList());
		node.setSons(children);
		return node;
	}

}
