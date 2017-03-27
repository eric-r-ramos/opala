package com.opala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opala.domain.Passageiro;

import com.opala.repository.PassageiroRepository;
import com.opala.repository.search.PassageiroSearchRepository;
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
 * REST controller for managing Passageiro.
 */
@RestController
@RequestMapping("/api")
public class PassageiroResource {

    private final Logger log = LoggerFactory.getLogger(PassageiroResource.class);

    private static final String ENTITY_NAME = "passageiro";
        
    private final PassageiroRepository passageiroRepository;

    private final PassageiroSearchRepository passageiroSearchRepository;

    public PassageiroResource(PassageiroRepository passageiroRepository, PassageiroSearchRepository passageiroSearchRepository) {
        this.passageiroRepository = passageiroRepository;
        this.passageiroSearchRepository = passageiroSearchRepository;
    }

    /**
     * POST  /passageiros : Create a new passageiro.
     *
     * @param passageiro the passageiro to create
     * @return the ResponseEntity with status 201 (Created) and with body the new passageiro, or with status 400 (Bad Request) if the passageiro has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/passageiros")
    @Timed
    public ResponseEntity<Passageiro> createPassageiro(@RequestBody Passageiro passageiro) throws URISyntaxException {
        log.debug("REST request to save Passageiro : {}", passageiro);
        if (passageiro.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new passageiro cannot already have an ID")).body(null);
        }
        Passageiro result = passageiroRepository.save(passageiro);
        passageiroSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/passageiros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /passageiros : Updates an existing passageiro.
     *
     * @param passageiro the passageiro to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated passageiro,
     * or with status 400 (Bad Request) if the passageiro is not valid,
     * or with status 500 (Internal Server Error) if the passageiro couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/passageiros")
    @Timed
    public ResponseEntity<Passageiro> updatePassageiro(@RequestBody Passageiro passageiro) throws URISyntaxException {
        log.debug("REST request to update Passageiro : {}", passageiro);
        if (passageiro.getId() == null) {
            return createPassageiro(passageiro);
        }
        Passageiro result = passageiroRepository.save(passageiro);
        passageiroSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, passageiro.getId().toString()))
            .body(result);
    }

    /**
     * GET  /passageiros : get all the passageiros.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of passageiros in body
     */
    @GetMapping("/passageiros")
    @Timed
    public ResponseEntity<List<Passageiro>> getAllPassageiros(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Passageiros");
        Page<Passageiro> page = passageiroRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/passageiros");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /passageiros/:id : get the "id" passageiro.
     *
     * @param id the id of the passageiro to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the passageiro, or with status 404 (Not Found)
     */
    @GetMapping("/passageiros/{id}")
    @Timed
    public ResponseEntity<Passageiro> getPassageiro(@PathVariable Long id) {
        log.debug("REST request to get Passageiro : {}", id);
        Passageiro passageiro = passageiroRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(passageiro));
    }

    /**
     * DELETE  /passageiros/:id : delete the "id" passageiro.
     *
     * @param id the id of the passageiro to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/passageiros/{id}")
    @Timed
    public ResponseEntity<Void> deletePassageiro(@PathVariable Long id) {
        log.debug("REST request to delete Passageiro : {}", id);
        passageiroRepository.delete(id);
        passageiroSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/passageiros?query=:query : search for the passageiro corresponding
     * to the query.
     *
     * @param query the query of the passageiro search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/passageiros")
    @Timed
    public ResponseEntity<List<Passageiro>> searchPassageiros(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Passageiros for query {}", query);
        Page<Passageiro> page = passageiroSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/passageiros");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
