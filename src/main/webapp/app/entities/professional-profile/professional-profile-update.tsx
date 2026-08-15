import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './professional-profile.reducer';

export const ProfessionalProfileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const professionalProfileEntity = useAppSelector(state => state.professionalProfile.entity);
  const loading = useAppSelector(state => state.professionalProfile.loading);
  const updating = useAppSelector(state => state.professionalProfile.updating);
  const updateSuccess = useAppSelector(state => state.professionalProfile.updateSuccess);

  const handleClose = () => {
    navigate('/professional-profile');
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
    if (values.yearsOfExperience !== undefined && typeof values.yearsOfExperience !== 'number') {
      values.yearsOfExperience = Number(values.yearsOfExperience);
    }
    if (values.monthlyIncome !== undefined && typeof values.monthlyIncome !== 'number') {
      values.monthlyIncome = Number(values.monthlyIncome);
    }
    if (values.annualIncome !== undefined && typeof values.annualIncome !== 'number') {
      values.annualIncome = Number(values.annualIncome);
    }

    const entity = {
      ...professionalProfileEntity,
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
          ...professionalProfileEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.professionalProfile.home.createOrEditLabel" data-cy="ProfessionalProfileCreateUpdateHeading">
            <Translate contentKey="coopfullApp.professionalProfile.home.createOrEditLabel">Create or edit a ProfessionalProfile</Translate>
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
                  id="professional-profile-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.professionalProfile.employmentStatus')}
                id="professional-profile-employmentStatus"
                name="employmentStatus"
                data-cy="employmentStatus"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.professionalProfile.employerName')}
                id="professional-profile-employerName"
                name="employerName"
                data-cy="employerName"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.professionalProfile.jobTitle')}
                id="professional-profile-jobTitle"
                name="jobTitle"
                data-cy="jobTitle"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.professionalProfile.profession')}
                id="professional-profile-profession"
                name="profession"
                data-cy="profession"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.professionalProfile.sector')}
                id="professional-profile-sector"
                name="sector"
                data-cy="sector"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.professionalProfile.yearsOfExperience')}
                id="professional-profile-yearsOfExperience"
                name="yearsOfExperience"
                data-cy="yearsOfExperience"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.professionalProfile.monthlyIncome')}
                id="professional-profile-monthlyIncome"
                name="monthlyIncome"
                data-cy="monthlyIncome"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.professionalProfile.annualIncome')}
                id="professional-profile-annualIncome"
                name="annualIncome"
                data-cy="annualIncome"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.professionalProfile.employmentStartDate')}
                id="professional-profile-employmentStartDate"
                name="employmentStartDate"
                data-cy="employmentStartDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.professionalProfile.employerLocation')}
                id="professional-profile-employerLocation"
                name="employerLocation"
                data-cy="employerLocation"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.professionalProfile.notes')}
                id="professional-profile-notes"
                name="notes"
                data-cy="notes"
                type="textarea"
              />
              <Button
                as={Link as any}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/professional-profile"
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

export default ProfessionalProfileUpdate;
