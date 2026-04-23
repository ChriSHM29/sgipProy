package com.metroica.sgip_backend.repository.operativo;

import com.metroica.sgip_backend.model.enums.TipoMovimiento;
import com.metroica.sgip_backend.model.operativas.InventarioMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface InventarioMovimientoRepository extends JpaRepository<InventarioMovimiento, UUID> {

    // Extrae el historial de un producto en un rango de fechas (Datos para Python / Machine Learning)
    @Query("SELECT m FROM InventarioMovimiento m WHERE m.producto.id = :productoId AND m.fecha BETWEEN :inicio AND :fin ORDER BY m.fecha ASC")
    List<InventarioMovimiento> findHistorialParaPrediccion(
            @Param("productoId") UUID productoId,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

    List<InventarioMovimiento> findByTipoOrderByFechaAsc(TipoMovimiento tipo);
}