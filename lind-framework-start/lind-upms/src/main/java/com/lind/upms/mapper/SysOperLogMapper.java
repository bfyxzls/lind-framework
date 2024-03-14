package com.lind.upms.mapper;

import com.lind.upms.domain.SysOperLog;

import java.util.List;

/**
 * 操作日志记录Mapper接口
 *
 * @author ruoyi
 * @date 2023-08-27
 */
public interface SysOperLogMapper {

	/**
	 * 查询操作日志记录
	 * @param operId 操作日志记录主键
	 * @return 操作日志记录
	 */
	SysOperLog selectSysOperLogByOperId(Long operId);

	/**
	 * 查询操作日志记录列表
	 * @param sysOperLog 操作日志记录
	 * @return 操作日志记录集合
	 */
	List<SysOperLog> selectSysOperLogList(SysOperLog sysOperLog);

	/**
	 * 新增操作日志记录
	 * @param sysOperLog 操作日志记录
	 * @return 结果
	 */
	int insertSysOperLog(SysOperLog sysOperLog);

	/**
	 * 修改操作日志记录
	 * @param sysOperLog 操作日志记录
	 * @return 结果
	 */
	int updateSysOperLog(SysOperLog sysOperLog);

	/**
	 * 删除操作日志记录
	 * @param operId 操作日志记录主键
	 * @return 结果
	 */
	int deleteSysOperLogByOperId(Long operId);

	/**
	 * 批量删除操作日志记录
	 * @param operIds 需要删除的数据主键集合
	 * @return 结果
	 */
	int deleteSysOperLogByOperIds(Long[] operIds);

}
