package com.opala.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Motorista entity.
 */
public class MotoristaDTO implements Serializable {

    private Long id;

    private String nome;

    private String habilitacao;

    private LocalDate vencimentoHabilitacao;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getHabilitacao() {
        return habilitacao;
    }

    public void setHabilitacao(String habilitacao) {
        this.habilitacao = habilitacao;
    }
    public LocalDate getVencimentoHabilitacao() {
        return vencimentoHabilitacao;
    }

    public void setVencimentoHabilitacao(LocalDate vencimentoHabilitacao) {
        this.vencimentoHabilitacao = vencimentoHabilitacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MotoristaDTO motoristaDTO = (MotoristaDTO) o;

        if ( ! Objects.equals(id, motoristaDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "MotoristaDTO{" +
            "id=" + id +
            ", nome='" + nome + "'" +
            ", habilitacao='" + habilitacao + "'" +
            ", vencimentoHabilitacao='" + vencimentoHabilitacao + "'" +
            '}';
    }
}
