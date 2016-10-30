package com.opala.service;

import com.opala.service.dto.PassageiroDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Passageiro.
 */
public interface PassageiroService {

    /**
     * Save a passageiro.
     *
     * @param passageiroDTO the entity to save
     * @return the persisted entity
     */
    PassageiroDTO save(PassageiroDTO passageiroDTO);

    /**
     *  Get all the passageiros.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PassageiroDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" passageiro.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    PassageiroDTO findOne(Long id);

    /**
     *  Delete the "id" passageiro.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the passageiro corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<PassageiroDTO> search(String query, Pageable pageable);
}
