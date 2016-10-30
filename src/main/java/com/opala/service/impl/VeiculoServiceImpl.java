package com.opala.service.impl;

import com.opala.service.VeiculoService;
import com.opala.domain.Veiculo;
import com.opala.repository.VeiculoRepository;
import com.opala.repository.search.VeiculoSearchRepository;
import com.opala.service.dto.VeiculoDTO;
import com.opala.service.mapper.VeiculoMapper;
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
 * Service Implementation for managing Veiculo.
 */
@Service
@Transactional
public class VeiculoServiceImpl implements VeiculoService{

    private final Logger log = LoggerFactory.getLogger(VeiculoServiceImpl.class);
    
    @Inject
    private VeiculoRepository veiculoRepository;

    @Inject
    private VeiculoMapper veiculoMapper;

    @Inject
    private VeiculoSearchRepository veiculoSearchRepository;

    /**
     * Save a veiculo.
     *
     * @param veiculoDTO the entity to save
     * @return the persisted entity
     */
    public VeiculoDTO save(VeiculoDTO veiculoDTO) {
        log.debug("Request to save Veiculo : {}", veiculoDTO);
        Veiculo veiculo = veiculoMapper.veiculoDTOToVeiculo(veiculoDTO);
        veiculo = veiculoRepository.save(veiculo);
        VeiculoDTO result = veiculoMapper.veiculoToVeiculoDTO(veiculo);
        veiculoSearchRepository.save(veiculo);
        return result;
    }

    /**
     *  Get all the veiculos.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<VeiculoDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Veiculos");
        Page<Veiculo> result = veiculoRepository.findAll(pageable);
        return result.map(veiculo -> veiculoMapper.veiculoToVeiculoDTO(veiculo));
    }

    /**
     *  Get one veiculo by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public VeiculoDTO findOne(Long id) {
        log.debug("Request to get Veiculo : {}", id);
        Veiculo veiculo = veiculoRepository.findOne(id);
        VeiculoDTO veiculoDTO = veiculoMapper.veiculoToVeiculoDTO(veiculo);
        return veiculoDTO;
    }

    /**
     *  Delete the  veiculo by id.
     *
     *  @param id the id of the entity
     */
    public void delete(Long id) {
        log.debug("Request to delete Veiculo : {}", id);
        veiculoRepository.delete(id);
        veiculoSearchRepository.delete(id);
    }

    /**
     * Search for the veiculo corresponding to the query.
     *
     *  @param query the query of the search
     *  @return the list of entities
     */
    @Transactional(readOnly = true)
    public Page<VeiculoDTO> search(String query, Pageable pageable) {
        log.debug("Request to search for a page of Veiculos for query {}", query);
        Page<Veiculo> result = veiculoSearchRepository.search(queryStringQuery(query), pageable);
        return result.map(veiculo -> veiculoMapper.veiculoToVeiculoDTO(veiculo));
    }
}
