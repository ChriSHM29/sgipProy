package com.metroica.sgip_backend.repository.operativas;

import com.metroica.sgip_backend.model.operativas.Proveedor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
}