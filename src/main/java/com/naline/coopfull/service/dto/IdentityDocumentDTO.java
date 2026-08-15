package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.DocumentStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.IdentityDocument} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IdentityDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private String documentType;

    @NotNull
    private String documentNumber;

    private LocalDate issueDate;

    private LocalDate expiryDate;

    private String issuingAuthority;

    private String issuingCountry;

    @NotNull
    private DocumentStatus status;

    @NotNull
    private Boolean verified;

    private Instant verificationDate;

    private String verificationComment;

    private MemberDTO member;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDocumentType() {
        return documentType;
    }

    public void setDocumentType(String documentType) {
        this.documentType = documentType;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getIssuingAuthority() {
        return issuingAuthority;
    }

    public void setIssuingAuthority(String issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public String getIssuingCountry() {
        return issuingCountry;
    }

    public void setIssuingCountry(String issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public Boolean getVerified() {
        return verified;
    }

    public void setVerified(Boolean verified) {
        this.verified = verified;
    }

    public Instant getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(Instant verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getVerificationComment() {
        return verificationComment;
    }

    public void setVerificationComment(String verificationComment) {
        this.verificationComment = verificationComment;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof IdentityDocumentDTO)) {
            return false;
        }

        IdentityDocumentDTO identityDocumentDTO = (IdentityDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, identityDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdentityDocumentDTO{" +
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
            ", member=" + getMember() +
            "}";
    }
}
