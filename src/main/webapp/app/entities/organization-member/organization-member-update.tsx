import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './organization-member.reducer';

export const OrganizationMemberUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const organizationMemberEntity = useAppSelector(state => state.organizationMember.entity);
  const loading = useAppSelector(state => state.organizationMember.loading);
  const updating = useAppSelector(state => state.organizationMember.updating);
  const updateSuccess = useAppSelector(state => state.organizationMember.updateSuccess);

  const handleClose = () => {
    navigate('/organization-member');
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
      ...organizationMemberEntity,
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
          ...organizationMemberEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.organizationMember.home.createOrEditLabel" data-cy="OrganizationMemberCreateUpdateHeading">
            <Translate contentKey="coopfullApp.organizationMember.home.createOrEditLabel">Create or edit a OrganizationMember</Translate>
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
                  id="organization-member-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.organizationMember.legalName')}
                id="organization-member-legalName"
                name="legalName"
                data-cy="legalName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.organizationMember.tradeName')}
                id="organization-member-tradeName"
                name="tradeName"
                data-cy="tradeName"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.organizationMember.registrationNumber')}
                id="organization-member-registrationNumber"
                name="registrationNumber"
                data-cy="registrationNumber"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.organizationMember.taxNumber')}
                id="organization-member-taxNumber"
                name="taxNumber"
                data-cy="taxNumber"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.organizationMember.legalForm')}
                id="organization-member-legalForm"
                name="legalForm"
                data-cy="legalForm"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.organizationMember.registrationDate')}
                id="organization-member-registrationDate"
                name="registrationDate"
                data-cy="registrationDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.organizationMember.email')}
                id="organization-member-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.organizationMember.phoneNumber')}
                id="organization-member-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.organizationMember.website')}
                id="organization-member-website"
                name="website"
                data-cy="website"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.organizationMember.description')}
                id="organization-member-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/organization-member" replace variant="info">
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

export default OrganizationMemberUpdate;
