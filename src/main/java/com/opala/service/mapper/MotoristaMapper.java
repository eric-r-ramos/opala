package com.opala.service.mapper;

import com.opala.domain.*;
import com.opala.service.dto.MotoristaDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Motorista and its DTO MotoristaDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface MotoristaMapper {

    MotoristaDTO motoristaToMotoristaDTO(Motorista motorista);

    List<MotoristaDTO> motoristasToMotoristaDTOs(List<Motorista> motoristas);

    Motorista motoristaDTOToMotorista(MotoristaDTO motoristaDTO);

    List<Motorista> motoristaDTOsToMotoristas(List<MotoristaDTO> motoristaDTOs);
}
