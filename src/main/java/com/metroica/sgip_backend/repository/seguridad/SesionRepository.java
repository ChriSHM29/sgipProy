package com.metroica.sgip_backend.repository.seguridad;

import com.metroica.sgip_backend.model.seguridad.Sesion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SesionRepository extends JpaRepository<Sesion, UUID> {
}