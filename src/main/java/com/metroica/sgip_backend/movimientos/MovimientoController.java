package com.metroica.sgip_backend.movimientos;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @PostMapping
    public ResponseEntity<String> registrarMovimiento(@RequestBody @NonNull MovimientoRequestDTO request) {
        String resultado = movimientoService.registrarMovimientoReal(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(resultado);
    }
}
