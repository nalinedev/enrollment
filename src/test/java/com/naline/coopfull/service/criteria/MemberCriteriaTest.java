package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MemberCriteriaTest {

    @Test
    void newMemberCriteriaHasAllFiltersNullTest() {
        var memberCriteria = new MemberCriteria();
        assertThat(memberCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void memberCriteriaFluentMethodsCreatesFiltersTest() {
        var memberCriteria = new MemberCriteria();

        setAllFilters(memberCriteria);

        assertThat(memberCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void memberCriteriaCopyCreatesNullFilterTest() {
        var memberCriteria = new MemberCriteria();
        var copy = memberCriteria.copy();

        assertThat(memberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(memberCriteria)
        );
    }

    @Test
    void memberCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var memberCriteria = new MemberCriteria();
        setAllFilters(memberCriteria);

        var copy = memberCriteria.copy();

        assertThat(memberCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(memberCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var memberCriteria = new MemberCriteria();

        assertThat(memberCriteria).hasToString("MemberCriteria{}");
    }

    private static void setAllFilters(MemberCriteria memberCriteria) {
        memberCriteria.id();
        memberCriteria.memberNumber();
        memberCriteria.memberType();
        memberCriteria.status();
        memberCriteria.admissionDate();
        memberCriteria.exitDate();
        memberCriteria.exitReason();
        memberCriteria.createdDate();
        memberCriteria.lastModifiedDate();
        memberCriteria.individualMemberId();
        memberCriteria.organizationMemberId();
        memberCriteria.socialProfileId();
        memberCriteria.professionalProfileId();
        memberCriteria.cooperativeId();
        memberCriteria.branchId();
        memberCriteria.distinct();
    }

    private static Condition<MemberCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getMemberNumber()) &&
                condition.apply(criteria.getMemberType()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getAdmissionDate()) &&
                condition.apply(criteria.getExitDate()) &&
                condition.apply(criteria.getExitReason()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getIndividualMemberId()) &&
                condition.apply(criteria.getOrganizationMemberId()) &&
                condition.apply(criteria.getSocialProfileId()) &&
                condition.apply(criteria.getProfessionalProfileId()) &&
                condition.apply(criteria.getCooperativeId()) &&
                condition.apply(criteria.getBranchId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MemberCriteria> copyFiltersAre(MemberCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getMemberNumber(), copy.getMemberNumber()) &&
                condition.apply(criteria.getMemberType(), copy.getMemberType()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getAdmissionDate(), copy.getAdmissionDate()) &&
                condition.apply(criteria.getExitDate(), copy.getExitDate()) &&
                condition.apply(criteria.getExitReason(), copy.getExitReason()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getIndividualMemberId(), copy.getIndividualMemberId()) &&
                condition.apply(criteria.getOrganizationMemberId(), copy.getOrganizationMemberId()) &&
                condition.apply(criteria.getSocialProfileId(), copy.getSocialProfileId()) &&
                condition.apply(criteria.getProfessionalProfileId(), copy.getProfessionalProfileId()) &&
                condition.apply(criteria.getCooperativeId(), copy.getCooperativeId()) &&
                condition.apply(criteria.getBranchId(), copy.getBranchId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
