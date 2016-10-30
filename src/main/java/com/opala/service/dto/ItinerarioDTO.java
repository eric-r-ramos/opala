package com.opala.service.dto;

import java.io.Serializable;
import java.util.Objects;


/**
 * A DTO for the Itinerario entity.
 */
public class ItinerarioDTO implements Serializable {

    private Long id;

    private String descricao;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ItinerarioDTO itinerarioDTO = (ItinerarioDTO) o;

        if ( ! Objects.equals(id, itinerarioDTO.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "ItinerarioDTO{" +
            "id=" + id +
            ", descricao='" + descricao + "'" +
            '}';
    }
}
