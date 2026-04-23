package com.metroica.sgip_backend.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class PedidoItemDTO {

    @NotNull(message = "El ID del producto es obligatorio")
    private UUID productoId;

    @NotNull
    @Min(value = 1, message = "La cantidad debe ser mayor a cero")
    private Integer cantidad;
}