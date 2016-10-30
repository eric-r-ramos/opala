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
 * A Cliente.
 */
@Entity
@Table(name = "cliente")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "cliente")
public class Cliente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @OneToMany(mappedBy = "cliente")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Solicitante> listaSolicitantes = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Cliente nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Solicitante> getListaSolicitantes() {
        return listaSolicitantes;
    }

    public Cliente listaSolicitantes(Set<Solicitante> solicitantes) {
        this.listaSolicitantes = solicitantes;
        return this;
    }

    public Cliente addListaSolicitante(Solicitante solicitante) {
        listaSolicitantes.add(solicitante);
        solicitante.setCliente(this);
        return this;
    }

    public Cliente removeListaSolicitante(Solicitante solicitante) {
        listaSolicitantes.remove(solicitante);
        solicitante.setCliente(null);
        return this;
    }

    public void setListaSolicitantes(Set<Solicitante> solicitantes) {
        this.listaSolicitantes = solicitantes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Cliente cliente = (Cliente) o;
        if(cliente.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Cliente{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            '}';
    }
}
