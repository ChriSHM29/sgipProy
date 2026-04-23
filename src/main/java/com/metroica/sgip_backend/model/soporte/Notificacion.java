package com.metroica.sgip_backend.model.soporte;
import com.metroica.sgip_backend.model.seguridad.Usuario;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "notificaciones")
@Data
public class Notificacion {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // Notificación dirigida a un usuario específico

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String mensaje;

    @Column(nullable = false)
    private Boolean leida = false;

    @Column(nullable = false, length = 50)
    private String tipo = "INFO"; // INFO, ALERTA, ERROR

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}