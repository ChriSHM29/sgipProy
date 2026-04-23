package com.metroica.sgip_backend.model.ventas;
import com.metroica.sgip_backend.model.operativas.Producto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "pedido_detalle")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoDetalle {

    @Id
    @GeneratedValue // UUID generado por la extensión pgcrypto de PostgreSQL
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pedido_id", nullable = false)
    private Pedido pedido;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @Column(nullable = false)
    @Min(value = 1, message = "La cantidad mínima por ítem es 1")
    private Integer cantidad;

    @Column(name = "precio_unitario", precision = 10, scale = 2, nullable = false)
    private BigDecimal precioUnitario;

    /**
     * Campo calculado en la Base de Datos: (cantidad * precio_unitario)
     * Se marca como insertable=false y updatable=false para que Hibernate
     * no intente enviarlo y deje que PostgreSQL lo calcule.
     */
    @Column(insertable = false, updatable = false)
    private BigDecimal subtotal;
}