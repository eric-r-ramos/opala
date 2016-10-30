package com.opala.repository.search;

import com.opala.domain.Solicitante;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Solicitante entity.
 */
public interface SolicitanteSearchRepository extends ElasticsearchRepository<Solicitante, Long> {
}
