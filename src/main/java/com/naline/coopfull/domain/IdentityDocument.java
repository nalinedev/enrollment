package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naline.coopfull.domain.enumeration.DocumentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A IdentityDocument.
 */
@Entity
@Table(name = "identity_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IdentityDocument implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "document_type", nullable = false)
    private String documentType;

    @NotNull
    @Column(name = "document_number", nullable = false)
    private String documentNumber;

    @Column(name = "issue_date")
    private LocalDate issueDate;

    @Column(name = "expiry_date")
    private LocalDate expiryDate;

    @Column(name = "issuing_authority")
    private String issuingAuthority;

    @Column(name = "issuing_country")
    private String issuingCountry;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DocumentStatus status;

    @NotNull
    @Column(name = "verified", nullable = false)
    private Boolean verified;

    @Column(name = "verification_date")
    private Instant verificationDate;

    @Column(name = "verification_comment")
    private String verificationComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "individualMember", "organizationMember", "socialProfile", "professionalProfile", "cooperative", "branch" },
        allowSetters = true
    )
    private Member member;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public IdentityDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentType() {
        return this.documentType;
    }

    public IdentityDocument documentType(String documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return this.documentNumber;
    }

    public IdentityDocument documentNumber(String documentNumber) {
        this.setDocumentNumber(documentNumber);
        return this;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public LocalDate getIssueDate() {
        return this.issueDate;
    }

    public IdentityDocument issueDate(LocalDate issueDate) {
        this.setIssueDate(issueDate);
        return this;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return this.expiryDate;
    }

    public IdentityDocument expiryDate(LocalDate expiryDate) {
        this.setExpiryDate(expiryDate);
        return this;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getIssuingAuthority() {
        return this.issuingAuthority;
    }

    public IdentityDocument issuingAuthority(String issuingAuthority) {
        this.setIssuingAuthority(issuingAuthority);
        return this;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public String getIssuingCountry() {
        return this.issuingCountry;
    }

    public IdentityDocument issuingCountry(String issuingCountry) {
        this.setIssuingCountry(issuingCountry);
        return this;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public DocumentStatus getStatus() {
        return this.status;
    }

    public IdentityDocument status(DocumentStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public Boolean getVerified() {
        return this.verified;
    }

    public IdentityDocument verified(Boolean verified) {
        this.setVerified(verified);
        return this;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Instant getVerificationDate() {
        return this.verificationDate;
    }

    public IdentityDocument verificationDate(Instant verificationDate) {
        this.setVerificationDate(verificationDate);
        return this;
    }

    public void setVerificationDate(Instant verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getVerificationComment() {
        return this.verificationComment;
    }

    public IdentityDocument verificationComment(String verificationComment) {
        this.setVerificationComment(verificationComment);
        return this;
    }

    public void setVerificationComment(String verificationComment) {
        this.verificationComment = verificationComment;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public IdentityDocument member(Member member) {
        this.setMember(member);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdentityDocument)) {
            return false;
        }
        return getId() != null && getId().equals(((IdentityDocument) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdentityDocument{" +
            "id=" + getId() +
            ", documentType='" + getDocumentType() + "'" +
            ", documentNumber='" + getDocumentNumber() + "'" +
            ", issueDate='" + getIssueDate() + "'" +
            ", expiryDate='" + getExpiryDate() + "'" +
            ", issuingAuthority='" + getIssuingAuthority() + "'" +
            ", issuingCountry='" + getIssuingCountry() + "'" +
            ", status='" + getStatus() + "'" +
            ", verified='" + getVerified() + "'" +
            ", verificationDate='" + getVerificationDate() + "'" +
            ", verificationComment='" + getVerificationComment() + "'" +
            "}";
    }
}
