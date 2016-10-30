package com.opala.service.impl;

import com.opala.service.MotoristaService;
import com.opala.domain.Motorista;
import com.opala.repository.MotoristaRepository;
import com.opala.repository.search.MotoristaSearchRepository;
import com.opala.service.dto.MotoristaDTO;
import com.opala.service.mapper.MotoristaMapper;
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
 * Service Implementation for managing Motorista.
 */
@Service
@Transactional
public class MotoristaServiceImpl implements MotoristaService{

    private final Logger log = LoggerFactory.getLogger(MotoristaServiceImpl.class);
    
    @Inject
    private MotoristaRepository motoristaRepository;

    @Inject
    private MotoristaMapper motoristaMapper;

    @Inject
    private MotoristaSearchRepository motoristaSearchRepository;

    /**
     * Save a motorista.
     *
     * @param motoristaDTO the entity to save
     * @return the persisted entity
     */
    public MotoristaDTO save(MotoristaDTO motoristaDTO) {
        log.debug("Request to save Motorista : {}", motoristaDTO);
        Motorista motorista = motoristaMapper.motoristaDTOToMotorista(motoristaDTO);
        motorista = motoristaRepository.save(motorista);
        MotoristaDTO result = motoristaMapper.motoristaToMotoristaDTO(motorista);
        motoristaSearchRepository.save(motorista);
        return result;
    }

    /**
     *  Get all the motoristas.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<MotoristaDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Motoristas");
        Page<Motorista> result = motoristaRepository.findAll(pageable);
        return result.map(motorista -> motoristaMapper.motoristaToMotoristaDTO(motorista));
    }

    /**
     *  Get one motorista by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public MotoristaDTO findOne(Long id) {
        log.debug("Request to get Motorista : {}", id);
        Motorista motorista = motoristaRepository.findOne(id);
        MotoristaDTO motoristaDTO = motoristaMapper.motoristaToMotoristaDTO(motorista);
        return motoristaDTO;
    }

    /**
     *  Delete the  motorista by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Motorista : {}", id);
        motoristaRepository.delete(id);
        motoristaSearchRepository.delete(id);
    }

    /**
     * Search for the motorista corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<MotoristaDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Motoristas for query {}", query);
        Page<Motorista> result = motoristaSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(motorista -> motoristaMapper.motoristaToMotoristaDTO(motorista));
    }
}
