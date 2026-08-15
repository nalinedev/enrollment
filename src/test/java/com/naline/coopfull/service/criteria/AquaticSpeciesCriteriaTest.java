package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class AquaticSpeciesCriteriaTest {

    @Test
    void newAquaticSpeciesCriteriaHasAllFiltersNullTest() {
        var aquaticSpeciesCriteria = new AquaticSpeciesCriteria();
        assertThat(aquaticSpeciesCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void aquaticSpeciesCriteriaFluentMethodsCreatesFiltersTest() {
        var aquaticSpeciesCriteria = new AquaticSpeciesCriteria();

        setAllFilters(aquaticSpeciesCriteria);

        assertThat(aquaticSpeciesCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void aquaticSpeciesCriteriaCopyCreatesNullFilterTest() {
        var aquaticSpeciesCriteria = new AquaticSpeciesCriteria();
        var copy = aquaticSpeciesCriteria.copy();

        assertThat(aquaticSpeciesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(aquaticSpeciesCriteria)
        );
    }

    @Test
    void aquaticSpeciesCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var aquaticSpeciesCriteria = new AquaticSpeciesCriteria();
        setAllFilters(aquaticSpeciesCriteria);

        var copy = aquaticSpeciesCriteria.copy();

        assertThat(aquaticSpeciesCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(aquaticSpeciesCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var aquaticSpeciesCriteria = new AquaticSpeciesCriteria();

        assertThat(aquaticSpeciesCriteria).hasToString("AquaticSpeciesCriteria{}");
    }

    private static void setAllFilters(AquaticSpeciesCriteria aquaticSpeciesCriteria) {
        aquaticSpeciesCriteria.id();
        aquaticSpeciesCriteria.code();
        aquaticSpeciesCriteria.name();
        aquaticSpeciesCriteria.scientificName();
        aquaticSpeciesCriteria.category();
        aquaticSpeciesCriteria.freshwater();
        aquaticSpeciesCriteria.saltwater();
        aquaticSpeciesCriteria.active();
        aquaticSpeciesCriteria.distinct();
    }

    private static Condition<AquaticSpeciesCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getScientificName()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getFreshwater()) &&
                condition.apply(criteria.getSaltwater()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<AquaticSpeciesCriteria> copyFiltersAre(
        AquaticSpeciesCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getScientificName(), copy.getScientificName()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getFreshwater(), copy.getFreshwater()) &&
                condition.apply(criteria.getSaltwater(), copy.getSaltwater()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
