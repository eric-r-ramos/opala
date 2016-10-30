package com.opala.service.mapper;

import com.opala.domain.*;
import com.opala.service.dto.ClienteDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Cliente and its DTO ClienteDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClienteMapper {

    ClienteDTO clienteToClienteDTO(Cliente cliente);

    List<ClienteDTO> clientesToClienteDTOs(List<Cliente> clientes);

    @Mapping(target = "listaSolicitantes", ignore = true)
    Cliente clienteDTOToCliente(ClienteDTO clienteDTO);

    List<Cliente> clienteDTOsToClientes(List<ClienteDTO> clienteDTOs);
}
