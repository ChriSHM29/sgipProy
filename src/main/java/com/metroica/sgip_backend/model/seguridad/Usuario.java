package com.metroica.sgip_backend.model.seguridad;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.metroica.sgip_backend.model.enums.RolUsuario;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 150)
    private String nombre;

    @Column(nullable = false, length = 150)
    private String apellido;

    @Column(nullable = false, length = 150, unique = true)
    private String email;

    // Las anotaciones van separadas. @JsonProperty es para React, @Column es para Postgres
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash = "PENDING_HASH";

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolUsuario rol = RolUsuario.OPERARIO;

    @Column(nullable = false)
    private Boolean activo = true;

    @Column(name = "ultimo_login")
    private LocalDateTime ultimoLogin;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}