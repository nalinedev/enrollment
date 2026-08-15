import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './individual-member.reducer';

export const IndividualMemberUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const individualMemberEntity = useAppSelector(state => state.individualMember.entity);
  const loading = useAppSelector(state => state.individualMember.loading);
  const updating = useAppSelector(state => state.individualMember.updating);
  const updateSuccess = useAppSelector(state => state.individualMember.updateSuccess);

  const handleClose = () => {
    navigate('/individual-member');
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

    const entity = {
      ...individualMemberEntity,
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
          ...individualMemberEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.individualMember.home.createOrEditLabel" data-cy="IndividualMemberCreateUpdateHeading">
            <Translate contentKey="coopfullApp.individualMember.home.createOrEditLabel">Create or edit a IndividualMember</Translate>
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
                  id="individual-member-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.individualMember.firstName')}
                id="individual-member-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.individualMember.middleName')}
                id="individual-member-middleName"
                name="middleName"
                data-cy="middleName"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.individualMember.lastName')}
                id="individual-member-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.individualMember.maidenName')}
                id="individual-member-maidenName"
                name="maidenName"
                data-cy="maidenName"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.individualMember.gender')}
                id="individual-member-gender"
                name="gender"
                data-cy="gender"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.individualMember.birthDate')}
                id="individual-member-birthDate"
                name="birthDate"
                data-cy="birthDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.individualMember.birthPlace')}
                id="individual-member-birthPlace"
                name="birthPlace"
                data-cy="birthPlace"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.individualMember.nationality')}
                id="individual-member-nationality"
                name="nationality"
                data-cy="nationality"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.individualMember.email')}
                id="individual-member-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.individualMember.phoneNumber')}
                id="individual-member-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.individualMember.occupation')}
                id="individual-member-occupation"
                name="occupation"
                data-cy="occupation"
                type="text"
              />
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/individual-member" replace variant="info">
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

export default IndividualMemberUpdate;
