package com.opala.service.mapper;

import com.opala.domain.*;
import com.opala.service.dto.AgendamentoDTO;

import org.mapstruct.*;
import java.util.List;

/**
 * Mapper for the entity Agendamento and its DTO AgendamentoDTO.
 */
@Mapper(componentModel = "spring", uses = {PassageiroMapper.class, EnderecoMapper.class, })
public interface AgendamentoMapper {

    @Mapping(source = "cliente.id", target = "clienteId")
    @Mapping(source = "motorista.id", target = "motoristaId")
    @Mapping(source = "origem.id", target = "origemId")
    @Mapping(source = "solicitante.id", target = "solicitanteId")
    @Mapping(source = "veiculo.id", target = "veiculoId")
    @Mapping(source = "itinerario.id", target = "itinerarioId")
    AgendamentoDTO agendamentoToAgendamentoDTO(Agendamento agendamento);

    List<AgendamentoDTO> agendamentosToAgendamentoDTOs(List<Agendamento> agendamentos);

    @Mapping(source = "clienteId", target = "cliente")
    @Mapping(source = "motoristaId", target = "motorista")
    @Mapping(source = "origemId", target = "origem")
    @Mapping(source = "solicitanteId", target = "solicitante")
    @Mapping(source = "veiculoId", target = "veiculo")
    @Mapping(source = "itinerarioId", target = "itinerario")
    Agendamento agendamentoDTOToAgendamento(AgendamentoDTO agendamentoDTO);

    List<Agendamento> agendamentoDTOsToAgendamentos(List<AgendamentoDTO> agendamentoDTOs);

    default Cliente clienteFromId(Long id) {
        if (id == null) {
            return null;
        }
        Cliente cliente = new Cliente();
        cliente.setId(id);
        return cliente;
    }

    default Motorista motoristaFromId(Long id) {
        if (id == null) {
            return null;
        }
        Motorista motorista = new Motorista();
        motorista.setId(id);
        return motorista;
    }

    default Endereco enderecoFromId(Long id) {
        if (id == null) {
            return null;
        }
        Endereco endereco = new Endereco();
        endereco.setId(id);
        return endereco;
    }

    default Solicitante solicitanteFromId(Long id) {
        if (id == null) {
            return null;
        }
        Solicitante solicitante = new Solicitante();
        solicitante.setId(id);
        return solicitante;
    }

    default Veiculo veiculoFromId(Long id) {
        if (id == null) {
            return null;
        }
        Veiculo veiculo = new Veiculo();
        veiculo.setId(id);
        return veiculo;
    }

    default Itinerario itinerarioFromId(Long id) {
        if (id == null) {
            return null;
        }
        Itinerario itinerario = new Itinerario();
        itinerario.setId(id);
        return itinerario;
    }

    default Passageiro passageiroFromId(Long id) {
        if (id == null) {
            return null;
        }
        Passageiro passageiro = new Passageiro();
        passageiro.setId(id);
        return passageiro;
    }
}
