package com.abelem.money.api.resource;

import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.abelem.money.api.event.RecursoCriadoEvent;
import com.abelem.money.api.model.Lancamento;
import com.abelem.money.api.model.Pessoa;
import com.abelem.money.api.repository.LancamentoRepository;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Lancamento> list() {
		return lancamentoRepository.findAll();
	}
	
	/**
	 * Find (id)
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<Lancamento> find(@PathVariable Long id) {
		Lancamento l = lancamentoRepository.findById(id).orElse(null);
		
		return l == null ? 
				ResponseEntity.notFound().build() : ResponseEntity.ok().body(l);
	}
	
	
	@PostMapping
	public ResponseEntity<Lancamento> create(@Valid @RequestBody Lancamento lancamento, HttpServletResponse response) {
		Lancamento lancamentoSaved = lancamentoRepository.save(lancamento);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, lancamentoSaved.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(lancamentoSaved);
	}

	

}
