package com.metroica.sgip_backend.dto.response;

import lombok.Data;

@Data
public class CategoriaResponseDTO {
    private Integer id;

    private String nombre;

    private String descripcion;

    private Boolean activa;
}
