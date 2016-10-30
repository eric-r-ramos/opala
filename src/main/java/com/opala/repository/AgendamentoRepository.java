package com.opala.repository;

import com.opala.domain.Agendamento;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Agendamento entity.
 */
@SuppressWarnings("unused")
public interface AgendamentoRepository extends JpaRepository<Agendamento,Long> {

    @Query("select distinct agendamento from Agendamento agendamento left join fetch agendamento.listaPassageiros left join fetch agendamento.listaDestinos")
    List<Agendamento> findAllWithEagerRelationships();

    @Query("select agendamento from Agendamento agendamento left join fetch agendamento.listaPassageiros left join fetch agendamento.listaDestinos where agendamento.id =:id")
    Agendamento findOneWithEagerRelationships(@Param("id") Long id);

}
