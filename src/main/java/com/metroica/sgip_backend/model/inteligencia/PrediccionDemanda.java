package com.metroica.sgip_backend.model.inteligencia;

import com.metroica.sgip_backend.model.operativas.Producto;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "predicciones_demanda",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"producto_id", "semana_inicio"})})
@Data
public class PrediccionDemanda {

    @Id
    @GeneratedValue // UUID generado por Postgres
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "semana_inicio", nullable = false)
    private LocalDate semanaInicio; // Usamos LocalDate para el tipo DATE de SQL

    @Column(name = "semana_fin", nullable = false)
    private LocalDate semanaFin;

    @Column(name = "cantidad_predicha", nullable = false)
    private Integer cantidadPredicha;

    @Column(name = "cantidad_real")
    private Integer cantidadReal; // Se llena a posteriori para calcular el error

    @Column(name = "error_porcentaje", precision = 6, scale = 2)
    private BigDecimal errorPorcentaje;

    @Column(precision = 5, scale = 2)
    private BigDecimal confianza; // % de confianza del modelo de IA

    @Column(name = "modelo_version", nullable = false, length = 50)
    private String modeloVersion = "v1.0-linreg";

    @Column(name = "generado_en", insertable = false, updatable = false)
    private LocalDateTime generadoEn;
}