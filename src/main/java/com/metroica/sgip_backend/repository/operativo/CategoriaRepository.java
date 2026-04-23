package com.metroica.sgip_backend.repository.operativo;

import com.metroica.sgip_backend.model.operativas.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}