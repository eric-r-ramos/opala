package com.opala.service.impl;

import com.opala.service.EnderecoService;
import com.opala.domain.Endereco;
import com.opala.repository.EnderecoRepository;
import com.opala.repository.search.EnderecoSearchRepository;
import com.opala.service.dto.EnderecoDTO;
import com.opala.service.mapper.EnderecoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Endereco.
 */
@Service
@Transactional
public class EnderecoServiceImpl implements EnderecoService{

    private final Logger log = LoggerFactory.getLogger(EnderecoServiceImpl.class);
    
    @Inject
    private EnderecoRepository enderecoRepository;

    @Inject
    private EnderecoMapper enderecoMapper;

    @Inject
    private EnderecoSearchRepository enderecoSearchRepository;

    /**
     * Save a endereco.
     *
     * @param enderecoDTO the entity to save
     * @return the persisted entity
     */
    public EnderecoDTO save(EnderecoDTO enderecoDTO) {
        log.debug("Request to save Endereco : {}", enderecoDTO);
        Endereco endereco = enderecoMapper.enderecoDTOToEndereco(enderecoDTO);
        endereco = enderecoRepository.save(endereco);
        EnderecoDTO result = enderecoMapper.enderecoToEnderecoDTO(endereco);
        enderecoSearchRepository.save(endereco);
        return result;
    }

    /**
     *  Get all the enderecos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<EnderecoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Enderecos");
        Page<Endereco> result = enderecoRepository.findAll(pageable);
        return result.map(endereco -> enderecoMapper.enderecoToEnderecoDTO(endereco));
    }

    /**
     *  Get one endereco by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public EnderecoDTO findOne(Long id) {
        log.debug("Request to get Endereco : {}", id);
        Endereco endereco = enderecoRepository.findOne(id);
        EnderecoDTO enderecoDTO = enderecoMapper.enderecoToEnderecoDTO(endereco);
        return enderecoDTO;
    }

    /**
     *  Delete the  endereco by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Endereco : {}", id);
        enderecoRepository.delete(id);
        enderecoSearchRepository.delete(id);
    }

    /**
     * Search for the endereco corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<EnderecoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Enderecos for query {}", query);
        Page<Endereco> result = enderecoSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(endereco -> enderecoMapper.enderecoToEnderecoDTO(endereco));
    }
}
