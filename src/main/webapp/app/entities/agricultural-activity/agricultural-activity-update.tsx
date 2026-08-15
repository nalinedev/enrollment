import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { AgriculturalExploitationMode } from 'app/shared/model/enumerations/agricultural-exploitation-mode.model';
import { LandOwnershipType } from 'app/shared/model/enumerations/land-ownership-type.model';

import { createEntity, getEntity, reset, updateEntity } from './agricultural-activity.reducer';

export const AgriculturalActivityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const locations = useAppSelector(state => state.location.entities);
  const agriculturalActivityEntity = useAppSelector(state => state.agriculturalActivity.entity);
  const loading = useAppSelector(state => state.agriculturalActivity.loading);
  const updating = useAppSelector(state => state.agriculturalActivity.updating);
  const updateSuccess = useAppSelector(state => state.agriculturalActivity.updateSuccess);
  const agriculturalExploitationModeValues = Object.keys(AgriculturalExploitationMode);
  const landOwnershipTypeValues = Object.keys(LandOwnershipType);

  const handleClose = () => {
    navigate('/agricultural-activity');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getLocations({}));
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

    const entity = {
      ...agriculturalActivityEntity,
      ...values,
      location: locations.find(it => it.id.toString() === values.location?.toString()),
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
          exploitationMode: 'FAMILY',
          ownershipType: 'OWNER',
          ...agriculturalActivityEntity,
          location: agriculturalActivityEntity?.location?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.agriculturalActivity.home.createOrEditLabel" data-cy="AgriculturalActivityCreateUpdateHeading">
            <Translate contentKey="coopfullApp.agriculturalActivity.home.createOrEditLabel">
              Create or edit a AgriculturalActivity
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
                  id="agricultural-activity-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.agriculturalActivity.totalArea')}
                id="agricultural-activity-totalArea"
                name="totalArea"
                data-cy="totalArea"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalActivity.areaUnit')}
                id="agricultural-activity-areaUnit"
                name="areaUnit"
                data-cy="areaUnit"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalActivity.exploitationMode')}
                id="agricultural-activity-exploitationMode"
                name="exploitationMode"
                data-cy="exploitationMode"
                type="select"
              >
                {agriculturalExploitationModeValues.map(agriculturalExploitationMode => (
                  <option value={agriculturalExploitationMode} key={agriculturalExploitationMode}>
                    {translate(`coopfullApp.AgriculturalExploitationMode.${agriculturalExploitationMode}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.agriculturalActivity.ownershipType')}
                id="agricultural-activity-ownershipType"
                name="ownershipType"
                data-cy="ownershipType"
                type="select"
              >
                {landOwnershipTypeValues.map(landOwnershipType => (
                  <option value={landOwnershipType} key={landOwnershipType}>
                    {translate(`coopfullApp.LandOwnershipType.${landOwnershipType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.agriculturalActivity.startDate')}
                id="agricultural-activity-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalActivity.irrigationAvailable')}
                id="agricultural-activity-irrigationAvailable"
                name="irrigationAvailable"
                data-cy="irrigationAvailable"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalActivity.organicProduction')}
                id="agricultural-activity-organicProduction"
                name="organicProduction"
                data-cy="organicProduction"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalActivity.certification')}
                id="agricultural-activity-certification"
                name="certification"
                data-cy="certification"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.agriculturalActivity.description')}
                id="agricultural-activity-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                id="agricultural-activity-location"
                name="location"
                data-cy="location"
                label={translate('coopfullApp.agriculturalActivity.location')}
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
              <Button
                as={Link as any}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/agricultural-activity"
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

export default AgriculturalActivityUpdate;
