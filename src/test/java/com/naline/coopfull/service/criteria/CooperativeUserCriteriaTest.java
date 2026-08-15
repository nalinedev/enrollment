package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CooperativeUserCriteriaTest {

    @Test
    void newCooperativeUserCriteriaHasAllFiltersNullTest() {
        var cooperativeUserCriteria = new CooperativeUserCriteria();
        assertThat(cooperativeUserCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cooperativeUserCriteriaFluentMethodsCreatesFiltersTest() {
        var cooperativeUserCriteria = new CooperativeUserCriteria();

        setAllFilters(cooperativeUserCriteria);

        assertThat(cooperativeUserCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cooperativeUserCriteriaCopyCreatesNullFilterTest() {
        var cooperativeUserCriteria = new CooperativeUserCriteria();
        var copy = cooperativeUserCriteria.copy();

        assertThat(cooperativeUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cooperativeUserCriteria)
        );
    }

    @Test
    void cooperativeUserCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cooperativeUserCriteria = new CooperativeUserCriteria();
        setAllFilters(cooperativeUserCriteria);

        var copy = cooperativeUserCriteria.copy();

        assertThat(cooperativeUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cooperativeUserCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cooperativeUserCriteria = new CooperativeUserCriteria();

        assertThat(cooperativeUserCriteria).hasToString("CooperativeUserCriteria{}");
    }

    private static void setAllFilters(CooperativeUserCriteria cooperativeUserCriteria) {
        cooperativeUserCriteria.id();
        cooperativeUserCriteria.startDate();
        cooperativeUserCriteria.endDate();
        cooperativeUserCriteria.active();
        cooperativeUserCriteria.appUserId();
        cooperativeUserCriteria.cooperativeId();
        cooperativeUserCriteria.roleId();
        cooperativeUserCriteria.distinct();
    }

    private static Condition<CooperativeUserCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getAppUserId()) &&
                condition.apply(criteria.getCooperativeId()) &&
                condition.apply(criteria.getRoleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CooperativeUserCriteria> copyFiltersAre(
        CooperativeUserCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getAppUserId(), copy.getAppUserId()) &&
                condition.apply(criteria.getCooperativeId(), copy.getCooperativeId()) &&
                condition.apply(criteria.getRoleId(), copy.getRoleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
