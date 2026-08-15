package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class NumberSequenceCriteriaTest {

    @Test
    void newNumberSequenceCriteriaHasAllFiltersNullTest() {
        var numberSequenceCriteria = new NumberSequenceCriteria();
        assertThat(numberSequenceCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void numberSequenceCriteriaFluentMethodsCreatesFiltersTest() {
        var numberSequenceCriteria = new NumberSequenceCriteria();

        setAllFilters(numberSequenceCriteria);

        assertThat(numberSequenceCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void numberSequenceCriteriaCopyCreatesNullFilterTest() {
        var numberSequenceCriteria = new NumberSequenceCriteria();
        var copy = numberSequenceCriteria.copy();

        assertThat(numberSequenceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(numberSequenceCriteria)
        );
    }

    @Test
    void numberSequenceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var numberSequenceCriteria = new NumberSequenceCriteria();
        setAllFilters(numberSequenceCriteria);

        var copy = numberSequenceCriteria.copy();

        assertThat(numberSequenceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(numberSequenceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var numberSequenceCriteria = new NumberSequenceCriteria();

        assertThat(numberSequenceCriteria).hasToString("NumberSequenceCriteria{}");
    }

    private static void setAllFilters(NumberSequenceCriteria numberSequenceCriteria) {
        numberSequenceCriteria.id();
        numberSequenceCriteria.sequenceType();
        numberSequenceCriteria.prefix();
        numberSequenceCriteria.year();
        numberSequenceCriteria.currentValue();
        numberSequenceCriteria.padding();
        numberSequenceCriteria.cooperativeId();
        numberSequenceCriteria.distinct();
    }

    private static Condition<NumberSequenceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSequenceType()) &&
                condition.apply(criteria.getPrefix()) &&
                condition.apply(criteria.getYear()) &&
                condition.apply(criteria.getCurrentValue()) &&
                condition.apply(criteria.getPadding()) &&
                condition.apply(criteria.getCooperativeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<NumberSequenceCriteria> copyFiltersAre(
        NumberSequenceCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSequenceType(), copy.getSequenceType()) &&
                condition.apply(criteria.getPrefix(), copy.getPrefix()) &&
                condition.apply(criteria.getYear(), copy.getYear()) &&
                condition.apply(criteria.getCurrentValue(), copy.getCurrentValue()) &&
                condition.apply(criteria.getPadding(), copy.getPadding()) &&
                condition.apply(criteria.getCooperativeId(), copy.getCooperativeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
