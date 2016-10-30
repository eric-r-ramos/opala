package com.opala.repository;

import com.opala.domain.Veiculo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Veiculo entity.
 */
@SuppressWarnings("unused")
public interface VeiculoRepository extends JpaRepository<Veiculo,Long> {

}
