package com.abelem.money.api.exceptionhandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class MoneyExceptionHandler extends ResponseEntityExceptionHandler {
	
	@Autowired
	private MessageSource messageSource;
	
	/** 
	 * mensagem que nao sao possiveis de serem lidas
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		String messageUser = messageSource.getMessage("mensagem.invalida", null , LocaleContextHolder.getLocale());
		String messageDeveloper = ex.getCause() != null ? ex.getCause().toString() : ex.getMessage().toString();
		
		List<ErrorHandler> errors = Arrays.asList
				(new ErrorHandler(messageUser, messageDeveloper));
		
		return handleExceptionInternal(ex, errors , headers, status, request);
	}
	
	@ExceptionHandler({EmptyResultDataAccessException.class})
	public ResponseEntity<Object> handleEmptyResultDataAccessException(RuntimeException ex, WebRequest request) {
		String messageUser = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale());
		String messageDeveloper = ex.toString();
		
		List<ErrorHandler> errors = Arrays.asList(new ErrorHandler(messageUser, messageDeveloper));
		return handleExceptionInternal(ex, errors , new HttpHeaders(), HttpStatus.NOT_FOUND, request);
	}
	
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		
		List<ErrorHandler> errors = createErrorsList(ex.getBindingResult());
		return handleExceptionInternal(ex, errors , headers, HttpStatus.BAD_REQUEST, request);
	}
	
	
	@ExceptionHandler({DataIntegrityViolationException.class})
	public ResponseEntity<Object> handleDateIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
		String messageUser = messageSource.getMessage("recurso.operacao-nao-permitida", null, LocaleContextHolder.getLocale());
		                          /* from commons-lang3 */
		String messageDeveloper = ExceptionUtils.getRootCauseMessage(ex);
		
		List<ErrorHandler> errors = Arrays.asList(new ErrorHandler(messageUser, messageDeveloper));
		return handleExceptionInternal(ex, errors , new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}
	
	private List<ErrorHandler> createErrorsList(BindingResult bindingResult) {
		List<ErrorHandler> errors = new ArrayList<ErrorHandler>();
		
		for (FieldError fe : bindingResult.getFieldErrors()) {
			String messageUser = messageSource.getMessage(fe, LocaleContextHolder.getLocale());
			String messageDeveloper = fe.toString();
			errors.add(new ErrorHandler(messageUser, messageDeveloper));
		}
		
		return errors;
	}
	
	public static class ErrorHandler{
		private String messageUser;
		private String messageDeveloper;
		
		public ErrorHandler(String messageUser, String messageDeveloper) {
			super();
			this.messageUser = messageUser;
			this.messageDeveloper = messageDeveloper;
		}
		public void setMessageUser(String messageUser) {
			this.messageUser = messageUser;
		}
		public void setMessageDeveloper(String messageDeveloper) {
			this.messageDeveloper = messageDeveloper;
		}
		public String getMessageUser() {
			return messageUser;
		}
		public String getMessageDeveloper() {
			return messageDeveloper;
		}
	}
	
	
}
