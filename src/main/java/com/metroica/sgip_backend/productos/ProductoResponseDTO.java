package com.metroica.sgip_backend.productos;

import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductoResponseDTO {
    private UUID id;
    private String sku;
    private String nombre;
    private String categoriaNombre;
    private String proveedorNombre;
    private BigDecimal precioVenta;
    private Integer stockActual;
    private Integer puntoPedido;
    private String estado;
}
