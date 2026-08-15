package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A OrganizationMember.
 */
@Entity
@Table(name = "organization_member")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OrganizationMember implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "legal_name", nullable = false)
    private String legalName;

    @Column(name = "trade_name")
    private String tradeName;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "tax_number")
    private String taxNumber;

    @Column(name = "legal_form")
    private String legalForm;

    @Column(name = "registration_date")
    private LocalDate registrationDate;

    @Column(name = "email")
    private String email;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "website")
    private String website;

    @Lob
    @Column(name = "description")
    private String description;

    @JsonIgnoreProperties(
        value = { "individualMember", "organizationMember", "socialProfile", "professionalProfile", "cooperative", "branch" },
        allowSetters = true
    )
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "organizationMember")
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public OrganizationMember id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLegalName() {
        return this.legalName;
    }

    public OrganizationMember legalName(String legalName) {
        this.setLegalName(legalName);
        return this;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public String getTradeName() {
        return this.tradeName;
    }

    public OrganizationMember tradeName(String tradeName) {
        this.setTradeName(tradeName);
        return this;
    }

    public void setTradeName(String tradeName) {
        this.tradeName = tradeName;
    }

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

    public OrganizationMember registrationNumber(String registrationNumber) {
        this.setRegistrationNumber(registrationNumber);
        return this;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public String getTaxNumber() {
        return this.taxNumber;
    }

    public OrganizationMember taxNumber(String taxNumber) {
        this.setTaxNumber(taxNumber);
        return this;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getLegalForm() {
        return this.legalForm;
    }

    public OrganizationMember legalForm(String legalForm) {
        this.setLegalForm(legalForm);
        return this;
    }

    public void setLegalForm(String legalForm) {
        this.legalForm = legalForm;
    }

    public LocalDate getRegistrationDate() {
        return this.registrationDate;
    }

    public OrganizationMember registrationDate(LocalDate registrationDate) {
        this.setRegistrationDate(registrationDate);
        return this;
    }

    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getEmail() {
        return this.email;
    }

    public OrganizationMember email(String email) {
        this.setEmail(email);
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public OrganizationMember phoneNumber(String phoneNumber) {
        this.setPhoneNumber(phoneNumber);
        return this;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getWebsite() {
        return this.website;
    }

    public OrganizationMember website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getDescription() {
        return this.description;
    }

    public OrganizationMember description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        if (this.member != null) {
            this.member.setOrganizationMember(null);
        }
        if (member != null) {
            member.setOrganizationMember(this);
        }
        this.member = member;
    }

    public OrganizationMember member(Member member) {
        this.setMember(member);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrganizationMember)) {
            return false;
        }
        return getId() != null && getId().equals(((OrganizationMember) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrganizationMember{" +
            "id=" + getId() +
            ", legalName='" + getLegalName() + "'" +
            ", tradeName='" + getTradeName() + "'" +
            ", registrationNumber='" + getRegistrationNumber() + "'" +
            ", taxNumber='" + getTaxNumber() + "'" +
            ", legalForm='" + getLegalForm() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            ", email='" + getEmail() + "'" +
            ", phoneNumber='" + getPhoneNumber() + "'" +
            ", website='" + getWebsite() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
