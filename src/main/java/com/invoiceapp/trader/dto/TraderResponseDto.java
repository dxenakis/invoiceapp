package com.invoiceapp.trader.dto;

import com.invoiceapp.global.TraderDomain;
import com.invoiceapp.trader.Trader;

import java.time.LocalDateTime;

public class TraderResponseDto {

    private Long id;
    private String code;
    private String name;
    private String phone;
    private String address;
    private String city;
    private String zip;

    private Long countryId;
    private String countryName;

    private TraderDomain domain;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // --- Getters / Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public Long getCountryId() {
        return countryId;
    }

    public void setCountryId(Long countryId) {
        this.countryId = countryId;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public TraderDomain getDomain() {
        return domain;
    }

    public void setDomain(TraderDomain domain) {
        this.domain = domain;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    // --- Mapping helper ---

    public static TraderResponseDto fromEntity(Trader trader) {
        TraderResponseDto dto = new TraderResponseDto();
        dto.setId(trader.getId());
        dto.setCode(trader.getCode());
        dto.setName(trader.getName());
        dto.setPhone(trader.getPhone());
        dto.setAddress(trader.getAddress());
        dto.setCity(trader.getCity());
        dto.setZip(trader.getZip());

        if (trader.getCountry() != null) {
            dto.setCountryId(trader.getCountry().getId());
            dto.setCountryName(trader.getCountry().getName());
        }

        dto.setDomain(trader.getTraderDomain());
        dto.setCreatedAt(trader.getCreatedAt());
        dto.setUpdatedAt(trader.getUpdatedAt());

        return dto;
    }
}
