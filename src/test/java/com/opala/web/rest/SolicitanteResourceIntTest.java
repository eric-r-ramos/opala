package com.opala.web.rest;

import com.opala.OpalaApp;

import com.opala.domain.Solicitante;
import com.opala.repository.SolicitanteRepository;
import com.opala.repository.search.SolicitanteSearchRepository;
import com.opala.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SolicitanteResource REST controller.
 *
 * @see SolicitanteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpalaApp.class)
public class SolicitanteResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private SolicitanteRepository solicitanteRepository;

    @Autowired
    private SolicitanteSearchRepository solicitanteSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSolicitanteMockMvc;

    private Solicitante solicitante;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SolicitanteResource solicitanteResource = new SolicitanteResource(solicitanteRepository, solicitanteSearchRepository);
        this.restSolicitanteMockMvc = MockMvcBuilders.standaloneSetup(solicitanteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Solicitante createEntity(EntityManager em) {
        Solicitante solicitante = new Solicitante()
            .nome(DEFAULT_NOME);
        return solicitante;
    }

    @Before
    public void initTest() {
        solicitanteSearchRepository.deleteAll();
        solicitante = createEntity(em);
    }

    @Test
    @Transactional
    public void createSolicitante() throws Exception {
        int databaseSizeBeforeCreate = solicitanteRepository.findAll().size();

        // Create the Solicitante
        restSolicitanteMockMvc.perform(post("/api/solicitantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitante)))
            .andExpect(status().isCreated());

        // Validate the Solicitante in the database
        List<Solicitante> solicitanteList = solicitanteRepository.findAll();
        assertThat(solicitanteList).hasSize(databaseSizeBeforeCreate + 1);
        Solicitante testSolicitante = solicitanteList.get(solicitanteList.size() - 1);
        assertThat(testSolicitante.getNome()).isEqualTo(DEFAULT_NOME);

        // Validate the Solicitante in Elasticsearch
        Solicitante solicitanteEs = solicitanteSearchRepository.findOne(testSolicitante.getId());
        assertThat(solicitanteEs).isEqualToComparingFieldByField(testSolicitante);
    }

    @Test
    @Transactional
    public void createSolicitanteWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = solicitanteRepository.findAll().size();

        // Create the Solicitante with an existing ID
        solicitante.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSolicitanteMockMvc.perform(post("/api/solicitantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitante)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Solicitante> solicitanteList = solicitanteRepository.findAll();
        assertThat(solicitanteList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllSolicitantes() throws Exception {
        // Initialize the database
        solicitanteRepository.saveAndFlush(solicitante);

        // Get all the solicitanteList
        restSolicitanteMockMvc.perform(get("/api/solicitantes?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solicitante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getSolicitante() throws Exception {
        // Initialize the database
        solicitanteRepository.saveAndFlush(solicitante);

        // Get the solicitante
        restSolicitanteMockMvc.perform(get("/api/solicitantes/{id}", solicitante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(solicitante.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSolicitante() throws Exception {
        // Get the solicitante
        restSolicitanteMockMvc.perform(get("/api/solicitantes/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSolicitante() throws Exception {
        // Initialize the database
        solicitanteRepository.saveAndFlush(solicitante);
        solicitanteSearchRepository.save(solicitante);
        int databaseSizeBeforeUpdate = solicitanteRepository.findAll().size();

        // Update the solicitante
        Solicitante updatedSolicitante = solicitanteRepository.findOne(solicitante.getId());
        updatedSolicitante
            .nome(UPDATED_NOME);

        restSolicitanteMockMvc.perform(put("/api/solicitantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSolicitante)))
            .andExpect(status().isOk());

        // Validate the Solicitante in the database
        List<Solicitante> solicitanteList = solicitanteRepository.findAll();
        assertThat(solicitanteList).hasSize(databaseSizeBeforeUpdate);
        Solicitante testSolicitante = solicitanteList.get(solicitanteList.size() - 1);
        assertThat(testSolicitante.getNome()).isEqualTo(UPDATED_NOME);

        // Validate the Solicitante in Elasticsearch
        Solicitante solicitanteEs = solicitanteSearchRepository.findOne(testSolicitante.getId());
        assertThat(solicitanteEs).isEqualToComparingFieldByField(testSolicitante);
    }

    @Test
    @Transactional
    public void updateNonExistingSolicitante() throws Exception {
        int databaseSizeBeforeUpdate = solicitanteRepository.findAll().size();

        // Create the Solicitante

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSolicitanteMockMvc.perform(put("/api/solicitantes")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(solicitante)))
            .andExpect(status().isCreated());

        // Validate the Solicitante in the database
        List<Solicitante> solicitanteList = solicitanteRepository.findAll();
        assertThat(solicitanteList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSolicitante() throws Exception {
        // Initialize the database
        solicitanteRepository.saveAndFlush(solicitante);
        solicitanteSearchRepository.save(solicitante);
        int databaseSizeBeforeDelete = solicitanteRepository.findAll().size();

        // Get the solicitante
        restSolicitanteMockMvc.perform(delete("/api/solicitantes/{id}", solicitante.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean solicitanteExistsInEs = solicitanteSearchRepository.exists(solicitante.getId());
        assertThat(solicitanteExistsInEs).isFalse();

        // Validate the database is empty
        List<Solicitante> solicitanteList = solicitanteRepository.findAll();
        assertThat(solicitanteList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSolicitante() throws Exception {
        // Initialize the database
        solicitanteRepository.saveAndFlush(solicitante);
        solicitanteSearchRepository.save(solicitante);

        // Search the solicitante
        restSolicitanteMockMvc.perform(get("/api/_search/solicitantes?query=id:" + solicitante.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(solicitante.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Solicitante.class);
    }
}
