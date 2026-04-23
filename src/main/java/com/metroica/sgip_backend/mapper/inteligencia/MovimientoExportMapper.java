package com.metroica.sgip_backend.mapper.inteligencia;

import com.metroica.sgip_backend.dto.response.MovimientoExportDTO;
import com.metroica.sgip_backend.model.operativas.InventarioMovimiento;
import org.springframework.stereotype.Component;

@Component
public class MovimientoExportMapper {

    public MovimientoExportDTO toDTO(InventarioMovimiento movimiento) {
        MovimientoExportDTO dto = new MovimientoExportDTO();

        dto.setProductoId(movimiento.getProducto().getId());
        // Agregamos la extracción del nombre navegando por la relación JPA
        dto.setProductoNombre(movimiento.getProducto().getNombre());

        dto.setCantidad(movimiento.getCantidad());
        dto.setFecha(movimiento.getFecha());
        return dto;
    }
}