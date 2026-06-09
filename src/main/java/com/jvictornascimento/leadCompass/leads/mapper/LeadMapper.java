package com.jvictornascimento.leadCompass.leads.mapper;

import java.util.List;

import com.jvictornascimento.leadCompass.leads.dto.LeadDetailResponse;
import com.jvictornascimento.leadCompass.leads.dto.LeadResponse;
import com.jvictornascimento.leadCompass.leads.model.Lead;

import org.springframework.stereotype.Component;

@Component
public class LeadMapper {

	public LeadResponse toResponse(Lead lead) {
		return new LeadResponse(
				lead.getId(),
				lead.getBusinessName(),
				lead.getNiche(),
				lead.getCity(),
				lead.getState(),
				lead.getPhone(),
				lead.getWhatsapp(),
				lead.getWebsite(),
				lead.getInstagram(),
				lead.getGoogleMapsUrl(),
				lead.getRating(),
				lead.getReviewCount(),
				lead.isHasWebsite(),
				lead.getOpportunityScore(),
				lead.getStatus(),
				lead.isDuplicate(),
				lead.getCreatedAt(),
				lead.getUpdatedAt());
	}

	public LeadDetailResponse toDetailResponse(Lead lead) {
		return new LeadDetailResponse(
				lead.getId(),
				lead.getBusinessName(),
				lead.getNiche(),
				lead.getCity(),
				lead.getState(),
				lead.getAddress(),
				lead.getPhone(),
				lead.getWhatsapp(),
				lead.getWebsite(),
				lead.getInstagram(),
				lead.getGoogleMapsUrl(),
				lead.getGooglePlaceId(),
				lead.getRating(),
				lead.getReviewCount(),
				lead.isHasWebsite(),
				lead.isHasHttps(),
				lead.isResponsive(),
				lead.isSiteSlow(),
				lead.getOpportunityScore(),
				lead.getStatus(),
				lead.isDuplicate(),
				List.of(),
				List.of(),
				lead.getCreatedAt(),
				lead.getUpdatedAt());
	}
}
