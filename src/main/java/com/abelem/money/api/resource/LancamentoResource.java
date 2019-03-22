package com.abelem.money.api.resource;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abelem.money.api.model.Lancamento;
import com.abelem.money.api.repository.LancamentoRepository;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	
	@GetMapping
	public List<Lancamento> list() {
		return lancamentoRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<Lancamento> find(@PathVariable Long id) {
		Lancamento l = lancamentoRepository.findById(id).orElse(null);
		
		return l == null ? 
				ResponseEntity.notFound().build() : ResponseEntity.ok().body(l);
	}

	

}
