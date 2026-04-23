package com.metroica.sgip_backend.repository.operativo;

import com.metroica.sgip_backend.model.operativas.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, UUID> {

    Optional<Producto> findByCodigoBarras(String codigoBarras);
    Optional<Producto> findBySku(String sku);

    // Lógica para el RF-04: Buscar stock que cayó por debajo del ROP (Punto de Pedido)
    @Query("SELECT p FROM Producto p WHERE p.stockActual <= p.puntoPedido AND p.estado = 'ACTIVO'")
    List<Producto> findProductosConStockCritico();
}