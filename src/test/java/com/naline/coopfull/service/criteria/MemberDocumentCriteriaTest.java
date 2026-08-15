package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class MemberDocumentCriteriaTest {

    @Test
    void newMemberDocumentCriteriaHasAllFiltersNullTest() {
        var memberDocumentCriteria = new MemberDocumentCriteria();
        assertThat(memberDocumentCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void memberDocumentCriteriaFluentMethodsCreatesFiltersTest() {
        var memberDocumentCriteria = new MemberDocumentCriteria();

        setAllFilters(memberDocumentCriteria);

        assertThat(memberDocumentCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void memberDocumentCriteriaCopyCreatesNullFilterTest() {
        var memberDocumentCriteria = new MemberDocumentCriteria();
        var copy = memberDocumentCriteria.copy();

        assertThat(memberDocumentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(memberDocumentCriteria)
        );
    }

    @Test
    void memberDocumentCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var memberDocumentCriteria = new MemberDocumentCriteria();
        setAllFilters(memberDocumentCriteria);

        var copy = memberDocumentCriteria.copy();

        assertThat(memberDocumentCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(memberDocumentCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var memberDocumentCriteria = new MemberDocumentCriteria();

        assertThat(memberDocumentCriteria).hasToString("MemberDocumentCriteria{}");
    }

    private static void setAllFilters(MemberDocumentCriteria memberDocumentCriteria) {
        memberDocumentCriteria.id();
        memberDocumentCriteria.documentType();
        memberDocumentCriteria.originalFileName();
        memberDocumentCriteria.storedFileName();
        memberDocumentCriteria.contentType();
        memberDocumentCriteria.fileSize();
        memberDocumentCriteria.storagePath();
        memberDocumentCriteria.checksum();
        memberDocumentCriteria.verificationStatus();
        memberDocumentCriteria.uploadedAt();
        memberDocumentCriteria.verifiedAt();
        memberDocumentCriteria.memberId();
        memberDocumentCriteria.uploadedById();
        memberDocumentCriteria.distinct();
    }

    private static Condition<MemberDocumentCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getDocumentType()) &&
                condition.apply(criteria.getOriginalFileName()) &&
                condition.apply(criteria.getStoredFileName()) &&
                condition.apply(criteria.getContentType()) &&
                condition.apply(criteria.getFileSize()) &&
                condition.apply(criteria.getStoragePath()) &&
                condition.apply(criteria.getChecksum()) &&
                condition.apply(criteria.getVerificationStatus()) &&
                condition.apply(criteria.getUploadedAt()) &&
                condition.apply(criteria.getVerifiedAt()) &&
                condition.apply(criteria.getMemberId()) &&
                condition.apply(criteria.getUploadedById()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<MemberDocumentCriteria> copyFiltersAre(
        MemberDocumentCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getDocumentType(), copy.getDocumentType()) &&
                condition.apply(criteria.getOriginalFileName(), copy.getOriginalFileName()) &&
                condition.apply(criteria.getStoredFileName(), copy.getStoredFileName()) &&
                condition.apply(criteria.getContentType(), copy.getContentType()) &&
                condition.apply(criteria.getFileSize(), copy.getFileSize()) &&
                condition.apply(criteria.getStoragePath(), copy.getStoragePath()) &&
                condition.apply(criteria.getChecksum(), copy.getChecksum()) &&
                condition.apply(criteria.getVerificationStatus(), copy.getVerificationStatus()) &&
                condition.apply(criteria.getUploadedAt(), copy.getUploadedAt()) &&
                condition.apply(criteria.getVerifiedAt(), copy.getVerifiedAt()) &&
                condition.apply(criteria.getMemberId(), copy.getMemberId()) &&
                condition.apply(criteria.getUploadedById(), copy.getUploadedById()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
