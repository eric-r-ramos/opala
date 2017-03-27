package com.opala.web.rest;

import com.opala.OpalaApp;

import com.opala.domain.Itinerario;
import com.opala.repository.ItinerarioRepository;
import com.opala.repository.search.ItinerarioSearchRepository;
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
 * Test class for the ItinerarioResource REST controller.
 *
 * @see ItinerarioResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpalaApp.class)
public class ItinerarioResourceIntTest {

    private static final String DEFAULT_DESCRICAO = "AAAAAAAAAA";
    private static final String UPDATED_DESCRICAO = "BBBBBBBBBB";

    @Autowired
    private ItinerarioRepository itinerarioRepository;

    @Autowired
    private ItinerarioSearchRepository itinerarioSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restItinerarioMockMvc;

    private Itinerario itinerario;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ItinerarioResource itinerarioResource = new ItinerarioResource(itinerarioRepository, itinerarioSearchRepository);
        this.restItinerarioMockMvc = MockMvcBuilders.standaloneSetup(itinerarioResource)
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
    public static Itinerario createEntity(EntityManager em) {
        Itinerario itinerario = new Itinerario()
            .descricao(DEFAULT_DESCRICAO);
        return itinerario;
    }

    @Before
    public void initTest() {
        itinerarioSearchRepository.deleteAll();
        itinerario = createEntity(em);
    }

    @Test
    @Transactional
    public void createItinerario() throws Exception {
        int databaseSizeBeforeCreate = itinerarioRepository.findAll().size();

        // Create the Itinerario
        restItinerarioMockMvc.perform(post("/api/itinerarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itinerario)))
            .andExpect(status().isCreated());

        // Validate the Itinerario in the database
        List<Itinerario> itinerarioList = itinerarioRepository.findAll();
        assertThat(itinerarioList).hasSize(databaseSizeBeforeCreate + 1);
        Itinerario testItinerario = itinerarioList.get(itinerarioList.size() - 1);
        assertThat(testItinerario.getDescricao()).isEqualTo(DEFAULT_DESCRICAO);

        // Validate the Itinerario in Elasticsearch
        Itinerario itinerarioEs = itinerarioSearchRepository.findOne(testItinerario.getId());
        assertThat(itinerarioEs).isEqualToComparingFieldByField(testItinerario);
    }

    @Test
    @Transactional
    public void createItinerarioWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = itinerarioRepository.findAll().size();

        // Create the Itinerario with an existing ID
        itinerario.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restItinerarioMockMvc.perform(post("/api/itinerarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itinerario)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Itinerario> itinerarioList = itinerarioRepository.findAll();
        assertThat(itinerarioList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllItinerarios() throws Exception {
        // Initialize the database
        itinerarioRepository.saveAndFlush(itinerario);

        // Get all the itinerarioList
        restItinerarioMockMvc.perform(get("/api/itinerarios?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itinerario.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void getItinerario() throws Exception {
        // Initialize the database
        itinerarioRepository.saveAndFlush(itinerario);

        // Get the itinerario
        restItinerarioMockMvc.perform(get("/api/itinerarios/{id}", itinerario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(itinerario.getId().intValue()))
            .andExpect(jsonPath("$.descricao").value(DEFAULT_DESCRICAO.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingItinerario() throws Exception {
        // Get the itinerario
        restItinerarioMockMvc.perform(get("/api/itinerarios/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateItinerario() throws Exception {
        // Initialize the database
        itinerarioRepository.saveAndFlush(itinerario);
        itinerarioSearchRepository.save(itinerario);
        int databaseSizeBeforeUpdate = itinerarioRepository.findAll().size();

        // Update the itinerario
        Itinerario updatedItinerario = itinerarioRepository.findOne(itinerario.getId());
        updatedItinerario
            .descricao(UPDATED_DESCRICAO);

        restItinerarioMockMvc.perform(put("/api/itinerarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedItinerario)))
            .andExpect(status().isOk());

        // Validate the Itinerario in the database
        List<Itinerario> itinerarioList = itinerarioRepository.findAll();
        assertThat(itinerarioList).hasSize(databaseSizeBeforeUpdate);
        Itinerario testItinerario = itinerarioList.get(itinerarioList.size() - 1);
        assertThat(testItinerario.getDescricao()).isEqualTo(UPDATED_DESCRICAO);

        // Validate the Itinerario in Elasticsearch
        Itinerario itinerarioEs = itinerarioSearchRepository.findOne(testItinerario.getId());
        assertThat(itinerarioEs).isEqualToComparingFieldByField(testItinerario);
    }

    @Test
    @Transactional
    public void updateNonExistingItinerario() throws Exception {
        int databaseSizeBeforeUpdate = itinerarioRepository.findAll().size();

        // Create the Itinerario

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restItinerarioMockMvc.perform(put("/api/itinerarios")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(itinerario)))
            .andExpect(status().isCreated());

        // Validate the Itinerario in the database
        List<Itinerario> itinerarioList = itinerarioRepository.findAll();
        assertThat(itinerarioList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteItinerario() throws Exception {
        // Initialize the database
        itinerarioRepository.saveAndFlush(itinerario);
        itinerarioSearchRepository.save(itinerario);
        int databaseSizeBeforeDelete = itinerarioRepository.findAll().size();

        // Get the itinerario
        restItinerarioMockMvc.perform(delete("/api/itinerarios/{id}", itinerario.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean itinerarioExistsInEs = itinerarioSearchRepository.exists(itinerario.getId());
        assertThat(itinerarioExistsInEs).isFalse();

        // Validate the database is empty
        List<Itinerario> itinerarioList = itinerarioRepository.findAll();
        assertThat(itinerarioList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchItinerario() throws Exception {
        // Initialize the database
        itinerarioRepository.saveAndFlush(itinerario);
        itinerarioSearchRepository.save(itinerario);

        // Search the itinerario
        restItinerarioMockMvc.perform(get("/api/_search/itinerarios?query=id:" + itinerario.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(itinerario.getId().intValue())))
            .andExpect(jsonPath("$.[*].descricao").value(hasItem(DEFAULT_DESCRICAO.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Itinerario.class);
    }
}
