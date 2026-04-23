package com.metroica.sgip_backend.repository.inteligencia;

import com.metroica.sgip_backend.model.inteligencia.PrediccionDemanda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PrediccionDemandaRepository extends JpaRepository<PrediccionDemanda, UUID> {
}