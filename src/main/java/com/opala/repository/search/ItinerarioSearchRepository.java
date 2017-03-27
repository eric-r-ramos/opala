package com.opala.repository.search;

import com.opala.domain.Itinerario;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the Itinerario entity.
 */
public interface ItinerarioSearchRepository extends ElasticsearchRepository<Itinerario, Long> {
}
