package com.opala.repository.search;

import com.opala.domain.Motorista;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Motorista entity.
 */
public interface MotoristaSearchRepository extends ElasticsearchRepository<Motorista, Long> {
}
