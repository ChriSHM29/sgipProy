package com.metroica.sgip_backend.model.soporte;


import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "configuracion")
@Data
public class Configuracion {

    @Id
    @Column(length = 100)
    private String clave; // PK es un VARCHAR (ej. 'ia.horizonte.dias')

    @Column(nullable = false, columnDefinition = "TEXT")
    private String valor;

    private String descripcion;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}