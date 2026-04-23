package com.metroica.sgip_backend.mapper.operativas;

import com.metroica.sgip_backend.dto.request.ProductoCreateDTO;
import com.metroica.sgip_backend.dto.response.ProductoResponseDTO;
import com.metroica.sgip_backend.model.operativas.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductoMapper {

    // Entidad a Response DTO (Aplanando las relaciones de Categoría y Proveedor)
    @Mapping(source = "categoria.nombre", target = "categoriaNombre")
    @Mapping(source = "proveedor.nombre", target = "proveedorNombre")
    ProductoResponseDTO toResponseDTO(Producto producto);

    // Request DTO a Entidad (Ignoramos el ID y relaciones complejas que el Service buscará)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "categoria", ignore = true) // El Service inyectará la entidad Categoria real
    @Mapping(target = "proveedor", ignore = true) // El Service inyectará la entidad Proveedor real
    @Mapping(target = "estado", constant = "ACTIVO") // Por defecto al crear
    Producto toEntity(ProductoCreateDTO dto);
}