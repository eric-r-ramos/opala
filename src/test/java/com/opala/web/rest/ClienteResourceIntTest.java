package com.opala.web.rest;

import com.opala.OpalaApp;

import com.opala.domain.Cliente;
import com.opala.repository.ClienteRepository;
import com.opala.service.ClienteService;
import com.opala.repository.search.ClienteSearchRepository;
import com.opala.service.dto.ClienteDTO;
import com.opala.service.mapper.ClienteMapper;

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
 * Test class for the ClienteResource REST controller.
 *
 * @see ClienteResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpalaApp.class)
public class ClienteResourceIntTest {

    private static final String DEFAULT_NOME = "AAAAA";
    private static final String UPDATED_NOME = "BBBBB";

    @Inject
    private ClienteRepository clienteRepository;

    @Inject
    private ClienteMapper clienteMapper;

    @Inject
    private ClienteService clienteService;

    @Inject
    private ClienteSearchRepository clienteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Inject
    private EntityManager em;

    private MockMvc restClienteMockMvc;

    private Cliente cliente;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ClienteResource clienteResource = new ClienteResource();
        ReflectionTestUtils.setField(clienteResource, "clienteService", clienteService);
        this.restClienteMockMvc = MockMvcBuilders.standaloneSetup(clienteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cliente createEntity(EntityManager em) {
        Cliente cliente = new Cliente()
                .nome(DEFAULT_NOME);
        return cliente;
    }

    @Before
    public void initTest() {
        clienteSearchRepository.deleteAll();
        cliente = createEntity(em);
    }

    @Test
    @Transactional
    public void createCliente() throws Exception {
        int databaseSizeBeforeCreate = clienteRepository.findAll().size();

        // Create the Cliente
        ClienteDTO clienteDTO = clienteMapper.clienteToClienteDTO(cliente);

        restClienteMockMvc.perform(post("/api/clientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
                .andExpect(status().isCreated());

        // Validate the Cliente in the database
        List<Cliente> clientes = clienteRepository.findAll();
        assertThat(clientes).hasSize(databaseSizeBeforeCreate + 1);
        Cliente testCliente = clientes.get(clientes.size() - 1);
        assertThat(testCliente.getNome()).isEqualTo(DEFAULT_NOME);

        // Validate the Cliente in ElasticSearch
        Cliente clienteEs = clienteSearchRepository.findOne(testCliente.getId());
        assertThat(clienteEs).isEqualToComparingFieldByField(testCliente);
    }

    @Test
    @Transactional
    public void getAllClientes() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get all the clientes
        restClienteMockMvc.perform(get("/api/clientes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
                .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
                .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }

    @Test
    @Transactional
    public void getCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);

        // Get the cliente
        restClienteMockMvc.perform(get("/api/clientes/{id}", cliente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(cliente.getId().intValue()))
            .andExpect(jsonPath("$.nome").value(DEFAULT_NOME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingCliente() throws Exception {
        // Get the cliente
        restClienteMockMvc.perform(get("/api/clientes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);
        clienteSearchRepository.save(cliente);
        int databaseSizeBeforeUpdate = clienteRepository.findAll().size();

        // Update the cliente
        Cliente updatedCliente = clienteRepository.findOne(cliente.getId());
        updatedCliente
                .nome(UPDATED_NOME);
        ClienteDTO clienteDTO = clienteMapper.clienteToClienteDTO(updatedCliente);

        restClienteMockMvc.perform(put("/api/clientes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(clienteDTO)))
                .andExpect(status().isOk());

        // Validate the Cliente in the database
        List<Cliente> clientes = clienteRepository.findAll();
        assertThat(clientes).hasSize(databaseSizeBeforeUpdate);
        Cliente testCliente = clientes.get(clientes.size() - 1);
        assertThat(testCliente.getNome()).isEqualTo(UPDATED_NOME);

        // Validate the Cliente in ElasticSearch
        Cliente clienteEs = clienteSearchRepository.findOne(testCliente.getId());
        assertThat(clienteEs).isEqualToComparingFieldByField(testCliente);
    }

    @Test
    @Transactional
    public void deleteCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);
        clienteSearchRepository.save(cliente);
        int databaseSizeBeforeDelete = clienteRepository.findAll().size();

        // Get the cliente
        restClienteMockMvc.perform(delete("/api/clientes/{id}", cliente.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate ElasticSearch is empty
        boolean clienteExistsInEs = clienteSearchRepository.exists(cliente.getId());
        assertThat(clienteExistsInEs).isFalse();

        // Validate the database is empty
        List<Cliente> clientes = clienteRepository.findAll();
        assertThat(clientes).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchCliente() throws Exception {
        // Initialize the database
        clienteRepository.saveAndFlush(cliente);
        clienteSearchRepository.save(cliente);

        // Search the cliente
        restClienteMockMvc.perform(get("/api/_search/clientes?query=id:" + cliente.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cliente.getId().intValue())))
            .andExpect(jsonPath("$.[*].nome").value(hasItem(DEFAULT_NOME.toString())));
    }
}
