import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getAgriculturalActivities } from 'app/entities/agricultural-activity/agricultural-activity.reducer';
import { getEntities as getAquacultureActivities } from 'app/entities/aquaculture-activity/aquaculture-activity.reducer';
import { getEntities as getEconomicActivityTypes } from 'app/entities/economic-activity-type/economic-activity-type.reducer';
import { getEntities as getLivestockActivities } from 'app/entities/livestock-activity/livestock-activity.reducer';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { getEntities as getMembers } from 'app/entities/member/member.reducer';
import { EconomicActivityStatus } from 'app/shared/model/enumerations/economic-activity-status.model';

import { createEntity, getEntity, reset, updateEntity } from './economic-activity.reducer';

export const EconomicActivityUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const agriculturalActivities = useAppSelector(state => state.agriculturalActivity.entities);
  const livestockActivities = useAppSelector(state => state.livestockActivity.entities);
  const aquacultureActivities = useAppSelector(state => state.aquacultureActivity.entities);
  const members = useAppSelector(state => state.member.entities);
  const economicActivityTypes = useAppSelector(state => state.economicActivityType.entities);
  const locations = useAppSelector(state => state.location.entities);
  const economicActivityEntity = useAppSelector(state => state.economicActivity.entity);
  const loading = useAppSelector(state => state.economicActivity.loading);
  const updating = useAppSelector(state => state.economicActivity.updating);
  const updateSuccess = useAppSelector(state => state.economicActivity.updateSuccess);
  const economicActivityStatusValues = Object.keys(EconomicActivityStatus);

  const handleClose = () => {
    navigate('/economic-activity');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAgriculturalActivities({}));
    dispatch(getLivestockActivities({}));
    dispatch(getAquacultureActivities({}));
    dispatch(getMembers({}));
    dispatch(getEconomicActivityTypes({}));
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
    if (values.annualRevenue !== undefined && typeof values.annualRevenue !== 'number') {
      values.annualRevenue = Number(values.annualRevenue);
    }
    if (values.monthlyRevenue !== undefined && typeof values.monthlyRevenue !== 'number') {
      values.monthlyRevenue = Number(values.monthlyRevenue);
    }
    if (values.numberOfEmployees !== undefined && typeof values.numberOfEmployees !== 'number') {
      values.numberOfEmployees = Number(values.numberOfEmployees);
    }

    const entity = {
      ...economicActivityEntity,
      ...values,
      agriculturalActivity: agriculturalActivities.find(it => it.id.toString() === values.agriculturalActivity?.toString()),
      livestockActivity: livestockActivities.find(it => it.id.toString() === values.livestockActivity?.toString()),
      aquacultureActivity: aquacultureActivities.find(it => it.id.toString() === values.aquacultureActivity?.toString()),
      member: members.find(it => it.id.toString() === values.member?.toString()),
      activityType: economicActivityTypes.find(it => it.id.toString() === values.activityType?.toString()),
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
          status: 'ACTIVE',
          ...economicActivityEntity,
          agriculturalActivity: economicActivityEntity?.agriculturalActivity?.id,
          livestockActivity: economicActivityEntity?.livestockActivity?.id,
          aquacultureActivity: economicActivityEntity?.aquacultureActivity?.id,
          member: economicActivityEntity?.member?.id,
          activityType: economicActivityEntity?.activityType?.id,
          location: economicActivityEntity?.location?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.economicActivity.home.createOrEditLabel" data-cy="EconomicActivityCreateUpdateHeading">
            <Translate contentKey="coopfullApp.economicActivity.home.createOrEditLabel">Create or edit a EconomicActivity</Translate>
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
                  id="economic-activity-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.economicActivity.name')}
                id="economic-activity-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.economicActivity.description')}
                id="economic-activity-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.economicActivity.mainActivity')}
                id="economic-activity-mainActivity"
                name="mainActivity"
                data-cy="mainActivity"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('coopfullApp.economicActivity.startDate')}
                id="economic-activity-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.economicActivity.endDate')}
                id="economic-activity-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.economicActivity.annualRevenue')}
                id="economic-activity-annualRevenue"
                name="annualRevenue"
                data-cy="annualRevenue"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.economicActivity.monthlyRevenue')}
                id="economic-activity-monthlyRevenue"
                name="monthlyRevenue"
                data-cy="monthlyRevenue"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.economicActivity.numberOfEmployees')}
                id="economic-activity-numberOfEmployees"
                name="numberOfEmployees"
                data-cy="numberOfEmployees"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.economicActivity.status')}
                id="economic-activity-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {economicActivityStatusValues.map(economicActivityStatus => (
                  <option value={economicActivityStatus} key={economicActivityStatus}>
                    {translate(`coopfullApp.EconomicActivityStatus.${economicActivityStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.economicActivity.notes')}
                id="economic-activity-notes"
                name="notes"
                data-cy="notes"
                type="textarea"
              />
              <ValidatedField
                id="economic-activity-agriculturalActivity"
                name="agriculturalActivity"
                data-cy="agriculturalActivity"
                label={translate('coopfullApp.economicActivity.agriculturalActivity')}
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
                id="economic-activity-livestockActivity"
                name="livestockActivity"
                data-cy="livestockActivity"
                label={translate('coopfullApp.economicActivity.livestockActivity')}
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
              <ValidatedField
                id="economic-activity-aquacultureActivity"
                name="aquacultureActivity"
                data-cy="aquacultureActivity"
                label={translate('coopfullApp.economicActivity.aquacultureActivity')}
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
              <ValidatedField
                id="economic-activity-member"
                name="member"
                data-cy="member"
                label={translate('coopfullApp.economicActivity.member')}
                type="select"
              >
                <option value="" key="0" />
                {members
                  ? members.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.memberNumber}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="economic-activity-activityType"
                name="activityType"
                data-cy="activityType"
                label={translate('coopfullApp.economicActivity.activityType')}
                type="select"
              >
                <option value="" key="0" />
                {economicActivityTypes
                  ? economicActivityTypes.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="economic-activity-location"
                name="location"
                data-cy="location"
                label={translate('coopfullApp.economicActivity.location')}
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
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/economic-activity" replace variant="info">
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

export default EconomicActivityUpdate;
