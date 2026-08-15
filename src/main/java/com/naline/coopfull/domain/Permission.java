package com.naline.coopfull.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Permission.
 */
@Entity
@Table(name = "permission")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Permission implements Serializable {

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

    @Column(name = "resource")
    private String resource;

    @Column(name = "action")
    private String action;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "permissionses")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "permissionses" }, allowSetters = true)
    private Set<CooperativeRole> roleses = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Permission id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Permission code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Permission name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Permission description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResource() {
        return this.resource;
    }

    public Permission resource(String resource) {
        this.setResource(resource);
        return this;
    }

    public void setResource(String resource) {
        this.resource = resource;
    }

    public String getAction() {
        return this.action;
    }

    public Permission action(String action) {
        this.setAction(action);
        return this;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Boolean getActive() {
        return this.active;
    }

    public Permission active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Set<CooperativeRole> getRoleses() {
        return this.roleses;
    }

    public void setRoleses(Set<CooperativeRole> cooperativeRoles) {
        if (this.roleses != null) {
            this.roleses.forEach(i -> i.removePermissions(this));
        }
        if (cooperativeRoles != null) {
            cooperativeRoles.forEach(i -> i.addPermissions(this));
        }
        this.roleses = cooperativeRoles;
    }

    public Permission roleses(Set<CooperativeRole> cooperativeRoles) {
        this.setRoleses(cooperativeRoles);
        return this;
    }

    public Permission addRoles(CooperativeRole cooperativeRole) {
        this.roleses.add(cooperativeRole);
        cooperativeRole.getPermissionses().add(this);
        return this;
    }

    public Permission removeRoles(CooperativeRole cooperativeRole) {
        this.roleses.remove(cooperativeRole);
        cooperativeRole.getPermissionses().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Permission)) {
            return false;
        }
        return getId() != null && getId().equals(((Permission) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Permission{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", resource='" + getResource() + "'" +
            ", action='" + getAction() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
