package com.opala.web.rest;

import com.opala.OpalaApp;

import com.opala.domain.Veiculo;
import com.opala.repository.VeiculoRepository;
import com.opala.repository.search.VeiculoSearchRepository;
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
 * Test class for the VeiculoResource REST controller.
 *
 * @see VeiculoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpalaApp.class)
public class VeiculoResourceIntTest {

    private static final String DEFAULT_MARCA = "AAAAAAAAAA";
    private static final String UPDATED_MARCA = "BBBBBBBBBB";

    private static final String DEFAULT_MODELO = "AAAAAAAAAA";
    private static final String UPDATED_MODELO = "BBBBBBBBBB";

    private static final String DEFAULT_COR = "AAAAAAAAAA";
    private static final String UPDATED_COR = "BBBBBBBBBB";

    private static final String DEFAULT_PLACA = "AAAAAAAAAA";
    private static final String UPDATED_PLACA = "BBBBBBBBBB";

    private static final String DEFAULT_ANO = "AAAAAAAAAA";
    private static final String UPDATED_ANO = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ATIVO = false;
    private static final Boolean UPDATED_ATIVO = true;

    @Autowired
    private VeiculoRepository veiculoRepository;

    @Autowired
    private VeiculoSearchRepository veiculoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restVeiculoMockMvc;

    private Veiculo veiculo;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        VeiculoResource veiculoResource = new VeiculoResource(veiculoRepository, veiculoSearchRepository);
        this.restVeiculoMockMvc = MockMvcBuilders.standaloneSetup(veiculoResource)
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
    public static Veiculo createEntity(EntityManager em) {
        Veiculo veiculo = new Veiculo()
            .marca(DEFAULT_MARCA)
            .modelo(DEFAULT_MODELO)
            .cor(DEFAULT_COR)
            .placa(DEFAULT_PLACA)
            .ano(DEFAULT_ANO)
            .ativo(DEFAULT_ATIVO);
        return veiculo;
    }

    @Before
    public void initTest() {
        veiculoSearchRepository.deleteAll();
        veiculo = createEntity(em);
    }

