package com.opala.web.rest;

import com.opala.OpalaApp;

import com.opala.domain.Endereco;
import com.opala.repository.EnderecoRepository;
import com.opala.repository.search.EnderecoSearchRepository;
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
 * Test class for the EnderecoResource REST controller.
 *
 * @see EnderecoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpalaApp.class)
public class EnderecoResourceIntTest {

    private static final String DEFAULT_LINHA_1 = "AAAAAAAAAA";
    private static final String UPDATED_LINHA_1 = "BBBBBBBBBB";

    private static final String DEFAULT_LINHA_2 = "AAAAAAAAAA";
    private static final String UPDATED_LINHA_2 = "BBBBBBBBBB";

    private static final String DEFAULT_CIDADE = "AAAAAAAAAA";
    private static final String UPDATED_CIDADE = "BBBBBBBBBB";

    private static final String DEFAULT_ESTADO = "AAAAAAAAAA";
    private static final String UPDATED_ESTADO = "BBBBBBBBBB";

    private static final String DEFAULT_CEP = "AAAAAAAAAA";
    private static final String UPDATED_CEP = "BBBBBBBBBB";

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private EnderecoSearchRepository enderecoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restEnderecoMockMvc;

    private Endereco endereco;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EnderecoResource enderecoResource = new EnderecoResource(enderecoRepository, enderecoSearchRepository);
        this.restEnderecoMockMvc = MockMvcBuilders.standaloneSetup(enderecoResource)
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
    public static Endereco createEntity(EntityManager em) {
        Endereco endereco = new Endereco()
            .linha1(DEFAULT_LINHA_1)
            .linha2(DEFAULT_LINHA_2)
            .cidade(DEFAULT_CIDADE)
            .estado(DEFAULT_ESTADO)
            .cep(DEFAULT_CEP);
        return endereco;
    }

    @Before
    public void initTest() {
        enderecoSearchRepository.deleteAll();
        endereco = createEntity(em);
    }

    @Test
    @Transactional
    public void createEndereco() throws Exception {
        int databaseSizeBeforeCreate = enderecoRepository.findAll().size();

        // Create the Endereco
        restEnderecoMockMvc.perform(post("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(endereco)))
            .andExpect(status().isCreated());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeCreate + 1);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getLinha1()).isEqualTo(DEFAULT_LINHA_1);
        assertThat(testEndereco.getLinha2()).isEqualTo(DEFAULT_LINHA_2);
        assertThat(testEndereco.getCidade()).isEqualTo(DEFAULT_CIDADE);
        assertThat(testEndereco.getEstado()).isEqualTo(DEFAULT_ESTADO);
        assertThat(testEndereco.getCep()).isEqualTo(DEFAULT_CEP);

        // Validate the Endereco in Elasticsearch
        Endereco enderecoEs = enderecoSearchRepository.findOne(testEndereco.getId());
        assertThat(enderecoEs).isEqualToComparingFieldByField(testEndereco);
    }

    @Test
    @Transactional
    public void createEnderecoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = enderecoRepository.findAll().size();

        // Create the Endereco with an existing ID
        endereco.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restEnderecoMockMvc.perform(post("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(endereco)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllEnderecos() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get all the enderecoList
        restEnderecoMockMvc.perform(get("/api/enderecos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
            .andExpect(jsonPath("$.[*].linha1").value(hasItem(DEFAULT_LINHA_1.toString())))
            .andExpect(jsonPath("$.[*].linha2").value(hasItem(DEFAULT_LINHA_2.toString())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())));
    }

    @Test
    @Transactional
    public void getEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);

        // Get the endereco
        restEnderecoMockMvc.perform(get("/api/enderecos/{id}", endereco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(endereco.getId().intValue()))
            .andExpect(jsonPath("$.linha1").value(DEFAULT_LINHA_1.toString()))
            .andExpect(jsonPath("$.linha2").value(DEFAULT_LINHA_2.toString()))
            .andExpect(jsonPath("$.cidade").value(DEFAULT_CIDADE.toString()))
            .andExpect(jsonPath("$.estado").value(DEFAULT_ESTADO.toString()))
            .andExpect(jsonPath("$.cep").value(DEFAULT_CEP.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEndereco() throws Exception {
        // Get the endereco
        restEnderecoMockMvc.perform(get("/api/enderecos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);
        enderecoSearchRepository.save(endereco);
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();

        // Update the endereco
        Endereco updatedEndereco = enderecoRepository.findOne(endereco.getId());
        updatedEndereco
            .linha1(UPDATED_LINHA_1)
            .linha2(UPDATED_LINHA_2)
            .cidade(UPDATED_CIDADE)
            .estado(UPDATED_ESTADO)
            .cep(UPDATED_CEP);

        restEnderecoMockMvc.perform(put("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedEndereco)))
            .andExpect(status().isOk());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate);
        Endereco testEndereco = enderecoList.get(enderecoList.size() - 1);
        assertThat(testEndereco.getLinha1()).isEqualTo(UPDATED_LINHA_1);
        assertThat(testEndereco.getLinha2()).isEqualTo(UPDATED_LINHA_2);
        assertThat(testEndereco.getCidade()).isEqualTo(UPDATED_CIDADE);
        assertThat(testEndereco.getEstado()).isEqualTo(UPDATED_ESTADO);
        assertThat(testEndereco.getCep()).isEqualTo(UPDATED_CEP);

        // Validate the Endereco in Elasticsearch
        Endereco enderecoEs = enderecoSearchRepository.findOne(testEndereco.getId());
        assertThat(enderecoEs).isEqualToComparingFieldByField(testEndereco);
    }

    @Test
    @Transactional
    public void updateNonExistingEndereco() throws Exception {
        int databaseSizeBeforeUpdate = enderecoRepository.findAll().size();

        // Create the Endereco

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restEnderecoMockMvc.perform(put("/api/enderecos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(endereco)))
            .andExpect(status().isCreated());

        // Validate the Endereco in the database
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);
        enderecoSearchRepository.save(endereco);
        int databaseSizeBeforeDelete = enderecoRepository.findAll().size();

        // Get the endereco
        restEnderecoMockMvc.perform(delete("/api/enderecos/{id}", endereco.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean enderecoExistsInEs = enderecoSearchRepository.exists(endereco.getId());
        assertThat(enderecoExistsInEs).isFalse();

        // Validate the database is empty
        List<Endereco> enderecoList = enderecoRepository.findAll();
        assertThat(enderecoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchEndereco() throws Exception {
        // Initialize the database
        enderecoRepository.saveAndFlush(endereco);
        enderecoSearchRepository.save(endereco);

        // Search the endereco
        restEnderecoMockMvc.perform(get("/api/_search/enderecos?query=id:" + endereco.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(endereco.getId().intValue())))
            .andExpect(jsonPath("$.[*].linha1").value(hasItem(DEFAULT_LINHA_1.toString())))
            .andExpect(jsonPath("$.[*].linha2").value(hasItem(DEFAULT_LINHA_2.toString())))
            .andExpect(jsonPath("$.[*].cidade").value(hasItem(DEFAULT_CIDADE.toString())))
            .andExpect(jsonPath("$.[*].estado").value(hasItem(DEFAULT_ESTADO.toString())))
            .andExpect(jsonPath("$.[*].cep").value(hasItem(DEFAULT_CEP.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Endereco.class);
    }
}
