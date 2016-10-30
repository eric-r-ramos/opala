package com.opala.service.impl;

import com.opala.service.SolicitanteService;
import com.opala.domain.Solicitante;
import com.opala.repository.SolicitanteRepository;
import com.opala.repository.search.SolicitanteSearchRepository;
import com.opala.service.dto.SolicitanteDTO;
import com.opala.service.mapper.SolicitanteMapper;
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
 * Service Implementation for managing Solicitante.
 */
@Service
@Transactional
public class SolicitanteServiceImpl implements SolicitanteService{

    private final Logger log = LoggerFactory.getLogger(SolicitanteServiceImpl.class);
    
    @Inject
    private SolicitanteRepository solicitanteRepository;

    @Inject
    private SolicitanteMapper solicitanteMapper;

    @Inject
    private SolicitanteSearchRepository solicitanteSearchRepository;

    /**
     * Save a solicitante.
     *
     * @param solicitanteDTO the entity to save
     * @return the persisted entity
     */
    public SolicitanteDTO save(SolicitanteDTO solicitanteDTO) {
        log.debug("Request to save Solicitante : {}", solicitanteDTO);
        Solicitante solicitante = solicitanteMapper.solicitanteDTOToSolicitante(solicitanteDTO);
        solicitante = solicitanteRepository.save(solicitante);
        SolicitanteDTO result = solicitanteMapper.solicitanteToSolicitanteDTO(solicitante);
        solicitanteSearchRepository.save(solicitante);
        return result;
    }

    /**
     *  Get all the solicitantes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<SolicitanteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Solicitantes");
        Page<Solicitante> result = solicitanteRepository.findAll(pageable);
        return result.map(solicitante -> solicitanteMapper.solicitanteToSolicitanteDTO(solicitante));
    }

    /**
     *  Get one solicitante by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public SolicitanteDTO findOne(Long id) {
        log.debug("Request to get Solicitante : {}", id);
        Solicitante solicitante = solicitanteRepository.findOne(id);
        SolicitanteDTO solicitanteDTO = solicitanteMapper.solicitanteToSolicitanteDTO(solicitante);
        return solicitanteDTO;
    }

    /**
     *  Delete the  solicitante by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Solicitante : {}", id);
        solicitanteRepository.delete(id);
        solicitanteSearchRepository.delete(id);
    }

    /**
     * Search for the solicitante corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<SolicitanteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Solicitantes for query {}", query);
        Page<Solicitante> result = solicitanteSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(solicitante -> solicitanteMapper.solicitanteToSolicitanteDTO(solicitante));
    }
}
