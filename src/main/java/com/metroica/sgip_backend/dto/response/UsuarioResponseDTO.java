package com.metroica.sgip_backend.dto.response;

import com.metroica.sgip_backend.model.enums.RolUsuario;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UsuarioResponseDTO {
    private UUID id;
    private String nombre;
    private String apellido;
    private String email;
    private RolUsuario rol;
    private Boolean activo;
    private LocalDateTime ultimoLogin;
    // La fecha de creación (createdAt) y el password se omiten deliberadamente
}