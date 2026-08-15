package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class BranchUserCriteriaTest {

    @Test
    void newBranchUserCriteriaHasAllFiltersNullTest() {
        var branchUserCriteria = new BranchUserCriteria();
        assertThat(branchUserCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void branchUserCriteriaFluentMethodsCreatesFiltersTest() {
        var branchUserCriteria = new BranchUserCriteria();

        setAllFilters(branchUserCriteria);

        assertThat(branchUserCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void branchUserCriteriaCopyCreatesNullFilterTest() {
        var branchUserCriteria = new BranchUserCriteria();
        var copy = branchUserCriteria.copy();

        assertThat(branchUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(branchUserCriteria)
        );
    }

    @Test
    void branchUserCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var branchUserCriteria = new BranchUserCriteria();
        setAllFilters(branchUserCriteria);

        var copy = branchUserCriteria.copy();

        assertThat(branchUserCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(branchUserCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var branchUserCriteria = new BranchUserCriteria();

        assertThat(branchUserCriteria).hasToString("BranchUserCriteria{}");
    }

    private static void setAllFilters(BranchUserCriteria branchUserCriteria) {
        branchUserCriteria.id();
        branchUserCriteria.startDate();
        branchUserCriteria.endDate();
        branchUserCriteria.active();
        branchUserCriteria.appUserId();
        branchUserCriteria.branchId();
        branchUserCriteria.roleId();
        branchUserCriteria.distinct();
    }

    private static Condition<BranchUserCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getAppUserId()) &&
                condition.apply(criteria.getBranchId()) &&
                condition.apply(criteria.getRoleId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<BranchUserCriteria> copyFiltersAre(BranchUserCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getAppUserId(), copy.getAppUserId()) &&
                condition.apply(criteria.getBranchId(), copy.getBranchId()) &&
                condition.apply(criteria.getRoleId(), copy.getRoleId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
