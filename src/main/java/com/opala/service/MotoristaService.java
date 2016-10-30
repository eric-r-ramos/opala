package com.opala.service;

import com.opala.service.dto.MotoristaDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Motorista.
 */
public interface MotoristaService {

    /**
     * Save a motorista.
     *
     * @param motoristaDTO the entity to save
     * @return the persisted entity
     */
    MotoristaDTO save(MotoristaDTO motoristaDTO);

    /**
     *  Get all the motoristas.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MotoristaDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" motorista.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    MotoristaDTO findOne(Long id);

    /**
     *  Delete the "id" motorista.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the motorista corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<MotoristaDTO> search(String query, Pageable pageable);
}
