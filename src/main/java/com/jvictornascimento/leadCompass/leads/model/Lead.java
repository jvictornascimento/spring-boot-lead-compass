package com.jvictornascimento.leadCompass.leads.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;

@Entity
@Table(name = "leads")
public class Lead {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 160)
	private String businessName;

	@Column(nullable = false, length = 80)
	private String niche;

	@Column(nullable = false, length = 120)
	private String city;

	@Column(nullable = false, length = 2)
	private String state;

	private String address;

	@Column(length = 30)
	private String phone;

	@Column(length = 30)
	private String whatsapp;

	private String website;

	private String instagram;

	private String googleMapsUrl;

	@Column(length = 160)
	private String googlePlaceId;

	@Column(precision = 3, scale = 2)
	private BigDecimal rating;

	private Integer reviewCount;

	private boolean hasWebsite;

	private boolean hasHttps;

	private boolean responsive;

	private boolean siteSlow;

	private Integer opportunityScore;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false, length = 40)
	private LeadStatus status = LeadStatus.NEW;

	@Column(name = "is_duplicate", nullable = false)
	private boolean duplicate;

	@Enumerated(EnumType.STRING)
	@Column(length = 40)
	private LostReason lostReason;

	@Column(nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(nullable = false)
	private LocalDateTime updatedAt;

	@PrePersist
	void prePersist() {
		LocalDateTime now = LocalDateTime.now();
		createdAt = now;
		updatedAt = now;
	}

	@PreUpdate
	void preUpdate() {
		updatedAt = LocalDateTime.now();
	}

	public Long getId() {
		return id;
	}

	public String getBusinessName() {
		return businessName;
	}

	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	public String getNiche() {
		return niche;
	}

	public void setNiche(String niche) {
		this.niche = niche;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getWhatsapp() {
		return whatsapp;
	}

	public void setWhatsapp(String whatsapp) {
		this.whatsapp = whatsapp;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public String getInstagram() {
		return instagram;
	}

	public void setInstagram(String instagram) {
		this.instagram = instagram;
	}

	public String getGoogleMapsUrl() {
		return googleMapsUrl;
	}

	public void setGoogleMapsUrl(String googleMapsUrl) {
		this.googleMapsUrl = googleMapsUrl;
	}

	public String getGooglePlaceId() {
		return googlePlaceId;
	}

	public void setGooglePlaceId(String googlePlaceId) {
		this.googlePlaceId = googlePlaceId;
	}

	public BigDecimal getRating() {
		return rating;
	}

	public void setRating(BigDecimal rating) {
		this.rating = rating;
	}

	public Integer getReviewCount() {
		return reviewCount;
	}

	public void setReviewCount(Integer reviewCount) {
		this.reviewCount = reviewCount;
	}

	public boolean isHasWebsite() {
		return hasWebsite;
	}

	public void setHasWebsite(boolean hasWebsite) {
		this.hasWebsite = hasWebsite;
	}

	public boolean isHasHttps() {
		return hasHttps;
	}

	public void setHasHttps(boolean hasHttps) {
		this.hasHttps = hasHttps;
	}

	public boolean isResponsive() {
		return responsive;
	}

	public void setResponsive(boolean responsive) {
		this.responsive = responsive;
	}

	public boolean isSiteSlow() {
		return siteSlow;
	}

	public void setSiteSlow(boolean siteSlow) {
		this.siteSlow = siteSlow;
	}

	public Integer getOpportunityScore() {
		return opportunityScore;
	}

	public void setOpportunityScore(Integer opportunityScore) {
		this.opportunityScore = opportunityScore;
	}

	public LeadStatus getStatus() {
		return status;
	}

	public void setStatus(LeadStatus status) {
		this.status = status;
	}

	public boolean isDuplicate() {
		return duplicate;
	}

	public void setDuplicate(boolean duplicate) {
		this.duplicate = duplicate;
	}

	public LostReason getLostReason() {
		return lostReason;
	}

	public void setLostReason(LostReason lostReason) {
		this.lostReason = lostReason;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}
}
