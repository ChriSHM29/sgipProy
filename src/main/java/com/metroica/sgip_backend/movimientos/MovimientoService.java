package com.metroica.sgip_backend.movimientos;

import com.metroica.sgip_backend.productos.Producto;
import com.metroica.sgip_backend.productos.ProductoRepository;
import com.metroica.sgip_backend.seguridad.Usuario;
import com.metroica.sgip_backend.shared.enums.TipoMovimiento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final ProductoRepository productoRepository;

    @Transactional
    public String registrarMovimientoReal(MovimientoRequestDTO request) {
        Producto producto = productoRepository.findById(request.getProductoId())
                .orElseThrow(() -> new RuntimeException("Producto no encontrado en la base de datos"));

        int stockAntes = producto.getStockActual();
        int stockDespues = stockAntes;

        if (request.getTipo().equalsIgnoreCase("SALIDA")) {
            if (stockAntes < request.getCantidad()) {
                throw new RuntimeException("Stock insuficiente. Solo hay " + stockAntes + " unidades disponibles.");
            }
            stockDespues = stockAntes - request.getCantidad();
        } else if (request.getTipo().equalsIgnoreCase("ENTRADA")) {
            stockDespues = stockAntes + request.getCantidad();
        }

        producto.setStockActual(stockDespues);
        productoRepository.save(producto);

        InventarioMovimiento movimiento = new InventarioMovimiento();
        movimiento.setProducto(producto);
        movimiento.setTipo(TipoMovimiento.valueOf(request.getTipo().toUpperCase()));
        movimiento.setCantidad(request.getCantidad());
        movimiento.setStockAntes(stockAntes);
        movimiento.setStockDespues(stockDespues);
        movimiento.setMotivo(request.getMotivo());
        movimiento.setFecha(LocalDateTime.now());

        Usuario admin = new Usuario();
        admin.setId(UUID.fromString("11111111-1111-1111-1111-111111111111"));
        movimiento.setUsuario(admin);

        movimientoRepository.save(movimiento);

        return "Stock de '" + producto.getNombre() + "' paso de " + stockAntes + " a " + stockDespues;
    }
}
