package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IndividualMemberCriteriaTest {

    @Test
    void newIndividualMemberCriteriaHasAllFiltersNullTest() {
        var individualMemberCriteria = new IndividualMemberCriteria();
        assertThat(individualMemberCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void individualMemberCriteriaFluentMethodsCreatesFiltersTest() {
        var individualMemberCriteria = new IndividualMemberCriteria();

        setAllFilters(individualMemberCriteria);

        assertThat(individualMemberCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void individualMemberCriteriaCopyCreatesNullFilterTest() {
        var individualMemberCriteria = new IndividualMemberCriteria();
        var copy = individualMemberCriteria.copy();

        assertThat(individualMemberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(individualMemberCriteria)
        );
    }

    @Test
    void individualMemberCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var individualMemberCriteria = new IndividualMemberCriteria();
        setAllFilters(individualMemberCriteria);

        var copy = individualMemberCriteria.copy();

        assertThat(individualMemberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(individualMemberCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var individualMemberCriteria = new IndividualMemberCriteria();

        assertThat(individualMemberCriteria).hasToString("IndividualMemberCriteria{}");
    }

    private static void setAllFilters(IndividualMemberCriteria individualMemberCriteria) {
        individualMemberCriteria.id();
        individualMemberCriteria.firstName();
        individualMemberCriteria.middleName();
        individualMemberCriteria.lastName();
        individualMemberCriteria.maidenName();
        individualMemberCriteria.gender();
        individualMemberCriteria.birthDate();
        individualMemberCriteria.birthPlace();
        individualMemberCriteria.nationality();
        individualMemberCriteria.email();
        individualMemberCriteria.phoneNumber();
        individualMemberCriteria.occupation();
        individualMemberCriteria.memberId();
        individualMemberCriteria.distinct();
    }

    private static Condition<IndividualMemberCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getFirstName()) &&
                condition.apply(criteria.getMiddleName()) &&
                condition.apply(criteria.getLastName()) &&
                condition.apply(criteria.getMaidenName()) &&
                condition.apply(criteria.getGender()) &&
                condition.apply(criteria.getBirthDate()) &&
                condition.apply(criteria.getBirthPlace()) &&
                condition.apply(criteria.getNationality()) &&
                condition.apply(criteria.getEmail()) &&
                condition.apply(criteria.getPhoneNumber()) &&
                condition.apply(criteria.getOccupation()) &&
                condition.apply(criteria.getMemberId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IndividualMemberCriteria> copyFiltersAre(
        IndividualMemberCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getFirstName(), copy.getFirstName()) &&
                condition.apply(criteria.getMiddleName(), copy.getMiddleName()) &&
                condition.apply(criteria.getLastName(), copy.getLastName()) &&
                condition.apply(criteria.getMaidenName(), copy.getMaidenName()) &&
                condition.apply(criteria.getGender(), copy.getGender()) &&
                condition.apply(criteria.getBirthDate(), copy.getBirthDate()) &&
                condition.apply(criteria.getBirthPlace(), copy.getBirthPlace()) &&
                condition.apply(criteria.getNationality(), copy.getNationality()) &&
                condition.apply(criteria.getEmail(), copy.getEmail()) &&
                condition.apply(criteria.getPhoneNumber(), copy.getPhoneNumber()) &&
                condition.apply(criteria.getOccupation(), copy.getOccupation()) &&
                condition.apply(criteria.getMemberId(), copy.getMemberId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
