package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.DocumentStatus;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.IdentityDocument} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.IdentityDocumentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /identity-documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class IdentityDocumentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering DocumentStatus
     */
    public static class DocumentStatusFilter extends Filter<DocumentStatus> {

        public DocumentStatusFilter() {}

        public DocumentStatusFilter(DocumentStatusFilter filter) {
            super(filter);
        }

        @Override
        public DocumentStatusFilter copy() {
            return new DocumentStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter documentType;

    private StringFilter documentNumber;

    private LocalDateFilter issueDate;

    private LocalDateFilter expiryDate;

    private StringFilter issuingAuthority;

    private StringFilter issuingCountry;

    private DocumentStatusFilter status;

    private BooleanFilter verified;

    private InstantFilter verificationDate;

    private StringFilter verificationComment;

    private LongFilter memberId;

    private Boolean distinct;

    public IdentityDocumentCriteria() {}

    public IdentityDocumentCriteria(IdentityDocumentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentType = other.optionalDocumentType().map(StringFilter::copy).orElse(null);
        this.documentNumber = other.optionalDocumentNumber().map(StringFilter::copy).orElse(null);
        this.issueDate = other.optionalIssueDate().map(LocalDateFilter::copy).orElse(null);
        this.expiryDate = other.optionalExpiryDate().map(LocalDateFilter::copy).orElse(null);
        this.issuingAuthority = other.optionalIssuingAuthority().map(StringFilter::copy).orElse(null);
        this.issuingCountry = other.optionalIssuingCountry().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(DocumentStatusFilter::copy).orElse(null);
        this.verified = other.optionalVerified().map(BooleanFilter::copy).orElse(null);
        this.verificationDate = other.optionalVerificationDate().map(InstantFilter::copy).orElse(null);
        this.verificationComment = other.optionalVerificationComment().map(StringFilter::copy).orElse(null);
        this.memberId = other.optionalMemberId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public IdentityDocumentCriteria copy() {
        return new IdentityDocumentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getDocumentType() {
        return documentType;
    }

    public Optional<StringFilter> optionalDocumentType() {
        return Optional.ofNullable(documentType);
    }

    public StringFilter documentType() {
        if (documentType == null) {
            setDocumentType(new StringFilter());
        }
        return documentType;
    }

    public void setDocumentType(StringFilter documentType) {
        this.documentType = documentType;
    }

    public StringFilter getDocumentNumber() {
        return documentNumber;
    }

    public Optional<StringFilter> optionalDocumentNumber() {
        return Optional.ofNullable(documentNumber);
    }

    public StringFilter documentNumber() {
        if (documentNumber == null) {
            setDocumentNumber(new StringFilter());
        }
        return documentNumber;
    }

    public void setDocumentNumber(StringFilter documentNumber) {
        this.documentNumber = documentNumber;
    }

    public LocalDateFilter getIssueDate() {
        return issueDate;
    }

    public Optional<LocalDateFilter> optionalIssueDate() {
        return Optional.ofNullable(issueDate);
    }

    public LocalDateFilter issueDate() {
        if (issueDate == null) {
            setIssueDate(new LocalDateFilter());
        }
        return issueDate;
    }

    public void setIssueDate(LocalDateFilter issueDate) {
        this.issueDate = issueDate;
    }

    public LocalDateFilter getExpiryDate() {
        return expiryDate;
    }

    public Optional<LocalDateFilter> optionalExpiryDate() {
        return Optional.ofNullable(expiryDate);
    }

    public LocalDateFilter expiryDate() {
        if (expiryDate == null) {
            setExpiryDate(new LocalDateFilter());
        }
        return expiryDate;
    }

    public void setExpiryDate(LocalDateFilter expiryDate) {
        this.expiryDate = expiryDate;
    }

    public StringFilter getIssuingAuthority() {
        return issuingAuthority;
    }

    public Optional<StringFilter> optionalIssuingAuthority() {
        return Optional.ofNullable(issuingAuthority);
    }

    public StringFilter issuingAuthority() {
        if (issuingAuthority == null) {
            setIssuingAuthority(new StringFilter());
        }
        return issuingAuthority;
    }

    public void setIssuingAuthority(StringFilter issuingAuthority) {
        this.issuingAuthority = issuingAuthority;
    }

    public StringFilter getIssuingCountry() {
        return issuingCountry;
    }

    public Optional<StringFilter> optionalIssuingCountry() {
        return Optional.ofNullable(issuingCountry);
    }

    public StringFilter issuingCountry() {
        if (issuingCountry == null) {
            setIssuingCountry(new StringFilter());
        }
        return issuingCountry;
    }

    public void setIssuingCountry(StringFilter issuingCountry) {
        this.issuingCountry = issuingCountry;
    }

    public DocumentStatusFilter getStatus() {
        return status;
    }

    public Optional<DocumentStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public DocumentStatusFilter status() {
        if (status == null) {
            setStatus(new DocumentStatusFilter());
        }
        return status;
    }

    public void setStatus(DocumentStatusFilter status) {
        this.status = status;
    }

    public BooleanFilter getVerified() {
        return verified;
    }

    public Optional<BooleanFilter> optionalVerified() {
        return Optional.ofNullable(verified);
    }

    public BooleanFilter verified() {
        if (verified == null) {
            setVerified(new BooleanFilter());
        }
        return verified;
    }

    public void setVerified(BooleanFilter verified) {
        this.verified = verified;
    }

    public InstantFilter getVerificationDate() {
        return verificationDate;
    }

    public Optional<InstantFilter> optionalVerificationDate() {
        return Optional.ofNullable(verificationDate);
    }

    public InstantFilter verificationDate() {
        if (verificationDate == null) {
            setVerificationDate(new InstantFilter());
        }
        return verificationDate;
    }

    public void setVerificationDate(InstantFilter verificationDate) {
        this.verificationDate = verificationDate;
    }

    public StringFilter getVerificationComment() {
        return verificationComment;
    }

    public Optional<StringFilter> optionalVerificationComment() {
        return Optional.ofNullable(verificationComment);
    }

    public StringFilter verificationComment() {
        if (verificationComment == null) {
            setVerificationComment(new StringFilter());
        }
        return verificationComment;
    }

    public void setVerificationComment(StringFilter verificationComment) {
        this.verificationComment = verificationComment;
    }

    public LongFilter getMemberId() {
        return memberId;
    }

    public Optional<LongFilter> optionalMemberId() {
        return Optional.ofNullable(memberId);
    }

    public LongFilter memberId() {
        if (memberId == null) {
            setMemberId(new LongFilter());
        }
        return memberId;
    }

    public void setMemberId(LongFilter memberId) {
        this.memberId = memberId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final IdentityDocumentCriteria that = (IdentityDocumentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentType, that.documentType) &&
            Objects.equals(documentNumber, that.documentNumber) &&
            Objects.equals(issueDate, that.issueDate) &&
            Objects.equals(expiryDate, that.expiryDate) &&
            Objects.equals(issuingAuthority, that.issuingAuthority) &&
            Objects.equals(issuingCountry, that.issuingCountry) &&
            Objects.equals(status, that.status) &&
            Objects.equals(verified, that.verified) &&
            Objects.equals(verificationDate, that.verificationDate) &&
            Objects.equals(verificationComment, that.verificationComment) &&
            Objects.equals(memberId, that.memberId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentType,
            documentNumber,
            issueDate,
            expiryDate,
            issuingAuthority,
            issuingCountry,
            status,
            verified,
            verificationDate,
            verificationComment,
            memberId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "IdentityDocumentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentType().map(f -> "documentType=" + f + ", ").orElse("") +
            optionalDocumentNumber().map(f -> "documentNumber=" + f + ", ").orElse("") +
            optionalIssueDate().map(f -> "issueDate=" + f + ", ").orElse("") +
            optionalExpiryDate().map(f -> "expiryDate=" + f + ", ").orElse("") +
            optionalIssuingAuthority().map(f -> "issuingAuthority=" + f + ", ").orElse("") +
            optionalIssuingCountry().map(f -> "issuingCountry=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalVerified().map(f -> "verified=" + f + ", ").orElse("") +
            optionalVerificationDate().map(f -> "verificationDate=" + f + ", ").orElse("") +
            optionalVerificationComment().map(f -> "verificationComment=" + f + ", ").orElse("") +
            optionalMemberId().map(f -> "memberId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
