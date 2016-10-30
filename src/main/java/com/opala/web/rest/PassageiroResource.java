package com.opala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opala.domain.Passageiro;

import com.opala.repository.PassageiroRepository;
import com.opala.repository.search.PassageiroSearchRepository;
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
 * REST controller for managing Passageiro.
 */
@RestController
@RequestMapping("/api")
public class PassageiroResource {

    private final Logger log = LoggerFactory.getLogger(PassageiroResource.class);
        
    @Inject
    private PassageiroRepository passageiroRepository;

    @Inject
    private PassageiroSearchRepository passageiroSearchRepository;

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
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("passageiro", "idexists", "A new passageiro cannot already have an ID")).body(null);
        }
        Passageiro result = passageiroRepository.save(passageiro);
        passageiroSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/passageiros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("passageiro", result.getId().toString()))
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
            .headers(HeaderUtil.createEntityUpdateAlert("passageiro", passageiro.getId().toString()))
            .body(result);
    }

    /**
     * GET  /passageiros : get all the passageiros.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of passageiros in body
     */
    @GetMapping("/passageiros")
    @Timed
    public List<Passageiro> getAllPassageiros() {
        log.debug("REST request to get all Passageiros");
        List<Passageiro> passageiros = passageiroRepository.findAll();
        return passageiros;
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
        return Optional.ofNullable(passageiro)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
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
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("passageiro", id.toString())).build();
    }

    /**
     * SEARCH  /_search/passageiros?query=:query : search for the passageiro corresponding
     * to the query.
     *
     * @param query the query of the passageiro search 
     * @return the result of the search
     */
    @GetMapping("/_search/passageiros")
    @Timed
    public List<Passageiro> searchPassageiros(@RequestParam String query) {
        log.debug("REST request to search Passageiros for query {}", query);
        return StreamSupport
            .stream(passageiroSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
