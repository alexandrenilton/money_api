package com.abelem.money.api.repository.lancamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.abelem.money.api.model.Lancamento;
import com.abelem.money.api.repository.filter.LancamentoFilter;

public interface LancamentoRepositoryQuery {
	
	public Page<Lancamento> filter(LancamentoFilter lancamentoFilter, Pageable pageable);
	
}
