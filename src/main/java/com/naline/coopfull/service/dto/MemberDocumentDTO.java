package com.naline.coopfull.service.dto;

import com.naline.coopfull.domain.enumeration.DocumentVerificationStatus;
import com.naline.coopfull.domain.enumeration.MemberDocumentType;
import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.MemberDocument} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MemberDocumentDTO implements Serializable {

    private Long id;

    @NotNull
    private MemberDocumentType documentType;

    private String originalFileName;

    private String storedFileName;

    private String contentType;

    private Long fileSize;

    private String storagePath;

    private String checksum;

    @NotNull
    private DocumentVerificationStatus verificationStatus;

    @NotNull
    private Instant uploadedAt;

    private Instant verifiedAt;

    @Lob
    private String notes;

    private MemberDTO member;

    private AppUserDTO uploadedBy;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MemberDocumentType getDocumentType() {
        return documentType;
    }

    public void setDocumentType(MemberDocumentType documentType) {
        this.documentType = documentType;
    }

    public String getOriginalFileName() {
        return originalFileName;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getStoredFileName() {
        return storedFileName;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getStoragePath() {
        return storagePath;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public DocumentVerificationStatus getVerificationStatus() {
        return verificationStatus;
    }

    public void setVerificationStatus(DocumentVerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Instant getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Instant getVerifiedAt() {
        return verifiedAt;
    }

    public void setVerifiedAt(Instant verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public MemberDTO getMember() {
        return member;
    }

    public void setMember(MemberDTO member) {
        this.member = member;
    }

    public AppUserDTO getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(AppUserDTO uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberDocumentDTO)) {
            return false;
        }

        MemberDocumentDTO memberDocumentDTO = (MemberDocumentDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, memberDocumentDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberDocumentDTO{" +
            "id=" + getId() +
            ", documentType='" + getDocumentType() + "'" +
            ", originalFileName='" + getOriginalFileName() + "'" +
            ", storedFileName='" + getStoredFileName() + "'" +
            ", contentType='" + getContentType() + "'" +
            ", fileSize=" + getFileSize() +
            ", storagePath='" + getStoragePath() + "'" +
            ", checksum='" + getChecksum() + "'" +
            ", verificationStatus='" + getVerificationStatus() + "'" +
            ", uploadedAt='" + getUploadedAt() + "'" +
            ", verifiedAt='" + getVerifiedAt() + "'" +
            ", notes='" + getNotes() + "'" +
            ", member=" + getMember() +
            ", uploadedBy=" + getUploadedBy() +
            "}";
    }
}
