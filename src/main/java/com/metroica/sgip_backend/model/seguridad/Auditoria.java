package com.metroica.sgip_backend.model.seguridad;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "auditoria")
@Data
public class Auditoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // BIGSERIAL en Postgres
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;

    @Column(nullable = false, length = 100)
    private String accion; // LOGIN, UPDATE_STOCK, etc.

    @Column(length = 100)
    private String tabla;

    @Column(name = "registro_id", length = 100)
    private String registroId;

    @Column(columnDefinition = "jsonb")
    private String detalle; // Detalles técnicos en formato JSON

    @Column(length = 45)
    private String ip;

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha = LocalDateTime.now();
}