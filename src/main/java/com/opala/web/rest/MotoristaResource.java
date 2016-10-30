package com.opala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opala.domain.Motorista;

import com.opala.repository.MotoristaRepository;
import com.opala.repository.search.MotoristaSearchRepository;
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
 * REST controller for managing Motorista.
 */
@RestController
@RequestMapping("/api")
public class MotoristaResource {

    private final Logger log = LoggerFactory.getLogger(MotoristaResource.class);
        
    @Inject
    private MotoristaRepository motoristaRepository;

    @Inject
    private MotoristaSearchRepository motoristaSearchRepository;

    /**
     * POST  /motoristas : Create a new motorista.
     *
     * @param motorista the motorista to create
     * @return the ResponseEntity with status 201 (Created) and with body the new motorista, or with status 400 (Bad Request) if the motorista has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/motoristas")
    @Timed
    public ResponseEntity<Motorista> createMotorista(@RequestBody Motorista motorista) throws URISyntaxException {
        log.debug("REST request to save Motorista : {}", motorista);
        if (motorista.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("motorista", "idexists", "A new motorista cannot already have an ID")).body(null);
        }
        Motorista result = motoristaRepository.save(motorista);
        motoristaSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/motoristas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("motorista", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /motoristas : Updates an existing motorista.
     *
     * @param motorista the motorista to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated motorista,
     * or with status 400 (Bad Request) if the motorista is not valid,
     * or with status 500 (Internal Server Error) if the motorista couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/motoristas")
    @Timed
    public ResponseEntity<Motorista> updateMotorista(@RequestBody Motorista motorista) throws URISyntaxException {
        log.debug("REST request to update Motorista : {}", motorista);
        if (motorista.getId() == null) {
            return createMotorista(motorista);
        }
        Motorista result = motoristaRepository.save(motorista);
        motoristaSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("motorista", motorista.getId().toString()))
            .body(result);
    }

    /**
     * GET  /motoristas : get all the motoristas.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of motoristas in body
     */
    @GetMapping("/motoristas")
    @Timed
    public List<Motorista> getAllMotoristas() {
        log.debug("REST request to get all Motoristas");
        List<Motorista> motoristas = motoristaRepository.findAll();
        return motoristas;
    }

    /**
     * GET  /motoristas/:id : get the "id" motorista.
     *
     * @param id the id of the motorista to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the motorista, or with status 404 (Not Found)
     */
    @GetMapping("/motoristas/{id}")
    @Timed
    public ResponseEntity<Motorista> getMotorista(@PathVariable Long id) {
        log.debug("REST request to get Motorista : {}", id);
        Motorista motorista = motoristaRepository.findOne(id);
        return Optional.ofNullable(motorista)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /motoristas/:id : delete the "id" motorista.
     *
     * @param id the id of the motorista to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/motoristas/{id}")
    @Timed
    public ResponseEntity<Void> deleteMotorista(@PathVariable Long id) {
        log.debug("REST request to delete Motorista : {}", id);
        motoristaRepository.delete(id);
        motoristaSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("motorista", id.toString())).build();
    }

    /**
     * SEARCH  /_search/motoristas?query=:query : search for the motorista corresponding
     * to the query.
     *
     * @param query the query of the motorista search 
     * @return the result of the search
     */
    @GetMapping("/_search/motoristas")
    @Timed
    public List<Motorista> searchMotoristas(@RequestParam String query) {
        log.debug("REST request to search Motoristas for query {}", query);
        return StreamSupport
            .stream(motoristaSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
