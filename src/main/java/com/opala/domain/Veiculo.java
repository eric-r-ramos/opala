package com.opala.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Veiculo.
 */
@Entity
@Table(name = "veiculo")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "veiculo")
public class Veiculo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "marca")
    private String marca;

    @Column(name = "modelo")
    private String modelo;

    @Column(name = "cor")
    private String cor;

    @Column(name = "placa")
    private String placa;

    @Column(name = "ano")
    private String ano;

    @Column(name = "ativo")
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

    public Veiculo marca(String marca) {
        this.marca = marca;
        return this;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public Veiculo modelo(String modelo) {
        this.modelo = modelo;
        return this;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getCor() {
        return cor;
    }

    public Veiculo cor(String cor) {
        this.cor = cor;
        return this;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }

    public String getPlaca() {
        return placa;
    }

    public Veiculo placa(String placa) {
        this.placa = placa;
        return this;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getAno() {
        return ano;
    }

    public Veiculo ano(String ano) {
        this.ano = ano;
        return this;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public Boolean isAtivo() {
        return ativo;
    }

    public Veiculo ativo(Boolean ativo) {
        this.ativo = ativo;
        return this;
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
        Veiculo veiculo = (Veiculo) o;
        if(veiculo.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, veiculo.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Veiculo{" +
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
