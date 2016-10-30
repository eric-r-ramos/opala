package com.opala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opala.service.VeiculoService;
import com.opala.web.rest.util.HeaderUtil;
import com.opala.web.rest.util.PaginationUtil;
import com.opala.service.dto.VeiculoDTO;
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
 * REST controller for managing Veiculo.
 */
@RestController
@RequestMapping("/api")
public class VeiculoResource {

    private final Logger log = LoggerFactory.getLogger(VeiculoResource.class);
        
    @Inject
    private VeiculoService veiculoService;

    /**
     * POST  /veiculos : Create a new veiculo.
     *
     * @param veiculoDTO the veiculoDTO to create
     * @return the ResponseEntity with status 201 (Created) and with body the new veiculoDTO, or with status 400 (Bad Request) if the veiculo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/veiculos")
    @Timed
    public ResponseEntity<VeiculoDTO> createVeiculo(@RequestBody VeiculoDTO veiculoDTO) throws URISyntaxException {
        log.debug("REST request to save Veiculo : {}", veiculoDTO);
        if (veiculoDTO.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("veiculo", "idexists", "A new veiculo cannot already have an ID")).body(null);
        }
        VeiculoDTO result = veiculoService.save(veiculoDTO);
        return ResponseEntity.created(new URI("/api/veiculos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("veiculo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /veiculos : Updates an existing veiculo.
     *
     * @param veiculoDTO the veiculoDTO to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated veiculoDTO,
     * or with status 400 (Bad Request) if the veiculoDTO is not valid,
     * or with status 500 (Internal Server Error) if the veiculoDTO couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/veiculos")
    @Timed
    public ResponseEntity<VeiculoDTO> updateVeiculo(@RequestBody VeiculoDTO veiculoDTO) throws URISyntaxException {
        log.debug("REST request to update Veiculo : {}", veiculoDTO);
        if (veiculoDTO.getId() == null) {
            return createVeiculo(veiculoDTO);
        }
        VeiculoDTO result = veiculoService.save(veiculoDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("veiculo", veiculoDTO.getId().toString()))
            .body(result);
    }

    /**
     * GET  /veiculos : get all the veiculos.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of veiculos in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/veiculos")
    @Timed
    public ResponseEntity<List<VeiculoDTO>> getAllVeiculos(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Veiculos");
        Page<VeiculoDTO> page = veiculoService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/veiculos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /veiculos/:id : get the "id" veiculo.
     *
     * @param id the id of the veiculoDTO to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the veiculoDTO, or with status 404 (Not Found)
     */
    @GetMapping("/veiculos/{id}")
    @Timed
    public ResponseEntity<VeiculoDTO> getVeiculo(@PathVariable Long id) {
        log.debug("REST request to get Veiculo : {}", id);
        VeiculoDTO veiculoDTO = veiculoService.findOne(id);
        return Optional.ofNullable(veiculoDTO)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /veiculos/:id : delete the "id" veiculo.
     *
     * @param id the id of the veiculoDTO to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/veiculos/{id}")
    @Timed
    public ResponseEntity<Void> deleteVeiculo(@PathVariable Long id) {
        log.debug("REST request to delete Veiculo : {}", id);
        veiculoService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("veiculo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/veiculos?query=:query : search for the veiculo corresponding
     * to the query.
     *
     * @param query the query of the veiculo search 
     * @param pageable the pagination information
     * @return the result of the search
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @GetMapping("/_search/veiculos")
    @Timed
    public ResponseEntity<List<VeiculoDTO>> searchVeiculos(@RequestParam String query, Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to search for a page of Veiculos for query {}", query);
        Page<VeiculoDTO> page = veiculoService.search(query, pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/veiculos");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
