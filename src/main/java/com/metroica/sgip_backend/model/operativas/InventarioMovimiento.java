package com.metroica.sgip_backend.model.operativas;
import com.metroica.sgip_backend.model.seguridad.Usuario;
import com.metroica.sgip_backend.model.enums.TipoMovimiento;
import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;


@Entity
@Table(name = "inventario_movimientos")
@Data
public class InventarioMovimiento {

    @Id
    @GeneratedValue // UUID gestionado por Postgres
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario; // Quién realizó la acción

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    private TipoMovimiento tipo;

    @Column(nullable = false)
    @Positive(message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;

    @Column(name = "stock_antes", nullable = false)
    private Integer stockAntes;

    @Column(name = "stock_despues", nullable = false)
    private Integer stockDespues;

    @Column(name = "costo_unitario")
    private BigDecimal costoUnitario;

    private String motivo;
    private String referencia; // Nro de guía, factura o pedido

    @Column(nullable = false, updatable = false)
    private LocalDateTime fecha = LocalDateTime.now();
}