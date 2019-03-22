package com.abelem.money.api.resource;

import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.abelem.money.api.event.RecursoCriadoEvent;
import com.abelem.money.api.exceptionhandler.MoneyExceptionHandler.ErrorHandler;
import com.abelem.money.api.model.Lancamento;
import com.abelem.money.api.repository.LancamentoRepository;
import com.abelem.money.api.repository.filter.LancamentoFilter;
import com.abelem.money.api.service.exception.PessoaInexistenteOuInativaException;

@RestController
@RequestMapping("/lancamentos")
public class LancamentoResource {
	
	@Autowired
	private LancamentoRepository lancamentoRepository;
	
//	@Autowired
//	private LancamentoService lancamentoService;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	@GetMapping
	public Page<Lancamento> search(LancamentoFilter lancamentoFilter, Pageable pageable) {
		return lancamentoRepository.filter(lancamentoFilter, pageable);
	}
	
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
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		lancamentoRepository.deleteById(id);
	}
	
	
	@ExceptionHandler({PessoaInexistenteOuInativaException.class})
	public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
		String messageUser = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale());
		String messageDeveloper = ex.toString();
		List<ErrorHandler> errors = Arrays.asList(new ErrorHandler(messageUser, messageDeveloper));
		
		return ResponseEntity.badRequest().body(errors);
	}

}
