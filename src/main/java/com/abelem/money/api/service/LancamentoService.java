package com.abelem.money.api.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.abelem.money.api.model.Lancamento;
import com.abelem.money.api.model.Pessoa;
import com.abelem.money.api.repository.LancamentoRepository;
import com.abelem.money.api.repository.PessoaRepository;
import com.abelem.money.api.service.exception.PessoaInexistenteOuInativaException;

@Service
public class LancamentoService {

	@Autowired
	private PessoaRepository pessoaRepository;
	
	@Autowired
	private LancamentoRepository lancamentoRepository;

	public LancamentoService(LancamentoRepository lancamentoRepository) {
		super();
		this.lancamentoRepository = lancamentoRepository;
	}
	
	public Lancamento save(Lancamento lancamento) {
		Pessoa pessoa = pessoaRepository.findById(lancamento.getPessoa().getId()).orElse(null);
		if ( pessoa == null || pessoa.isInativo() ) {
			throw new PessoaInexistenteOuInativaException();
		}
		
		return lancamentoRepository.save(lancamento);
	}
	
	
}
