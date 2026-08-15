package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class IdentityDocumentCriteriaTest {

    @Test
    void newIdentityDocumentCriteriaHasAllFiltersNullTest() {
        var identityDocumentCriteria = new IdentityDocumentCriteria();
        assertThat(identityDocumentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void identityDocumentCriteriaFluentMethodsCreatesFiltersTest() {
        var identityDocumentCriteria = new IdentityDocumentCriteria();

        setAllFilters(identityDocumentCriteria);

        assertThat(identityDocumentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void identityDocumentCriteriaCopyCreatesNullFilterTest() {
        var identityDocumentCriteria = new IdentityDocumentCriteria();
        var copy = identityDocumentCriteria.copy();

        assertThat(identityDocumentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(identityDocumentCriteria)
        );
    }

    @Test
    void identityDocumentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var identityDocumentCriteria = new IdentityDocumentCriteria();
        setAllFilters(identityDocumentCriteria);

        var copy = identityDocumentCriteria.copy();

        assertThat(identityDocumentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(identityDocumentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var identityDocumentCriteria = new IdentityDocumentCriteria();

        assertThat(identityDocumentCriteria).hasToString("IdentityDocumentCriteria{}");
    }

    private static void setAllFilters(IdentityDocumentCriteria identityDocumentCriteria) {
        identityDocumentCriteria.id();
        identityDocumentCriteria.documentType();
        identityDocumentCriteria.documentNumber();
        identityDocumentCriteria.issueDate();
        identityDocumentCriteria.expiryDate();
        identityDocumentCriteria.issuingAuthority();
        identityDocumentCriteria.issuingCountry();
        identityDocumentCriteria.status();
        identityDocumentCriteria.verified();
        identityDocumentCriteria.verificationDate();
        identityDocumentCriteria.verificationComment();
        identityDocumentCriteria.memberId();
        identityDocumentCriteria.distinct();
    }

    private static Condition<IdentityDocumentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentType()) &&
                condition.apply(criteria.getDocumentNumber()) &&
                condition.apply(criteria.getIssueDate()) &&
                condition.apply(criteria.getExpiryDate()) &&
                condition.apply(criteria.getIssuingAuthority()) &&
                condition.apply(criteria.getIssuingCountry()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getVerified()) &&
                condition.apply(criteria.getVerificationDate()) &&
                condition.apply(criteria.getVerificationComment()) &&
                condition.apply(criteria.getMemberId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<IdentityDocumentCriteria> copyFiltersAre(
        IdentityDocumentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentType(), copy.getDocumentType()) &&
                condition.apply(criteria.getDocumentNumber(), copy.getDocumentNumber()) &&
                condition.apply(criteria.getIssueDate(), copy.getIssueDate()) &&
                condition.apply(criteria.getExpiryDate(), copy.getExpiryDate()) &&
                condition.apply(criteria.getIssuingAuthority(), copy.getIssuingAuthority()) &&
                condition.apply(criteria.getIssuingCountry(), copy.getIssuingCountry()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getVerified(), copy.getVerified()) &&
                condition.apply(criteria.getVerificationDate(), copy.getVerificationDate()) &&
                condition.apply(criteria.getVerificationComment(), copy.getVerificationComment()) &&
                condition.apply(criteria.getMemberId(), copy.getMemberId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
