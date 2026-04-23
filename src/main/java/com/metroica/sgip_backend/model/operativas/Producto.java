package com.metroica.sgip_backend.model.operativas;

import com.metroica.sgip_backend.model.enums.EstadoProducto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue // PostgreSQL genera el UUID vía gen_random_uuid()
    private UUID id;

    @Column(unique = true, length = 50)
    @NotBlank(message = "El SKU es obligatorio para el control interno")
    private String sku; // Código interno Metro

    @Column(name = "codigo_barras", unique = true, length = 50)
    private String codigoBarras;

    @Column(nullable = false, length = 250)
    @NotBlank(message = "El nombre del producto no puede estar vacío")
    private String nombre;

    private String descripcion;
    private String marca;

    @Column(name = "unidad_medida", length = 30)
    private String unidadMedida = "UNIDAD";

    // RELACIONES: Muchos productos pertenecen a una categoría/proveedor
    @ManyToOne(fetch = FetchType.EAGER) // Eager para tener la categoría siempre lista en el front
    @JoinColumn(name = "categoria_id", nullable = false)
    private Categoria categoria;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proveedor_id", nullable = false)
    private Proveedor proveedor;

    // PRECIOS: Usamos BigDecimal para precisión financiera
    @Column(name = "precio_costo", precision = 10, scale = 2, nullable = false)
    @PositiveOrZero(message = "El precio de costo no puede ser negativo")
    private BigDecimal precioCosto;

    @Column(name = "precio_venta", precision = 10, scale = 2, nullable = false)
    @PositiveOrZero(message = "El precio de venta no puede ser negativo")
    private BigDecimal precioVenta;

    // GESTIÓN DE STOCK (Teoría ROP)
    @Column(name = "stock_actual", nullable = false)
    private Integer stockActual = 0;

    @Column(name = "stock_minimo", nullable = false)
    private Integer stockMinimo = 10;

    @Column(name = "stock_maximo", nullable = false)
    private Integer stockMaximo = 500;

    @Column(name = "punto_pedido", nullable = false)
    private Integer puntoPedido = 30; // Dispara trg_alerta_stock en la DB

    @Enumerated(EnumType.STRING)
    private EstadoProducto estado = EstadoProducto.ACTIVO;

    @Column(name = "imagen_url")
    private String imagenUrl;

    // FECHAS: Gestionadas 100% por PostgreSQL Triggers
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}