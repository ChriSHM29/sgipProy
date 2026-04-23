package com.metroica.sgip_backend.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductoResponseDTO {
    private UUID id;
    private String sku;
    private String nombre;
    private String categoriaNombre; // Aplanado: React solo necesita el texto
    private String proveedorNombre; // Aplanado: React solo necesita el texto
    private BigDecimal precioVenta;
    private Integer stockActual;
    private Integer puntoPedido;
    private String estado; // ACTIVO, INACTIVO
    // Nota: El "Nivel de Stock" (OK/Bajo/Crítico) lo calcularemos en el Service antes de enviar
}