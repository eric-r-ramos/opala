package com.opala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opala.domain.Agendamento;

import com.opala.repository.AgendamentoRepository;
import com.opala.repository.search.AgendamentoSearchRepository;
import com.opala.web.rest.util.HeaderUtil;
import com.opala.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
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

    private static final String ENTITY_NAME = "agendamento";
        
    private final AgendamentoRepository agendamentoRepository;

    private final AgendamentoSearchRepository agendamentoSearchRepository;

    public AgendamentoResource(AgendamentoRepository agendamentoRepository, AgendamentoSearchRepository agendamentoSearchRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.agendamentoSearchRepository = agendamentoSearchRepository;
    }

    /**
     * POST  /agendamentos : Create a new agendamento.
     *
     * @param agendamento the agendamento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new agendamento, or with status 400 (Bad Request) if the agendamento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/agendamentos")
    @Timed
    public ResponseEntity<Agendamento> createAgendamento(@RequestBody Agendamento agendamento) throws URISyntaxException {
        log.debug("REST request to save Agendamento : {}", agendamento);
        if (agendamento.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new agendamento cannot already have an ID")).body(null);
        }
        Agendamento result = agendamentoRepository.save(agendamento);
        agendamentoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/agendamentos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /agendamentos : Updates an existing agendamento.
     *
     * @param agendamento the agendamento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated agendamento,
     * or with status 400 (Bad Request) if the agendamento is not valid,
     * or with status 500 (Internal Server Error) if the agendamento couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/agendamentos")
    @Timed
    public ResponseEntity<Agendamento> updateAgendamento(@RequestBody Agendamento agendamento) throws URISyntaxException {
        log.debug("REST request to update Agendamento : {}", agendamento);
        if (agendamento.getId() == null) {
            return createAgendamento(agendamento);
        }
        Agendamento result = agendamentoRepository.save(agendamento);
        agendamentoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, agendamento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /agendamentos : get all the agendamentos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of agendamentos in body
     */
    @GetMapping("/agendamentos")
    @Timed
    public ResponseEntity<List<Agendamento>> getAllAgendamentos(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Agendamentos");
        Page<Agendamento> page = agendamentoRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/agendamentos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /agendamentos/:id : get the "id" agendamento.
     *
     * @param id the id of the agendamento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the agendamento, or with status 404 (Not Found)
     */
    @GetMapping("/agendamentos/{id}")
    @Timed
    public ResponseEntity<Agendamento> getAgendamento(@PathVariable Long id) {
        log.debug("REST request to get Agendamento : {}", id);
        Agendamento agendamento = agendamentoRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(agendamento));
    }

    /**
     * DELETE  /agendamentos/:id : delete the "id" agendamento.
     *
     * @param id the id of the agendamento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/agendamentos/{id}")
    @Timed
    public ResponseEntity<Void> deleteAgendamento(@PathVariable Long id) {
        log.debug("REST request to delete Agendamento : {}", id);
        agendamentoRepository.delete(id);
        agendamentoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/agendamentos?query=:query : search for the agendamento corresponding
     * to the query.
     *
     * @param query the query of the agendamento search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/agendamentos")
    @Timed
    public ResponseEntity<List<Agendamento>> searchAgendamentos(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Agendamentos for query {}", query);
        Page<Agendamento> page = agendamentoSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/agendamentos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
