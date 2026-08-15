package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AgriculturalProductionCriteriaTest {

    @Test
    void newAgriculturalProductionCriteriaHasAllFiltersNullTest() {
        var agriculturalProductionCriteria = new AgriculturalProductionCriteria();
        assertThat(agriculturalProductionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void agriculturalProductionCriteriaFluentMethodsCreatesFiltersTest() {
        var agriculturalProductionCriteria = new AgriculturalProductionCriteria();

        setAllFilters(agriculturalProductionCriteria);

        assertThat(agriculturalProductionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void agriculturalProductionCriteriaCopyCreatesNullFilterTest() {
        var agriculturalProductionCriteria = new AgriculturalProductionCriteria();
        var copy = agriculturalProductionCriteria.copy();

        assertThat(agriculturalProductionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(agriculturalProductionCriteria)
        );
    }

    @Test
    void agriculturalProductionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var agriculturalProductionCriteria = new AgriculturalProductionCriteria();
        setAllFilters(agriculturalProductionCriteria);

        var copy = agriculturalProductionCriteria.copy();

        assertThat(agriculturalProductionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(agriculturalProductionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var agriculturalProductionCriteria = new AgriculturalProductionCriteria();

        assertThat(agriculturalProductionCriteria).hasToString("AgriculturalProductionCriteria{}");
    }

    private static void setAllFilters(AgriculturalProductionCriteria agriculturalProductionCriteria) {
        agriculturalProductionCriteria.id();
        agriculturalProductionCriteria.area();
        agriculturalProductionCriteria.areaUnit();
        agriculturalProductionCriteria.plantingDate();
        agriculturalProductionCriteria.harvestStartDate();
        agriculturalProductionCriteria.harvestEndDate();
        agriculturalProductionCriteria.productionQuantity();
        agriculturalProductionCriteria.productionUnit();
        agriculturalProductionCriteria.expectedAnnualProduction();
        agriculturalProductionCriteria.numberOfPlants();
        agriculturalProductionCriteria.plantingDensity();
        agriculturalProductionCriteria.productionYear();
        agriculturalProductionCriteria.status();
        agriculturalProductionCriteria.agriculturalActivityId();
        agriculturalProductionCriteria.cropId();
        agriculturalProductionCriteria.cropVarietyId();
        agriculturalProductionCriteria.distinct();
    }

    private static Condition<AgriculturalProductionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getArea()) &&
                condition.apply(criteria.getAreaUnit()) &&
                condition.apply(criteria.getPlantingDate()) &&
                condition.apply(criteria.getHarvestStartDate()) &&
                condition.apply(criteria.getHarvestEndDate()) &&
                condition.apply(criteria.getProductionQuantity()) &&
                condition.apply(criteria.getProductionUnit()) &&
                condition.apply(criteria.getExpectedAnnualProduction()) &&
                condition.apply(criteria.getNumberOfPlants()) &&
                condition.apply(criteria.getPlantingDensity()) &&
                condition.apply(criteria.getProductionYear()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getAgriculturalActivityId()) &&
                condition.apply(criteria.getCropId()) &&
                condition.apply(criteria.getCropVarietyId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AgriculturalProductionCriteria> copyFiltersAre(
        AgriculturalProductionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getArea(), copy.getArea()) &&
                condition.apply(criteria.getAreaUnit(), copy.getAreaUnit()) &&
                condition.apply(criteria.getPlantingDate(), copy.getPlantingDate()) &&
                condition.apply(criteria.getHarvestStartDate(), copy.getHarvestStartDate()) &&
                condition.apply(criteria.getHarvestEndDate(), copy.getHarvestEndDate()) &&
                condition.apply(criteria.getProductionQuantity(), copy.getProductionQuantity()) &&
                condition.apply(criteria.getProductionUnit(), copy.getProductionUnit()) &&
                condition.apply(criteria.getExpectedAnnualProduction(), copy.getExpectedAnnualProduction()) &&
                condition.apply(criteria.getNumberOfPlants(), copy.getNumberOfPlants()) &&
                condition.apply(criteria.getPlantingDensity(), copy.getPlantingDensity()) &&
                condition.apply(criteria.getProductionYear(), copy.getProductionYear()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getAgriculturalActivityId(), copy.getAgriculturalActivityId()) &&
                condition.apply(criteria.getCropId(), copy.getCropId()) &&
                condition.apply(criteria.getCropVarietyId(), copy.getCropVarietyId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
