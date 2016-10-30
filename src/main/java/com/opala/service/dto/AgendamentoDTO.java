package com.opala.service.dto;

import java.time.ZonedDateTime;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

import com.opala.domain.enumeration.Status;
import com.opala.domain.enumeration.Sentido;
import com.opala.domain.enumeration.Pagamento;
import com.opala.domain.enumeration.Categoria;

/**
 * A DTO for the Agendamento entity.
 */
public class AgendamentoDTO implements Serializable {

    private Long id;

    private ZonedDateTime data;

    private String numeroRequisicao;

    private String centroCusto;

    private String referencia;

    private Long valor;

    private String observacao;

    private Status status;

    private Sentido sentido;

    private Pagamento pagamento;

    private Categoria categoria;


    private Long clienteId;
    
    private Long motoristaId;
    
    private Long origemId;
    
    private Long solicitanteId;
    
    private Long veiculoId;
    
    private Long itinerarioId;
    
    private Set<PassageiroDTO> listaPassageiros = new HashSet<>();

    private Set<EnderecoDTO> listaDestinos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public ZonedDateTime getData() {
        return data;
    }

    public void setData(ZonedDateTime data) {
        this.data = data;
    }
    public String getNumeroRequisicao() {
        return numeroRequisicao;
    }

    public void setNumeroRequisicao(String numeroRequisicao) {
        this.numeroRequisicao = numeroRequisicao;
    }
    public String getCentroCusto() {
        return centroCusto;
    }

    public void setCentroCusto(String centroCusto) {
        this.centroCusto = centroCusto;
    }
    public String getReferencia() {
        return referencia;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }
    public Long getValor() {
        return valor;
    }

    public void setValor(Long valor) {
        this.valor = valor;
    }
    public String getObservacao() {
        return observacao;
    }

    public void setObservacao(String observacao) {
        this.observacao = observacao;
    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    public Sentido getSentido() {
        return sentido;
    }

    public void setSentido(Sentido sentido) {
        this.sentido = sentido;
    }
    public Pagamento getPagamento() {
        return pagamento;
    }

    public void setPagamento(Pagamento pagamento) {
        this.pagamento = pagamento;
    }
    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public Long getMotoristaId() {
        return motoristaId;
    }

    public void setMotoristaId(Long motoristaId) {
        this.motoristaId = motoristaId;
    }

    public Long getOrigemId() {
        return origemId;
    }

    public void setOrigemId(Long enderecoId) {
        this.origemId = enderecoId;
    }

    public Long getSolicitanteId() {
        return solicitanteId;
    }

    public void setSolicitanteId(Long solicitanteId) {
        this.solicitanteId = solicitanteId;
    }

    public Long getVeiculoId() {
        return veiculoId;
    }

    public void setVeiculoId(Long veiculoId) {
        this.veiculoId = veiculoId;
    }

    public Long getItinerarioId() {
        return itinerarioId;
    }

    public void setItinerarioId(Long itinerarioId) {
        this.itinerarioId = itinerarioId;
    }

    public Set<PassageiroDTO> getListaPassageiros() {
        return listaPassageiros;
    }

    public void setListaPassageiros(Set<PassageiroDTO> passageiros) {
        this.listaPassageiros = passageiros;
    }

    public Set<EnderecoDTO> getListaDestinos() {
        return listaDestinos;
    }

    public void setListaDestinos(Set<EnderecoDTO> enderecos) {
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

        AgendamentoDTO agendamentoDTO = (AgendamentoDTO) o;

        if ( ! Objects.equals(id, agendamentoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AgendamentoDTO{" +
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
