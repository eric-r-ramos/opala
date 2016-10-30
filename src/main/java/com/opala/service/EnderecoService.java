package com.opala.service;

import com.opala.service.dto.EnderecoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Endereco.
 */
public interface EnderecoService {

    /**
     * Save a endereco.
     *
     * @param enderecoDTO the entity to save
     * @return the persisted entity
     */
    EnderecoDTO save(EnderecoDTO enderecoDTO);

    /**
     *  Get all the enderecos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EnderecoDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" endereco.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    EnderecoDTO findOne(Long id);

    /**
     *  Delete the "id" endereco.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the endereco corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<EnderecoDTO> search(String query, Pageable pageable);
}
