package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CooperativeCriteriaTest {

    @Test
    void newCooperativeCriteriaHasAllFiltersNullTest() {
        var cooperativeCriteria = new CooperativeCriteria();
        assertThat(cooperativeCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cooperativeCriteriaFluentMethodsCreatesFiltersTest() {
        var cooperativeCriteria = new CooperativeCriteria();

        setAllFilters(cooperativeCriteria);

        assertThat(cooperativeCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cooperativeCriteriaCopyCreatesNullFilterTest() {
        var cooperativeCriteria = new CooperativeCriteria();
        var copy = cooperativeCriteria.copy();

        assertThat(cooperativeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cooperativeCriteria)
        );
    }

    @Test
    void cooperativeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cooperativeCriteria = new CooperativeCriteria();
        setAllFilters(cooperativeCriteria);

        var copy = cooperativeCriteria.copy();

        assertThat(cooperativeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cooperativeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cooperativeCriteria = new CooperativeCriteria();

        assertThat(cooperativeCriteria).hasToString("CooperativeCriteria{}");
    }

    private static void setAllFilters(CooperativeCriteria cooperativeCriteria) {
        cooperativeCriteria.id();
        cooperativeCriteria.code();
        cooperativeCriteria.name();
        cooperativeCriteria.legalName();
        cooperativeCriteria.registrationNumber();
        cooperativeCriteria.taxNumber();
        cooperativeCriteria.status();
        cooperativeCriteria.foundedDate();
        cooperativeCriteria.email();
        cooperativeCriteria.phone();
        cooperativeCriteria.website();
        cooperativeCriteria.createdDate();
        cooperativeCriteria.lastModifiedDate();
        cooperativeCriteria.distinct();
    }

    private static Condition<CooperativeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getLegalName()) &&
                condition.apply(criteria.getRegistrationNumber()) &&
                condition.apply(criteria.getTaxNumber()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getFoundedDate()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getWebsite()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CooperativeCriteria> copyFiltersAre(CooperativeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getLegalName(), copy.getLegalName()) &&
                condition.apply(criteria.getRegistrationNumber(), copy.getRegistrationNumber()) &&
                condition.apply(criteria.getTaxNumber(), copy.getTaxNumber()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getFoundedDate(), copy.getFoundedDate()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getWebsite(), copy.getWebsite()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
