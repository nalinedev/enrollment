package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.naline.coopfull.domain.enumeration.CooperativeRoleStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A CooperativeRole.
 */
@Entity
@Table(name = "cooperative_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CooperativeRole implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private CooperativeRoleStatus status;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_cooperative_role__permissions",
        joinColumns = @JoinColumn(name = "cooperative_role_id"),
        inverseJoinColumns = @JoinColumn(name = "permissions_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "roleses" }, allowSetters = true)
    private Set<Permission> permissionses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CooperativeRole id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public CooperativeRole code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public CooperativeRole name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public CooperativeRole description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public CooperativeRoleStatus getStatus() {
        return this.status;
    }

    public CooperativeRole status(CooperativeRoleStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CooperativeRoleStatus status) {
        this.status = status;
    }

    public Set<Permission> getPermissionses() {
        return this.permissionses;
    }

    public void setPermissionses(Set<Permission> permissions) {
        this.permissionses = permissions;
    }

    public CooperativeRole permissionses(Set<Permission> permissions) {
        this.setPermissionses(permissions);
        return this;
    }

    public CooperativeRole addPermissions(Permission permission) {
        this.permissionses.add(permission);
        return this;
    }

    public CooperativeRole removePermissions(Permission permission) {
        this.permissionses.remove(permission);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CooperativeRole)) {
            return false;
        }
        return getId() != null && getId().equals(((CooperativeRole) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CooperativeRole{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
