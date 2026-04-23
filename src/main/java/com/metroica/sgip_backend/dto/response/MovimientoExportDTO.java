package com.metroica.sgip_backend.dto.response;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class MovimientoExportDTO {
    private UUID productoId;
    private Integer cantidad;
    private LocalDateTime fecha;
    // Solo enviamos 3 datos. Pandas se encargará del resto en Python.
}