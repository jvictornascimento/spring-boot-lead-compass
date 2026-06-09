package com.jvictornascimento.leadCompass.interactions.repository;

import com.jvictornascimento.leadCompass.interactions.model.Interaction;

import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractionRepository extends JpaRepository<Interaction, Long> {
}
