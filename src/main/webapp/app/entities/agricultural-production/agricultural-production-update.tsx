import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getAgriculturalActivities } from 'app/entities/agricultural-activity/agricultural-activity.reducer';
import { getEntities as getCrops } from 'app/entities/crop/crop.reducer';
import { getEntities as getCropVarieties } from 'app/entities/crop-variety/crop-variety.reducer';
import { ProductionStatus } from 'app/shared/model/enumerations/production-status.model';

import { createEntity, getEntity, reset, updateEntity } from './agricultural-production.reducer';

export const AgriculturalProductionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const agriculturalActivities = useAppSelector(state => state.agriculturalActivity.entities);
  const crops = useAppSelector(state => state.crop.entities);
  const cropVarieties = useAppSelector(state => state.cropVariety.entities);
  const agriculturalProductionEntity = useAppSelector(state => state.agriculturalProduction.entity);
  const loading = useAppSelector(state => state.agriculturalProduction.loading);
  const updating = useAppSelector(state => state.agriculturalProduction.updating);
  const updateSuccess = useAppSelector(state => state.agriculturalProduction.updateSuccess);
  const productionStatusValues = Object.keys(ProductionStatus);

  const handleClose = () => {
    navigate('/agricultural-production');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAgriculturalActivities({}));
    dispatch(getCrops({}));
    dispatch(getCropVarieties({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.area !== undefined && typeof values.area !== 'number') {
      values.area = Number(values.area);
    }
    if (values.productionQuantity !== undefined && typeof values.productionQuantity !== 'number') {
      values.productionQuantity = Number(values.productionQuantity);
    }
    if (values.expectedAnnualProduction !== undefined && typeof values.expectedAnnualProduction !== 'number') {
      values.expectedAnnualProduction = Number(values.expectedAnnualProduction);
    }
    if (values.numberOfPlants !== undefined && typeof values.numberOfPlants !== 'number') {
      values.numberOfPlants = Number(values.numberOfPlants);
    }
    if (values.plantingDensity !== undefined && typeof values.plantingDensity !== 'number') {
      values.plantingDensity = Number(values.plantingDensity);
    }
    if (values.productionYear !== undefined && typeof values.productionYear !== 'number') {
      values.productionYear = Number(values.productionYear);
    }

    const entity = {
      ...agriculturalProductionEntity,
      ...values,
      agriculturalActivity: agriculturalActivities.find(it => it.id.toString() === values.agriculturalActivity?.toString()),
      crop: crops.find(it => it.id.toString() === values.crop?.toString()),
      cropVariety: cropVarieties.find(it => it.id.toString() === values.cropVariety?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          status: 'PLANNED',
          ...agriculturalProductionEntity,
          agriculturalActivity: agriculturalProductionEntity?.agriculturalActivity?.id,
          crop: agriculturalProductionEntity?.crop?.id,
          cropVariety: agriculturalProductionEntity?.cropVariety?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.agriculturalProduction.home.createOrEditLabel" data-cy="AgriculturalProductionCreateUpdateHeading">
            <Translate contentKey="coopfullApp.agriculturalProduction.home.createOrEditLabel">
              Create or edit a AgriculturalProduction
            </Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew && (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="agricultural-production-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.agriculturalProduction.area')}
                id="agricultural-production-area"
                name="area"
                data-cy="area"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalProduction.areaUnit')}
                id="agricultural-production-areaUnit"
                name="areaUnit"
                data-cy="areaUnit"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalProduction.plantingDate')}
                id="agricultural-production-plantingDate"
                name="plantingDate"
                data-cy="plantingDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalProduction.harvestStartDate')}
                id="agricultural-production-harvestStartDate"
                name="harvestStartDate"
                data-cy="harvestStartDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalProduction.harvestEndDate')}
                id="agricultural-production-harvestEndDate"
                name="harvestEndDate"
                data-cy="harvestEndDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalProduction.productionQuantity')}
                id="agricultural-production-productionQuantity"
                name="productionQuantity"
                data-cy="productionQuantity"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalProduction.productionUnit')}
                id="agricultural-production-productionUnit"
                name="productionUnit"
                data-cy="productionUnit"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalProduction.expectedAnnualProduction')}
                id="agricultural-production-expectedAnnualProduction"
                name="expectedAnnualProduction"
                data-cy="expectedAnnualProduction"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalProduction.numberOfPlants')}
                id="agricultural-production-numberOfPlants"
                name="numberOfPlants"
                data-cy="numberOfPlants"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalProduction.plantingDensity')}
                id="agricultural-production-plantingDensity"
                name="plantingDensity"
                data-cy="plantingDensity"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalProduction.productionYear')}
                id="agricultural-production-productionYear"
                name="productionYear"
                data-cy="productionYear"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalProduction.status')}
                id="agricultural-production-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {productionStatusValues.map(productionStatus => (
                  <option value={productionStatus} key={productionStatus}>
                    {translate(`coopfullApp.ProductionStatus.${productionStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.agriculturalProduction.notes')}
                id="agricultural-production-notes"
                name="notes"
                data-cy="notes"
                type="textarea"
              />
              <ValidatedField
                id="agricultural-production-agriculturalActivity"
                name="agriculturalActivity"
                data-cy="agriculturalActivity"
                label={translate('coopfullApp.agriculturalProduction.agriculturalActivity')}
                type="select"
              >
                <option value="" key="0" />
                {agriculturalActivities
                  ? agriculturalActivities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="agricultural-production-crop"
                name="crop"
                data-cy="crop"
                label={translate('coopfullApp.agriculturalProduction.crop')}
                type="select"
              >
                <option value="" key="0" />
                {crops
                  ? crops.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="agricultural-production-cropVariety"
                name="cropVariety"
                data-cy="cropVariety"
                label={translate('coopfullApp.agriculturalProduction.cropVariety')}
                type="select"
              >
                <option value="" key="0" />
                {cropVarieties
                  ? cropVarieties.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button
                as={Link as any}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/agricultural-production"
                replace
                variant="info"
              >
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button variant="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default AgriculturalProductionUpdate;
