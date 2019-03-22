package com.abelem.money.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abelem.money.api.repository.LancamentoRepository;

@Service
public class LancamentoService {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;

	public LancamentoService(LancamentoRepository lancamentoRepository) {
		super();
		this.lancamentoRepository = lancamentoRepository;
	}
	
	
	
}
