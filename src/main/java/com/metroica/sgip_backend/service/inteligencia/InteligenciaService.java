package com.metroica.sgip_backend.service.inteligencia;

import com.metroica.sgip_backend.dto.response.MovimientoExportDTO;
import com.metroica.sgip_backend.mapper.inteligencia.MovimientoExportMapper;
import com.metroica.sgip_backend.model.enums.TipoMovimiento;
import com.metroica.sgip_backend.repository.operativas.InventarioMovimientoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InteligenciaService {

    private final InventarioMovimientoRepository movimientoRepository;
    private final MovimientoExportMapper mapper;

    @Transactional(readOnly = true)
    public List<MovimientoExportDTO> extraerDatosEntrenamiento() {
        // Traemos solo las SALIDAS (ventas/despachos) para analizar la demanda
        return movimientoRepository.findByTipoOrderByFechaAsc(TipoMovimiento.SALIDA)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}