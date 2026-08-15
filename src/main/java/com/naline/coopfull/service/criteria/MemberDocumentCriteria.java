package com.naline.coopfull.service.criteria;

import com.naline.coopfull.domain.enumeration.DocumentVerificationStatus;
import com.naline.coopfull.domain.enumeration.MemberDocumentType;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.naline.coopfull.domain.MemberDocument} entity. This class is used
 * in {@link com.naline.coopfull.web.rest.MemberDocumentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /member-documents?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MemberDocumentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering MemberDocumentType
     */
    public static class MemberDocumentTypeFilter extends Filter<MemberDocumentType> {

        public MemberDocumentTypeFilter() {}

        public MemberDocumentTypeFilter(MemberDocumentTypeFilter filter) {
            super(filter);
        }

        @Override
        public MemberDocumentTypeFilter copy() {
            return new MemberDocumentTypeFilter(this);
        }
    }

    /**
     * Class for filtering DocumentVerificationStatus
     */
    public static class DocumentVerificationStatusFilter extends Filter<DocumentVerificationStatus> {

        public DocumentVerificationStatusFilter() {}

        public DocumentVerificationStatusFilter(DocumentVerificationStatusFilter filter) {
            super(filter);
        }

        @Override
        public DocumentVerificationStatusFilter copy() {
            return new DocumentVerificationStatusFilter(this);
        }
    }

    @Serial
    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private MemberDocumentTypeFilter documentType;

    private StringFilter originalFileName;

    private StringFilter storedFileName;

    private StringFilter contentType;

    private LongFilter fileSize;

    private StringFilter storagePath;

    private StringFilter checksum;

    private DocumentVerificationStatusFilter verificationStatus;

    private InstantFilter uploadedAt;

    private InstantFilter verifiedAt;

    private LongFilter memberId;

    private LongFilter uploadedById;

    private Boolean distinct;

    public MemberDocumentCriteria() {}

    public MemberDocumentCriteria(MemberDocumentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.documentType = other.optionalDocumentType().map(MemberDocumentTypeFilter::copy).orElse(null);
        this.originalFileName = other.optionalOriginalFileName().map(StringFilter::copy).orElse(null);
        this.storedFileName = other.optionalStoredFileName().map(StringFilter::copy).orElse(null);
        this.contentType = other.optionalContentType().map(StringFilter::copy).orElse(null);
        this.fileSize = other.optionalFileSize().map(LongFilter::copy).orElse(null);
        this.storagePath = other.optionalStoragePath().map(StringFilter::copy).orElse(null);
        this.checksum = other.optionalChecksum().map(StringFilter::copy).orElse(null);
        this.verificationStatus = other.optionalVerificationStatus().map(DocumentVerificationStatusFilter::copy).orElse(null);
        this.uploadedAt = other.optionalUploadedAt().map(InstantFilter::copy).orElse(null);
        this.verifiedAt = other.optionalVerifiedAt().map(InstantFilter::copy).orElse(null);
        this.memberId = other.optionalMemberId().map(LongFilter::copy).orElse(null);
        this.uploadedById = other.optionalUploadedById().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public MemberDocumentCriteria copy() {
        return new MemberDocumentCriteria(this);
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

    public MemberDocumentTypeFilter getDocumentType() {
        return documentType;
    }

    public Optional<MemberDocumentTypeFilter> optionalDocumentType() {
        return Optional.ofNullable(documentType);
    }

    public MemberDocumentTypeFilter documentType() {
        if (documentType == null) {
            setDocumentType(new MemberDocumentTypeFilter());
        }
        return documentType;
    }

    public void setDocumentType(MemberDocumentTypeFilter documentType) {
        this.documentType = documentType;
    }

    public StringFilter getOriginalFileName() {
        return originalFileName;
    }

    public Optional<StringFilter> optionalOriginalFileName() {
        return Optional.ofNullable(originalFileName);
    }

    public StringFilter originalFileName() {
        if (originalFileName == null) {
            setOriginalFileName(new StringFilter());
        }
        return originalFileName;
    }

    public void setOriginalFileName(StringFilter originalFileName) {
        this.originalFileName = originalFileName;
    }

    public StringFilter getStoredFileName() {
        return storedFileName;
    }

    public Optional<StringFilter> optionalStoredFileName() {
        return Optional.ofNullable(storedFileName);
    }

    public StringFilter storedFileName() {
        if (storedFileName == null) {
            setStoredFileName(new StringFilter());
        }
        return storedFileName;
    }

    public void setStoredFileName(StringFilter storedFileName) {
        this.storedFileName = storedFileName;
    }

    public StringFilter getContentType() {
        return contentType;
    }

    public Optional<StringFilter> optionalContentType() {
        return Optional.ofNullable(contentType);
    }

    public StringFilter contentType() {
        if (contentType == null) {
            setContentType(new StringFilter());
        }
        return contentType;
    }

    public void setContentType(StringFilter contentType) {
        this.contentType = contentType;
    }

    public LongFilter getFileSize() {
        return fileSize;
    }

    public Optional<LongFilter> optionalFileSize() {
        return Optional.ofNullable(fileSize);
    }

    public LongFilter fileSize() {
        if (fileSize == null) {
            setFileSize(new LongFilter());
        }
        return fileSize;
    }

    public void setFileSize(LongFilter fileSize) {
        this.fileSize = fileSize;
    }

    public StringFilter getStoragePath() {
        return storagePath;
    }

    public Optional<StringFilter> optionalStoragePath() {
        return Optional.ofNullable(storagePath);
    }

    public StringFilter storagePath() {
        if (storagePath == null) {
            setStoragePath(new StringFilter());
        }
        return storagePath;
    }

    public void setStoragePath(StringFilter storagePath) {
        this.storagePath = storagePath;
    }

    public StringFilter getChecksum() {
        return checksum;
    }

    public Optional<StringFilter> optionalChecksum() {
        return Optional.ofNullable(checksum);
    }

    public StringFilter checksum() {
        if (checksum == null) {
            setChecksum(new StringFilter());
        }
        return checksum;
    }

    public void setChecksum(StringFilter checksum) {
        this.checksum = checksum;
    }

    public DocumentVerificationStatusFilter getVerificationStatus() {
        return verificationStatus;
    }

    public Optional<DocumentVerificationStatusFilter> optionalVerificationStatus() {
        return Optional.ofNullable(verificationStatus);
    }

    public DocumentVerificationStatusFilter verificationStatus() {
        if (verificationStatus == null) {
            setVerificationStatus(new DocumentVerificationStatusFilter());
        }
        return verificationStatus;
    }

    public void setVerificationStatus(DocumentVerificationStatusFilter verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public InstantFilter getUploadedAt() {
        return uploadedAt;
    }

    public Optional<InstantFilter> optionalUploadedAt() {
        return Optional.ofNullable(uploadedAt);
    }

    public InstantFilter uploadedAt() {
        if (uploadedAt == null) {
            setUploadedAt(new InstantFilter());
        }
        return uploadedAt;
    }

    public void setUploadedAt(InstantFilter uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public InstantFilter getVerifiedAt() {
        return verifiedAt;
    }

    public Optional<InstantFilter> optionalVerifiedAt() {
        return Optional.ofNullable(verifiedAt);
    }

    public InstantFilter verifiedAt() {
        if (verifiedAt == null) {
            setVerifiedAt(new InstantFilter());
        }
        return verifiedAt;
    }

    public void setVerifiedAt(InstantFilter verifiedAt) {
        this.verifiedAt = verifiedAt;
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

    public LongFilter getUploadedById() {
        return uploadedById;
    }

    public Optional<LongFilter> optionalUploadedById() {
        return Optional.ofNullable(uploadedById);
    }

    public LongFilter uploadedById() {
        if (uploadedById == null) {
            setUploadedById(new LongFilter());
        }
        return uploadedById;
    }

    public void setUploadedById(LongFilter uploadedById) {
        this.uploadedById = uploadedById;
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
        final MemberDocumentCriteria that = (MemberDocumentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(documentType, that.documentType) &&
            Objects.equals(originalFileName, that.originalFileName) &&
            Objects.equals(storedFileName, that.storedFileName) &&
            Objects.equals(contentType, that.contentType) &&
            Objects.equals(fileSize, that.fileSize) &&
            Objects.equals(storagePath, that.storagePath) &&
            Objects.equals(checksum, that.checksum) &&
            Objects.equals(verificationStatus, that.verificationStatus) &&
            Objects.equals(uploadedAt, that.uploadedAt) &&
            Objects.equals(verifiedAt, that.verifiedAt) &&
            Objects.equals(memberId, that.memberId) &&
            Objects.equals(uploadedById, that.uploadedById) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            documentType,
            originalFileName,
            storedFileName,
            contentType,
            fileSize,
            storagePath,
            checksum,
            verificationStatus,
            uploadedAt,
            verifiedAt,
            memberId,
            uploadedById,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberDocumentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalDocumentType().map(f -> "documentType=" + f + ", ").orElse("") +
            optionalOriginalFileName().map(f -> "originalFileName=" + f + ", ").orElse("") +
            optionalStoredFileName().map(f -> "storedFileName=" + f + ", ").orElse("") +
            optionalContentType().map(f -> "contentType=" + f + ", ").orElse("") +
            optionalFileSize().map(f -> "fileSize=" + f + ", ").orElse("") +
            optionalStoragePath().map(f -> "storagePath=" + f + ", ").orElse("") +
            optionalChecksum().map(f -> "checksum=" + f + ", ").orElse("") +
            optionalVerificationStatus().map(f -> "verificationStatus=" + f + ", ").orElse("") +
            optionalUploadedAt().map(f -> "uploadedAt=" + f + ", ").orElse("") +
            optionalVerifiedAt().map(f -> "verifiedAt=" + f + ", ").orElse("") +
            optionalMemberId().map(f -> "memberId=" + f + ", ").orElse("") +
            optionalUploadedById().map(f -> "uploadedById=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
