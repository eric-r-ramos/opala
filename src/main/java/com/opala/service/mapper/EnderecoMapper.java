package com.opala.service.mapper;

import com.opala.domain.*;
import com.opala.service.dto.EnderecoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Endereco and its DTO EnderecoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface EnderecoMapper {

    EnderecoDTO enderecoToEnderecoDTO(Endereco endereco);

    List<EnderecoDTO> enderecosToEnderecoDTOs(List<Endereco> enderecos);

    Endereco enderecoDTOToEndereco(EnderecoDTO enderecoDTO);

    List<Endereco> enderecoDTOsToEnderecos(List<EnderecoDTO> enderecoDTOs);
}
