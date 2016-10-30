package com.opala.service.impl;

import com.opala.service.AgendamentoService;
import com.opala.domain.Agendamento;
import com.opala.repository.AgendamentoRepository;
import com.opala.repository.search.AgendamentoSearchRepository;
import com.opala.service.dto.AgendamentoDTO;
import com.opala.service.mapper.AgendamentoMapper;
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
 * Service Implementation for managing Agendamento.
 */
@Service
@Transactional
public class AgendamentoServiceImpl implements AgendamentoService{

    private final Logger log = LoggerFactory.getLogger(AgendamentoServiceImpl.class);
    
    @Inject
    private AgendamentoRepository agendamentoRepository;

    @Inject
    private AgendamentoMapper agendamentoMapper;

    @Inject
    private AgendamentoSearchRepository agendamentoSearchRepository;

    /**
     * Save a agendamento.
     *
     * @param agendamentoDTO the entity to save
     * @return the persisted entity
     */
    public AgendamentoDTO save(AgendamentoDTO agendamentoDTO) {
        log.debug("Request to save Agendamento : {}", agendamentoDTO);
        Agendamento agendamento = agendamentoMapper.agendamentoDTOToAgendamento(agendamentoDTO);
        agendamento = agendamentoRepository.save(agendamento);
        AgendamentoDTO result = agendamentoMapper.agendamentoToAgendamentoDTO(agendamento);
        agendamentoSearchRepository.save(agendamento);
        return result;
    }

    /**
     *  Get all the agendamentos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<AgendamentoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Agendamentos");
        Page<Agendamento> result = agendamentoRepository.findAll(pageable);
        return result.map(agendamento -> agendamentoMapper.agendamentoToAgendamentoDTO(agendamento));
    }

    /**
     *  Get one agendamento by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public AgendamentoDTO findOne(Long id) {
        log.debug("Request to get Agendamento : {}", id);
        Agendamento agendamento = agendamentoRepository.findOneWithEagerRelationships(id);
        AgendamentoDTO agendamentoDTO = agendamentoMapper.agendamentoToAgendamentoDTO(agendamento);
        return agendamentoDTO;
    }

    /**
     *  Delete the  agendamento by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Agendamento : {}", id);
        agendamentoRepository.delete(id);
        agendamentoSearchRepository.delete(id);
    }

    /**
     * Search for the agendamento corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<AgendamentoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Agendamentos for query {}", query);
        Page<Agendamento> result = agendamentoSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(agendamento -> agendamentoMapper.agendamentoToAgendamentoDTO(agendamento));
    }
}
