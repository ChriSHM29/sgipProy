package com.metroica.sgip_backend.pedidos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    @Query("SELECT p FROM Pedido p WHERE p.estado NOT IN ('DESPACHADO', 'CANCELADO') ORDER BY p.prioridad ASC, p.fechaIngreso ASC")
    List<Pedido> findColaPedidosActivos();

    @Query("SELECT COUNT(p) FROM Pedido p WHERE p.estado NOT IN ('DESPACHADO', 'CANCELADO')")
    long countPedidosActivos();
}
