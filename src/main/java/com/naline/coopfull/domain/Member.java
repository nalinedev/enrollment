package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naline.coopfull.domain.enumeration.MemberStatus;
import com.naline.coopfull.domain.enumeration.MemberType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Member.
 */
@Entity
@Table(name = "member")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Member implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "member_number", nullable = false, unique = true)
    private String memberNumber;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "member_type", nullable = false)
    private MemberType memberType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private MemberStatus status;

    @Column(name = "admission_date")
    private LocalDate admissionDate;

    @Column(name = "exit_date")
    private LocalDate exitDate;

    @Column(name = "exit_reason")
    private String exitReason;

    @Lob
    @Column(name = "notes")
    private String notes;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    @JsonIgnoreProperties(value = { "member" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private IndividualMember individualMember;

    @JsonIgnoreProperties(value = { "member" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private OrganizationMember organizationMember;

    @JsonIgnoreProperties(value = { "member" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private SocialProfile socialProfile;

    @JsonIgnoreProperties(value = { "member" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private ProfessionalProfile professionalProfile;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cooperative cooperative;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "cooperative", "location" }, allowSetters = true)
    private CooperativeBranch branch;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Member id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMemberNumber() {
        return this.memberNumber;
    }

    public Member memberNumber(String memberNumber) {
        this.setMemberNumber(memberNumber);
        return this;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public MemberType getMemberType() {
        return this.memberType;
    }

    public Member memberType(MemberType memberType) {
        this.setMemberType(memberType);
        return this;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public MemberStatus getStatus() {
        return this.status;
    }

    public Member status(MemberStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public LocalDate getAdmissionDate() {
        return this.admissionDate;
    }

    public Member admissionDate(LocalDate admissionDate) {
        this.setAdmissionDate(admissionDate);
        return this;
    }

    public void setAdmissionDate(LocalDate admissionDate) {
        this.admissionDate = admissionDate;
    }

    public LocalDate getExitDate() {
        return this.exitDate;
    }

    public Member exitDate(LocalDate exitDate) {
        this.setExitDate(exitDate);
        return this;
    }

    public void setExitDate(LocalDate exitDate) {
        this.exitDate = exitDate;
    }

    public String getExitReason() {
        return this.exitReason;
    }

    public Member exitReason(String exitReason) {
        this.setExitReason(exitReason);
        return this;
    }

    public void setExitReason(String exitReason) {
        this.exitReason = exitReason;
    }

    public String getNotes() {
        return this.notes;
    }

    public Member notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public Member createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public Member lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public IndividualMember getIndividualMember() {
        return this.individualMember;
    }

    public void setIndividualMember(IndividualMember individualMember) {
        this.individualMember = individualMember;
    }

    public Member individualMember(IndividualMember individualMember) {
        this.setIndividualMember(individualMember);
        return this;
    }

    public OrganizationMember getOrganizationMember() {
        return this.organizationMember;
    }

    public void setOrganizationMember(OrganizationMember organizationMember) {
        this.organizationMember = organizationMember;
    }

    public Member organizationMember(OrganizationMember organizationMember) {
        this.setOrganizationMember(organizationMember);
        return this;
    }

    public SocialProfile getSocialProfile() {
        return this.socialProfile;
    }

    public void setSocialProfile(SocialProfile socialProfile) {
        this.socialProfile = socialProfile;
    }

    public Member socialProfile(SocialProfile socialProfile) {
        this.setSocialProfile(socialProfile);
        return this;
    }

    public ProfessionalProfile getProfessionalProfile() {
        return this.professionalProfile;
    }

    public void setProfessionalProfile(ProfessionalProfile professionalProfile) {
        this.professionalProfile = professionalProfile;
    }

    public Member professionalProfile(ProfessionalProfile professionalProfile) {
        this.setProfessionalProfile(professionalProfile);
        return this;
    }

    public Cooperative getCooperative() {
        return this.cooperative;
    }

    public void setCooperative(Cooperative cooperative) {
        this.cooperative = cooperative;
    }

    public Member cooperative(Cooperative cooperative) {
        this.setCooperative(cooperative);
        return this;
    }

    public CooperativeBranch getBranch() {
        return this.branch;
    }

    public void setBranch(CooperativeBranch cooperativeBranch) {
        this.branch = cooperativeBranch;
    }

    public Member branch(CooperativeBranch cooperativeBranch) {
        this.setBranch(cooperativeBranch);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Member)) {
            return false;
        }
        return getId() != null && getId().equals(((Member) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Member{" +
            "id=" + getId() +
            ", memberNumber='" + getMemberNumber() + "'" +
            ", memberType='" + getMemberType() + "'" +
            ", status='" + getStatus() + "'" +
            ", admissionDate='" + getAdmissionDate() + "'" +
            ", exitDate='" + getExitDate() + "'" +
            ", exitReason='" + getExitReason() + "'" +
            ", notes='" + getNotes() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
