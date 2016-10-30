package com.opala.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.opala.domain.Veiculo;

import com.opala.repository.VeiculoRepository;
import com.opala.repository.search.VeiculoSearchRepository;
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
 * REST controller for managing Veiculo.
 */
@RestController
@RequestMapping("/api")
public class VeiculoResource {

    private final Logger log = LoggerFactory.getLogger(VeiculoResource.class);
        
    @Inject
    private VeiculoRepository veiculoRepository;

    @Inject
    private VeiculoSearchRepository veiculoSearchRepository;

    /**
     * POST  /veiculos : Create a new veiculo.
     *
     * @param veiculo the veiculo to create
     * @return the ResponseEntity with status 201 (Created) and with body the new veiculo, or with status 400 (Bad Request) if the veiculo has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/veiculos")
    @Timed
    public ResponseEntity<Veiculo> createVeiculo(@RequestBody Veiculo veiculo) throws URISyntaxException {
        log.debug("REST request to save Veiculo : {}", veiculo);
        if (veiculo.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("veiculo", "idexists", "A new veiculo cannot already have an ID")).body(null);
        }
        Veiculo result = veiculoRepository.save(veiculo);
        veiculoSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/veiculos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("veiculo", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /veiculos : Updates an existing veiculo.
     *
     * @param veiculo the veiculo to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated veiculo,
     * or with status 400 (Bad Request) if the veiculo is not valid,
     * or with status 500 (Internal Server Error) if the veiculo couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/veiculos")
    @Timed
    public ResponseEntity<Veiculo> updateVeiculo(@RequestBody Veiculo veiculo) throws URISyntaxException {
        log.debug("REST request to update Veiculo : {}", veiculo);
        if (veiculo.getId() == null) {
            return createVeiculo(veiculo);
        }
        Veiculo result = veiculoRepository.save(veiculo);
        veiculoSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("veiculo", veiculo.getId().toString()))
            .body(result);
    }

    /**
     * GET  /veiculos : get all the veiculos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of veiculos in body
     */
    @GetMapping("/veiculos")
    @Timed
    public List<Veiculo> getAllVeiculos() {
        log.debug("REST request to get all Veiculos");
        List<Veiculo> veiculos = veiculoRepository.findAll();
        return veiculos;
    }

    /**
     * GET  /veiculos/:id : get the "id" veiculo.
     *
     * @param id the id of the veiculo to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the veiculo, or with status 404 (Not Found)
     */
    @GetMapping("/veiculos/{id}")
    @Timed
    public ResponseEntity<Veiculo> getVeiculo(@PathVariable Long id) {
        log.debug("REST request to get Veiculo : {}", id);
        Veiculo veiculo = veiculoRepository.findOne(id);
        return Optional.ofNullable(veiculo)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /veiculos/:id : delete the "id" veiculo.
     *
     * @param id the id of the veiculo to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/veiculos/{id}")
    @Timed
    public ResponseEntity<Void> deleteVeiculo(@PathVariable Long id) {
        log.debug("REST request to delete Veiculo : {}", id);
        veiculoRepository.delete(id);
        veiculoSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("veiculo", id.toString())).build();
    }

    /**
     * SEARCH  /_search/veiculos?query=:query : search for the veiculo corresponding
     * to the query.
     *
     * @param query the query of the veiculo search 
     * @return the result of the search
     */
    @GetMapping("/_search/veiculos")
    @Timed
    public List<Veiculo> searchVeiculos(@RequestParam String query) {
        log.debug("REST request to search Veiculos for query {}", query);
        return StreamSupport
            .stream(veiculoSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }


}
