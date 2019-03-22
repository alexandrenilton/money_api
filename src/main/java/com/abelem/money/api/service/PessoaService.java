package com.abelem.money.api.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import com.abelem.money.api.model.Pessoa;
import com.abelem.money.api.repository.PessoaRepository;

@Service
public class PessoaService {
	
	@Autowired
	private PessoaRepository pessoaRepository;
	
	public PessoaService(PessoaRepository pessoaRepository) {
		super();
		this.pessoaRepository = pessoaRepository;
	}


	public Pessoa update(Long id, Pessoa pessoa) {
		Pessoa pessoaSaved = findPersonById(id);
		
		//copiar pessoa para pessoaSaved, tirando o "id" 
		BeanUtils.copyProperties(pessoa,  pessoaSaved, "id");
		return pessoaRepository.save(pessoaSaved);
	}

	public void updateActive(Long id, Boolean active) {
		Pessoa pessoaSaved = findPersonById(id);
		pessoaSaved.setAtivo(active);
		pessoaRepository.save(pessoaSaved);
	}
	
	
	private Pessoa findPersonById(Long id) {
		Pessoa pessoaSaved = pessoaRepository.findById(id).orElse(null);
		if ( pessoaSaved == null ) {
			throw new EmptyResultDataAccessException(1); // esperava apenas 1 elemento
		}
		return pessoaSaved;
	}
}
