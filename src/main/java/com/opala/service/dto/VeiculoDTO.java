package com.opala.service.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Veiculo entity.
 */
public class VeiculoDTO implements Serializable {

    private Long id;

    private String marca;

    private String modelo;

    private String cor;

    private String placa;

    private String ano;

    private Boolean ativo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }
    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }
    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }
    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }
    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        VeiculoDTO veiculoDTO = (VeiculoDTO) o;

        if ( ! Objects.equals(id, veiculoDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "VeiculoDTO{" +
            "id=" + id +
            ", marca='" + marca + "'" +
            ", modelo='" + modelo + "'" +
            ", cor='" + cor + "'" +
            ", placa='" + placa + "'" +
            ", ano='" + ano + "'" +
            ", ativo='" + ativo + "'" +
            '}';
    }
}
