import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getLivestockActivities } from 'app/entities/livestock-activity/livestock-activity.reducer';
import { AnimalProductionStatus } from 'app/shared/model/enumerations/animal-production-status.model';
import { AnimalSex } from 'app/shared/model/enumerations/animal-sex.model';

import { createEntity, getEntity, reset, updateEntity } from './livestock-production.reducer';

export const LivestockProductionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const livestockActivities = useAppSelector(state => state.livestockActivity.entities);
  const livestockProductionEntity = useAppSelector(state => state.livestockProduction.entity);
  const loading = useAppSelector(state => state.livestockProduction.loading);
  const updating = useAppSelector(state => state.livestockProduction.updating);
  const updateSuccess = useAppSelector(state => state.livestockProduction.updateSuccess);
  const animalSexValues = Object.keys(AnimalSex);
  const animalProductionStatusValues = Object.keys(AnimalProductionStatus);

  const handleClose = () => {
    navigate('/livestock-production');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getLivestockActivities({}));
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
    if (values.averageAgeMonths !== undefined && typeof values.averageAgeMonths !== 'number') {
      values.averageAgeMonths = Number(values.averageAgeMonths);
    }
    if (values.averageWeightKg !== undefined && typeof values.averageWeightKg !== 'number') {
      values.averageWeightKg = Number(values.averageWeightKg);
    }
    if (values.productionQuantity !== undefined && typeof values.productionQuantity !== 'number') {
      values.productionQuantity = Number(values.productionQuantity);
    }
    if (values.mortalityCount !== undefined && typeof values.mortalityCount !== 'number') {
      values.mortalityCount = Number(values.mortalityCount);
    }
    if (values.birthCount !== undefined && typeof values.birthCount !== 'number') {
      values.birthCount = Number(values.birthCount);
    }
    if (values.soldCount !== undefined && typeof values.soldCount !== 'number') {
      values.soldCount = Number(values.soldCount);
    }

    const entity = {
      ...livestockProductionEntity,
      ...values,
      livestockActivity: livestockActivities.find(it => it.id.toString() === values.livestockActivity?.toString()),
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
          animalSex: 'MALE',
          status: 'ACTIVE',
          ...livestockProductionEntity,
          livestockActivity: livestockProductionEntity?.livestockActivity?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.livestockProduction.home.createOrEditLabel" data-cy="LivestockProductionCreateUpdateHeading">
            <Translate contentKey="coopfullApp.livestockProduction.home.createOrEditLabel">Create or edit a LivestockProduction</Translate>
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
                  id="livestock-production-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.livestockProduction.productionDate')}
                id="livestock-production-productionDate"
                name="productionDate"
                data-cy="productionDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockProduction.animalSex')}
                id="livestock-production-animalSex"
                name="animalSex"
                data-cy="animalSex"
                type="select"
              >
                {animalSexValues.map(animalSex => (
                  <option value={animalSex} key={animalSex}>
                    {translate(`coopfullApp.AnimalSex.${animalSex}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.livestockProduction.numberOfAnimals')}
                id="livestock-production-numberOfAnimals"
                name="numberOfAnimals"
                data-cy="numberOfAnimals"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.livestockProduction.averageAgeMonths')}
                id="livestock-production-averageAgeMonths"
                name="averageAgeMonths"
                data-cy="averageAgeMonths"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockProduction.averageWeightKg')}
                id="livestock-production-averageWeightKg"
                name="averageWeightKg"
                data-cy="averageWeightKg"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockProduction.productionQuantity')}
                id="livestock-production-productionQuantity"
                name="productionQuantity"
                data-cy="productionQuantity"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockProduction.productionUnit')}
                id="livestock-production-productionUnit"
                name="productionUnit"
                data-cy="productionUnit"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockProduction.mortalityCount')}
                id="livestock-production-mortalityCount"
                name="mortalityCount"
                data-cy="mortalityCount"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockProduction.birthCount')}
                id="livestock-production-birthCount"
                name="birthCount"
                data-cy="birthCount"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockProduction.soldCount')}
                id="livestock-production-soldCount"
                name="soldCount"
                data-cy="soldCount"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockProduction.status')}
                id="livestock-production-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {animalProductionStatusValues.map(animalProductionStatus => (
                  <option value={animalProductionStatus} key={animalProductionStatus}>
                    {translate(`coopfullApp.AnimalProductionStatus.${animalProductionStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.livestockProduction.notes')}
                id="livestock-production-notes"
                name="notes"
                data-cy="notes"
                type="textarea"
              />
              <ValidatedField
                id="livestock-production-livestockActivity"
                name="livestockActivity"
                data-cy="livestockActivity"
                label={translate('coopfullApp.livestockProduction.livestockActivity')}
                type="select"
              >
                <option value="" key="0" />
                {livestockActivities
                  ? livestockActivities.map(otherEntity => (
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
                to="/livestock-production"
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

export default LivestockProductionUpdate;
