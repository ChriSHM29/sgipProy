package com.metroica.sgip_backend.controller.inteligencia;

import com.metroica.sgip_backend.dto.response.MovimientoExportDTO;
import com.metroica.sgip_backend.service.inteligencia.InteligenciaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inteligencia")
@RequiredArgsConstructor
public class InteligenciaController {

    private final InteligenciaService inteligenciaService;

    // Python hará un GET a esta ruta para consumir el JSON
    @GetMapping("/datos-entrenamiento")
    public ResponseEntity<List<MovimientoExportDTO>> obtenerDatos() {
        return ResponseEntity.ok(inteligenciaService.extraerDatosEntrenamiento());
    }
}