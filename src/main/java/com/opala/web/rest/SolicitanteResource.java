package com.opala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opala.domain.Solicitante;

import com.opala.repository.SolicitanteRepository;
import com.opala.repository.search.SolicitanteSearchRepository;
import com.opala.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
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
    private SolicitanteRepository solicitanteRepository;

    @Inject
    private SolicitanteSearchRepository solicitanteSearchRepository;

    /**
     * POST  /solicitantes : Create a new solicitante.
     *
     * @param solicitante the solicitante to create
     * @return the ResponseEntity with status 201 (Created) and with body the new solicitante, or with status 400 (Bad Request) if the solicitante has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/solicitantes")
    @Timed
    public ResponseEntity<Solicitante> createSolicitante(@RequestBody Solicitante solicitante) throws URISyntaxException {
        log.debug("REST request to save Solicitante : {}", solicitante);
        if (solicitante.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("solicitante", "idexists", "A new solicitante cannot already have an ID")).body(null);
        }
        Solicitante result = solicitanteRepository.save(solicitante);
        solicitanteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/solicitantes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("solicitante", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /solicitantes : Updates an existing solicitante.
     *
     * @param solicitante the solicitante to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated solicitante,
     * or with status 400 (Bad Request) if the solicitante is not valid,
     * or with status 500 (Internal Server Error) if the solicitante couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/solicitantes")
    @Timed
    public ResponseEntity<Solicitante> updateSolicitante(@RequestBody Solicitante solicitante) throws URISyntaxException {
        log.debug("REST request to update Solicitante : {}", solicitante);
        if (solicitante.getId() == null) {
            return createSolicitante(solicitante);
        }
        Solicitante result = solicitanteRepository.save(solicitante);
        solicitanteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("solicitante", solicitante.getId().toString()))
            .body(result);
    }

    /**
     * GET  /solicitantes : get all the solicitantes.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of solicitantes in body
     */
    @GetMapping("/solicitantes")
    @Timed
    public List<Solicitante> getAllSolicitantes() {
        log.debug("REST request to get all Solicitantes");
        List<Solicitante> solicitantes = solicitanteRepository.findAll();
        return solicitantes;
    }

    /**
     * GET  /solicitantes/:id : get the "id" solicitante.
     *
     * @param id the id of the solicitante to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the solicitante, or with status 404 (Not Found)
     */
    @GetMapping("/solicitantes/{id}")
    @Timed
    public ResponseEntity<Solicitante> getSolicitante(@PathVariable Long id) {
        log.debug("REST request to get Solicitante : {}", id);
        Solicitante solicitante = solicitanteRepository.findOne(id);
        return Optional.ofNullable(solicitante)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /solicitantes/:id : delete the "id" solicitante.
     *
     * @param id the id of the solicitante to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/solicitantes/{id}")
    @Timed
    public ResponseEntity<Void> deleteSolicitante(@PathVariable Long id) {
        log.debug("REST request to delete Solicitante : {}", id);
        solicitanteRepository.delete(id);
        solicitanteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("solicitante", id.toString())).build();
    }

    /**
     * SEARCH  /_search/solicitantes?query=:query : search for the solicitante corresponding
     * to the query.
     *
     * @param query the query of the solicitante search 
     * @return the result of the search
     */
    @GetMapping("/_search/solicitantes")
    @Timed
    public List<Solicitante> searchSolicitantes(@RequestParam String query) {
        log.debug("REST request to search Solicitantes for query {}", query);
        return StreamSupport
            .stream(solicitanteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
