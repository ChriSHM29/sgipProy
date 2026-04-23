package com.metroica.sgip_backend.mapper.inteligencia;

import com.metroica.sgip_backend.dto.response.MovimientoExportDTO;
import com.metroica.sgip_backend.model.operativas.InventarioMovimiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MovimientoExportMapper {

    @Mapping(source = "producto.id", target = "productoId")
    MovimientoExportDTO toDTO(InventarioMovimiento movimiento);
}