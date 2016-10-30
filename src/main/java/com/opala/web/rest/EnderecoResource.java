package com.opala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opala.service.EnderecoService;
import com.opala.web.rest.util.HeaderUtil;
import com.opala.web.rest.util.PaginationUtil;
import com.opala.service.dto.EnderecoDTO;
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
 * REST controller for managing Endereco.
 */
@RestController
@RequestMapping("/api")
public class EnderecoResource {

    private final Logger log = LoggerFactory.getLogger(EnderecoResource.class);
        
    @Inject
    private EnderecoService enderecoService;

    /**
     * POST  /enderecos : Create a new endereco.
     *
     * @param enderecoDTO the enderecoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new enderecoDTO, or with status 400 (Bad Request) if the endereco has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/enderecos")
    @Timed
    public ResponseEntity<EnderecoDTO> createEndereco(@RequestBody EnderecoDTO enderecoDTO) throws URISyntaxException {
        log.debug("REST request to save Endereco : {}", enderecoDTO);
        if (enderecoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("endereco", "idexists", "A new endereco cannot already have an ID")).body(null);
        }
        EnderecoDTO result = enderecoService.save(enderecoDTO);
        return ResponseEntity.created(new URI("/api/enderecos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("endereco", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /enderecos : Updates an existing endereco.
     *
     * @param enderecoDTO the enderecoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated enderecoDTO,
     * or with status 400 (Bad Request) if the enderecoDTO is not valid,
     * or with status 500 (Internal Server Error) if the enderecoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/enderecos")
    @Timed
    public ResponseEntity<EnderecoDTO> updateEndereco(@RequestBody EnderecoDTO enderecoDTO) throws URISyntaxException {
        log.debug("REST request to update Endereco : {}", enderecoDTO);
        if (enderecoDTO.getId() == null) {
            return createEndereco(enderecoDTO);
        }
        EnderecoDTO result = enderecoService.save(enderecoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("endereco", enderecoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /enderecos : get all the enderecos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of enderecos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/enderecos")
    @Timed
    public ResponseEntity<List<EnderecoDTO>> getAllEnderecos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Enderecos");
        Page<EnderecoDTO> page = enderecoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/enderecos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /enderecos/:id : get the "id" endereco.
     *
     * @param id the id of the enderecoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the enderecoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/enderecos/{id}")
    @Timed
    public ResponseEntity<EnderecoDTO> getEndereco(@PathVariable Long id) {
        log.debug("REST request to get Endereco : {}", id);
        EnderecoDTO enderecoDTO = enderecoService.findOne(id);
        return Optional.ofNullable(enderecoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /enderecos/:id : delete the "id" endereco.
     *
     * @param id the id of the enderecoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/enderecos/{id}")
    @Timed
    public ResponseEntity<Void> deleteEndereco(@PathVariable Long id) {
        log.debug("REST request to delete Endereco : {}", id);
        enderecoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("endereco", id.toString())).build();
    }

    /**
     * SEARCH  /_search/enderecos?query=:query : search for the endereco corresponding
     * to the query.
     *
     * @param query the query of the endereco search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/enderecos")
    @Timed
    public ResponseEntity<List<EnderecoDTO>> searchEnderecos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Enderecos for query {}", query);
        Page<EnderecoDTO> page = enderecoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/enderecos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
