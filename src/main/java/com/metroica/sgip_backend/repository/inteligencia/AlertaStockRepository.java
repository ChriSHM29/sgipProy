package com.metroica.sgip_backend.repository.inteligencia;

import com.metroica.sgip_backend.model.inteligencia.AlertaStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AlertaStockRepository extends JpaRepository<AlertaStock, UUID> {
}
