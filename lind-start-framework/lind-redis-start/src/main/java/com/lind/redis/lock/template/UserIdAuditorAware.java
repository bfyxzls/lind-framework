package com.lind.redis.lock.template;

import org.springframework.data.domain.AuditorAware;

/**
 * 返回用户ID的标识接口,由程序使用者去实现.
 */
public interface UserIdAuditorAware extends AuditorAware<String> {

}
