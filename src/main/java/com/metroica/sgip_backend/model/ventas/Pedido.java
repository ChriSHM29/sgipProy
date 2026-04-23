package com.metroica.sgip_backend.model.ventas;
import com.metroica.sgip_backend.model.seguridad.Usuario;
import com.metroica.sgip_backend.model.enums.CanalPedido;
import com.metroica.sgip_backend.model.enums.EstadoPedido;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "pedidos")
@Data
public class Pedido {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(insertable = false, updatable = false)
    private Integer numero; // SERIAL en Postgres, se genera solo

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CanalPedido canal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoPedido estado = EstadoPedido.PENDIENTE;

    @Min(1) @Max(10)
    private Short prioridad = 5; // Para la cola visual del operario

    @Column(name = "cliente_nombre")
    private String clienteNombre;

    @Column(name = "cliente_telefono")
    private String clienteTelefono;

    @Column(name = "cliente_dir")
    private String clienteDireccion;

    private String observaciones;

    @Column(nullable = false)
    private BigDecimal total = BigDecimal.ZERO; // Se actualiza por el trigger SQL

    @Column(name = "fecha_ingreso", insertable = false, updatable = false)
    private LocalDateTime fechaIngreso;

    @Column(name = "fecha_despacho")
    private LocalDateTime fechaDespacho;
}