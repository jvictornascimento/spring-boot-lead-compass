package com.jvictornascimento.leadCompass.leads.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import com.jvictornascimento.leadCompass.interactions.model.InteractionType;
import com.jvictornascimento.leadCompass.interactions.repository.InteractionRepository;
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

	@Autowired
	private InteractionRepository interactionRepository;

	@BeforeEach
	void setUp() {
		interactionRepository.deleteAll();
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

	@Test
	void getsLeadByIdWithDiagnosticsAndInteractions() throws Exception {
		Lead lead = saveLead("Marcenaria Alfa", "marcenaria", "Campinas", "SP", LeadStatus.NEW, false, 85);

		mockMvc.perform(get("/api/leads/{id}", lead.getId())
						.with(user("admin")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(lead.getId()))
				.andExpect(jsonPath("$.businessName").value("Marcenaria Alfa"))
				.andExpect(jsonPath("$.niche").value("marcenaria"))
				.andExpect(jsonPath("$.city").value("Campinas"))
				.andExpect(jsonPath("$.state").value("SP"))
				.andExpect(jsonPath("$.status").value("NEW"))
				.andExpect(jsonPath("$.diagnostics", hasSize(0)))
				.andExpect(jsonPath("$.interactions", hasSize(0)));
	}

	@Test
	void returnsNotFoundWhenLeadDoesNotExist() throws Exception {
		mockMvc.perform(get("/api/leads/{id}", 999L)
						.with(user("admin")))
				.andExpect(status().isNotFound());
	}

	@Test
	void updatesLeadStatusAndCreatesInteraction() throws Exception {
		Lead lead = saveLead("Marcenaria Alfa", "marcenaria", "Campinas", "SP", LeadStatus.NEW, false, 85);

		mockMvc.perform(patch("/api/leads/{id}/status", lead.getId())
						.contentType("application/json")
						.content("{\"status\":\"CONTACTED\"}")
						.with(user("admin")))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(lead.getId()))
				.andExpect(jsonPath("$.status").value("CONTACTED"));

		assertThat(leadRepository.findById(lead.getId()))
				.isPresent()
				.get()
				.extracting(Lead::getStatus)
				.isEqualTo(LeadStatus.CONTACTED);

		assertThat(interactionRepository.findAll())
				.singleElement()
				.satisfies(interaction -> {
					assertThat(interaction.getLead().getId()).isEqualTo(lead.getId());
					assertThat(interaction.getType()).isEqualTo(InteractionType.STATUS_CHANGED);
					assertThat(interaction.getDescription()).contains("NEW").contains("CONTACTED");
				});
	}

	@Test
	void returnsNotFoundWhenUpdatingMissingLeadStatus() throws Exception {
		mockMvc.perform(patch("/api/leads/{id}/status", 999L)
						.contentType("application/json")
						.content("{\"status\":\"CONTACTED\"}")
						.with(user("admin")))
				.andExpect(status().isNotFound());
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
