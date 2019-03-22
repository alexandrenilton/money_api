package com.abelem.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abelem.money.api.model.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>{
	
}
