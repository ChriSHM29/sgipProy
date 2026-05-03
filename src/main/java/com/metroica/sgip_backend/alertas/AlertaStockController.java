package com.metroica.sgip_backend.alertas;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/alertas")
@RequiredArgsConstructor
public class AlertaStockController {

    private final AlertaStockRepository alertaStockRepository;
    private final AlertaStockMapper alertaStockMapper;

    @GetMapping("/activas")
    public ResponseEntity<List<AlertaStockResponseDTO>> listarAlertasActivas() {
        List<AlertaStockResponseDTO> alertas = alertaStockRepository.findAll()
                .stream()
                .filter(a -> a.getEstado().name().equals("ACTIVA"))
                .map(alertaStockMapper::toResponseDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(alertas);
    }
}
