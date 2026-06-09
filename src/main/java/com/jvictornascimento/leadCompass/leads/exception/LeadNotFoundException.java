package com.jvictornascimento.leadCompass.leads.exception;

public class LeadNotFoundException extends RuntimeException {

	public LeadNotFoundException(Long id) {
		super("Lead not found: " + id);
	}
}
