package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ProfessionalProfileCriteriaTest {

    @Test
    void newProfessionalProfileCriteriaHasAllFiltersNullTest() {
        var professionalProfileCriteria = new ProfessionalProfileCriteria();
        assertThat(professionalProfileCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void professionalProfileCriteriaFluentMethodsCreatesFiltersTest() {
        var professionalProfileCriteria = new ProfessionalProfileCriteria();

        setAllFilters(professionalProfileCriteria);

        assertThat(professionalProfileCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void professionalProfileCriteriaCopyCreatesNullFilterTest() {
        var professionalProfileCriteria = new ProfessionalProfileCriteria();
        var copy = professionalProfileCriteria.copy();

        assertThat(professionalProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(professionalProfileCriteria)
        );
    }

    @Test
    void professionalProfileCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var professionalProfileCriteria = new ProfessionalProfileCriteria();
        setAllFilters(professionalProfileCriteria);

        var copy = professionalProfileCriteria.copy();

        assertThat(professionalProfileCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(professionalProfileCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var professionalProfileCriteria = new ProfessionalProfileCriteria();

        assertThat(professionalProfileCriteria).hasToString("ProfessionalProfileCriteria{}");
    }

    private static void setAllFilters(ProfessionalProfileCriteria professionalProfileCriteria) {
        professionalProfileCriteria.id();
        professionalProfileCriteria.employmentStatus();
        professionalProfileCriteria.employerName();
        professionalProfileCriteria.jobTitle();
        professionalProfileCriteria.profession();
        professionalProfileCriteria.sector();
        professionalProfileCriteria.yearsOfExperience();
        professionalProfileCriteria.monthlyIncome();
        professionalProfileCriteria.annualIncome();
        professionalProfileCriteria.employmentStartDate();
        professionalProfileCriteria.employerLocation();
        professionalProfileCriteria.memberId();
        professionalProfileCriteria.distinct();
    }

    private static Condition<ProfessionalProfileCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getEmploymentStatus()) &&
                condition.apply(criteria.getEmployerName()) &&
                condition.apply(criteria.getJobTitle()) &&
                condition.apply(criteria.getProfession()) &&
                condition.apply(criteria.getSector()) &&
                condition.apply(criteria.getYearsOfExperience()) &&
                condition.apply(criteria.getMonthlyIncome()) &&
                condition.apply(criteria.getAnnualIncome()) &&
                condition.apply(criteria.getEmploymentStartDate()) &&
                condition.apply(criteria.getEmployerLocation()) &&
                condition.apply(criteria.getMemberId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ProfessionalProfileCriteria> copyFiltersAre(
        ProfessionalProfileCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getEmploymentStatus(), copy.getEmploymentStatus()) &&
                condition.apply(criteria.getEmployerName(), copy.getEmployerName()) &&
                condition.apply(criteria.getJobTitle(), copy.getJobTitle()) &&
                condition.apply(criteria.getProfession(), copy.getProfession()) &&
                condition.apply(criteria.getSector(), copy.getSector()) &&
                condition.apply(criteria.getYearsOfExperience(), copy.getYearsOfExperience()) &&
                condition.apply(criteria.getMonthlyIncome(), copy.getMonthlyIncome()) &&
                condition.apply(criteria.getAnnualIncome(), copy.getAnnualIncome()) &&
                condition.apply(criteria.getEmploymentStartDate(), copy.getEmploymentStartDate()) &&
                condition.apply(criteria.getEmployerLocation(), copy.getEmployerLocation()) &&
                condition.apply(criteria.getMemberId(), copy.getMemberId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
