package com.opala.web.rest;

import com.opala.OpalaApp;

import com.opala.domain.Motorista;
import com.opala.repository.MotoristaRepository;
import com.opala.repository.search.MotoristaSearchRepository;
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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the MotoristaResource REST controller.
 *
 * @see MotoristaResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpalaApp.class)
public class MotoristaResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    private static final String DEFAULT_HABILITACAO = "AAAAAAAAAA";
    private static final String UPDATED_HABILITACAO = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_VENCIMENTO_HABILITACAO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_VENCIMENTO_HABILITACAO = LocalDate.now(ZoneId.systemDefault());

    @Autowired
    private MotoristaRepository motoristaRepository;

    @Autowired
    private MotoristaSearchRepository motoristaSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restMotoristaMockMvc;

    private Motorista motorista;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        MotoristaResource motoristaResource = new MotoristaResource(motoristaRepository, motoristaSearchRepository);
        this.restMotoristaMockMvc = MockMvcBuilders.standaloneSetup(motoristaResource)
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
    public static Motorista createEntity(EntityManager em) {
        Motorista motorista = new Motorista()
            .nome(DEFAULT_NOME)
            .habilitacao(DEFAULT_HABILITACAO)
            .vencimentoHabilitacao(DEFAULT_VENCIMENTO_HABILITACAO);
        return motorista;
    }

    @Before
    public void initTest() {
        motoristaSearchRepository.deleteAll();
        motorista = createEntity(em);
    }

    @Test
    @Transactional
    public void createMotorista() throws Exception {
        int databaseSizeBeforeCreate = motoristaRepository.findAll().size();

        // Create the Motorista
        restMotoristaMockMvc.perform(post("/api/motoristas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motorista)))
            .andExpect(status().isCreated());

        // Validate the Motorista in the database
        List<Motorista> motoristaList = motoristaRepository.findAll();
        assertThat(motoristaList).hasSize(databaseSizeBeforeCreate + 1);
        Motorista testMotorista = motoristaList.get(motoristaList.size() - 1);
        assertThat(testMotorista.getNome()).isEqualTo(DEFAULT_NOME);
        assertThat(testMotorista.getHabilitacao()).isEqualTo(DEFAULT_HABILITACAO);
        assertThat(testMotorista.getVencimentoHabilitacao()).isEqualTo(DEFAULT_VENCIMENTO_HABILITACAO);

        // Validate the Motorista in Elasticsearch
        Motorista motoristaEs = motoristaSearchRepository.findOne(testMotorista.getId());
        assertThat(motoristaEs).isEqualToComparingFieldByField(testMotorista);
    }

    @Test
    @Transactional
    public void createMotoristaWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = motoristaRepository.findAll().size();

        // Create the Motorista with an existing ID
        motorista.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMotoristaMockMvc.perform(post("/api/motoristas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motorista)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Motorista> motoristaList = motoristaRepository.findAll();
        assertThat(motoristaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllMotoristas() throws Exception {
        // Initialize the database
        motoristaRepository.saveAndFlush(motorista);

        // Get all the motoristaList
        restMotoristaMockMvc.perform(get("/api/motoristas?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motorista.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].habilitacao").value(hasItem(DEFAULT_HABILITACAO.toString())))
            .andExpect(jsonPath("$.[*].vencimentoHabilitacao").value(hasItem(DEFAULT_VENCIMENTO_HABILITACAO.toString())));
    }

    @Test
    @Transactional
    public void getMotorista() throws Exception {
        // Initialize the database
        motoristaRepository.saveAndFlush(motorista);

        // Get the motorista
        restMotoristaMockMvc.perform(get("/api/motoristas/{id}", motorista.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(motorista.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()))
            .andExpect(jsonPath("$.habilitacao").value(DEFAULT_HABILITACAO.toString()))
            .andExpect(jsonPath("$.vencimentoHabilitacao").value(DEFAULT_VENCIMENTO_HABILITACAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingMotorista() throws Exception {
        // Get the motorista
        restMotoristaMockMvc.perform(get("/api/motoristas/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMotorista() throws Exception {
        // Initialize the database
        motoristaRepository.saveAndFlush(motorista);
        motoristaSearchRepository.save(motorista);
        int databaseSizeBeforeUpdate = motoristaRepository.findAll().size();

        // Update the motorista
        Motorista updatedMotorista = motoristaRepository.findOne(motorista.getId());
        updatedMotorista
            .nome(UPDATED_NOME)
            .habilitacao(UPDATED_HABILITACAO)
            .vencimentoHabilitacao(UPDATED_VENCIMENTO_HABILITACAO);

        restMotoristaMockMvc.perform(put("/api/motoristas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedMotorista)))
            .andExpect(status().isOk());

        // Validate the Motorista in the database
        List<Motorista> motoristaList = motoristaRepository.findAll();
        assertThat(motoristaList).hasSize(databaseSizeBeforeUpdate);
        Motorista testMotorista = motoristaList.get(motoristaList.size() - 1);
        assertThat(testMotorista.getNome()).isEqualTo(UPDATED_NOME);
        assertThat(testMotorista.getHabilitacao()).isEqualTo(UPDATED_HABILITACAO);
        assertThat(testMotorista.getVencimentoHabilitacao()).isEqualTo(UPDATED_VENCIMENTO_HABILITACAO);

        // Validate the Motorista in Elasticsearch
        Motorista motoristaEs = motoristaSearchRepository.findOne(testMotorista.getId());
        assertThat(motoristaEs).isEqualToComparingFieldByField(testMotorista);
    }

    @Test
    @Transactional
    public void updateNonExistingMotorista() throws Exception {
        int databaseSizeBeforeUpdate = motoristaRepository.findAll().size();

        // Create the Motorista

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restMotoristaMockMvc.perform(put("/api/motoristas")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(motorista)))
            .andExpect(status().isCreated());

        // Validate the Motorista in the database
        List<Motorista> motoristaList = motoristaRepository.findAll();
        assertThat(motoristaList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteMotorista() throws Exception {
        // Initialize the database
        motoristaRepository.saveAndFlush(motorista);
        motoristaSearchRepository.save(motorista);
        int databaseSizeBeforeDelete = motoristaRepository.findAll().size();

        // Get the motorista
        restMotoristaMockMvc.perform(delete("/api/motoristas/{id}", motorista.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean motoristaExistsInEs = motoristaSearchRepository.exists(motorista.getId());
        assertThat(motoristaExistsInEs).isFalse();

        // Validate the database is empty
        List<Motorista> motoristaList = motoristaRepository.findAll();
        assertThat(motoristaList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchMotorista() throws Exception {
        // Initialize the database
        motoristaRepository.saveAndFlush(motorista);
        motoristaSearchRepository.save(motorista);

        // Search the motorista
        restMotoristaMockMvc.perform(get("/api/_search/motoristas?query=id:" + motorista.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(motorista.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())))
            .andExpect(jsonPath("$.[*].habilitacao").value(hasItem(DEFAULT_HABILITACAO.toString())))
            .andExpect(jsonPath("$.[*].vencimentoHabilitacao").value(hasItem(DEFAULT_VENCIMENTO_HABILITACAO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Motorista.class);
    }
}
