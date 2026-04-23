package com.metroica.sgip_backend.model.soporte;

import com.metroica.sgip_backend.model.seguridad.Usuario;
import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;
import java.time.LocalDateTime;

@Entity
@Table(name = "reportes")
@Data
public class Reporte {

    @Id
    @GeneratedValue // UUID generado por Postgres
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // Quién generó el reporte

    @Column(nullable = false, length = 80)
    private String tipo; // INVENTARIO, PEDIDOS, PREDICCION

    @Column(nullable = false, length = 10)
    private String formato; // PDF, EXCEL

    @Column(columnDefinition = "jsonb")
    private String parametros; // Filtros usados para generar el reporte

    @Column(name = "ruta_archivo")
    private String rutaArchivo;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
}