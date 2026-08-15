package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LivestockProductionCriteriaTest {

    @Test
    void newLivestockProductionCriteriaHasAllFiltersNullTest() {
        var livestockProductionCriteria = new LivestockProductionCriteria();
        assertThat(livestockProductionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void livestockProductionCriteriaFluentMethodsCreatesFiltersTest() {
        var livestockProductionCriteria = new LivestockProductionCriteria();

        setAllFilters(livestockProductionCriteria);

        assertThat(livestockProductionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void livestockProductionCriteriaCopyCreatesNullFilterTest() {
        var livestockProductionCriteria = new LivestockProductionCriteria();
        var copy = livestockProductionCriteria.copy();

        assertThat(livestockProductionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(livestockProductionCriteria)
        );
    }

    @Test
    void livestockProductionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var livestockProductionCriteria = new LivestockProductionCriteria();
        setAllFilters(livestockProductionCriteria);

        var copy = livestockProductionCriteria.copy();

        assertThat(livestockProductionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(livestockProductionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var livestockProductionCriteria = new LivestockProductionCriteria();

        assertThat(livestockProductionCriteria).hasToString("LivestockProductionCriteria{}");
    }

    private static void setAllFilters(LivestockProductionCriteria livestockProductionCriteria) {
        livestockProductionCriteria.id();
        livestockProductionCriteria.productionDate();
        livestockProductionCriteria.animalSex();
        livestockProductionCriteria.numberOfAnimals();
        livestockProductionCriteria.averageAgeMonths();
        livestockProductionCriteria.averageWeightKg();
        livestockProductionCriteria.productionQuantity();
        livestockProductionCriteria.productionUnit();
        livestockProductionCriteria.mortalityCount();
        livestockProductionCriteria.birthCount();
        livestockProductionCriteria.soldCount();
        livestockProductionCriteria.status();
        livestockProductionCriteria.livestockActivityId();
        livestockProductionCriteria.distinct();
    }

    private static Condition<LivestockProductionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getProductionDate()) &&
                condition.apply(criteria.getAnimalSex()) &&
                condition.apply(criteria.getNumberOfAnimals()) &&
                condition.apply(criteria.getAverageAgeMonths()) &&
                condition.apply(criteria.getAverageWeightKg()) &&
                condition.apply(criteria.getProductionQuantity()) &&
                condition.apply(criteria.getProductionUnit()) &&
                condition.apply(criteria.getMortalityCount()) &&
                condition.apply(criteria.getBirthCount()) &&
                condition.apply(criteria.getSoldCount()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getLivestockActivityId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LivestockProductionCriteria> copyFiltersAre(
        LivestockProductionCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getProductionDate(), copy.getProductionDate()) &&
                condition.apply(criteria.getAnimalSex(), copy.getAnimalSex()) &&
                condition.apply(criteria.getNumberOfAnimals(), copy.getNumberOfAnimals()) &&
                condition.apply(criteria.getAverageAgeMonths(), copy.getAverageAgeMonths()) &&
                condition.apply(criteria.getAverageWeightKg(), copy.getAverageWeightKg()) &&
                condition.apply(criteria.getProductionQuantity(), copy.getProductionQuantity()) &&
                condition.apply(criteria.getProductionUnit(), copy.getProductionUnit()) &&
                condition.apply(criteria.getMortalityCount(), copy.getMortalityCount()) &&
                condition.apply(criteria.getBirthCount(), copy.getBirthCount()) &&
                condition.apply(criteria.getSoldCount(), copy.getSoldCount()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getLivestockActivityId(), copy.getLivestockActivityId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
