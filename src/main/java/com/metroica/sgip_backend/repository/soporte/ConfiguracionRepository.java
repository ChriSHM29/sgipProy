package com.metroica.sgip_backend.repository.soporte;

import com.metroica.sgip_backend.model.soporte.Configuracion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfiguracionRepository extends JpaRepository<Configuracion, String> {
}