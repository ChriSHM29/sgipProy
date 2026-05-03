package com.metroica.sgip_backend.alertas;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AlertaStockRepository extends JpaRepository<AlertaStock, UUID> {

    @Query("SELECT COUNT(a) FROM AlertaStock a WHERE a.estado = 'ACTIVA'")
    long countAlertasActivas();
}
