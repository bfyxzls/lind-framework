package com.lind.elasticsearch.repository;

import com.lind.elasticsearch.entity.LogDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @author lind
 * @date 2024/5/7 8:51
 * @since 1.0.0
 */
public interface LogDtoRepo extends ElasticsearchRepository<LogDto, String> {

}
