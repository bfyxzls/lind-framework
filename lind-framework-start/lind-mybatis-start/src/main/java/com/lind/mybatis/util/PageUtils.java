package com.lind.mybatis.util;

import com.github.pagehelper.PageHelper;
import com.lind.common.core.util.SqlUtil;
import com.lind.common.core.util.StringUtils;

/**
 * 分页工具类
 *
 * @author ruoyi
 */
public class PageUtils extends PageHelper {

	/**
	 * 设置请求分页数据
	 */
	public static void startPage() {
		PageDomain pageDomain = TableSupport.buildPageRequest();
		Integer pageNum = pageDomain.getPageNum();
		Integer pageSize = pageDomain.getPageSize();
		String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
		Boolean reasonable = pageDomain.getReasonable();
		PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
	}

	/**
	 * 清理分页的线程变量
	 */
	public static void clearPage() {
		PageHelper.clearPage();
	}


	/**
	 * 设置请求排序数据
	 */
	protected void startOrderBy() {
		PageDomain pageDomain = TableSupport.buildPageRequest();
		if (StringUtils.isNotEmpty(pageDomain.getOrderBy())) {
			String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
			PageHelper.orderBy(orderBy);
		}
	}

}
