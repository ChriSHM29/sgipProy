package com.metroica.sgip_backend.inteligencia;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/inteligencia")
@RequiredArgsConstructor
public class InteligenciaController {

    private final InteligenciaService inteligenciaService;
    private final PrediccionRepository prediccionRepository;

    @GetMapping("/datos-entrenamiento")
    public ResponseEntity<List<MovimientoExportDTO>> obtenerDatos() {
        return ResponseEntity.ok(inteligenciaService.extraerDatosEntrenamiento());
    }

    @GetMapping("/predicciones")
    public ResponseEntity<List<PrediccionResponseDTO>> obtenerPredicciones() {
        List<PrediccionResponseDTO> predicciones = prediccionRepository.findUltimasPredicciones()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(predicciones);
    }

    private PrediccionResponseDTO toDTO(PrediccionDemanda p) {
        PrediccionResponseDTO dto = new PrediccionResponseDTO();
        dto.setId(p.getId());
        dto.setProductoNombre(p.getProducto() != null ? p.getProducto().getNombre() : null);
        dto.setSemanaInicio(p.getSemanaInicio());
        dto.setSemanaFin(p.getSemanaFin());
        dto.setCantidadPredicha(p.getCantidadPredicha());
        dto.setCantidadReal(p.getCantidadReal());
        dto.setErrorPorcentaje(p.getErrorPorcentaje());
        dto.setConfianza(p.getConfianza());
        dto.setModeloVersion(p.getModeloVersion());
        dto.setGeneradoEn(p.getGeneradoEn());
        return dto;
    }
}
