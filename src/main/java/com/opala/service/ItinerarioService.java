package com.opala.service;

import com.opala.service.dto.ItinerarioDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Itinerario.
 */
public interface ItinerarioService {

    /**
     * Save a itinerario.
     *
     * @param itinerarioDTO the entity to save
     * @return the persisted entity
     */
    ItinerarioDTO save(ItinerarioDTO itinerarioDTO);

    /**
     *  Get all the itinerarios.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ItinerarioDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" itinerario.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    ItinerarioDTO findOne(Long id);

    /**
     *  Delete the "id" itinerario.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the itinerario corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<ItinerarioDTO> search(String query, Pageable pageable);
}
