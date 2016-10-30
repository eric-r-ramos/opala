package com.opala.repository.search;

import com.opala.domain.Endereco;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Endereco entity.
 */
public interface EnderecoSearchRepository extends ElasticsearchRepository<Endereco, Long> {
}
