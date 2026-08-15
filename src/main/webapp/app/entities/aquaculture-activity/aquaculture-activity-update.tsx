import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getAquaticSpecieses } from 'app/entities/aquatic-species/aquatic-species.reducer';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { AquacultureOwnershipType } from 'app/shared/model/enumerations/aquaculture-ownership-type.model';
import { AquacultureProductionMode } from 'app/shared/model/enumerations/aquaculture-production-mode.model';
import { AquacultureProductionType } from 'app/shared/model/enumerations/aquaculture-production-type.model';
import { AquacultureStatus } from 'app/shared/model/enumerations/aquaculture-status.model';
import { AquacultureSystemType } from 'app/shared/model/enumerations/aquaculture-system-type.model';

import { createEntity, getEntity, reset, updateEntity } from './aquaculture-activity.reducer';

export const AquacultureActivityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const locations = useAppSelector(state => state.location.entities);
  const aquaticSpecieses = useAppSelector(state => state.aquaticSpecies.entities);
  const aquacultureActivityEntity = useAppSelector(state => state.aquacultureActivity.entity);
  const loading = useAppSelector(state => state.aquacultureActivity.loading);
  const updating = useAppSelector(state => state.aquacultureActivity.updating);
  const updateSuccess = useAppSelector(state => state.aquacultureActivity.updateSuccess);
  const aquacultureProductionModeValues = Object.keys(AquacultureProductionMode);
  const aquacultureOwnershipTypeValues = Object.keys(AquacultureOwnershipType);
  const aquacultureProductionTypeValues = Object.keys(AquacultureProductionType);
  const aquacultureSystemTypeValues = Object.keys(AquacultureSystemType);
  const aquacultureStatusValues = Object.keys(AquacultureStatus);

  const handleClose = () => {
    navigate('/aquaculture-activity');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getLocations({}));
    dispatch(getAquaticSpecieses({}));
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
    if (values.numberOfProductionUnits !== undefined && typeof values.numberOfProductionUnits !== 'number') {
      values.numberOfProductionUnits = Number(values.numberOfProductionUnits);
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
      ...aquacultureActivityEntity,
      ...values,
      location: locations.find(it => it.id.toString() === values.location?.toString()),
      aquaticSpecies: aquaticSpecieses.find(it => it.id.toString() === values.aquaticSpecies?.toString()),
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
          productionType: 'FOOD',
          systemType: 'POND',
          status: 'ACTIVE',
          ...aquacultureActivityEntity,
          location: aquacultureActivityEntity?.location?.id,
          aquaticSpecies: aquacultureActivityEntity?.aquaticSpecies?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.aquacultureActivity.home.createOrEditLabel" data-cy="AquacultureActivityCreateUpdateHeading">
            <Translate contentKey="coopfullApp.aquacultureActivity.home.createOrEditLabel">Create or edit a AquacultureActivity</Translate>
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
                  id="aquaculture-activity-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.name')}
                id="aquaculture-activity-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.description')}
                id="aquaculture-activity-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.productionMode')}
                id="aquaculture-activity-productionMode"
                name="productionMode"
                data-cy="productionMode"
                type="select"
              >
                {aquacultureProductionModeValues.map(aquacultureProductionMode => (
                  <option value={aquacultureProductionMode} key={aquacultureProductionMode}>
                    {translate(`coopfullApp.AquacultureProductionMode.${aquacultureProductionMode}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.ownershipType')}
                id="aquaculture-activity-ownershipType"
                name="ownershipType"
                data-cy="ownershipType"
                type="select"
              >
                {aquacultureOwnershipTypeValues.map(aquacultureOwnershipType => (
                  <option value={aquacultureOwnershipType} key={aquacultureOwnershipType}>
                    {translate(`coopfullApp.AquacultureOwnershipType.${aquacultureOwnershipType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.productionType')}
                id="aquaculture-activity-productionType"
                name="productionType"
                data-cy="productionType"
                type="select"
              >
                {aquacultureProductionTypeValues.map(aquacultureProductionType => (
                  <option value={aquacultureProductionType} key={aquacultureProductionType}>
                    {translate(`coopfullApp.AquacultureProductionType.${aquacultureProductionType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.systemType')}
                id="aquaculture-activity-systemType"
                name="systemType"
                data-cy="systemType"
                type="select"
              >
                {aquacultureSystemTypeValues.map(aquacultureSystemType => (
                  <option value={aquacultureSystemType} key={aquacultureSystemType}>
                    {translate(`coopfullApp.AquacultureSystemType.${aquacultureSystemType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.startDate')}
                id="aquaculture-activity-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.totalArea')}
                id="aquaculture-activity-totalArea"
                name="totalArea"
                data-cy="totalArea"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.areaUnit')}
                id="aquaculture-activity-areaUnit"
                name="areaUnit"
                data-cy="areaUnit"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.waterSource')}
                id="aquaculture-activity-waterSource"
                name="waterSource"
                data-cy="waterSource"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.numberOfProductionUnits')}
                id="aquaculture-activity-numberOfProductionUnits"
                name="numberOfProductionUnits"
                data-cy="numberOfProductionUnits"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.productionUnitDescription')}
                id="aquaculture-activity-productionUnitDescription"
                name="productionUnitDescription"
                data-cy="productionUnitDescription"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.status')}
                id="aquaculture-activity-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {aquacultureStatusValues.map(aquacultureStatus => (
                  <option value={aquacultureStatus} key={aquacultureStatus}>
                    {translate(`coopfullApp.AquacultureStatus.${aquacultureStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.annualRevenue')}
                id="aquaculture-activity-annualRevenue"
                name="annualRevenue"
                data-cy="annualRevenue"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.monthlyRevenue')}
                id="aquaculture-activity-monthlyRevenue"
                name="monthlyRevenue"
                data-cy="monthlyRevenue"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.employees')}
                id="aquaculture-activity-employees"
                name="employees"
                data-cy="employees"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.certification')}
                id="aquaculture-activity-certification"
                name="certification"
                data-cy="certification"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquacultureActivity.notes')}
                id="aquaculture-activity-notes"
                name="notes"
                data-cy="notes"
                type="textarea"
              />
              <ValidatedField
                id="aquaculture-activity-location"
                name="location"
                data-cy="location"
                label={translate('coopfullApp.aquacultureActivity.location')}
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
                id="aquaculture-activity-aquaticSpecies"
                name="aquaticSpecies"
                data-cy="aquaticSpecies"
                label={translate('coopfullApp.aquacultureActivity.aquaticSpecies')}
                type="select"
              >
                <option value="" key="0" />
                {aquaticSpecieses
                  ? aquaticSpecieses.map(otherEntity => (
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
                to="/aquaculture-activity"
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

export default AquacultureActivityUpdate;
