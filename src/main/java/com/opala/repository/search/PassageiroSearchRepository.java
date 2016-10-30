package com.opala.repository.search;

import com.opala.domain.Passageiro;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Passageiro entity.
 */
public interface PassageiroSearchRepository extends ElasticsearchRepository<Passageiro, Long> {
}
