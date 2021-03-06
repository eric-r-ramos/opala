package com.opala.web.rest;

import com.opala.OpalaApp;

import com.opala.domain.Passageiro;
import com.opala.repository.PassageiroRepository;
import com.opala.repository.search.PassageiroSearchRepository;
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
 * Test class for the PassageiroResource REST controller.
 *
 * @see PassageiroResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpalaApp.class)
public class PassageiroResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAAAAAAA";
    private static final String UPDATED_NOME = "BBBBBBBBBB";

    @Autowired
    private PassageiroRepository passageiroRepository;

    @Autowired
    private PassageiroSearchRepository passageiroSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restPassageiroMockMvc;

    private Passageiro passageiro;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PassageiroResource passageiroResource = new PassageiroResource(passageiroRepository, passageiroSearchRepository);
        this.restPassageiroMockMvc = MockMvcBuilders.standaloneSetup(passageiroResource)
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
    public static Passageiro createEntity(EntityManager em) {
        Passageiro passageiro = new Passageiro()
            .nome(DEFAULT_NOME);
        return passageiro;
    }

    @Before
    public void initTest() {
        passageiroSearchRepository.deleteAll();
        passageiro = createEntity(em);
    }

    @Test
    @Transactional
    public void createPassageiro() throws Exception {
        int databaseSizeBeforeCreate = passageiroRepository.findAll().size();

        // Create the Passageiro
        restPassageiroMockMvc.perform(post("/api/passageiros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passageiro)))
            .andExpect(status().isCreated());

        // Validate the Passageiro in the database
        List<Passageiro> passageiroList = passageiroRepository.findAll();
        assertThat(passageiroList).hasSize(databaseSizeBeforeCreate + 1);
        Passageiro testPassageiro = passageiroList.get(passageiroList.size() - 1);
        assertThat(testPassageiro.getNome()).isEqualTo(DEFAULT_NOME);

        // Validate the Passageiro in Elasticsearch
        Passageiro passageiroEs = passageiroSearchRepository.findOne(testPassageiro.getId());
        assertThat(passageiroEs).isEqualToComparingFieldByField(testPassageiro);
    }

    @Test
    @Transactional
    public void createPassageiroWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = passageiroRepository.findAll().size();

        // Create the Passageiro with an existing ID
        passageiro.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPassageiroMockMvc.perform(post("/api/passageiros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passageiro)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Passageiro> passageiroList = passageiroRepository.findAll();
        assertThat(passageiroList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllPassageiros() throws Exception {
        // Initialize the database
        passageiroRepository.saveAndFlush(passageiro);

        // Get all the passageiroList
        restPassageiroMockMvc.perform(get("/api/passageiros?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passageiro.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getPassageiro() throws Exception {
        // Initialize the database
        passageiroRepository.saveAndFlush(passageiro);

        // Get the passageiro
        restPassageiroMockMvc.perform(get("/api/passageiros/{id}", passageiro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(passageiro.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPassageiro() throws Exception {
        // Get the passageiro
        restPassageiroMockMvc.perform(get("/api/passageiros/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePassageiro() throws Exception {
        // Initialize the database
        passageiroRepository.saveAndFlush(passageiro);
        passageiroSearchRepository.save(passageiro);
        int databaseSizeBeforeUpdate = passageiroRepository.findAll().size();

        // Update the passageiro
        Passageiro updatedPassageiro = passageiroRepository.findOne(passageiro.getId());
        updatedPassageiro
            .nome(UPDATED_NOME);

        restPassageiroMockMvc.perform(put("/api/passageiros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedPassageiro)))
            .andExpect(status().isOk());

        // Validate the Passageiro in the database
        List<Passageiro> passageiroList = passageiroRepository.findAll();
        assertThat(passageiroList).hasSize(databaseSizeBeforeUpdate);
        Passageiro testPassageiro = passageiroList.get(passageiroList.size() - 1);
        assertThat(testPassageiro.getNome()).isEqualTo(UPDATED_NOME);

        // Validate the Passageiro in Elasticsearch
        Passageiro passageiroEs = passageiroSearchRepository.findOne(testPassageiro.getId());
        assertThat(passageiroEs).isEqualToComparingFieldByField(testPassageiro);
    }

    @Test
    @Transactional
    public void updateNonExistingPassageiro() throws Exception {
        int databaseSizeBeforeUpdate = passageiroRepository.findAll().size();

        // Create the Passageiro

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPassageiroMockMvc.perform(put("/api/passageiros")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(passageiro)))
            .andExpect(status().isCreated());

        // Validate the Passageiro in the database
        List<Passageiro> passageiroList = passageiroRepository.findAll();
        assertThat(passageiroList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deletePassageiro() throws Exception {
        // Initialize the database
        passageiroRepository.saveAndFlush(passageiro);
        passageiroSearchRepository.save(passageiro);
        int databaseSizeBeforeDelete = passageiroRepository.findAll().size();

        // Get the passageiro
        restPassageiroMockMvc.perform(delete("/api/passageiros/{id}", passageiro.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean passageiroExistsInEs = passageiroSearchRepository.exists(passageiro.getId());
        assertThat(passageiroExistsInEs).isFalse();

        // Validate the database is empty
        List<Passageiro> passageiroList = passageiroRepository.findAll();
        assertThat(passageiroList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchPassageiro() throws Exception {
        // Initialize the database
        passageiroRepository.saveAndFlush(passageiro);
        passageiroSearchRepository.save(passageiro);

        // Search the passageiro
        restPassageiroMockMvc.perform(get("/api/_search/passageiros?query=id:" + passageiro.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(passageiro.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Passageiro.class);
    }
}
