package com.opala.repository;

import com.opala.domain.Itinerario;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Itinerario entity.
 */
@SuppressWarnings("unused")
public interface ItinerarioRepository extends JpaRepository<Itinerario,Long> {

}
