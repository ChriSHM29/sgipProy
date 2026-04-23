package com.metroica.sgip_backend.mapper.operativas;

import com.metroica.sgip_backend.model.operativas.Categoria;
// Asumiendo que crearás un CategoriaDTO simple con id, nombre y descripcion
import com.metroica.sgip_backend.dto.response.CategoriaResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoriaMapper {

    CategoriaResponseDTO toResponseDTO(Categoria categoria);

}