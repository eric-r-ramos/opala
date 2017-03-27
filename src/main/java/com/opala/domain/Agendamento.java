package com.opala.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.opala.domain.enumeration.Status;

import com.opala.domain.enumeration.Sentido;

import com.opala.domain.enumeration.Pagamento;

import com.opala.domain.enumeration.Categoria;

/**
 * A Agendamento.
 */
@Entity
@Table(name = "agendamento")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "agendamento")
public class Agendamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data")
    private ZonedDateTime data;

    @Column(name = "numero_requisicao")
    private String numeroRequisicao;

    @Column(name = "centro_custo")
    private String centroCusto;

    @Column(name = "referencia")
    private String referencia;

    @Column(name = "valor")
    private Long valor;

    @Column(name = "observacao")
    private String observacao;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Enumerated(EnumType.STRING)
    @Column(name = "sentido")
    private Sentido sentido;

    @Enumerated(EnumType.STRING)
    @Column(name = "pagamento")
    private Pagamento pagamento;

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private Categoria categoria;

    @ManyToOne
    private Cliente cliente;

    @ManyToOne
    private Motorista motorista;

    @ManyToOne
    private Endereco origem;

    @ManyToOne
    private Solicitante solicitante;

    @ManyToOne
    private Itinerario itinerario;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "agendamento_lista_passageiros",
               joinColumns = @JoinColumn(name="agendamentos_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="lista_passageiros_id", referencedColumnName="id"))
    private Set<Passageiro> listaPassageiros = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JoinTable(name = "agendamento_lista_destinos",
               joinColumns = @JoinColumn(name="agendamentos_id", referencedColumnName="id"),
               inverseJoinColumns = @JoinColumn(name="lista_destinos_id", referencedColumnName="id"))
    private Set<Endereco> listaDestinos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ZonedDateTime getData() {
        return data;
    }

    public Agendamento data(ZonedDateTime data) {
        this.data = data;
        return this;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }

    public String getNumeroRequisicao() {
        return numeroRequisicao;
    }

    public Agendamento numeroRequisicao(String numeroRequisicao) {
        this.numeroRequisicao = numeroRequisicao;
        return this;
    }

    public void setNumeroRequisicao(String numeroRequisicao) {
        this.numeroRequisicao = numeroRequisicao;
    }

    public String getCentroCusto() {
        return centroCusto;
    }

    public Agendamento centroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
        return this;
    }

    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }

    public String getReferencia() {
        return referencia;
    }

    public Agendamento referencia(String referencia) {
        this.referencia = referencia;
        return this;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Long getValor() {
        return valor;
    }

    public Agendamento valor(Long valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }

    public String getObservacao() {
        return observacao;
    }

    public Agendamento observacao(String observacao) {
        this.observacao = observacao;
        return this;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }

    public Status getStatus() {
        return status;
    }

    public Agendamento status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Sentido getSentido() {
        return sentido;
    }

    public Agendamento sentido(Sentido sentido) {
        this.sentido = sentido;
        return this;
    }

    public void setSentido(Sentido sentido) {
        this.sentido = sentido;
    }

    public Pagamento getPagamento() {
        return pagamento;
    }

    public Agendamento pagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
        return this;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Agendamento categoria(Categoria categoria) {
        this.categoria = categoria;
        return this;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Agendamento cliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Motorista getMotorista() {
        return motorista;
    }

    public Agendamento motorista(Motorista motorista) {
        this.motorista = motorista;
        return this;
    }

    public void setMotorista(Motorista motorista) {
        this.motorista = motorista;
    }

    public Endereco getOrigem() {
        return origem;
    }

    public Agendamento origem(Endereco endereco) {
        this.origem = endereco;
        return this;
    }

    public void setOrigem(Endereco endereco) {
        this.origem = endereco;
    }

    public Solicitante getSolicitante() {
        return solicitante;
    }

    public Agendamento solicitante(Solicitante solicitante) {
        this.solicitante = solicitante;
        return this;
    }

    public void setSolicitante(Solicitante solicitante) {
        this.solicitante = solicitante;
    }

    public Itinerario getItinerario() {
        return itinerario;
    }

    public Agendamento itinerario(Itinerario itinerario) {
        this.itinerario = itinerario;
        return this;
    }

    public void setItinerario(Itinerario itinerario) {
        this.itinerario = itinerario;
    }

    public Set<Passageiro> getListaPassageiros() {
        return listaPassageiros;
    }

    public Agendamento listaPassageiros(Set<Passageiro> passageiros) {
        this.listaPassageiros = passageiros;
        return this;
    }

    public Agendamento addListaPassageiros(Passageiro passageiro) {
        this.listaPassageiros.add(passageiro);
        return this;
    }

    public Agendamento removeListaPassageiros(Passageiro passageiro) {
        this.listaPassageiros.remove(passageiro);
        return this;
    }

    public void setListaPassageiros(Set<Passageiro> passageiros) {
        this.listaPassageiros = passageiros;
    }

    public Set<Endereco> getListaDestinos() {
        return listaDestinos;
    }

    public Agendamento listaDestinos(Set<Endereco> enderecos) {
        this.listaDestinos = enderecos;
        return this;
    }

    public Agendamento addListaDestinos(Endereco endereco) {
        this.listaDestinos.add(endereco);
        return this;
    }

    public Agendamento removeListaDestinos(Endereco endereco) {
        this.listaDestinos.remove(endereco);
        return this;
    }

    public void setListaDestinos(Set<Endereco> enderecos) {
        this.listaDestinos = enderecos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Agendamento agendamento = (Agendamento) o;
        if (agendamento.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, agendamento.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Agendamento{" +
            "id=" + id +
            ", data='" + data + "'" +
            ", numeroRequisicao='" + numeroRequisicao + "'" +
            ", centroCusto='" + centroCusto + "'" +
            ", referencia='" + referencia + "'" +
            ", valor='" + valor + "'" +
            ", observacao='" + observacao + "'" +
            ", status='" + status + "'" +
            ", sentido='" + sentido + "'" +
            ", pagamento='" + pagamento + "'" +
            ", categoria='" + categoria + "'" +
            '}';
    }
}
