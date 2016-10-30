package com.opala.service.impl;

import com.opala.service.ItinerarioService;
import com.opala.domain.Itinerario;
import com.opala.repository.ItinerarioRepository;
import com.opala.repository.search.ItinerarioSearchRepository;
import com.opala.service.dto.ItinerarioDTO;
import com.opala.service.mapper.ItinerarioMapper;
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
 * Service Implementation for managing Itinerario.
 */
@Service
@Transactional
public class ItinerarioServiceImpl implements ItinerarioService{

    private final Logger log = LoggerFactory.getLogger(ItinerarioServiceImpl.class);
    
    @Inject
    private ItinerarioRepository itinerarioRepository;

    @Inject
    private ItinerarioMapper itinerarioMapper;

    @Inject
    private ItinerarioSearchRepository itinerarioSearchRepository;

    /**
     * Save a itinerario.
     *
     * @param itinerarioDTO the entity to save
     * @return the persisted entity
     */
    public ItinerarioDTO save(ItinerarioDTO itinerarioDTO) {
        log.debug("Request to save Itinerario : {}", itinerarioDTO);
        Itinerario itinerario = itinerarioMapper.itinerarioDTOToItinerario(itinerarioDTO);
        itinerario = itinerarioRepository.save(itinerario);
        ItinerarioDTO result = itinerarioMapper.itinerarioToItinerarioDTO(itinerario);
        itinerarioSearchRepository.save(itinerario);
        return result;
    }

    /**
     *  Get all the itinerarios.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ItinerarioDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Itinerarios");
        Page<Itinerario> result = itinerarioRepository.findAll(pageable);
        return result.map(itinerario -> itinerarioMapper.itinerarioToItinerarioDTO(itinerario));
    }

    /**
     *  Get one itinerario by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ItinerarioDTO findOne(Long id) {
        log.debug("Request to get Itinerario : {}", id);
        Itinerario itinerario = itinerarioRepository.findOne(id);
        ItinerarioDTO itinerarioDTO = itinerarioMapper.itinerarioToItinerarioDTO(itinerario);
        return itinerarioDTO;
    }

    /**
     *  Delete the  itinerario by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Itinerario : {}", id);
        itinerarioRepository.delete(id);
        itinerarioSearchRepository.delete(id);
    }

    /**
     * Search for the itinerario corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ItinerarioDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Itinerarios for query {}", query);
        Page<Itinerario> result = itinerarioSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(itinerario -> itinerarioMapper.itinerarioToItinerarioDTO(itinerario));
    }
}
