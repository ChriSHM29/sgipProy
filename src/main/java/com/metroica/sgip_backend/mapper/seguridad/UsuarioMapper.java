package com.metroica.sgip_backend.mapper.seguridad;

import com.metroica.sgip_backend.model.seguridad.Usuario;
import com.metroica.sgip_backend.dto.response.UsuarioResponseDTO; // DTO con id, nombre, apellido, email, rol, activo
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UsuarioMapper {

    // Convierte la entidad al DTO de salida (El DTO no debe tener el campo password)
    UsuarioResponseDTO toResponseDTO(Usuario usuario);

}