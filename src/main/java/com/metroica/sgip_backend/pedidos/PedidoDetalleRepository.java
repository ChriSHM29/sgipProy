package com.metroica.sgip_backend.pedidos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PedidoDetalleRepository extends JpaRepository<PedidoDetalle, UUID> {
}
