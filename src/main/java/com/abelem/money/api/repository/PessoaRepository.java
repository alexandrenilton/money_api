package com.abelem.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abelem.money.api.model.Pessoa;

public interface PessoaRepository extends JpaRepository<Pessoa, Long> {

}