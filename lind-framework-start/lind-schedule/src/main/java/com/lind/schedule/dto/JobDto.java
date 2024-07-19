package com.lind.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * job对象
 */
@Data
@ToString
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class JobDto {

	private String jobClassName;

	private String jobName;

	private String groupName;

	private String triggerName;

	private String triggerGroupName;

	private String cronExpression;

	// 下面是只有返回时的字段
	private String desc;

	private String time;

	private String status;

}
