package com.lind.mybatis.enums;

/**
 * @author lind
 * @date 2024/4/23 15:38
 * @since 1.0.0
 */
public enum UserStatus {

	online(0, "在线"), offline(1, "离线"), unknown(2, "未知");

	private int status;

	private String desc;

	UserStatus(final int status, final String desc) {
		this.status = status;
		this.desc = desc;
	}

	public static UserStatus get(int status) {
		UserStatus[] statusEnums = UserStatus.values();
		for (UserStatus se : statusEnums) {
			if (se.status == status) {
				return se;
			}
		}
		return UserStatus.unknown;
	}

	public String getDesc() {
		return desc;
	}

}
