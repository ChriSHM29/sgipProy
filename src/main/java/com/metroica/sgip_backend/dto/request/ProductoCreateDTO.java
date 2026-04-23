package com.metroica.sgip_backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class ProductoCreateDTO {

    @NotBlank(message = "El SKU es obligatorio")
    private String sku;

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String nombre;

    @NotNull(message = "Debe asignar una categoría")
    private Integer categoriaId; // Solo pedimos el ID, no el objeto entero

    @NotNull(message = "Debe asignar un proveedor")
    private Integer proveedorId;

    @NotNull
    @PositiveOrZero(message = "El precio de costo no puede ser negativo")
    private BigDecimal precioCosto;

    @NotNull
    @PositiveOrZero(message = "El precio de venta no puede ser negativo")
    private BigDecimal precioVenta;

    @NotNull
    @Min(value = 0, message = "El stock inicial no puede ser negativo")
    private Integer stockActual;

    @NotNull
    @Min(value = 1, message = "El punto de pedido (ROP) debe ser al menos 1")
    private Integer puntoPedido;
}