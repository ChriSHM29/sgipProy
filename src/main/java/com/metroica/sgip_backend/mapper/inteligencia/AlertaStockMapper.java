package com.metroica.sgip_backend.mapper.inteligencia;

import com.metroica.sgip_backend.dto.response.AlertaStockResponseDTO;
import com.metroica.sgip_backend.model.inteligencia.AlertaStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AlertaStockMapper {

    @Mapping(source = "producto.nombre", target = "productoNombre")
    AlertaStockResponseDTO toResponseDTO(AlertaStock alerta);
}