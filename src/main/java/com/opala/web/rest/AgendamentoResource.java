package com.opala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opala.service.AgendamentoService;
import com.opala.web.rest.util.HeaderUtil;
import com.opala.web.rest.util.PaginationUtil;
import com.opala.service.dto.AgendamentoDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Agendamento.
 */
@RestController
@RequestMapping("/api")
public class AgendamentoResource {

    private final Logger log = LoggerFactory.getLogger(AgendamentoResource.class);
        
    @Inject
    private AgendamentoService agendamentoService;

    /**
     * POST  /agendamentos : Create a new agendamento.
     *
     * @param agendamentoDTO the agendamentoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new agendamentoDTO, or with status 400 (Bad Request) if the agendamento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/agendamentos")
    @Timed
    public ResponseEntity<AgendamentoDTO> createAgendamento(@RequestBody AgendamentoDTO agendamentoDTO) throws URISyntaxException {
        log.debug("REST request to save Agendamento : {}", agendamentoDTO);
        if (agendamentoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("agendamento", "idexists", "A new agendamento cannot already have an ID")).body(null);
        }
        AgendamentoDTO result = agendamentoService.save(agendamentoDTO);
        return ResponseEntity.created(new URI("/api/agendamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("agendamento", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agendamentos : Updates an existing agendamento.
     *
     * @param agendamentoDTO the agendamentoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated agendamentoDTO,
     * or with status 400 (Bad Request) if the agendamentoDTO is not valid,
     * or with status 500 (Internal Server Error) if the agendamentoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/agendamentos")
    @Timed
    public ResponseEntity<AgendamentoDTO> updateAgendamento(@RequestBody AgendamentoDTO agendamentoDTO) throws URISyntaxException {
        log.debug("REST request to update Agendamento : {}", agendamentoDTO);
        if (agendamentoDTO.getId() == null) {
            return createAgendamento(agendamentoDTO);
        }
        AgendamentoDTO result = agendamentoService.save(agendamentoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("agendamento", agendamentoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /agendamentos : get all the agendamentos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of agendamentos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/agendamentos")
    @Timed
    public ResponseEntity<List<AgendamentoDTO>> getAllAgendamentos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Agendamentos");
        Page<AgendamentoDTO> page = agendamentoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/agendamentos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /agendamentos/:id : get the "id" agendamento.
     *
     * @param id the id of the agendamentoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the agendamentoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/agendamentos/{id}")
    @Timed
    public ResponseEntity<AgendamentoDTO> getAgendamento(@PathVariable Long id) {
        log.debug("REST request to get Agendamento : {}", id);
        AgendamentoDTO agendamentoDTO = agendamentoService.findOne(id);
        return Optional.ofNullable(agendamentoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /agendamentos/:id : delete the "id" agendamento.
     *
     * @param id the id of the agendamentoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/agendamentos/{id}")
    @Timed
    public ResponseEntity<Void> deleteAgendamento(@PathVariable Long id) {
        log.debug("REST request to delete Agendamento : {}", id);
        agendamentoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("agendamento", id.toString())).build();
    }

    /**
     * SEARCH  /_search/agendamentos?query=:query : search for the agendamento corresponding
     * to the query.
     *
     * @param query the query of the agendamento search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/agendamentos")
    @Timed
    public ResponseEntity<List<AgendamentoDTO>> searchAgendamentos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Agendamentos for query {}", query);
        Page<AgendamentoDTO> page = agendamentoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/agendamentos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
