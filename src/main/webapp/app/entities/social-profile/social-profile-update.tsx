import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { MaritalStatus } from 'app/shared/model/enumerations/marital-status.model';

import { createEntity, getEntity, reset, updateEntity } from './social-profile.reducer';

export const SocialProfileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const socialProfileEntity = useAppSelector(state => state.socialProfile.entity);
  const loading = useAppSelector(state => state.socialProfile.loading);
  const updating = useAppSelector(state => state.socialProfile.updating);
  const updateSuccess = useAppSelector(state => state.socialProfile.updateSuccess);
  const maritalStatusValues = Object.keys(MaritalStatus);

  const handleClose = () => {
    navigate('/social-profile');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
    if (values.numberOfChildren !== undefined && typeof values.numberOfChildren !== 'number') {
      values.numberOfChildren = Number(values.numberOfChildren);
    }
    if (values.numberOfDependents !== undefined && typeof values.numberOfDependents !== 'number') {
      values.numberOfDependents = Number(values.numberOfDependents);
    }

    const entity = {
      ...socialProfileEntity,
      ...values,
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
          maritalStatus: 'SINGLE',
          ...socialProfileEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.socialProfile.home.createOrEditLabel" data-cy="SocialProfileCreateUpdateHeading">
            <Translate contentKey="coopfullApp.socialProfile.home.createOrEditLabel">Create or edit a SocialProfile</Translate>
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
                  id="social-profile-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.socialProfile.maritalStatus')}
                id="social-profile-maritalStatus"
                name="maritalStatus"
                data-cy="maritalStatus"
                type="select"
              >
                {maritalStatusValues.map(maritalStatus => (
                  <option value={maritalStatus} key={maritalStatus}>
                    {translate(`coopfullApp.MaritalStatus.${maritalStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.socialProfile.numberOfChildren')}
                id="social-profile-numberOfChildren"
                name="numberOfChildren"
                data-cy="numberOfChildren"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.socialProfile.numberOfDependents')}
                id="social-profile-numberOfDependents"
                name="numberOfDependents"
                data-cy="numberOfDependents"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.socialProfile.educationLevel')}
                id="social-profile-educationLevel"
                name="educationLevel"
                data-cy="educationLevel"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.socialProfile.housingStatus')}
                id="social-profile-housingStatus"
                name="housingStatus"
                data-cy="housingStatus"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.socialProfile.residenceSince')}
                id="social-profile-residenceSince"
                name="residenceSince"
                data-cy="residenceSince"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.socialProfile.disabilityStatus')}
                id="social-profile-disabilityStatus"
                name="disabilityStatus"
                data-cy="disabilityStatus"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('coopfullApp.socialProfile.disabilityDescription')}
                id="social-profile-disabilityDescription"
                name="disabilityDescription"
                data-cy="disabilityDescription"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.socialProfile.socialCategory')}
                id="social-profile-socialCategory"
                name="socialCategory"
                data-cy="socialCategory"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.socialProfile.notes')}
                id="social-profile-notes"
                name="notes"
                data-cy="notes"
                type="textarea"
              />
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/social-profile" replace variant="info">
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

export default SocialProfileUpdate;
