package com.opala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opala.service.MotoristaService;
import com.opala.web.rest.util.HeaderUtil;
import com.opala.web.rest.util.PaginationUtil;
import com.opala.service.dto.MotoristaDTO;
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
 * REST controller for managing Motorista.
 */
@RestController
@RequestMapping("/api")
public class MotoristaResource {

    private final Logger log = LoggerFactory.getLogger(MotoristaResource.class);
        
    @Inject
    private MotoristaService motoristaService;

    /**
     * POST  /motoristas : Create a new motorista.
     *
     * @param motoristaDTO the motoristaDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new motoristaDTO, or with status 400 (Bad Request) if the motorista has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/motoristas")
    @Timed
    public ResponseEntity<MotoristaDTO> createMotorista(@RequestBody MotoristaDTO motoristaDTO) throws URISyntaxException {
        log.debug("REST request to save Motorista : {}", motoristaDTO);
        if (motoristaDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("motorista", "idexists", "A new motorista cannot already have an ID")).body(null);
        }
        MotoristaDTO result = motoristaService.save(motoristaDTO);
        return ResponseEntity.created(new URI("/api/motoristas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("motorista", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /motoristas : Updates an existing motorista.
     *
     * @param motoristaDTO the motoristaDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated motoristaDTO,
     * or with status 400 (Bad Request) if the motoristaDTO is not valid,
     * or with status 500 (Internal Server Error) if the motoristaDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/motoristas")
    @Timed
    public ResponseEntity<MotoristaDTO> updateMotorista(@RequestBody MotoristaDTO motoristaDTO) throws URISyntaxException {
        log.debug("REST request to update Motorista : {}", motoristaDTO);
        if (motoristaDTO.getId() == null) {
            return createMotorista(motoristaDTO);
        }
        MotoristaDTO result = motoristaService.save(motoristaDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("motorista", motoristaDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /motoristas : get all the motoristas.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of motoristas in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/motoristas")
    @Timed
    public ResponseEntity<List<MotoristaDTO>> getAllMotoristas(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Motoristas");
        Page<MotoristaDTO> page = motoristaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/motoristas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /motoristas/:id : get the "id" motorista.
     *
     * @param id the id of the motoristaDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the motoristaDTO, or with status 404 (Not Found)
     */
    @GetMapping("/motoristas/{id}")
    @Timed
    public ResponseEntity<MotoristaDTO> getMotorista(@PathVariable Long id) {
        log.debug("REST request to get Motorista : {}", id);
        MotoristaDTO motoristaDTO = motoristaService.findOne(id);
        return Optional.ofNullable(motoristaDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /motoristas/:id : delete the "id" motorista.
     *
     * @param id the id of the motoristaDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/motoristas/{id}")
    @Timed
    public ResponseEntity<Void> deleteMotorista(@PathVariable Long id) {
        log.debug("REST request to delete Motorista : {}", id);
        motoristaService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("motorista", id.toString())).build();
    }

    /**
     * SEARCH  /_search/motoristas?query=:query : search for the motorista corresponding
     * to the query.
     *
     * @param query the query of the motorista search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/motoristas")
    @Timed
    public ResponseEntity<List<MotoristaDTO>> searchMotoristas(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Motoristas for query {}", query);
        Page<MotoristaDTO> page = motoristaService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/motoristas");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
