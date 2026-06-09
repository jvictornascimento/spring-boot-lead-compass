package com.jvictornascimento.leadCompass.leads.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.jvictornascimento.leadCompass.leads.model.LeadStatus;

public record LeadResponse(
		Long id,
		String businessName,
		String niche,
		String city,
		String state,
		String phone,
		String whatsapp,
		String website,
		String instagram,
		String googleMapsUrl,
		BigDecimal rating,
		Integer reviewCount,
		boolean hasWebsite,
		Integer opportunityScore,
		LeadStatus status,
		boolean duplicate,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
}
