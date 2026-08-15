package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MembershipApplicationCriteriaTest {

    @Test
    void newMembershipApplicationCriteriaHasAllFiltersNullTest() {
        var membershipApplicationCriteria = new MembershipApplicationCriteria();
        assertThat(membershipApplicationCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void membershipApplicationCriteriaFluentMethodsCreatesFiltersTest() {
        var membershipApplicationCriteria = new MembershipApplicationCriteria();

        setAllFilters(membershipApplicationCriteria);

        assertThat(membershipApplicationCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void membershipApplicationCriteriaCopyCreatesNullFilterTest() {
        var membershipApplicationCriteria = new MembershipApplicationCriteria();
        var copy = membershipApplicationCriteria.copy();

        assertThat(membershipApplicationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(membershipApplicationCriteria)
        );
    }

    @Test
    void membershipApplicationCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var membershipApplicationCriteria = new MembershipApplicationCriteria();
        setAllFilters(membershipApplicationCriteria);

        var copy = membershipApplicationCriteria.copy();

        assertThat(membershipApplicationCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(membershipApplicationCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var membershipApplicationCriteria = new MembershipApplicationCriteria();

        assertThat(membershipApplicationCriteria).hasToString("MembershipApplicationCriteria{}");
    }

    private static void setAllFilters(MembershipApplicationCriteria membershipApplicationCriteria) {
        membershipApplicationCriteria.id();
        membershipApplicationCriteria.applicationNumber();
        membershipApplicationCriteria.status();
        membershipApplicationCriteria.applicationDate();
        membershipApplicationCriteria.submittedAt();
        membershipApplicationCriteria.reviewedAt();
        membershipApplicationCriteria.approvedAt();
        membershipApplicationCriteria.rejectedAt();
        membershipApplicationCriteria.confirmation();
        membershipApplicationCriteria.memberId();
        membershipApplicationCriteria.cooperativeId();
        membershipApplicationCriteria.branchId();
        membershipApplicationCriteria.distinct();
    }

    private static Condition<MembershipApplicationCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getApplicationNumber()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getApplicationDate()) &&
                condition.apply(criteria.getSubmittedAt()) &&
                condition.apply(criteria.getReviewedAt()) &&
                condition.apply(criteria.getApprovedAt()) &&
                condition.apply(criteria.getRejectedAt()) &&
                condition.apply(criteria.getConfirmation()) &&
                condition.apply(criteria.getMemberId()) &&
                condition.apply(criteria.getCooperativeId()) &&
                condition.apply(criteria.getBranchId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MembershipApplicationCriteria> copyFiltersAre(
        MembershipApplicationCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getApplicationNumber(), copy.getApplicationNumber()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getApplicationDate(), copy.getApplicationDate()) &&
                condition.apply(criteria.getSubmittedAt(), copy.getSubmittedAt()) &&
                condition.apply(criteria.getReviewedAt(), copy.getReviewedAt()) &&
                condition.apply(criteria.getApprovedAt(), copy.getApprovedAt()) &&
                condition.apply(criteria.getRejectedAt(), copy.getRejectedAt()) &&
                condition.apply(criteria.getConfirmation(), copy.getConfirmation()) &&
                condition.apply(criteria.getMemberId(), copy.getMemberId()) &&
                condition.apply(criteria.getCooperativeId(), copy.getCooperativeId()) &&
                condition.apply(criteria.getBranchId(), copy.getBranchId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
