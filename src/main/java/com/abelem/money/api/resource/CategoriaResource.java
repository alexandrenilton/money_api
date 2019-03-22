package com.abelem.money.api.resource;

import java.net.URI;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.abelem.money.api.event.RecursoCriadoEvent;
import com.abelem.money.api.model.Categoria;
import com.abelem.money.api.repository.CategoriaRepository;

@RestController
@RequestMapping("/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public List<Categoria> list() {
		 return categoriaRepository.findAll();
	}
	
	/**
	 * Metodo salva e já retorna o objeto salvo.
	 * Pensando no HATEOS
	 * @param categoria
	 * @param response
	 * @return
	 */
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Categoria> create(
			@Valid @RequestBody Categoria categoria, HttpServletResponse response) {
		
		Categoria categoriaSaved = categoriaRepository.save(categoria);
		publisher.publishEvent(new RecursoCriadoEvent(this, response, categoriaSaved.getId()));
		return ResponseEntity.status(HttpStatus.CREATED).body(categoriaSaved);
	}
	
	/**
	 * Busca pelo ID
	 * @param id
	 * @return
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> find(@PathVariable("id") Long id) {
		Categoria c = categoriaRepository.findById(id).orElse(null);
		
		return c == null ? 
				ResponseEntity.notFound().build() : ResponseEntity.ok().body(c);
	}
}
