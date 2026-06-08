package com.jvictornascimento.leadCompass.leads.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import com.jvictornascimento.leadCompass.leads.model.Lead;
import com.jvictornascimento.leadCompass.leads.model.LeadStatus;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class LeadRepositoryTests {

	@Autowired
	private LeadRepository leadRepository;

	@Test
	void savesLeadWithCommercialAndDigitalData() {
		Lead lead = new Lead();
		lead.setBusinessName("Marcenaria Exemplo");
		lead.setNiche("marcenaria");
		lead.setCity("Campinas");
		lead.setState("SP");
		lead.setAddress("Rua Exemplo, 123");
		lead.setPhone("19999999999");
		lead.setWhatsapp("19999999999");
		lead.setGoogleMapsUrl("https://maps.google.com/example");
		lead.setGooglePlaceId("places-123");
		lead.setRating(new BigDecimal("4.2"));
		lead.setReviewCount(12);
		lead.setHasWebsite(false);
		lead.setHasHttps(false);
		lead.setResponsive(false);
		lead.setSiteSlow(false);
		lead.setOpportunityScore(85);
		lead.setStatus(LeadStatus.NEW);
		lead.setDuplicate(false);

		Lead savedLead = leadRepository.saveAndFlush(lead);

		assertThat(savedLead.getId()).isNotNull();
		assertThat(savedLead.getCreatedAt()).isNotNull();
		assertThat(savedLead.getUpdatedAt()).isNotNull();

		assertThat(leadRepository.findById(savedLead.getId()))
				.isPresent()
				.get()
				.satisfies(foundLead -> {
					assertThat(foundLead.getBusinessName()).isEqualTo("Marcenaria Exemplo");
					assertThat(foundLead.getCity()).isEqualTo("Campinas");
					assertThat(foundLead.getState()).isEqualTo("SP");
					assertThat(foundLead.getStatus()).isEqualTo(LeadStatus.NEW);
					assertThat(foundLead.getOpportunityScore()).isEqualTo(85);
					assertThat(foundLead.isHasWebsite()).isFalse();
					assertThat(foundLead.isDuplicate()).isFalse();
				});
	}
}
