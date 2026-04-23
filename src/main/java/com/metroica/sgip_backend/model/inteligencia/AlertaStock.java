package com.metroica.sgip_backend.model.inteligencia;
import com.metroica.sgip_backend.model.operativas.Producto;
import com.metroica.sgip_backend.model.seguridad.Usuario;
import com.metroica.sgip_backend.model.enums.EstadoAlerta;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "alertas_stock")
@Data
public class AlertaStock {

    @Id
    @GeneratedValue // UUID generado por PostgreSQL
    private UUID id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(name = "stock_al_generar", nullable = false)
    private Integer stockAlGenerar;

    @Column(name = "punto_pedido_ref", nullable = false)
    private Integer puntoPedidoReferencia;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoAlerta estado = EstadoAlerta.ACTIVA;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resuelta_por")
    private Usuario resueltaPor; // El usuario que atendió la alerta

    @Column(name = "fecha_generada", insertable = false, updatable = false)
    private LocalDateTime fechaGenerada;

    @Column(name = "fecha_resuelta")
    private LocalDateTime fechaResuelta;
}