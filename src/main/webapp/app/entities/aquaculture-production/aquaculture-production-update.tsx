import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getAquacultureActivities } from 'app/entities/aquaculture-activity/aquaculture-activity.reducer';
import { AquacultureProductionStatus } from 'app/shared/model/enumerations/aquaculture-production-status.model';

import { createEntity, getEntity, reset, updateEntity } from './aquaculture-production.reducer';

export const AquacultureProductionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const aquacultureActivities = useAppSelector(state => state.aquacultureActivity.entities);
  const aquacultureProductionEntity = useAppSelector(state => state.aquacultureProduction.entity);
  const loading = useAppSelector(state => state.aquacultureProduction.loading);
  const updating = useAppSelector(state => state.aquacultureProduction.updating);
  const updateSuccess = useAppSelector(state => state.aquacultureProduction.updateSuccess);
  const aquacultureProductionStatusValues = Object.keys(AquacultureProductionStatus);

  const handleClose = () => {
    navigate('/aquaculture-production');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAquacultureActivities({}));
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
    if (values.numberOfAnimals !== undefined && typeof values.numberOfAnimals !== 'number') {
      values.numberOfAnimals = Number(values.numberOfAnimals);
    }
    if (values.stockingDensity !== undefined && typeof values.stockingDensity !== 'number') {
      values.stockingDensity = Number(values.stockingDensity);
    }
    if (values.productionQuantity !== undefined && typeof values.productionQuantity !== 'number') {
      values.productionQuantity = Number(values.productionQuantity);
    }
    if (values.averageWeightGrams !== undefined && typeof values.averageWeightGrams !== 'number') {
      values.averageWeightGrams = Number(values.averageWeightGrams);
    }
    if (values.mortalityCount !== undefined && typeof values.mortalityCount !== 'number') {
      values.mortalityCount = Number(values.mortalityCount);
    }
    if (values.stockingCount !== undefined && typeof values.stockingCount !== 'number') {
      values.stockingCount = Number(values.stockingCount);
    }
    if (values.harvestedCount !== undefined && typeof values.harvestedCount !== 'number') {
      values.harvestedCount = Number(values.harvestedCount);
    }
    if (values.expectedProduction !== undefined && typeof values.expectedProduction !== 'number') {
      values.expectedProduction = Number(values.expectedProduction);
    }

    const entity = {
      ...aquacultureProductionEntity,
      ...values,
      aquacultureActivity: aquacultureActivities.find(it => it.id.toString() === values.aquacultureActivity?.toString()),
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
          ...aquacultureProductionEntity,
          aquacultureActivity: aquacultureProductionEntity?.aquacultureActivity?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.aquacultureProduction.home.createOrEditLabel" data-cy="AquacultureProductionCreateUpdateHeading">
            <Translate contentKey="coopfullApp.aquacultureProduction.home.createOrEditLabel">
              Create or edit a AquacultureProduction
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
                  id="aquaculture-production-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.aquacultureProduction.productionDate')}
                id="aquaculture-production-productionDate"
                name="productionDate"
                data-cy="productionDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureProduction.numberOfAnimals')}
                id="aquaculture-production-numberOfAnimals"
                name="numberOfAnimals"
                data-cy="numberOfAnimals"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureProduction.stockingDensity')}
                id="aquaculture-production-stockingDensity"
                name="stockingDensity"
                data-cy="stockingDensity"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureProduction.productionQuantity')}
                id="aquaculture-production-productionQuantity"
                name="productionQuantity"
                data-cy="productionQuantity"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureProduction.productionUnit')}
                id="aquaculture-production-productionUnit"
                name="productionUnit"
                data-cy="productionUnit"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureProduction.averageWeightGrams')}
                id="aquaculture-production-averageWeightGrams"
                name="averageWeightGrams"
                data-cy="averageWeightGrams"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureProduction.mortalityCount')}
                id="aquaculture-production-mortalityCount"
                name="mortalityCount"
                data-cy="mortalityCount"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureProduction.stockingCount')}
                id="aquaculture-production-stockingCount"
                name="stockingCount"
                data-cy="stockingCount"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureProduction.harvestedCount')}
                id="aquaculture-production-harvestedCount"
                name="harvestedCount"
                data-cy="harvestedCount"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureProduction.expectedProduction')}
                id="aquaculture-production-expectedProduction"
                name="expectedProduction"
                data-cy="expectedProduction"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureProduction.expectedHarvestDate')}
                id="aquaculture-production-expectedHarvestDate"
                name="expectedHarvestDate"
                data-cy="expectedHarvestDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureProduction.actualHarvestDate')}
                id="aquaculture-production-actualHarvestDate"
                name="actualHarvestDate"
                data-cy="actualHarvestDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureProduction.status')}
                id="aquaculture-production-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {aquacultureProductionStatusValues.map(aquacultureProductionStatus => (
                  <option value={aquacultureProductionStatus} key={aquacultureProductionStatus}>
                    {translate(`coopfullApp.AquacultureProductionStatus.${aquacultureProductionStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.aquacultureProduction.notes')}
                id="aquaculture-production-notes"
                name="notes"
                data-cy="notes"
                type="textarea"
              />
              <ValidatedField
                id="aquaculture-production-aquacultureActivity"
                name="aquacultureActivity"
                data-cy="aquacultureActivity"
                label={translate('coopfullApp.aquacultureProduction.aquacultureActivity')}
                type="select"
              >
                <option value="" key="0" />
                {aquacultureActivities
                  ? aquacultureActivities.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button
                as={Link as any}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/aquaculture-production"
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

export default AquacultureProductionUpdate;
