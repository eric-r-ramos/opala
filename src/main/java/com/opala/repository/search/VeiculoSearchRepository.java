package com.opala.repository.search;

import com.opala.domain.Veiculo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Veiculo entity.
 */
public interface VeiculoSearchRepository extends ElasticsearchRepository<Veiculo, Long> {
}
