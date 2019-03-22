package com.abelem.money.api.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.abelem.money.api.model.Lancamento;
import com.abelem.money.api.repository.lancamento.LancamentoRepositoryQuery;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long>, LancamentoRepositoryQuery{
	
}
