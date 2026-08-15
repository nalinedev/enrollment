package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class LivestockTypeCriteriaTest {

    @Test
    void newLivestockTypeCriteriaHasAllFiltersNullTest() {
        var livestockTypeCriteria = new LivestockTypeCriteria();
        assertThat(livestockTypeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void livestockTypeCriteriaFluentMethodsCreatesFiltersTest() {
        var livestockTypeCriteria = new LivestockTypeCriteria();

        setAllFilters(livestockTypeCriteria);

        assertThat(livestockTypeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void livestockTypeCriteriaCopyCreatesNullFilterTest() {
        var livestockTypeCriteria = new LivestockTypeCriteria();
        var copy = livestockTypeCriteria.copy();

        assertThat(livestockTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(livestockTypeCriteria)
        );
    }

    @Test
    void livestockTypeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var livestockTypeCriteria = new LivestockTypeCriteria();
        setAllFilters(livestockTypeCriteria);

        var copy = livestockTypeCriteria.copy();

        assertThat(livestockTypeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(livestockTypeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var livestockTypeCriteria = new LivestockTypeCriteria();

        assertThat(livestockTypeCriteria).hasToString("LivestockTypeCriteria{}");
    }

    private static void setAllFilters(LivestockTypeCriteria livestockTypeCriteria) {
        livestockTypeCriteria.id();
        livestockTypeCriteria.code();
        livestockTypeCriteria.name();
        livestockTypeCriteria.scientificName();
        livestockTypeCriteria.category();
        livestockTypeCriteria.active();
        livestockTypeCriteria.distinct();
    }

    private static Condition<LivestockTypeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getScientificName()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<LivestockTypeCriteria> copyFiltersAre(
        LivestockTypeCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getScientificName(), copy.getScientificName()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
