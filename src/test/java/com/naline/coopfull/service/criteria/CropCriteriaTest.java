package com.naline.coopfull.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class CropCriteriaTest {

    @Test
    void newCropCriteriaHasAllFiltersNullTest() {
        var cropCriteria = new CropCriteria();
        assertThat(cropCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void cropCriteriaFluentMethodsCreatesFiltersTest() {
        var cropCriteria = new CropCriteria();

        setAllFilters(cropCriteria);

        assertThat(cropCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void cropCriteriaCopyCreatesNullFilterTest() {
        var cropCriteria = new CropCriteria();
        var copy = cropCriteria.copy();

        assertThat(cropCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(cropCriteria)
        );
    }

    @Test
    void cropCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var cropCriteria = new CropCriteria();
        setAllFilters(cropCriteria);

        var copy = cropCriteria.copy();

        assertThat(cropCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(copyFiltersAre(copy, (a, b) -> a == null || a instanceof Boolean ? a == b : a != b && a.equals(b))),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(cropCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var cropCriteria = new CropCriteria();

        assertThat(cropCriteria).hasToString("CropCriteria{}");
    }

    private static void setAllFilters(CropCriteria cropCriteria) {
        cropCriteria.id();
        cropCriteria.code();
        cropCriteria.name();
        cropCriteria.scientificName();
        cropCriteria.category();
        cropCriteria.perennial();
        cropCriteria.active();
        cropCriteria.distinct();
    }

    private static Condition<CropCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getScientificName()) &&
                condition.apply(criteria.getCategory()) &&
                condition.apply(criteria.getPerennial()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<CropCriteria> copyFiltersAre(CropCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getScientificName(), copy.getScientificName()) &&
                condition.apply(criteria.getCategory(), copy.getCategory()) &&
                condition.apply(criteria.getPerennial(), copy.getPerennial()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
