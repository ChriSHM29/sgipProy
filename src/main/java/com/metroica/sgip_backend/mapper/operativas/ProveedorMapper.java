package com.metroica.sgip_backend.mapper.operativas;

import com.metroica.sgip_backend.model.operativas.Proveedor;
import com.metroica.sgip_backend.dto.response.ProveedorResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProveedorMapper {

    ProveedorResponseDTO toResponseDTO(Proveedor proveedor);

}