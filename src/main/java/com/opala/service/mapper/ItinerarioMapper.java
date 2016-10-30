package com.opala.service.mapper;

import com.opala.domain.*;
import com.opala.service.dto.ItinerarioDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Itinerario and its DTO ItinerarioDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ItinerarioMapper {

    ItinerarioDTO itinerarioToItinerarioDTO(Itinerario itinerario);

    List<ItinerarioDTO> itinerariosToItinerarioDTOs(List<Itinerario> itinerarios);

    Itinerario itinerarioDTOToItinerario(ItinerarioDTO itinerarioDTO);

    List<Itinerario> itinerarioDTOsToItinerarios(List<ItinerarioDTO> itinerarioDTOs);
}
