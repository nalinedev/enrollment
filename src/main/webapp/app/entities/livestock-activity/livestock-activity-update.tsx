import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getLivestockTypes } from 'app/entities/livestock-type/livestock-type.reducer';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { LivestockOwnershipType } from 'app/shared/model/enumerations/livestock-ownership-type.model';
import { LivestockProductionMode } from 'app/shared/model/enumerations/livestock-production-mode.model';
import { LivestockProductionType } from 'app/shared/model/enumerations/livestock-production-type.model';
import { LivestockStatus } from 'app/shared/model/enumerations/livestock-status.model';

import { createEntity, getEntity, reset, updateEntity } from './livestock-activity.reducer';

export const LivestockActivityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const locations = useAppSelector(state => state.location.entities);
  const livestockTypes = useAppSelector(state => state.livestockType.entities);
  const livestockActivityEntity = useAppSelector(state => state.livestockActivity.entity);
  const loading = useAppSelector(state => state.livestockActivity.loading);
  const updating = useAppSelector(state => state.livestockActivity.updating);
  const updateSuccess = useAppSelector(state => state.livestockActivity.updateSuccess);
  const livestockProductionModeValues = Object.keys(LivestockProductionMode);
  const livestockOwnershipTypeValues = Object.keys(LivestockOwnershipType);
  const livestockProductionTypeValues = Object.keys(LivestockProductionType);
  const livestockStatusValues = Object.keys(LivestockStatus);

  const handleClose = () => {
    navigate('/livestock-activity');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getLocations({}));
    dispatch(getLivestockTypes({}));
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
    if (values.totalArea !== undefined && typeof values.totalArea !== 'number') {
      values.totalArea = Number(values.totalArea);
    }
    if (values.numberOfAnimals !== undefined && typeof values.numberOfAnimals !== 'number') {
      values.numberOfAnimals = Number(values.numberOfAnimals);
    }
    if (values.annualRevenue !== undefined && typeof values.annualRevenue !== 'number') {
      values.annualRevenue = Number(values.annualRevenue);
    }
    if (values.monthlyRevenue !== undefined && typeof values.monthlyRevenue !== 'number') {
      values.monthlyRevenue = Number(values.monthlyRevenue);
    }
    if (values.employees !== undefined && typeof values.employees !== 'number') {
      values.employees = Number(values.employees);
    }

    const entity = {
      ...livestockActivityEntity,
      ...values,
      location: locations.find(it => it.id.toString() === values.location?.toString()),
      livestockType: livestockTypes.find(it => it.id.toString() === values.livestockType?.toString()),
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
          productionMode: 'FAMILY',
          ownershipType: 'OWNER',
          productionType: 'MEAT',
          status: 'ACTIVE',
          ...livestockActivityEntity,
          location: livestockActivityEntity?.location?.id,
          livestockType: livestockActivityEntity?.livestockType?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.livestockActivity.home.createOrEditLabel" data-cy="LivestockActivityCreateUpdateHeading">
            <Translate contentKey="coopfullApp.livestockActivity.home.createOrEditLabel">Create or edit a LivestockActivity</Translate>
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
                  id="livestock-activity-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.name')}
                id="livestock-activity-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.description')}
                id="livestock-activity-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.productionMode')}
                id="livestock-activity-productionMode"
                name="productionMode"
                data-cy="productionMode"
                type="select"
              >
                {livestockProductionModeValues.map(livestockProductionMode => (
                  <option value={livestockProductionMode} key={livestockProductionMode}>
                    {translate(`coopfullApp.LivestockProductionMode.${livestockProductionMode}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.ownershipType')}
                id="livestock-activity-ownershipType"
                name="ownershipType"
                data-cy="ownershipType"
                type="select"
              >
                {livestockOwnershipTypeValues.map(livestockOwnershipType => (
                  <option value={livestockOwnershipType} key={livestockOwnershipType}>
                    {translate(`coopfullApp.LivestockOwnershipType.${livestockOwnershipType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.productionType')}
                id="livestock-activity-productionType"
                name="productionType"
                data-cy="productionType"
                type="select"
              >
                {livestockProductionTypeValues.map(livestockProductionType => (
                  <option value={livestockProductionType} key={livestockProductionType}>
                    {translate(`coopfullApp.LivestockProductionType.${livestockProductionType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.startDate')}
                id="livestock-activity-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.totalArea')}
                id="livestock-activity-totalArea"
                name="totalArea"
                data-cy="totalArea"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.areaUnit')}
                id="livestock-activity-areaUnit"
                name="areaUnit"
                data-cy="areaUnit"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.status')}
                id="livestock-activity-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {livestockStatusValues.map(livestockStatus => (
                  <option value={livestockStatus} key={livestockStatus}>
                    {translate(`coopfullApp.LivestockStatus.${livestockStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.numberOfAnimals')}
                id="livestock-activity-numberOfAnimals"
                name="numberOfAnimals"
                data-cy="numberOfAnimals"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.annualRevenue')}
                id="livestock-activity-annualRevenue"
                name="annualRevenue"
                data-cy="annualRevenue"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.monthlyRevenue')}
                id="livestock-activity-monthlyRevenue"
                name="monthlyRevenue"
                data-cy="monthlyRevenue"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.employees')}
                id="livestock-activity-employees"
                name="employees"
                data-cy="employees"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.veterinaryServiceAvailable')}
                id="livestock-activity-veterinaryServiceAvailable"
                name="veterinaryServiceAvailable"
                data-cy="veterinaryServiceAvailable"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.feedSource')}
                id="livestock-activity-feedSource"
                name="feedSource"
                data-cy="feedSource"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.waterSource')}
                id="livestock-activity-waterSource"
                name="waterSource"
                data-cy="waterSource"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.certification')}
                id="livestock-activity-certification"
                name="certification"
                data-cy="certification"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockActivity.notes')}
                id="livestock-activity-notes"
                name="notes"
                data-cy="notes"
                type="textarea"
              />
              <ValidatedField
                id="livestock-activity-location"
                name="location"
                data-cy="location"
                label={translate('coopfullApp.livestockActivity.location')}
                type="select"
              >
                <option value="" key="0" />
                {locations
                  ? locations.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="livestock-activity-livestockType"
                name="livestockType"
                data-cy="livestockType"
                label={translate('coopfullApp.livestockActivity.livestockType')}
                type="select"
              >
                <option value="" key="0" />
                {livestockTypes
                  ? livestockTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/livestock-activity" replace variant="info">
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

export default LivestockActivityUpdate;