    @Test
    @Transactional
    public void createVeiculo() throws Exception {
        int databaseSizeBeforeCreate = veiculoRepository.findAll().size();

        // Create the Veiculo
        restVeiculoMockMvc.perform(post("/api/veiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(veiculo)))
            .andExpect(status().isCreated());

        // Validate the Veiculo in the database
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeCreate + 1);
        Veiculo testVeiculo = veiculoList.get(veiculoList.size() - 1);
        assertThat(testVeiculo.getMarca()).isEqualTo(DEFAULT_MARCA);
        assertThat(testVeiculo.getModelo()).isEqualTo(DEFAULT_MODELO);
        assertThat(testVeiculo.getCor()).isEqualTo(DEFAULT_COR);
        assertThat(testVeiculo.getPlaca()).isEqualTo(DEFAULT_PLACA);
        assertThat(testVeiculo.getAno()).isEqualTo(DEFAULT_ANO);
        assertThat(testVeiculo.isAtivo()).isEqualTo(DEFAULT_ATIVO);

        // Validate the Veiculo in Elasticsearch
        Veiculo veiculoEs = veiculoSearchRepository.findOne(testVeiculo.getId());
        assertThat(veiculoEs).isEqualToComparingFieldByField(testVeiculo);
    }

    @Test
    @Transactional
    public void createVeiculoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = veiculoRepository.findAll().size();

        // Create the Veiculo with an existing ID
        veiculo.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restVeiculoMockMvc.perform(post("/api/veiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(veiculo)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllVeiculos() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get all the veiculoList
        restVeiculoMockMvc.perform(get("/api/veiculos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(veiculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].marca").value(hasItem(DEFAULT_MARCA.toString())))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO.toString())))
            .andExpect(jsonPath("$.[*].cor").value(hasItem(DEFAULT_COR.toString())))
            .andExpect(jsonPath("$.[*].placa").value(hasItem(DEFAULT_PLACA.toString())))
            .andExpect(jsonPath("$.[*].ano").value(hasItem(DEFAULT_ANO.toString())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));
    }

    @Test
    @Transactional
    public void getVeiculo() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);

        // Get the veiculo
        restVeiculoMockMvc.perform(get("/api/veiculos/{id}", veiculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(veiculo.getId().intValue()))
            .andExpect(jsonPath("$.marca").value(DEFAULT_MARCA.toString()))
            .andExpect(jsonPath("$.modelo").value(DEFAULT_MODELO.toString()))
            .andExpect(jsonPath("$.cor").value(DEFAULT_COR.toString()))
            .andExpect(jsonPath("$.placa").value(DEFAULT_PLACA.toString()))
            .andExpect(jsonPath("$.ano").value(DEFAULT_ANO.toString()))
            .andExpect(jsonPath("$.ativo").value(DEFAULT_ATIVO.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingVeiculo() throws Exception {
        // Get the veiculo
        restVeiculoMockMvc.perform(get("/api/veiculos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateVeiculo() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);
        veiculoSearchRepository.save(veiculo);
        int databaseSizeBeforeUpdate = veiculoRepository.findAll().size();

        // Update the veiculo
        Veiculo updatedVeiculo = veiculoRepository.findOne(veiculo.getId());
        updatedVeiculo
            .marca(UPDATED_MARCA)
            .modelo(UPDATED_MODELO)
            .cor(UPDATED_COR)
            .placa(UPDATED_PLACA)
            .ano(UPDATED_ANO)
            .ativo(UPDATED_ATIVO);

        restVeiculoMockMvc.perform(put("/api/veiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedVeiculo)))
            .andExpect(status().isOk());

        // Validate the Veiculo in the database
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeUpdate);
        Veiculo testVeiculo = veiculoList.get(veiculoList.size() - 1);
        assertThat(testVeiculo.getMarca()).isEqualTo(UPDATED_MARCA);
        assertThat(testVeiculo.getModelo()).isEqualTo(UPDATED_MODELO);
        assertThat(testVeiculo.getCor()).isEqualTo(UPDATED_COR);
        assertThat(testVeiculo.getPlaca()).isEqualTo(UPDATED_PLACA);
        assertThat(testVeiculo.getAno()).isEqualTo(UPDATED_ANO);
        assertThat(testVeiculo.isAtivo()).isEqualTo(UPDATED_ATIVO);

        // Validate the Veiculo in Elasticsearch
        Veiculo veiculoEs = veiculoSearchRepository.findOne(testVeiculo.getId());
        assertThat(veiculoEs).isEqualToComparingFieldByField(testVeiculo);
    }

    @Test
    @Transactional
    public void updateNonExistingVeiculo() throws Exception {
        int databaseSizeBeforeUpdate = veiculoRepository.findAll().size();

        // Create the Veiculo

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restVeiculoMockMvc.perform(put("/api/veiculos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(veiculo)))
            .andExpect(status().isCreated());

        // Validate the Veiculo in the database
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteVeiculo() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);
        veiculoSearchRepository.save(veiculo);
        int databaseSizeBeforeDelete = veiculoRepository.findAll().size();

        // Get the veiculo
        restVeiculoMockMvc.perform(delete("/api/veiculos/{id}", veiculo.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean veiculoExistsInEs = veiculoSearchRepository.exists(veiculo.getId());
        assertThat(veiculoExistsInEs).isFalse();

        // Validate the database is empty
        List<Veiculo> veiculoList = veiculoRepository.findAll();
        assertThat(veiculoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchVeiculo() throws Exception {
        // Initialize the database
        veiculoRepository.saveAndFlush(veiculo);
        veiculoSearchRepository.save(veiculo);

        // Search the veiculo
        restVeiculoMockMvc.perform(get("/api/_search/veiculos?query=id:" + veiculo.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(veiculo.getId().intValue())))
            .andExpect(jsonPath("$.[*].marca").value(hasItem(DEFAULT_MARCA.toString())))
            .andExpect(jsonPath("$.[*].modelo").value(hasItem(DEFAULT_MODELO.toString())))
            .andExpect(jsonPath("$.[*].cor").value(hasItem(DEFAULT_COR.toString())))
            .andExpect(jsonPath("$.[*].placa").value(hasItem(DEFAULT_PLACA.toString())))
            .andExpect(jsonPath("$.[*].ano").value(hasItem(DEFAULT_ANO.toString())))
            .andExpect(jsonPath("$.[*].ativo").value(hasItem(DEFAULT_ATIVO.booleanValue())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Veiculo.class);
    }
}
