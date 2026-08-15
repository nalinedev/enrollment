package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naline.coopfull.domain.enumeration.DocumentVerificationStatus;
import com.naline.coopfull.domain.enumeration.MemberDocumentType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A MemberDocument.
 */
@Entity
@Table(name = "member_document")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MemberDocument implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "document_type", nullable = false)
    private MemberDocumentType documentType;

    @Column(name = "original_file_name")
    private String originalFileName;

    @Column(name = "stored_file_name")
    private String storedFileName;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "file_size")
    private Long fileSize;

    @Column(name = "storage_path")
    private String storagePath;

    @Column(name = "checksum")
    private String checksum;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", nullable = false)
    private DocumentVerificationStatus verificationStatus;

    @NotNull
    @Column(name = "uploaded_at", nullable = false)
    private Instant uploadedAt;

    @Column(name = "verified_at")
    private Instant verifiedAt;

    @Lob
    @Column(name = "notes")
    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = { "individualMember", "organizationMember", "socialProfile", "professionalProfile", "cooperative", "branch" },
        allowSetters = true
    )
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private AppUser uploadedBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MemberDocument id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MemberDocumentType getDocumentType() {
        return this.documentType;
    }

    public MemberDocument documentType(MemberDocumentType documentType) {
        this.setDocumentType(documentType);
        return this;
    }

    public void setDocumentType(MemberDocumentType documentType) {
        this.documentType = documentType;
    }

    public String getOriginalFileName() {
        return this.originalFileName;
    }

    public MemberDocument originalFileName(String originalFileName) {
        this.setOriginalFileName(originalFileName);
        return this;
    }

    public void setOriginalFileName(String originalFileName) {
        this.originalFileName = originalFileName;
    }

    public String getStoredFileName() {
        return this.storedFileName;
    }

    public MemberDocument storedFileName(String storedFileName) {
        this.setStoredFileName(storedFileName);
        return this;
    }

    public void setStoredFileName(String storedFileName) {
        this.storedFileName = storedFileName;
    }

    public String getContentType() {
        return this.contentType;
    }

    public MemberDocument contentType(String contentType) {
        this.setContentType(contentType);
        return this;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public Long getFileSize() {
        return this.fileSize;
    }

    public MemberDocument fileSize(Long fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getStoragePath() {
        return this.storagePath;
    }

    public MemberDocument storagePath(String storagePath) {
        this.setStoragePath(storagePath);
        return this;
    }

    public void setStoragePath(String storagePath) {
        this.storagePath = storagePath;
    }

    public String getChecksum() {
        return this.checksum;
    }

    public MemberDocument checksum(String checksum) {
        this.setChecksum(checksum);
        return this;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public DocumentVerificationStatus getVerificationStatus() {
        return this.verificationStatus;
    }

    public MemberDocument verificationStatus(DocumentVerificationStatus verificationStatus) {
        this.setVerificationStatus(verificationStatus);
        return this;
    }

    public void setVerificationStatus(DocumentVerificationStatus verificationStatus) {
        this.verificationStatus = verificationStatus;
    }

    public Instant getUploadedAt() {
        return this.uploadedAt;
    }

    public MemberDocument uploadedAt(Instant uploadedAt) {
        this.setUploadedAt(uploadedAt);
        return this;
    }

    public void setUploadedAt(Instant uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Instant getVerifiedAt() {
        return this.verifiedAt;
    }

    public MemberDocument verifiedAt(Instant verifiedAt) {
        this.setVerifiedAt(verifiedAt);
        return this;
    }

    public void setVerifiedAt(Instant verifiedAt) {
        this.verifiedAt = verifiedAt;
    }

    public String getNotes() {
        return this.notes;
    }

    public MemberDocument notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Member getMember() {
        return this.member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public MemberDocument member(Member member) {
        this.setMember(member);
        return this;
    }

    public AppUser getUploadedBy() {
        return this.uploadedBy;
    }

    public void setUploadedBy(AppUser appUser) {
        this.uploadedBy = appUser;
    }

    public MemberDocument uploadedBy(AppUser appUser) {
        this.setUploadedBy(appUser);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MemberDocument)) {
            return false;
        }
        return getId() != null && getId().equals(((MemberDocument) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MemberDocument{" +
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
            "}";
    }
}
