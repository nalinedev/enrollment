package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SocialProfileCriteriaTest {

    @Test
    void newSocialProfileCriteriaHasAllFiltersNullTest() {
        var socialProfileCriteria = new SocialProfileCriteria();
        assertThat(socialProfileCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void socialProfileCriteriaFluentMethodsCreatesFiltersTest() {
        var socialProfileCriteria = new SocialProfileCriteria();

        setAllFilters(socialProfileCriteria);

        assertThat(socialProfileCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void socialProfileCriteriaCopyCreatesNullFilterTest() {
        var socialProfileCriteria = new SocialProfileCriteria();
        var copy = socialProfileCriteria.copy();

        assertThat(socialProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(socialProfileCriteria)
        );
    }

    @Test
    void socialProfileCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var socialProfileCriteria = new SocialProfileCriteria();
        setAllFilters(socialProfileCriteria);

        var copy = socialProfileCriteria.copy();

        assertThat(socialProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(socialProfileCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var socialProfileCriteria = new SocialProfileCriteria();

        assertThat(socialProfileCriteria).hasToString("SocialProfileCriteria{}");
    }

    private static void setAllFilters(SocialProfileCriteria socialProfileCriteria) {
        socialProfileCriteria.id();
        socialProfileCriteria.maritalStatus();
        socialProfileCriteria.numberOfChildren();
        socialProfileCriteria.numberOfDependents();
        socialProfileCriteria.educationLevel();
        socialProfileCriteria.housingStatus();
        socialProfileCriteria.residenceSince();
        socialProfileCriteria.disabilityStatus();
        socialProfileCriteria.socialCategory();
        socialProfileCriteria.memberId();
        socialProfileCriteria.distinct();
    }

    private static Condition<SocialProfileCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMaritalStatus()) &&
                condition.apply(criteria.getNumberOfChildren()) &&
                condition.apply(criteria.getNumberOfDependents()) &&
                condition.apply(criteria.getEducationLevel()) &&
                condition.apply(criteria.getHousingStatus()) &&
                condition.apply(criteria.getResidenceSince()) &&
                condition.apply(criteria.getDisabilityStatus()) &&
                condition.apply(criteria.getSocialCategory()) &&
                condition.apply(criteria.getMemberId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SocialProfileCriteria> copyFiltersAre(
        SocialProfileCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMaritalStatus(), copy.getMaritalStatus()) &&
                condition.apply(criteria.getNumberOfChildren(), copy.getNumberOfChildren()) &&
                condition.apply(criteria.getNumberOfDependents(), copy.getNumberOfDependents()) &&
                condition.apply(criteria.getEducationLevel(), copy.getEducationLevel()) &&
                condition.apply(criteria.getHousingStatus(), copy.getHousingStatus()) &&
                condition.apply(criteria.getResidenceSince(), copy.getResidenceSince()) &&
                condition.apply(criteria.getDisabilityStatus(), copy.getDisabilityStatus()) &&
                condition.apply(criteria.getSocialCategory(), copy.getSocialCategory()) &&
                condition.apply(criteria.getMemberId(), copy.getMemberId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
