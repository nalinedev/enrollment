package com.naline.coopfull.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.naline.coopfull.domain.AquaticSpecies} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AquaticSpeciesDTO implements Serializable {

    private Long id;

    @NotNull
    private String code;

    @NotNull
    private String name;

    private String scientificName;

    private String category;

    @Lob
    private String description;

    private Boolean freshwater;

    private Boolean saltwater;

    @NotNull
    private Boolean active;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getScientificName() {
        return scientificName;
    }

    public void setScientificName(String scientificName) {
        this.scientificName = scientificName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getFreshwater() {
        return freshwater;
    }

    public void setFreshwater(Boolean freshwater) {
        this.freshwater = freshwater;
    }

    public Boolean getSaltwater() {
        return saltwater;
    }

    public void setSaltwater(Boolean saltwater) {
        this.saltwater = saltwater;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AquaticSpeciesDTO)) {
            return false;
        }

        AquaticSpeciesDTO aquaticSpeciesDTO = (AquaticSpeciesDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, aquaticSpeciesDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AquaticSpeciesDTO{" +
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
