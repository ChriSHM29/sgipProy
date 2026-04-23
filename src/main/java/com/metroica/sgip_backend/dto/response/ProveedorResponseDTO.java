package com.metroica.sgip_backend.dto.response;


import lombok.Data;


@Data
public class ProveedorResponseDTO {
    private Integer id;

    private String nombre;

    private String ruc;

    private String contacto;
    private String telefono;
    private String email;
    private String direccion;
    private Integer leadTimeDias;

    private Boolean activo;
}
