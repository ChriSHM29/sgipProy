package com.metroica.sgip_backend.movimientos;

import com.metroica.sgip_backend.shared.enums.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovimientoRepository extends JpaRepository<InventarioMovimiento, UUID> {

    List<InventarioMovimiento> findByTipoOrderByFechaAsc(TipoMovimiento tipo);
}
