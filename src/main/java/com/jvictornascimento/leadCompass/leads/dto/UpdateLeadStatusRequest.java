package com.jvictornascimento.leadCompass.leads.dto;

import com.jvictornascimento.leadCompass.leads.model.LeadStatus;

import jakarta.validation.constraints.NotNull;

public record UpdateLeadStatusRequest(@NotNull LeadStatus status) {
}
