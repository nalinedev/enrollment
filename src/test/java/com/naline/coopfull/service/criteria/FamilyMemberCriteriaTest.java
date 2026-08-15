package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class FamilyMemberCriteriaTest {

    @Test
    void newFamilyMemberCriteriaHasAllFiltersNullTest() {
        var familyMemberCriteria = new FamilyMemberCriteria();
        assertThat(familyMemberCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void familyMemberCriteriaFluentMethodsCreatesFiltersTest() {
        var familyMemberCriteria = new FamilyMemberCriteria();

        setAllFilters(familyMemberCriteria);

        assertThat(familyMemberCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void familyMemberCriteriaCopyCreatesNullFilterTest() {
        var familyMemberCriteria = new FamilyMemberCriteria();
        var copy = familyMemberCriteria.copy();

        assertThat(familyMemberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(familyMemberCriteria)
        );
    }

    @Test
    void familyMemberCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var familyMemberCriteria = new FamilyMemberCriteria();
        setAllFilters(familyMemberCriteria);

        var copy = familyMemberCriteria.copy();

        assertThat(familyMemberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(familyMemberCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var familyMemberCriteria = new FamilyMemberCriteria();

        assertThat(familyMemberCriteria).hasToString("FamilyMemberCriteria{}");
    }

    private static void setAllFilters(FamilyMemberCriteria familyMemberCriteria) {
        familyMemberCriteria.id();
        familyMemberCriteria.firstName();
        familyMemberCriteria.middleName();
        familyMemberCriteria.lastName();
        familyMemberCriteria.relationship();
        familyMemberCriteria.gender();
        familyMemberCriteria.birthDate();
        familyMemberCriteria.birthPlace();
        familyMemberCriteria.nationality();
        familyMemberCriteria.phoneNumber();
        familyMemberCriteria.occupation();
        familyMemberCriteria.dependent();
        familyMemberCriteria.memberId();
        familyMemberCriteria.distinct();
    }

    private static Condition<FamilyMemberCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getMiddleName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getRelationship()) &&
                condition.apply(criteria.getGender()) &&
                condition.apply(criteria.getBirthDate()) &&
                condition.apply(criteria.getBirthPlace()) &&
                condition.apply(criteria.getNationality()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getOccupation()) &&
                condition.apply(criteria.getDependent()) &&
                condition.apply(criteria.getMemberId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<FamilyMemberCriteria> copyFiltersAre(
        FamilyMemberCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getMiddleName(), copy.getMiddleName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getRelationship(), copy.getRelationship()) &&
                condition.apply(criteria.getGender(), copy.getGender()) &&
                condition.apply(criteria.getBirthDate(), copy.getBirthDate()) &&
                condition.apply(criteria.getBirthPlace(), copy.getBirthPlace()) &&
                condition.apply(criteria.getNationality(), copy.getNationality()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getOccupation(), copy.getOccupation()) &&
                condition.apply(criteria.getDependent(), copy.getDependent()) &&
                condition.apply(criteria.getMemberId(), copy.getMemberId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
