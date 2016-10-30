package com.opala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opala.service.SolicitanteService;
import com.opala.web.rest.util.HeaderUtil;
import com.opala.web.rest.util.PaginationUtil;
import com.opala.service.dto.SolicitanteDTO;
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
 * REST controller for managing Solicitante.
 */
@RestController
@RequestMapping("/api")
public class SolicitanteResource {

    private final Logger log = LoggerFactory.getLogger(SolicitanteResource.class);
        
    @Inject
    private SolicitanteService solicitanteService;

    /**
     * POST  /solicitantes : Create a new solicitante.
     *
     * @param solicitanteDTO the solicitanteDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new solicitanteDTO, or with status 400 (Bad Request) if the solicitante has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/solicitantes")
    @Timed
    public ResponseEntity<SolicitanteDTO> createSolicitante(@RequestBody SolicitanteDTO solicitanteDTO) throws URISyntaxException {
        log.debug("REST request to save Solicitante : {}", solicitanteDTO);
        if (solicitanteDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("solicitante", "idexists", "A new solicitante cannot already have an ID")).body(null);
        }
        SolicitanteDTO result = solicitanteService.save(solicitanteDTO);
        return ResponseEntity.created(new URI("/api/solicitantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("solicitante", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /solicitantes : Updates an existing solicitante.
     *
     * @param solicitanteDTO the solicitanteDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated solicitanteDTO,
     * or with status 400 (Bad Request) if the solicitanteDTO is not valid,
     * or with status 500 (Internal Server Error) if the solicitanteDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/solicitantes")
    @Timed
    public ResponseEntity<SolicitanteDTO> updateSolicitante(@RequestBody SolicitanteDTO solicitanteDTO) throws URISyntaxException {
        log.debug("REST request to update Solicitante : {}", solicitanteDTO);
        if (solicitanteDTO.getId() == null) {
            return createSolicitante(solicitanteDTO);
        }
        SolicitanteDTO result = solicitanteService.save(solicitanteDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("solicitante", solicitanteDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /solicitantes : get all the solicitantes.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of solicitantes in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/solicitantes")
    @Timed
    public ResponseEntity<List<SolicitanteDTO>> getAllSolicitantes(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Solicitantes");
        Page<SolicitanteDTO> page = solicitanteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/solicitantes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /solicitantes/:id : get the "id" solicitante.
     *
     * @param id the id of the solicitanteDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the solicitanteDTO, or with status 404 (Not Found)
     */
    @GetMapping("/solicitantes/{id}")
    @Timed
    public ResponseEntity<SolicitanteDTO> getSolicitante(@PathVariable Long id) {
        log.debug("REST request to get Solicitante : {}", id);
        SolicitanteDTO solicitanteDTO = solicitanteService.findOne(id);
        return Optional.ofNullable(solicitanteDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /solicitantes/:id : delete the "id" solicitante.
     *
     * @param id the id of the solicitanteDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/solicitantes/{id}")
    @Timed
    public ResponseEntity<Void> deleteSolicitante(@PathVariable Long id) {
        log.debug("REST request to delete Solicitante : {}", id);
        solicitanteService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("solicitante", id.toString())).build();
    }

    /**
     * SEARCH  /_search/solicitantes?query=:query : search for the solicitante corresponding
     * to the query.
     *
     * @param query the query of the solicitante search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/solicitantes")
    @Timed
    public ResponseEntity<List<SolicitanteDTO>> searchSolicitantes(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Solicitantes for query {}", query);
        Page<SolicitanteDTO> page = solicitanteService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/solicitantes");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
