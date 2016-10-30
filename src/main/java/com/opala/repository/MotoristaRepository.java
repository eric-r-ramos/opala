package com.opala.repository;

import com.opala.domain.Motorista;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Motorista entity.
 */
@SuppressWarnings("unused")
public interface MotoristaRepository extends JpaRepository<Motorista,Long> {

}
