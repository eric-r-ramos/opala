package com.opala.service.mapper;

import com.opala.domain.*;
import com.opala.service.dto.VeiculoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Veiculo and its DTO VeiculoDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface VeiculoMapper {

    VeiculoDTO veiculoToVeiculoDTO(Veiculo veiculo);

    List<VeiculoDTO> veiculosToVeiculoDTOs(List<Veiculo> veiculos);

    Veiculo veiculoDTOToVeiculo(VeiculoDTO veiculoDTO);

    List<Veiculo> veiculoDTOsToVeiculos(List<VeiculoDTO> veiculoDTOs);
}
