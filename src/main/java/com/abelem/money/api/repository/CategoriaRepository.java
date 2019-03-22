package com.abelem.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abelem.money.api.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long>{

}
