package com.opala.service.mapper;

import com.opala.domain.*;
import com.opala.service.dto.PassageiroDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Passageiro and its DTO PassageiroDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PassageiroMapper {

    PassageiroDTO passageiroToPassageiroDTO(Passageiro passageiro);

    List<PassageiroDTO> passageirosToPassageiroDTOs(List<Passageiro> passageiros);

    Passageiro passageiroDTOToPassageiro(PassageiroDTO passageiroDTO);

    List<Passageiro> passageiroDTOsToPassageiros(List<PassageiroDTO> passageiroDTOs);
}
