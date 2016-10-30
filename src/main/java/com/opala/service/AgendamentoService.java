package com.opala.service;

import com.opala.service.dto.AgendamentoDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.LinkedList;
import java.util.List;

/**
 * Service Interface for managing Agendamento.
 */
public interface AgendamentoService {

    /**
     * Save a agendamento.
     *
     * @param agendamentoDTO the entity to save
     * @return the persisted entity
     */
    AgendamentoDTO save(AgendamentoDTO agendamentoDTO);

    /**
     *  Get all the agendamentos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AgendamentoDTO> findAll(Pageable pageable);

    /**
     *  Get the "id" agendamento.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    AgendamentoDTO findOne(Long id);

    /**
     *  Delete the "id" agendamento.
     *
     *  @param id the id of the entity
     */
    void delete(Long id);

    /**
     * Search for the agendamento corresponding to the query.
     *
     *  @param query the query of the search
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<AgendamentoDTO> search(String query, Pageable pageable);
}
