package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PermissionCriteriaTest {

    @Test
    void newPermissionCriteriaHasAllFiltersNullTest() {
        var permissionCriteria = new PermissionCriteria();
        assertThat(permissionCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void permissionCriteriaFluentMethodsCreatesFiltersTest() {
        var permissionCriteria = new PermissionCriteria();

        setAllFilters(permissionCriteria);

        assertThat(permissionCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void permissionCriteriaCopyCreatesNullFilterTest() {
        var permissionCriteria = new PermissionCriteria();
        var copy = permissionCriteria.copy();

        assertThat(permissionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(permissionCriteria)
        );
    }

    @Test
    void permissionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var permissionCriteria = new PermissionCriteria();
        setAllFilters(permissionCriteria);

        var copy = permissionCriteria.copy();

        assertThat(permissionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(permissionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var permissionCriteria = new PermissionCriteria();

        assertThat(permissionCriteria).hasToString("PermissionCriteria{}");
    }

    private static void setAllFilters(PermissionCriteria permissionCriteria) {
        permissionCriteria.id();
        permissionCriteria.code();
        permissionCriteria.name();
        permissionCriteria.resource();
        permissionCriteria.action();
        permissionCriteria.active();
        permissionCriteria.rolesId();
        permissionCriteria.distinct();
    }

    private static Condition<PermissionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getResource()) &&
                condition.apply(criteria.getAction()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getRolesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PermissionCriteria> copyFiltersAre(PermissionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getResource(), copy.getResource()) &&
                condition.apply(criteria.getAction(), copy.getAction()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getRolesId(), copy.getRolesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
