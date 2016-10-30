package com.opala.service.impl;

import com.opala.service.PassageiroService;
import com.opala.domain.Passageiro;
import com.opala.repository.PassageiroRepository;
import com.opala.repository.search.PassageiroSearchRepository;
import com.opala.service.dto.PassageiroDTO;
import com.opala.service.mapper.PassageiroMapper;
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
 * Service Implementation for managing Passageiro.
 */
@Service
@Transactional
public class PassageiroServiceImpl implements PassageiroService{

    private final Logger log = LoggerFactory.getLogger(PassageiroServiceImpl.class);
    
    @Inject
    private PassageiroRepository passageiroRepository;

    @Inject
    private PassageiroMapper passageiroMapper;

    @Inject
    private PassageiroSearchRepository passageiroSearchRepository;

    /**
     * Save a passageiro.
     *
     * @param passageiroDTO the entity to save
     * @return the persisted entity
     */
    public PassageiroDTO save(PassageiroDTO passageiroDTO) {
        log.debug("Request to save Passageiro : {}", passageiroDTO);
        Passageiro passageiro = passageiroMapper.passageiroDTOToPassageiro(passageiroDTO);
        passageiro = passageiroRepository.save(passageiro);
        PassageiroDTO result = passageiroMapper.passageiroToPassageiroDTO(passageiro);
        passageiroSearchRepository.save(passageiro);
        return result;
    }

    /**
     *  Get all the passageiros.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<PassageiroDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Passageiros");
        Page<Passageiro> result = passageiroRepository.findAll(pageable);
        return result.map(passageiro -> passageiroMapper.passageiroToPassageiroDTO(passageiro));
    }

    /**
     *  Get one passageiro by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public PassageiroDTO findOne(Long id) {
        log.debug("Request to get Passageiro : {}", id);
        Passageiro passageiro = passageiroRepository.findOne(id);
        PassageiroDTO passageiroDTO = passageiroMapper.passageiroToPassageiroDTO(passageiro);
        return passageiroDTO;
    }

    /**
     *  Delete the  passageiro by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Passageiro : {}", id);
        passageiroRepository.delete(id);
        passageiroSearchRepository.delete(id);
    }

    /**
     * Search for the passageiro corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<PassageiroDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Passageiros for query {}", query);
        Page<Passageiro> result = passageiroSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(passageiro -> passageiroMapper.passageiroToPassageiroDTO(passageiro));
    }
}
