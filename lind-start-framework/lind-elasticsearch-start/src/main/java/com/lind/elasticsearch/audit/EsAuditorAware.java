package com.lind.elasticsearch.audit;

import java.util.Optional;

/**
 * Es获取审核当前对象.
 *
 * @param <T> 类型
 */
public interface EsAuditorAware<T> {

	Optional<T> getCurrentAuditor();

}
