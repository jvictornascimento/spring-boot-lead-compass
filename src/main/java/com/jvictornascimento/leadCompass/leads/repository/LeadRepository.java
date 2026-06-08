package com.jvictornascimento.leadCompass.leads.repository;

import com.jvictornascimento.leadCompass.leads.model.Lead;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LeadRepository extends JpaRepository<Lead, Long> {
}
