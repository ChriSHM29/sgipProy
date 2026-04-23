package com.metroica.sgip_backend.repository.ventas;

import com.metroica.sgip_backend.model.ventas.PedidoDetalle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, UUID> {
}