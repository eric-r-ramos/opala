package com.opala.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Solicitante.
 */
@Entity
@Table(name = "solicitante")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "solicitante")
public class Solicitante implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @ManyToOne
    private Cliente cliente;

    @OneToMany(mappedBy = "solicitante")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Agendamento> listaAgendamentos = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Solicitante nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public Solicitante cliente(Cliente cliente) {
        this.cliente = cliente;
        return this;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Set<Agendamento> getListaAgendamentos() {
        return listaAgendamentos;
    }

    public Solicitante listaAgendamentos(Set<Agendamento> agendamentos) {
        this.listaAgendamentos = agendamentos;
        return this;
    }

    public Solicitante addListaAgendamentos(Agendamento agendamento) {
        listaAgendamentos.add(agendamento);
        agendamento.setSolicitante(this);
        return this;
    }

    public Solicitante removeListaAgendamentos(Agendamento agendamento) {
        listaAgendamentos.remove(agendamento);
        agendamento.setSolicitante(null);
        return this;
    }

    public void setListaAgendamentos(Set<Agendamento> agendamentos) {
        this.listaAgendamentos = agendamentos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Solicitante solicitante = (Solicitante) o;
        if(solicitante.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, solicitante.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Solicitante{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            '}';
    }
}
