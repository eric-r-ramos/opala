package com.opala.service.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Endereco entity.
 */
public class EnderecoDTO implements Serializable {

    private Long id;

    private String linha1;

    private String linha2;

    private String cidade;

    private String estado;

    private String cep;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getLinha1() {
        return linha1;
    }

    public void setLinha1(String linha1) {
        this.linha1 = linha1;
    }
    public String getLinha2() {
        return linha2;
    }

    public void setLinha2(String linha2) {
        this.linha2 = linha2;
    }
    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        EnderecoDTO enderecoDTO = (EnderecoDTO) o;

        if ( ! Objects.equals(id, enderecoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "EnderecoDTO{" +
            "id=" + id +
            ", linha1='" + linha1 + "'" +
            ", linha2='" + linha2 + "'" +
            ", cidade='" + cidade + "'" +
            ", estado='" + estado + "'" +
            ", cep='" + cep + "'" +
            '}';
    }
}
