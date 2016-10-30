package com.opala.web.rest;

import com.opala.OpalaApp;

import com.opala.domain.Solicitante;
import com.opala.repository.SolicitanteRepository;
import com.opala.service.SolicitanteService;
import com.opala.repository.search.SolicitanteSearchRepository;
import com.opala.service.dto.SolicitanteDTO;
import com.opala.service.mapper.SolicitanteMapper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";

    @Inject
    private SolicitanteRepository solicitanteRepository;

    @Inject
    private SolicitanteMapper solicitanteMapper;

    @Inject
    private SolicitanteService solicitanteService;

    @Inject
    private SolicitanteSearchRepository solicitanteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restSolicitanteMockMvc;

    private Solicitante solicitante;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SolicitanteResource solicitanteResource = new SolicitanteResource();
        ReflectionTestUtils.setField(solicitanteResource, "solicitanteService", solicitanteService);
        this.restSolicitanteMockMvc = MockMvcBuilders.standaloneSetup(solicitanteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
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
        SolicitanteDTO solicitanteDTO = solicitanteMapper.solicitanteToSolicitanteDTO(solicitante);

        restSolicitanteMockMvc.perform(post("/api/solicitantes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(solicitanteDTO)))
                .andExpect(status().isCreated());

        // Validate the Solicitante in the database
        List<Solicitante> solicitantes = solicitanteRepository.findAll();
        assertThat(solicitantes).hasSize(databaseSizeBeforeCreate + 1);
        Solicitante testSolicitante = solicitantes.get(solicitantes.size() - 1);
        assertThat(testSolicitante.getNome()).isEqualTo(DEFAULT_NOME);

        // Validate the Solicitante in ElasticSearch
        Solicitante solicitanteEs = solicitanteSearchRepository.findOne(testSolicitante.getId());
        assertThat(solicitanteEs).isEqualToComparingFieldByField(testSolicitante);
    }

    @Test
    @Transactional
    public void getAllSolicitantes() throws Exception {
        // Initialize the database
        solicitanteRepository.saveAndFlush(solicitante);

        // Get all the solicitantes
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
        SolicitanteDTO solicitanteDTO = solicitanteMapper.solicitanteToSolicitanteDTO(updatedSolicitante);

        restSolicitanteMockMvc.perform(put("/api/solicitantes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(solicitanteDTO)))
                .andExpect(status().isOk());

        // Validate the Solicitante in the database
        List<Solicitante> solicitantes = solicitanteRepository.findAll();
        assertThat(solicitantes).hasSize(databaseSizeBeforeUpdate);
        Solicitante testSolicitante = solicitantes.get(solicitantes.size() - 1);
        assertThat(testSolicitante.getNome()).isEqualTo(UPDATED_NOME);

        // Validate the Solicitante in ElasticSearch
        Solicitante solicitanteEs = solicitanteSearchRepository.findOne(testSolicitante.getId());
        assertThat(solicitanteEs).isEqualToComparingFieldByField(testSolicitante);
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

        // Validate ElasticSearch is empty
        boolean solicitanteExistsInEs = solicitanteSearchRepository.exists(solicitante.getId());
        assertThat(solicitanteExistsInEs).isFalse();

        // Validate the database is empty
        List<Solicitante> solicitantes = solicitanteRepository.findAll();
        assertThat(solicitantes).hasSize(databaseSizeBeforeDelete - 1);
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
}
