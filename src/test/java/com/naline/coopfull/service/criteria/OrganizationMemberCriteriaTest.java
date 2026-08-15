package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OrganizationMemberCriteriaTest {

    @Test
    void newOrganizationMemberCriteriaHasAllFiltersNullTest() {
        var organizationMemberCriteria = new OrganizationMemberCriteria();
        assertThat(organizationMemberCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void organizationMemberCriteriaFluentMethodsCreatesFiltersTest() {
        var organizationMemberCriteria = new OrganizationMemberCriteria();

        setAllFilters(organizationMemberCriteria);

        assertThat(organizationMemberCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void organizationMemberCriteriaCopyCreatesNullFilterTest() {
        var organizationMemberCriteria = new OrganizationMemberCriteria();
        var copy = organizationMemberCriteria.copy();

        assertThat(organizationMemberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(organizationMemberCriteria)
        );
    }

    @Test
    void organizationMemberCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var organizationMemberCriteria = new OrganizationMemberCriteria();
        setAllFilters(organizationMemberCriteria);

        var copy = organizationMemberCriteria.copy();

        assertThat(organizationMemberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(organizationMemberCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var organizationMemberCriteria = new OrganizationMemberCriteria();

        assertThat(organizationMemberCriteria).hasToString("OrganizationMemberCriteria{}");
    }

    private static void setAllFilters(OrganizationMemberCriteria organizationMemberCriteria) {
        organizationMemberCriteria.id();
        organizationMemberCriteria.legalName();
        organizationMemberCriteria.tradeName();
        organizationMemberCriteria.registrationNumber();
        organizationMemberCriteria.taxNumber();
        organizationMemberCriteria.legalForm();
        organizationMemberCriteria.registrationDate();
        organizationMemberCriteria.email();
        organizationMemberCriteria.phoneNumber();
        organizationMemberCriteria.website();
        organizationMemberCriteria.memberId();
        organizationMemberCriteria.distinct();
    }

    private static Condition<OrganizationMemberCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLegalName()) &&
                condition.apply(criteria.getTradeName()) &&
                condition.apply(criteria.getRegistrationNumber()) &&
                condition.apply(criteria.getTaxNumber()) &&
                condition.apply(criteria.getLegalForm()) &&
                condition.apply(criteria.getRegistrationDate()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getWebsite()) &&
                condition.apply(criteria.getMemberId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OrganizationMemberCriteria> copyFiltersAre(
        OrganizationMemberCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLegalName(), copy.getLegalName()) &&
                condition.apply(criteria.getTradeName(), copy.getTradeName()) &&
                condition.apply(criteria.getRegistrationNumber(), copy.getRegistrationNumber()) &&
                condition.apply(criteria.getTaxNumber(), copy.getTaxNumber()) &&
                condition.apply(criteria.getLegalForm(), copy.getLegalForm()) &&
                condition.apply(criteria.getRegistrationDate(), copy.getRegistrationDate()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getWebsite(), copy.getWebsite()) &&
                condition.apply(criteria.getMemberId(), copy.getMemberId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
