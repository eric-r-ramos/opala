package com.opala.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A Motorista.
 */
@Entity
@Table(name = "motorista")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "motorista")
public class Motorista implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome")
    private String nome;

    @Column(name = "habilitacao")
    private String habilitacao;

    @Column(name = "vencimento_habilitacao")
    private LocalDate vencimentoHabilitacao;

    @OneToOne
    @JoinColumn(unique = true)
    private User login;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public Motorista nome(String nome) {
        this.nome = nome;
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getHabilitacao() {
        return habilitacao;
    }

    public Motorista habilitacao(String habilitacao) {
        this.habilitacao = habilitacao;
        return this;
    }

    public void setHabilitacao(String habilitacao) {
        this.habilitacao = habilitacao;
    }

    public LocalDate getVencimentoHabilitacao() {
        return vencimentoHabilitacao;
    }

    public Motorista vencimentoHabilitacao(LocalDate vencimentoHabilitacao) {
        this.vencimentoHabilitacao = vencimentoHabilitacao;
        return this;
    }

    public void setVencimentoHabilitacao(LocalDate vencimentoHabilitacao) {
        this.vencimentoHabilitacao = vencimentoHabilitacao;
    }

    public User getLogin() {
        return login;
    }

    public Motorista login(User user) {
        this.login = user;
        return this;
    }

    public void setLogin(User user) {
        this.login = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Motorista motorista = (Motorista) o;
        if (motorista.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, motorista.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Motorista{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            ", habilitacao='" + habilitacao + "'" +
            ", vencimentoHabilitacao='" + vencimentoHabilitacao + "'" +
            '}';
    }
}
