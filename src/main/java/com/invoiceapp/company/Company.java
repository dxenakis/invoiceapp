package com.invoiceapp.company;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Entity
@Table(
    name = "companies",
    uniqueConstraints = @UniqueConstraint(name = "uk_company_vat", columnNames = "afm")
)
public class Company {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Size(min = 9, max = 15)
    @Column(name = "afm", nullable = false, length = 15)
    private String afm;

    @NotBlank
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "address_line") private String addressLine;
    @Column(name = "city") private String city;
    @Column(name = "postal_code", length = 20) private String postalCode;
    @Column(name = "country_code", length = 5) private String countryCode = "GR";
    @Column(name = "email") private String email;
    @Column(name = "phone", length = 50) private String phone;

    public Long getId() { return id; }
    public String getAfm() { return afm; }
    public void setAfm(String afm) { this.afm = afm; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddressLine() { return addressLine; }
    public void setAddressLine(String addressLine) { this.addressLine = addressLine; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getCountryCode() { return countryCode; }
    public void setCountryCode(String countryCode) { this.countryCode = countryCode; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Company)) return false;
        Company other = (Company) o;
        return id != null && id.equals(other.id);
    }
    @Override
    public int hashCode() { return getClass().hashCode(); }

    @Override
    public String toString() {
        return "Company{id=%d, vat='%s', name='%s'}".formatted(id, afm, name);
    }
}
