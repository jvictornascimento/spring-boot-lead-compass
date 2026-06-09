package com.jvictornascimento.leadCompass.leads.service;

import java.util.List;

import com.jvictornascimento.leadCompass.interactions.model.Interaction;
import com.jvictornascimento.leadCompass.interactions.model.InteractionType;
import com.jvictornascimento.leadCompass.interactions.repository.InteractionRepository;
import com.jvictornascimento.leadCompass.leads.dto.LeadDetailResponse;
import com.jvictornascimento.leadCompass.leads.dto.LeadResponse;
import com.jvictornascimento.leadCompass.leads.dto.UpdateLeadStatusRequest;
import com.jvictornascimento.leadCompass.leads.exception.LeadNotFoundException;
import com.jvictornascimento.leadCompass.leads.mapper.LeadMapper;
import com.jvictornascimento.leadCompass.leads.model.Lead;
import com.jvictornascimento.leadCompass.leads.model.LeadStatus;
import com.jvictornascimento.leadCompass.leads.repository.LeadRepository;

import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
public class LeadService {

	private final LeadRepository leadRepository;
	private final LeadMapper leadMapper;
	private final InteractionRepository interactionRepository;

	public LeadService(
			LeadRepository leadRepository,
			LeadMapper leadMapper,
			InteractionRepository interactionRepository) {
		this.leadRepository = leadRepository;
		this.leadMapper = leadMapper;
		this.interactionRepository = interactionRepository;
	}

	@Transactional(readOnly = true)
	public List<LeadResponse> listLeads(
			String city,
			String niche,
			LeadStatus status,
			Boolean withoutWebsite,
			Integer minScore) {
		return leadRepository.findAll(filterBy(city, niche, status, withoutWebsite, minScore)).stream()
				.map(leadMapper::toResponse)
				.toList();
	}

	@Transactional(readOnly = true)
	public LeadDetailResponse getLead(Long id) {
		return leadRepository.findById(id)
				.map(leadMapper::toDetailResponse)
				.orElseThrow(() -> new LeadNotFoundException(id));
	}

	@Transactional
	public LeadResponse updateStatus(Long id, UpdateLeadStatusRequest request) {
		Lead lead = leadRepository.findById(id)
				.orElseThrow(() -> new LeadNotFoundException(id));
		LeadStatus previousStatus = lead.getStatus();

		lead.setStatus(request.status());

		Interaction interaction = new Interaction();
		interaction.setLead(lead);
		interaction.setType(InteractionType.STATUS_CHANGED);
		interaction.setDescription("Status changed from " + previousStatus + " to " + request.status());
		interactionRepository.save(interaction);

		return leadMapper.toResponse(lead);
	}

	private Specification<Lead> filterBy(
			String city,
			String niche,
			LeadStatus status,
			Boolean withoutWebsite,
			Integer minScore) {
		return (root, query, criteriaBuilder) -> {
			Predicate predicate = criteriaBuilder.conjunction();

			if (StringUtils.hasText(city)) {
				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.equal(criteriaBuilder.lower(root.get("city")), city.toLowerCase()));
			}

			if (StringUtils.hasText(niche)) {
				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.equal(criteriaBuilder.lower(root.get("niche")), niche.toLowerCase()));
			}

			if (status != null) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("status"), status));
			}

			if (Boolean.TRUE.equals(withoutWebsite)) {
				predicate = criteriaBuilder.and(predicate, criteriaBuilder.isFalse(root.get("hasWebsite")));
			}

			if (minScore != null) {
				predicate = criteriaBuilder.and(
						predicate,
						criteriaBuilder.greaterThanOrEqualTo(root.get("opportunityScore"), minScore));
			}

			return predicate;
		};
	}
}
