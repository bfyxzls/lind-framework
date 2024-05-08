package com.lind.elasticsearch.repository;

import com.lind.elasticsearch.entity.EsDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface EsRepo extends ElasticsearchRepository<EsDto, String> {

}
