package com.opala.repository;

import com.opala.domain.Passageiro;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Passageiro entity.
 */
@SuppressWarnings("unused")
public interface PassageiroRepository extends JpaRepository<Passageiro,Long> {

}
