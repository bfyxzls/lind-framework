package com.lind.common.pattern.lockstatusmachine;

/**
 * 状态按着某个事件的触发而发生变化，而在某个状态下，也只能执行某个事件. 状态：Wait_Leader Wait_Manager Wait_HR Finish
 * 事件：LEADER_PASS MGR_PASS HR_PASS
 * @author lind
 * @date 2023/2/28 17:45
 * @since 1.0.0
 */
public enum TurnstileEvents {

	LEADER_PASS, MGR_PASS, HR_PASS

}
