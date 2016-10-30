package com.opala.repository;

import com.opala.domain.Solicitante;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Solicitante entity.
 */
@SuppressWarnings("unused")
public interface SolicitanteRepository extends JpaRepository<Solicitante,Long> {

}
