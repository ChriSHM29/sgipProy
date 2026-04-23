package com.metroica.sgip_backend.controller.operativo;

import com.metroica.sgip_backend.dto.request.MovimientoRequestDTO;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movimientos")
public class MovimientoController {

    // POST: http://localhost:8080/api/v1/movimientos
    @PostMapping
    public ResponseEntity<String> registrarMovimiento(@RequestBody @NonNull MovimientoRequestDTO request) {

        // 1. Imprimimos en la terminal de Java para confirmar que los datos llegaron
        System.out.println("🚨 ¡ALERTA DE CAJA!");
        System.out.println("Se vendieron " + request.getCantidad() + " unidades del producto: " + request.getProductoId());

        // 2. Le respondemos a Postman que todo salió perfecto
        return ResponseEntity.status(HttpStatus.CREATED).body("¡Venta registrada con éxito en el backend!");
    }
}