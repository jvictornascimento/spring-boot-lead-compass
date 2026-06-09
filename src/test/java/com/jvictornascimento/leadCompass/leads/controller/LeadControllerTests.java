package com.jvictornascimento.leadCompass.leads.controller;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import com.jvictornascimento.leadCompass.leads.model.Lead;
import com.jvictornascimento.leadCompass.leads.model.LeadStatus;
import com.jvictornascimento.leadCompass.leads.repository.LeadRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
class LeadControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private LeadRepository leadRepository;

	@BeforeEach
	void setUp() {
		leadRepository.deleteAll();
	}

	@Test
	void listsLeadsFilteredByCity() throws Exception {
		saveLead("Marcenaria Alfa", "marcenaria", "Campinas", "SP", LeadStatus.NEW, false, 85);
		saveLead("Clínica Beta", "clinica", "Sorocaba", "SP", LeadStatus.QUALIFIED, true, 30);

		mockMvc.perform(get("/api/leads")
						.queryParam("city", "Campinas")
						.with(user("admin")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].businessName").value("Marcenaria Alfa"))
				.andExpect(jsonPath("$[0].city").value("Campinas"))
				.andExpect(jsonPath("$[0].status").value("NEW"));
	}

	@Test
	void listsLeadsFilteredByCombinedCriteria() throws Exception {
		saveLead("Marcenaria Alfa", "marcenaria", "Campinas", "SP", LeadStatus.NEW, false, 85);
		saveLead("Marcenaria Beta", "marcenaria", "Campinas", "SP", LeadStatus.NEW, false, 65);
		saveLead("Marcenaria Gama", "marcenaria", "Campinas", "SP", LeadStatus.NEW, true, 90);
		saveLead("Marcenaria Delta", "marcenaria", "Campinas", "SP", LeadStatus.CONTACTED, false, 95);
		saveLead("Oficina Epsilon", "oficina", "Campinas", "SP", LeadStatus.NEW, false, 88);

		mockMvc.perform(get("/api/leads")
						.queryParam("city", "Campinas")
						.queryParam("niche", "marcenaria")
						.queryParam("status", "NEW")
						.queryParam("withoutWebsite", "true")
						.queryParam("minScore", "70")
						.with(user("admin")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[*].businessName", containsInAnyOrder("Marcenaria Alfa")))
				.andExpect(jsonPath("$[0].hasWebsite").value(false))
				.andExpect(jsonPath("$[0].opportunityScore").value(85));
	}

	private Lead saveLead(
			String businessName,
			String niche,
			String city,
			String state,
			LeadStatus status,
			boolean hasWebsite,
			int opportunityScore) {
		Lead lead = new Lead();
		lead.setBusinessName(businessName);
		lead.setNiche(niche);
		lead.setCity(city);
		lead.setState(state);
		lead.setPhone("19999999999");
		lead.setWhatsapp("19999999999");
		lead.setRating(new BigDecimal("4.5"));
		lead.setReviewCount(20);
		lead.setStatus(status);
		lead.setHasWebsite(hasWebsite);
		lead.setOpportunityScore(opportunityScore);
		return leadRepository.saveAndFlush(lead);
	}
}
