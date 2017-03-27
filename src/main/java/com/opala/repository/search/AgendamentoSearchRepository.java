package com.opala.repository.search;

import com.opala.domain.Agendamento;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Agendamento entity.
 */
public interface AgendamentoSearchRepository extends ElasticsearchRepository<Agendamento, Long> {
}
