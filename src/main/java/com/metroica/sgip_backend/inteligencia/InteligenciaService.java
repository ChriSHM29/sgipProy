package com.metroica.sgip_backend.inteligencia;

import com.metroica.sgip_backend.movimientos.MovimientoRepository;
import com.metroica.sgip_backend.shared.enums.TipoMovimiento;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InteligenciaService {

    private final MovimientoRepository movimientoRepository;
    private final MovimientoExportMapper mapper;

    @Transactional(readOnly = true)
    public List<MovimientoExportDTO> extraerDatosEntrenamiento() {
        return movimientoRepository.findByTipoOrderByFechaAsc(TipoMovimiento.SALIDA)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }
}
