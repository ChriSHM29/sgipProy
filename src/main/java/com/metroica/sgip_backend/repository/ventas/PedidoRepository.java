package com.metroica.sgip_backend.repository.ventas;

import com.metroica.sgip_backend.model.ventas.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, UUID> {

    // RF-06: Cola de pedidos ordenada por prioridad (1=urgente) y fecha de ingreso
    @Query("SELECT p FROM Pedido p WHERE p.estado NOT IN ('DESPACHADO', 'CANCELADO') ORDER BY p.prioridad ASC, p.fechaIngreso ASC")
    List<Pedido> findColaPedidosActivos();
}