package com.opala.service.mapper;

import com.opala.domain.*;
import com.opala.service.dto.SolicitanteDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Solicitante and its DTO SolicitanteDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface SolicitanteMapper {

    @Mapping(source = "cliente.id", target = "clienteId")
    SolicitanteDTO solicitanteToSolicitanteDTO(Solicitante solicitante);

    List<SolicitanteDTO> solicitantesToSolicitanteDTOs(List<Solicitante> solicitantes);

    @Mapping(source = "clienteId", target = "cliente")
    @Mapping(target = "listaAgendamentos", ignore = true)
    Solicitante solicitanteDTOToSolicitante(SolicitanteDTO solicitanteDTO);

    List<Solicitante> solicitanteDTOsToSolicitantes(List<SolicitanteDTO> solicitanteDTOs);

    default Cliente clienteFromId(Long id) {
        if (id == null) {
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setId(id);
        return cliente;
    }
}
