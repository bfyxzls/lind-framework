package com.lind.upms.controller;

import com.lind.upms.annotation.Log;
import com.lind.upms.core.controller.BaseController;
import com.lind.upms.core.domain.AjaxResult;
import com.lind.upms.core.page.TableDataInfo;
import com.lind.upms.domain.SysNotice;
import com.lind.upms.enums.BusinessType;
import com.lind.upms.service.ISysNoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 公告 信息操作处理
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/notice")
public class SysNoticeController extends BaseController {

	@Autowired
	private ISysNoticeService noticeService;

	/**
	 * 获取通知公告列表
	 */
	@PreAuthorize("@ss.hasPermi('system:notice:list')")
	@GetMapping("/list")
	public TableDataInfo list(SysNotice notice) {
		startPage();
		List<SysNotice> list = noticeService.selectNoticeList(notice);
		return getDataTable(list);
	}

	/**
	 * 根据通知公告编号获取详细信息
	 */
	@PreAuthorize("@ss.hasPermi('system:notice:query')")
	@GetMapping(value = "/{noticeId}")
	public AjaxResult getInfo(@PathVariable Long noticeId) {
		return success(noticeService.selectNoticeById(noticeId));
	}

	/**
	 * 新增通知公告
	 */
	@PreAuthorize("@ss.hasPermi('system:notice:add')")
	@Log(title = "通知公告", businessType = BusinessType.INSERT)
	@PostMapping
	public AjaxResult add(@Validated @RequestBody SysNotice notice) {
		notice.setCreateBy(getUsername());
		return toAjax(noticeService.insertNotice(notice));
	}

	/**
	 * 修改通知公告
	 */
	@PreAuthorize("@ss.hasPermi('system:notice:edit')")
	@Log(title = "通知公告", businessType = BusinessType.UPDATE)
	@PutMapping
	public AjaxResult edit(@Validated @RequestBody SysNotice notice) {
		notice.setUpdateBy(getUsername());
		return toAjax(noticeService.updateNotice(notice));
	}

	/**
	 * 删除通知公告
	 */
	@PreAuthorize("@ss.hasPermi('system:notice:remove')")
	@Log(title = "通知公告", businessType = BusinessType.DELETE)
	@DeleteMapping("/{noticeIds}")
	public AjaxResult remove(@PathVariable Long[] noticeIds) {
		return toAjax(noticeService.deleteNoticeByIds(noticeIds));
	}

}
