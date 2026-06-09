package com.jvictornascimento.leadCompass.leads.controller;

import com.jvictornascimento.leadCompass.leads.exception.LeadNotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = LeadController.class)
public class LeadExceptionHandler {

	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(LeadNotFoundException.class)
	void handleLeadNotFound() {
	}
}
