package com.invoiceapp.trader.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TraderRequestDto {

    @NotBlank
    @Size(max = 200)
    private String code;

    @NotBlank
    @Size(max = 200)
    private String name;

    @Size(max = 50)
    private String phone;

    @Size(max = 500)
    private String address;

    @Size(max = 500)
    private String city;

    @Size(max = 500)
    private String zip;

    @NotNull
    private Long countryId;

    // --- Getters / Setters ---

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
}
