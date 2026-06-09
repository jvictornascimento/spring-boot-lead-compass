package com.jvictornascimento.leadCompass.leads.controller;

import java.util.List;

import com.jvictornascimento.leadCompass.leads.dto.LeadResponse;
import com.jvictornascimento.leadCompass.leads.model.LeadStatus;
import com.jvictornascimento.leadCompass.leads.service.LeadService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/leads")
public class LeadController {

	private final LeadService leadService;

	public LeadController(LeadService leadService) {
		this.leadService = leadService;
	}

	@GetMapping
	public List<LeadResponse> listLeads(
			@RequestParam(required = false) String city,
			@RequestParam(required = false) String niche,
			@RequestParam(required = false) LeadStatus status,
			@RequestParam(required = false) Boolean withoutWebsite,
			@RequestParam(required = false) Integer minScore) {
		return leadService.listLeads(city, niche, status, withoutWebsite, minScore);
	}
}
