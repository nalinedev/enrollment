package com.naline.coopfull.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AquaticSpecies.
 */
@Entity
@Table(name = "aquatic_species")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AquaticSpecies implements Serializable {

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

    @Column(name = "scientific_name")
    private String scientificName;

    @Column(name = "category")
    private String category;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "freshwater")
    private Boolean freshwater;

    @Column(name = "saltwater")
    private Boolean saltwater;

    @NotNull
    @Column(name = "active", nullable = false)
    private Boolean active;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AquaticSpecies id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public AquaticSpecies code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public AquaticSpecies name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScientificName() {
        return this.scientificName;
    }

    public AquaticSpecies scientificName(String scientificName) {
        this.setScientificName(scientificName);
        return this;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCategory() {
        return this.category;
    }

    public AquaticSpecies category(String category) {
        this.setCategory(category);
        return this;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return this.description;
    }

    public AquaticSpecies description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFreshwater() {
        return this.freshwater;
    }

    public AquaticSpecies freshwater(Boolean freshwater) {
        this.setFreshwater(freshwater);
        return this;
    }

    public void setFreshwater(Boolean freshwater) {
        this.freshwater = freshwater;
    }

    public Boolean getSaltwater() {
        return this.saltwater;
    }

    public AquaticSpecies saltwater(Boolean saltwater) {
        this.setSaltwater(saltwater);
        return this;
    }

    public void setSaltwater(Boolean saltwater) {
        this.saltwater = saltwater;
    }

    public Boolean getActive() {
        return this.active;
    }

    public AquaticSpecies active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AquaticSpecies)) {
            return false;
        }
        return getId() != null && getId().equals(((AquaticSpecies) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AquaticSpecies{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", scientificName='" + getScientificName() + "'" +
            ", category='" + getCategory() + "'" +
            ", description='" + getDescription() + "'" +
            ", freshwater='" + getFreshwater() + "'" +
            ", saltwater='" + getSaltwater() + "'" +
            ", active='" + getActive() + "'" +
            "}";
    }
}
