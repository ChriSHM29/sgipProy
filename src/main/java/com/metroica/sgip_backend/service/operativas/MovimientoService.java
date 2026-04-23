package com.metroica.sgip_backend.service.operativas; // <- Ajustado a tu carpeta "operativas"

import com.metroica.sgip_backend.dto.request.MovimientoRequestDTO;
import com.metroica.sgip_backend.model.enums.TipoMovimiento;
import com.metroica.sgip_backend.model.operativas.InventarioMovimiento;
import com.metroica.sgip_backend.model.operativas.Producto;
import com.metroica.sgip_backend.model.seguridad.Usuario; // <- ¡Tu entidad Usuario correcta!
import com.metroica.sgip_backend.repository.operativas.InventarioMovimientoRepository;
import com.metroica.sgip_backend.repository.operativas.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final InventarioMovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public String registrarMovimientoReal(MovimientoRequestDTO request) {
        // 1. Buscamos el producto en la base de datos
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Error: Producto no encontrado en la base de datos"));

        // 2. Capturamos el stock actual antes de tocarlo
        int stockAntes = producto.getStockActual();
        int stockDespues = stockAntes;

        // 3. Lógica matemática: ¿Es entrada o salida?
        if (request.getTipo().equalsIgnoreCase("SALIDA")) {
            if (stockAntes < request.getCantidad()) {
                throw new RuntimeException("Error: Stock insuficiente. Solo hay " + stockAntes + " unidades disponibles.");
            }
            stockDespues = stockAntes - request.getCantidad();
        } else if (request.getTipo().equalsIgnoreCase("ENTRADA")) {
            stockDespues = stockAntes + request.getCantidad();
        }

        // 4. Actualizamos el stock del producto
        producto.setStockActual(stockDespues);
        productoRepository.save(producto);

        // 5. Creamos el registro del historial (El recibo)
        InventarioMovimiento movimiento = new InventarioMovimiento();
        movimiento.setProducto(producto);
        movimiento.setTipo(TipoMovimiento.valueOf(request.getTipo().toUpperCase()));
        movimiento.setCantidad(request.getCantidad());
        movimiento.setStockAntes(stockAntes);
        movimiento.setStockDespues(stockDespues);
        movimiento.setMotivo(request.getMotivo());
        movimiento.setFecha(LocalDateTime.now());

        // 6. Evitamos el Error 500 conectando el Usuario Admin
        Usuario admin = new Usuario();
        admin.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        movimiento.setUsuario(admin);

        movimientoRepository.save(movimiento);

        // ¡ESTO ES LO QUE LE FALTABA A TU CÓDIGO!
        return "¡Éxito! El stock de '" + producto.getNombre() + "' pasó de " + stockAntes + " a " + stockDespues;
    }
}