package com.metroica.sgip_backend.repository.seguridad;

import com.metroica.sgip_backend.model.seguridad.Auditoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditoriaRepository extends JpaRepository<Auditoria, Long> {
}