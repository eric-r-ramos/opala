package com.opala.service.impl;

import com.opala.service.ClienteService;
import com.opala.domain.Cliente;
import com.opala.repository.ClienteRepository;
import com.opala.repository.search.ClienteSearchRepository;
import com.opala.service.dto.ClienteDTO;
import com.opala.service.mapper.ClienteMapper;
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
 * Service Implementation for managing Cliente.
 */
@Service
@Transactional
public class ClienteServiceImpl implements ClienteService{

    private final Logger log = LoggerFactory.getLogger(ClienteServiceImpl.class);
    
    @Inject
    private ClienteRepository clienteRepository;

    @Inject
    private ClienteMapper clienteMapper;

    @Inject
    private ClienteSearchRepository clienteSearchRepository;

    /**
     * Save a cliente.
     *
     * @param clienteDTO the entity to save
     * @return the persisted entity
     */
    public ClienteDTO save(ClienteDTO clienteDTO) {
        log.debug("Request to save Cliente : {}", clienteDTO);
        Cliente cliente = clienteMapper.clienteDTOToCliente(clienteDTO);
        cliente = clienteRepository.save(cliente);
        ClienteDTO result = clienteMapper.clienteToClienteDTO(cliente);
        clienteSearchRepository.save(cliente);
        return result;
    }

    /**
     *  Get all the clientes.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<ClienteDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Clientes");
        Page<Cliente> result = clienteRepository.findAll(pageable);
        return result.map(cliente -> clienteMapper.clienteToClienteDTO(cliente));
    }

    /**
     *  Get one cliente by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public ClienteDTO findOne(Long id) {
        log.debug("Request to get Cliente : {}", id);
        Cliente cliente = clienteRepository.findOne(id);
        ClienteDTO clienteDTO = clienteMapper.clienteToClienteDTO(cliente);
        return clienteDTO;
    }

    /**
     *  Delete the  cliente by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Cliente : {}", id);
        clienteRepository.delete(id);
        clienteSearchRepository.delete(id);
    }

    /**
     * Search for the cliente corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<ClienteDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Clientes for query {}", query);
        Page<Cliente> result = clienteSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(cliente -> clienteMapper.clienteToClienteDTO(cliente));
    }
}
