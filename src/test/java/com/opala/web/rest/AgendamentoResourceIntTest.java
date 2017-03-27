package com.opala.web.rest;

import com.opala.OpalaApp;

import com.opala.domain.Agendamento;
import com.opala.repository.AgendamentoRepository;
import com.opala.repository.search.AgendamentoSearchRepository;
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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;

import static com.opala.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.opala.domain.enumeration.Status;
import com.opala.domain.enumeration.Sentido;
import com.opala.domain.enumeration.Pagamento;
import com.opala.domain.enumeration.Categoria;
/**
 * Test class for the AgendamentoResource REST controller.
 *
 * @see AgendamentoResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = OpalaApp.class)
public class AgendamentoResourceIntTest {

    private static final ZonedDateTime DEFAULT_DATA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_DATA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_NUMERO_REQUISICAO = "AAAAAAAAAA";
    private static final String UPDATED_NUMERO_REQUISICAO = "BBBBBBBBBB";

    private static final String DEFAULT_CENTRO_CUSTO = "AAAAAAAAAA";
    private static final String UPDATED_CENTRO_CUSTO = "BBBBBBBBBB";

    private static final String DEFAULT_REFERENCIA = "AAAAAAAAAA";
    private static final String UPDATED_REFERENCIA = "BBBBBBBBBB";

    private static final Long DEFAULT_VALOR = 1L;
    private static final Long UPDATED_VALOR = 2L;

    private static final String DEFAULT_OBSERVACAO = "AAAAAAAAAA";
    private static final String UPDATED_OBSERVACAO = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.AGENDADO;
    private static final Status UPDATED_STATUS = Status.ENVIADO;

    private static final Sentido DEFAULT_SENTIDO = Sentido.LEVAR;
    private static final Sentido UPDATED_SENTIDO = Sentido.BUSCAR;

    private static final Pagamento DEFAULT_PAGAMENTO = Pagamento.FATURADO;
    private static final Pagamento UPDATED_PAGAMENTO = Pagamento.DIRETO;

    private static final Categoria DEFAULT_CATEGORIA = Categoria.EXECUTIVO;
    private static final Categoria UPDATED_CATEGORIA = Categoria.LUXO;

    @Autowired
    private AgendamentoRepository agendamentoRepository;

    @Autowired
    private AgendamentoSearchRepository agendamentoSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restAgendamentoMockMvc;

    private Agendamento agendamento;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AgendamentoResource agendamentoResource = new AgendamentoResource(agendamentoRepository, agendamentoSearchRepository);
        this.restAgendamentoMockMvc = MockMvcBuilders.standaloneSetup(agendamentoResource)
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
    public static Agendamento createEntity(EntityManager em) {
        Agendamento agendamento = new Agendamento()
            .data(DEFAULT_DATA)
            .numeroRequisicao(DEFAULT_NUMERO_REQUISICAO)
            .centroCusto(DEFAULT_CENTRO_CUSTO)
            .referencia(DEFAULT_REFERENCIA)
            .valor(DEFAULT_VALOR)
            .observacao(DEFAULT_OBSERVACAO)
            .status(DEFAULT_STATUS)
            .sentido(DEFAULT_SENTIDO)
            .pagamento(DEFAULT_PAGAMENTO)
            .categoria(DEFAULT_CATEGORIA);
        return agendamento;
    }

    @Before
    public void initTest() {
        agendamentoSearchRepository.deleteAll();
        agendamento = createEntity(em);
    }

    @Test
    @Transactional
    public void createAgendamento() throws Exception {
        int databaseSizeBeforeCreate = agendamentoRepository.findAll().size();

        // Create the Agendamento
        restAgendamentoMockMvc.perform(post("/api/agendamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agendamento)))
            .andExpect(status().isCreated());

        // Validate the Agendamento in the database
        List<Agendamento> agendamentoList = agendamentoRepository.findAll();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeCreate + 1);
        Agendamento testAgendamento = agendamentoList.get(agendamentoList.size() - 1);
        assertThat(testAgendamento.getData()).isEqualTo(DEFAULT_DATA);
        assertThat(testAgendamento.getNumeroRequisicao()).isEqualTo(DEFAULT_NUMERO_REQUISICAO);
        assertThat(testAgendamento.getCentroCusto()).isEqualTo(DEFAULT_CENTRO_CUSTO);
        assertThat(testAgendamento.getReferencia()).isEqualTo(DEFAULT_REFERENCIA);
        assertThat(testAgendamento.getValor()).isEqualTo(DEFAULT_VALOR);
        assertThat(testAgendamento.getObservacao()).isEqualTo(DEFAULT_OBSERVACAO);
        assertThat(testAgendamento.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testAgendamento.getSentido()).isEqualTo(DEFAULT_SENTIDO);
        assertThat(testAgendamento.getPagamento()).isEqualTo(DEFAULT_PAGAMENTO);
        assertThat(testAgendamento.getCategoria()).isEqualTo(DEFAULT_CATEGORIA);

        // Validate the Agendamento in Elasticsearch
        Agendamento agendamentoEs = agendamentoSearchRepository.findOne(testAgendamento.getId());
        assertThat(agendamentoEs).isEqualToComparingFieldByField(testAgendamento);
    }

    @Test
    @Transactional
    public void createAgendamentoWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = agendamentoRepository.findAll().size();

        // Create the Agendamento with an existing ID
        agendamento.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgendamentoMockMvc.perform(post("/api/agendamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agendamento)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<Agendamento> agendamentoList = agendamentoRepository.findAll();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void getAllAgendamentos() throws Exception {
        // Initialize the database
        agendamentoRepository.saveAndFlush(agendamento);

        // Get all the agendamentoList
        restAgendamentoMockMvc.perform(get("/api/agendamentos?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agendamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].numeroRequisicao").value(hasItem(DEFAULT_NUMERO_REQUISICAO.toString())))
            .andExpect(jsonPath("$.[*].centroCusto").value(hasItem(DEFAULT_CENTRO_CUSTO.toString())))
            .andExpect(jsonPath("$.[*].referencia").value(hasItem(DEFAULT_REFERENCIA.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.intValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].sentido").value(hasItem(DEFAULT_SENTIDO.toString())))
            .andExpect(jsonPath("$.[*].pagamento").value(hasItem(DEFAULT_PAGAMENTO.toString())))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA.toString())));
    }

    @Test
    @Transactional
    public void getAgendamento() throws Exception {
        // Initialize the database
        agendamentoRepository.saveAndFlush(agendamento);

        // Get the agendamento
        restAgendamentoMockMvc.perform(get("/api/agendamentos/{id}", agendamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(agendamento.getId().intValue()))
            .andExpect(jsonPath("$.data").value(sameInstant(DEFAULT_DATA)))
            .andExpect(jsonPath("$.numeroRequisicao").value(DEFAULT_NUMERO_REQUISICAO.toString()))
            .andExpect(jsonPath("$.centroCusto").value(DEFAULT_CENTRO_CUSTO.toString()))
            .andExpect(jsonPath("$.referencia").value(DEFAULT_REFERENCIA.toString()))
            .andExpect(jsonPath("$.valor").value(DEFAULT_VALOR.intValue()))
            .andExpect(jsonPath("$.observacao").value(DEFAULT_OBSERVACAO.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.sentido").value(DEFAULT_SENTIDO.toString()))
            .andExpect(jsonPath("$.pagamento").value(DEFAULT_PAGAMENTO.toString()))
            .andExpect(jsonPath("$.categoria").value(DEFAULT_CATEGORIA.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingAgendamento() throws Exception {
        // Get the agendamento
        restAgendamentoMockMvc.perform(get("/api/agendamentos/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAgendamento() throws Exception {
        // Initialize the database
        agendamentoRepository.saveAndFlush(agendamento);
        agendamentoSearchRepository.save(agendamento);
        int databaseSizeBeforeUpdate = agendamentoRepository.findAll().size();

        // Update the agendamento
        Agendamento updatedAgendamento = agendamentoRepository.findOne(agendamento.getId());
        updatedAgendamento
            .data(UPDATED_DATA)
            .numeroRequisicao(UPDATED_NUMERO_REQUISICAO)
            .centroCusto(UPDATED_CENTRO_CUSTO)
            .referencia(UPDATED_REFERENCIA)
            .valor(UPDATED_VALOR)
            .observacao(UPDATED_OBSERVACAO)
            .status(UPDATED_STATUS)
            .sentido(UPDATED_SENTIDO)
            .pagamento(UPDATED_PAGAMENTO)
            .categoria(UPDATED_CATEGORIA);

        restAgendamentoMockMvc.perform(put("/api/agendamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedAgendamento)))
            .andExpect(status().isOk());

        // Validate the Agendamento in the database
        List<Agendamento> agendamentoList = agendamentoRepository.findAll();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeUpdate);
        Agendamento testAgendamento = agendamentoList.get(agendamentoList.size() - 1);
        assertThat(testAgendamento.getData()).isEqualTo(UPDATED_DATA);
        assertThat(testAgendamento.getNumeroRequisicao()).isEqualTo(UPDATED_NUMERO_REQUISICAO);
        assertThat(testAgendamento.getCentroCusto()).isEqualTo(UPDATED_CENTRO_CUSTO);
        assertThat(testAgendamento.getReferencia()).isEqualTo(UPDATED_REFERENCIA);
        assertThat(testAgendamento.getValor()).isEqualTo(UPDATED_VALOR);
        assertThat(testAgendamento.getObservacao()).isEqualTo(UPDATED_OBSERVACAO);
        assertThat(testAgendamento.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testAgendamento.getSentido()).isEqualTo(UPDATED_SENTIDO);
        assertThat(testAgendamento.getPagamento()).isEqualTo(UPDATED_PAGAMENTO);
        assertThat(testAgendamento.getCategoria()).isEqualTo(UPDATED_CATEGORIA);

        // Validate the Agendamento in Elasticsearch
        Agendamento agendamentoEs = agendamentoSearchRepository.findOne(testAgendamento.getId());
        assertThat(agendamentoEs).isEqualToComparingFieldByField(testAgendamento);
    }

    @Test
    @Transactional
    public void updateNonExistingAgendamento() throws Exception {
        int databaseSizeBeforeUpdate = agendamentoRepository.findAll().size();

        // Create the Agendamento

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restAgendamentoMockMvc.perform(put("/api/agendamentos")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(agendamento)))
            .andExpect(status().isCreated());

        // Validate the Agendamento in the database
        List<Agendamento> agendamentoList = agendamentoRepository.findAll();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteAgendamento() throws Exception {
        // Initialize the database
        agendamentoRepository.saveAndFlush(agendamento);
        agendamentoSearchRepository.save(agendamento);
        int databaseSizeBeforeDelete = agendamentoRepository.findAll().size();

        // Get the agendamento
        restAgendamentoMockMvc.perform(delete("/api/agendamentos/{id}", agendamento.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean agendamentoExistsInEs = agendamentoSearchRepository.exists(agendamento.getId());
        assertThat(agendamentoExistsInEs).isFalse();

        // Validate the database is empty
        List<Agendamento> agendamentoList = agendamentoRepository.findAll();
        assertThat(agendamentoList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchAgendamento() throws Exception {
        // Initialize the database
        agendamentoRepository.saveAndFlush(agendamento);
        agendamentoSearchRepository.save(agendamento);

        // Search the agendamento
        restAgendamentoMockMvc.perform(get("/api/_search/agendamentos?query=id:" + agendamento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agendamento.getId().intValue())))
            .andExpect(jsonPath("$.[*].data").value(hasItem(sameInstant(DEFAULT_DATA))))
            .andExpect(jsonPath("$.[*].numeroRequisicao").value(hasItem(DEFAULT_NUMERO_REQUISICAO.toString())))
            .andExpect(jsonPath("$.[*].centroCusto").value(hasItem(DEFAULT_CENTRO_CUSTO.toString())))
            .andExpect(jsonPath("$.[*].referencia").value(hasItem(DEFAULT_REFERENCIA.toString())))
            .andExpect(jsonPath("$.[*].valor").value(hasItem(DEFAULT_VALOR.intValue())))
            .andExpect(jsonPath("$.[*].observacao").value(hasItem(DEFAULT_OBSERVACAO.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].sentido").value(hasItem(DEFAULT_SENTIDO.toString())))
            .andExpect(jsonPath("$.[*].pagamento").value(hasItem(DEFAULT_PAGAMENTO.toString())))
            .andExpect(jsonPath("$.[*].categoria").value(hasItem(DEFAULT_CATEGORIA.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Agendamento.class);
    }
}
