package com.opala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opala.service.ItinerarioService;
import com.opala.web.rest.util.HeaderUtil;
import com.opala.web.rest.util.PaginationUtil;
import com.opala.service.dto.ItinerarioDTO;
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
 * REST controller for managing Itinerario.
 */
@RestController
@RequestMapping("/api")
public class ItinerarioResource {

    private final Logger log = LoggerFactory.getLogger(ItinerarioResource.class);
        
    @Inject
    private ItinerarioService itinerarioService;

    /**
     * POST  /itinerarios : Create a new itinerario.
     *
     * @param itinerarioDTO the itinerarioDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new itinerarioDTO, or with status 400 (Bad Request) if the itinerario has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/itinerarios")
    @Timed
    public ResponseEntity<ItinerarioDTO> createItinerario(@RequestBody ItinerarioDTO itinerarioDTO) throws URISyntaxException {
        log.debug("REST request to save Itinerario : {}", itinerarioDTO);
        if (itinerarioDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("itinerario", "idexists", "A new itinerario cannot already have an ID")).body(null);
        }
        ItinerarioDTO result = itinerarioService.save(itinerarioDTO);
        return ResponseEntity.created(new URI("/api/itinerarios/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("itinerario", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /itinerarios : Updates an existing itinerario.
     *
     * @param itinerarioDTO the itinerarioDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated itinerarioDTO,
     * or with status 400 (Bad Request) if the itinerarioDTO is not valid,
     * or with status 500 (Internal Server Error) if the itinerarioDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/itinerarios")
    @Timed
    public ResponseEntity<ItinerarioDTO> updateItinerario(@RequestBody ItinerarioDTO itinerarioDTO) throws URISyntaxException {
        log.debug("REST request to update Itinerario : {}", itinerarioDTO);
        if (itinerarioDTO.getId() == null) {
            return createItinerario(itinerarioDTO);
        }
        ItinerarioDTO result = itinerarioService.save(itinerarioDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("itinerario", itinerarioDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /itinerarios : get all the itinerarios.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of itinerarios in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/itinerarios")
    @Timed
    public ResponseEntity<List<ItinerarioDTO>> getAllItinerarios(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Itinerarios");
        Page<ItinerarioDTO> page = itinerarioService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/itinerarios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /itinerarios/:id : get the "id" itinerario.
     *
     * @param id the id of the itinerarioDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the itinerarioDTO, or with status 404 (Not Found)
     */
    @GetMapping("/itinerarios/{id}")
    @Timed
    public ResponseEntity<ItinerarioDTO> getItinerario(@PathVariable Long id) {
        log.debug("REST request to get Itinerario : {}", id);
        ItinerarioDTO itinerarioDTO = itinerarioService.findOne(id);
        return Optional.ofNullable(itinerarioDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /itinerarios/:id : delete the "id" itinerario.
     *
     * @param id the id of the itinerarioDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/itinerarios/{id}")
    @Timed
    public ResponseEntity<Void> deleteItinerario(@PathVariable Long id) {
        log.debug("REST request to delete Itinerario : {}", id);
        itinerarioService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("itinerario", id.toString())).build();
    }

    /**
     * SEARCH  /_search/itinerarios?query=:query : search for the itinerario corresponding
     * to the query.
     *
     * @param query the query of the itinerario search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/itinerarios")
    @Timed
    public ResponseEntity<List<ItinerarioDTO>> searchItinerarios(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Itinerarios for query {}", query);
        Page<ItinerarioDTO> page = itinerarioService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/itinerarios");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
