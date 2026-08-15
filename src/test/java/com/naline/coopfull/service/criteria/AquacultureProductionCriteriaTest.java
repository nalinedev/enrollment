package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AquacultureProductionCriteriaTest {

    @Test
    void newAquacultureProductionCriteriaHasAllFiltersNullTest() {
        var aquacultureProductionCriteria = new AquacultureProductionCriteria();
        assertThat(aquacultureProductionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void aquacultureProductionCriteriaFluentMethodsCreatesFiltersTest() {
        var aquacultureProductionCriteria = new AquacultureProductionCriteria();

        setAllFilters(aquacultureProductionCriteria);

        assertThat(aquacultureProductionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void aquacultureProductionCriteriaCopyCreatesNullFilterTest() {
        var aquacultureProductionCriteria = new AquacultureProductionCriteria();
        var copy = aquacultureProductionCriteria.copy();

        assertThat(aquacultureProductionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(aquacultureProductionCriteria)
        );
    }

    @Test
    void aquacultureProductionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var aquacultureProductionCriteria = new AquacultureProductionCriteria();
        setAllFilters(aquacultureProductionCriteria);

        var copy = aquacultureProductionCriteria.copy();

        assertThat(aquacultureProductionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(aquacultureProductionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var aquacultureProductionCriteria = new AquacultureProductionCriteria();

        assertThat(aquacultureProductionCriteria).hasToString("AquacultureProductionCriteria{}");
    }

    private static void setAllFilters(AquacultureProductionCriteria aquacultureProductionCriteria) {
        aquacultureProductionCriteria.id();
        aquacultureProductionCriteria.productionDate();
        aquacultureProductionCriteria.numberOfAnimals();
        aquacultureProductionCriteria.stockingDensity();
        aquacultureProductionCriteria.productionQuantity();
        aquacultureProductionCriteria.productionUnit();
        aquacultureProductionCriteria.averageWeightGrams();
        aquacultureProductionCriteria.mortalityCount();
        aquacultureProductionCriteria.stockingCount();
        aquacultureProductionCriteria.harvestedCount();
        aquacultureProductionCriteria.expectedProduction();
        aquacultureProductionCriteria.expectedHarvestDate();
        aquacultureProductionCriteria.actualHarvestDate();
        aquacultureProductionCriteria.status();
        aquacultureProductionCriteria.aquacultureActivityId();
        aquacultureProductionCriteria.distinct();
    }

    private static Condition<AquacultureProductionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getProductionDate()) &&
                condition.apply(criteria.getNumberOfAnimals()) &&
                condition.apply(criteria.getStockingDensity()) &&
                condition.apply(criteria.getProductionQuantity()) &&
                condition.apply(criteria.getProductionUnit()) &&
                condition.apply(criteria.getAverageWeightGrams()) &&
                condition.apply(criteria.getMortalityCount()) &&
                condition.apply(criteria.getStockingCount()) &&
                condition.apply(criteria.getHarvestedCount()) &&
                condition.apply(criteria.getExpectedProduction()) &&
                condition.apply(criteria.getExpectedHarvestDate()) &&
                condition.apply(criteria.getActualHarvestDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getAquacultureActivityId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AquacultureProductionCriteria> copyFiltersAre(
        AquacultureProductionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getProductionDate(), copy.getProductionDate()) &&
                condition.apply(criteria.getNumberOfAnimals(), copy.getNumberOfAnimals()) &&
                condition.apply(criteria.getStockingDensity(), copy.getStockingDensity()) &&
                condition.apply(criteria.getProductionQuantity(), copy.getProductionQuantity()) &&
                condition.apply(criteria.getProductionUnit(), copy.getProductionUnit()) &&
                condition.apply(criteria.getAverageWeightGrams(), copy.getAverageWeightGrams()) &&
                condition.apply(criteria.getMortalityCount(), copy.getMortalityCount()) &&
                condition.apply(criteria.getStockingCount(), copy.getStockingCount()) &&
                condition.apply(criteria.getHarvestedCount(), copy.getHarvestedCount()) &&
                condition.apply(criteria.getExpectedProduction(), copy.getExpectedProduction()) &&
                condition.apply(criteria.getExpectedHarvestDate(), copy.getExpectedHarvestDate()) &&
                condition.apply(criteria.getActualHarvestDate(), copy.getActualHarvestDate()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getAquacultureActivityId(), copy.getAquacultureActivityId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
