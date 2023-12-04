/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lind.codegen.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lind.codegen.entity.GenFormConf;
import com.lind.codegen.service.GenFormConfService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 表单管理
 *
 * @author lengleng
 * @date 2019-08-12 15:55:35
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/form")
public class GenFormConfController {

	private final GenFormConfService genRecordService;

	/**
	 * 分页查询
	 * @param page 分页对象
	 * @param formConf 生成记录
	 * @return
	 */
	@GetMapping("/page")
	public ResponseEntity<IPage<GenFormConf>> getGenFormConfPage(Page page, GenFormConf formConf) {
		return ResponseEntity.ok(genRecordService.page(page, Wrappers.query(formConf)));
	}

	/**
	 * 通过id查询生成记录
	 * @param id id
	 * @return R
	 */
	@GetMapping("/{id}")
	public ResponseEntity<GenFormConf> getById(@PathVariable("id") Integer id) {
		return ResponseEntity.ok(genRecordService.getById(id));
	}

	/**
	 * 通过id查询生成记录
	 * @param dsName 数据源ID
	 * @param tableName tableName
	 * @return R
	 */
	@GetMapping("/info")
	public ResponseEntity<String> form(String dsName, String tableName) {
		return ResponseEntity.ok(genRecordService.getForm(dsName, tableName));
	}

	/**
	 * 新增生成记录
	 * @param formConf 生成记录
	 * @return R
	 */
	@PostMapping
	public ResponseEntity<Boolean> save(@RequestBody GenFormConf formConf) {
		return ResponseEntity.ok(genRecordService.save(formConf));
	}

	/**
	 * 通过id删除生成记录
	 * @param id id
	 * @return R
	 */
	@DeleteMapping("/{id}")
	public ResponseEntity<Boolean> removeById(@PathVariable Long id) {
		return ResponseEntity.ok(genRecordService.removeById(id));
	}

}
