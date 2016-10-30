package com.opala.service;

import com.opala.service.dto.SolicitanteDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Solicitante.
 */
public interface SolicitanteService {

    /**
     * Save a solicitante.
     *
     * @param solicitanteDTO the entity to save
     * @return the persisted entity
     */
    SolicitanteDTO save(SolicitanteDTO solicitanteDTO);

    /**
     *  Get all the solicitantes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SolicitanteDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" solicitante.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    SolicitanteDTO findOne(Long id);

    /**
     *  Delete the "id" solicitante.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the solicitante corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<SolicitanteDTO> search(String query, Pageable pageable);
}
