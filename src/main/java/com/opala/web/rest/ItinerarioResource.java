package com.opala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opala.domain.Itinerario;

import com.opala.repository.ItinerarioRepository;
import com.opala.repository.search.ItinerarioSearchRepository;
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
 * REST controller for managing Itinerario.
 */
@RestController
@RequestMapping("/api")
public class ItinerarioResource {

    private final Logger log = LoggerFactory.getLogger(ItinerarioResource.class);

    private static final String ENTITY_NAME = "itinerario";
        
    private final ItinerarioRepository itinerarioRepository;

    private final ItinerarioSearchRepository itinerarioSearchRepository;

    public ItinerarioResource(ItinerarioRepository itinerarioRepository, ItinerarioSearchRepository itinerarioSearchRepository) {
        this.itinerarioRepository = itinerarioRepository;
        this.itinerarioSearchRepository = itinerarioSearchRepository;
    }

    /**
     * POST  /itinerarios : Create a new itinerario.
     *
     * @param itinerario the itinerario to create
     * @return the ResponseEntity with status 201 (Created) and with body the new itinerario, or with status 400 (Bad Request) if the itinerario has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/itinerarios")
    @Timed
    public ResponseEntity<Itinerario> createItinerario(@RequestBody Itinerario itinerario) throws URISyntaxException {
        log.debug("REST request to save Itinerario : {}", itinerario);
        if (itinerario.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new itinerario cannot already have an ID")).body(null);
        }
        Itinerario result = itinerarioRepository.save(itinerario);
        itinerarioSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/itinerarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /itinerarios : Updates an existing itinerario.
     *
     * @param itinerario the itinerario to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated itinerario,
     * or with status 400 (Bad Request) if the itinerario is not valid,
     * or with status 500 (Internal Server Error) if the itinerario couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/itinerarios")
    @Timed
    public ResponseEntity<Itinerario> updateItinerario(@RequestBody Itinerario itinerario) throws URISyntaxException {
        log.debug("REST request to update Itinerario : {}", itinerario);
        if (itinerario.getId() == null) {
            return createItinerario(itinerario);
        }
        Itinerario result = itinerarioRepository.save(itinerario);
        itinerarioSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, itinerario.getId().toString()))
            .body(result);
    }

    /**
     * GET  /itinerarios : get all the itinerarios.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of itinerarios in body
     */
    @GetMapping("/itinerarios")
    @Timed
    public ResponseEntity<List<Itinerario>> getAllItinerarios(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of Itinerarios");
        Page<Itinerario> page = itinerarioRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/itinerarios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /itinerarios/:id : get the "id" itinerario.
     *
     * @param id the id of the itinerario to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the itinerario, or with status 404 (Not Found)
     */
    @GetMapping("/itinerarios/{id}")
    @Timed
    public ResponseEntity<Itinerario> getItinerario(@PathVariable Long id) {
        log.debug("REST request to get Itinerario : {}", id);
        Itinerario itinerario = itinerarioRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(itinerario));
    }

    /**
     * DELETE  /itinerarios/:id : delete the "id" itinerario.
     *
     * @param id the id of the itinerario to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/itinerarios/{id}")
    @Timed
    public ResponseEntity<Void> deleteItinerario(@PathVariable Long id) {
        log.debug("REST request to delete Itinerario : {}", id);
        itinerarioRepository.delete(id);
        itinerarioSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/itinerarios?query=:query : search for the itinerario corresponding
     * to the query.
     *
     * @param query the query of the itinerario search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/itinerarios")
    @Timed
    public ResponseEntity<List<Itinerario>> searchItinerarios(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of Itinerarios for query {}", query);
        Page<Itinerario> page = itinerarioSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/itinerarios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
