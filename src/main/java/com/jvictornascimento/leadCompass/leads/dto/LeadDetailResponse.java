package com.jvictornascimento.leadCompass.leads.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.jvictornascimento.leadCompass.leads.model.LeadStatus;

public record LeadDetailResponse(
		Long id,
		String businessName,
		String niche,
		String city,
		String state,
		String address,
		String phone,
		String whatsapp,
		String website,
		String instagram,
		String googleMapsUrl,
		String googlePlaceId,
		BigDecimal rating,
		Integer reviewCount,
		boolean hasWebsite,
		boolean hasHttps,
		boolean responsive,
		boolean siteSlow,
		Integer opportunityScore,
		LeadStatus status,
		boolean duplicate,
		List<Object> diagnostics,
		List<Object> interactions,
		LocalDateTime createdAt,
		LocalDateTime updatedAt) {
}
