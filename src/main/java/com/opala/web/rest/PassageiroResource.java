package com.opala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opala.service.PassageiroService;
import com.opala.web.rest.util.HeaderUtil;
import com.opala.web.rest.util.PaginationUtil;
import com.opala.service.dto.PassageiroDTO;
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
 * REST controller for managing Passageiro.
 */
@RestController
@RequestMapping("/api")
public class PassageiroResource {

    private final Logger log = LoggerFactory.getLogger(PassageiroResource.class);
        
    @Inject
    private PassageiroService passageiroService;

    /**
     * POST  /passageiros : Create a new passageiro.
     *
     * @param passageiroDTO the passageiroDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new passageiroDTO, or with status 400 (Bad Request) if the passageiro has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/passageiros")
    @Timed
    public ResponseEntity<PassageiroDTO> createPassageiro(@RequestBody PassageiroDTO passageiroDTO) throws URISyntaxException {
        log.debug("REST request to save Passageiro : {}", passageiroDTO);
        if (passageiroDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("passageiro", "idexists", "A new passageiro cannot already have an ID")).body(null);
        }
        PassageiroDTO result = passageiroService.save(passageiroDTO);
        return ResponseEntity.created(new URI("/api/passageiros/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("passageiro", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /passageiros : Updates an existing passageiro.
     *
     * @param passageiroDTO the passageiroDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated passageiroDTO,
     * or with status 400 (Bad Request) if the passageiroDTO is not valid,
     * or with status 500 (Internal Server Error) if the passageiroDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/passageiros")
    @Timed
    public ResponseEntity<PassageiroDTO> updatePassageiro(@RequestBody PassageiroDTO passageiroDTO) throws URISyntaxException {
        log.debug("REST request to update Passageiro : {}", passageiroDTO);
        if (passageiroDTO.getId() == null) {
            return createPassageiro(passageiroDTO);
        }
        PassageiroDTO result = passageiroService.save(passageiroDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("passageiro", passageiroDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /passageiros : get all the passageiros.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of passageiros in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/passageiros")
    @Timed
    public ResponseEntity<List<PassageiroDTO>> getAllPassageiros(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Passageiros");
        Page<PassageiroDTO> page = passageiroService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/passageiros");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /passageiros/:id : get the "id" passageiro.
     *
     * @param id the id of the passageiroDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the passageiroDTO, or with status 404 (Not Found)
     */
    @GetMapping("/passageiros/{id}")
    @Timed
    public ResponseEntity<PassageiroDTO> getPassageiro(@PathVariable Long id) {
        log.debug("REST request to get Passageiro : {}", id);
        PassageiroDTO passageiroDTO = passageiroService.findOne(id);
        return Optional.ofNullable(passageiroDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /passageiros/:id : delete the "id" passageiro.
     *
     * @param id the id of the passageiroDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/passageiros/{id}")
    @Timed
    public ResponseEntity<Void> deletePassageiro(@PathVariable Long id) {
        log.debug("REST request to delete Passageiro : {}", id);
        passageiroService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("passageiro", id.toString())).build();
    }

    /**
     * SEARCH  /_search/passageiros?query=:query : search for the passageiro corresponding
     * to the query.
     *
     * @param query the query of the passageiro search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/passageiros")
    @Timed
    public ResponseEntity<List<PassageiroDTO>> searchPassageiros(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Passageiros for query {}", query);
        Page<PassageiroDTO> page = passageiroService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/passageiros");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
