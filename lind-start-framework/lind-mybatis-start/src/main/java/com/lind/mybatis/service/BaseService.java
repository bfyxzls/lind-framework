package com.lind.mybatis.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

/**
 * 业务基类.
 */
@FunctionalInterface
public interface BaseService<E> {

	/**
	 * 需要子类中把方法实现注入进来.
	 * @return
	 */
	BaseMapper<E> getRepository();

	/**
	 * 根据ID获取.
	 * @param id 主键
	 * @return
	 */
	default E get(String id) {
		return getRepository().selectById(id);
	}

	/**
	 * 保存.
	 * @param entity 实体
	 * @return
	 */
	default Integer insert(E entity) {
		return getRepository().insert(entity);
	}

	/**
	 * 修改.
	 * @param entity 实体
	 * @return
	 */
	default Integer update(E entity) {
		return getRepository().updateById(entity);
	}

	/**
	 * 修改.
	 * @param entity 实体
	 * @return
	 */
	default Integer update(Wrapper<E> entityWrapper, E entity) {
		return getRepository().update(entity, entityWrapper);
	}

	/**
	 * 删除.
	 * @param id 主键
	 */
	default void delete(String id) {
		getRepository().deleteById(id);
	}

	/**
	 * 批量删除.
	 * @param idList 列表
	 */
	default void delete(List<String> idList) {
		getRepository().deleteBatchIds(idList);
	}

	/**
	 * 根据条件查询获取.
	 * @param entityWrapper 条件
	 * @return
	 */
	default List<E> findAll(Wrapper<E> entityWrapper) {
		return getRepository().selectList(entityWrapper);
	}

	/**
	 * 分页获取.
	 * @param pageNum 页号
	 * @param pageSize 每页记录数
	 * @param entityWrapper 条件
	 * @return
	 */
	default IPage<E> findAll(int pageNum, int pageSize, Wrapper<E> entityWrapper) {
		Page<E> page = new Page<>(pageNum, pageSize);
		IPage<E> results = getRepository().selectPage(page, entityWrapper);
		return results;
	}

	/**
	 * 获取查询条件的结果数
	 * @param entityWrapper 条件
	 * @return
	 */
	default long count(Wrapper<E> entityWrapper) {
		return getRepository().selectCount(entityWrapper);
	}

}
