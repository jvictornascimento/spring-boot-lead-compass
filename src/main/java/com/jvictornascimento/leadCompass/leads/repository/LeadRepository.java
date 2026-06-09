package com.jvictornascimento.leadCompass.leads.repository;

import com.jvictornascimento.leadCompass.leads.model.Lead;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface LeadRepository extends JpaRepository<Lead, Long>, JpaSpecificationExecutor<Lead> {
}
