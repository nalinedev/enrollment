import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { CooperativeStatus } from 'app/shared/model/enumerations/cooperative-status.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { createEntity, getEntity, reset, updateEntity } from './cooperative.reducer';

export const CooperativeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cooperativeEntity = useAppSelector(state => state.cooperative.entity);
  const loading = useAppSelector(state => state.cooperative.loading);
  const updating = useAppSelector(state => state.cooperative.updating);
  const updateSuccess = useAppSelector(state => state.cooperative.updateSuccess);
  const cooperativeStatusValues = Object.keys(CooperativeStatus);

  const handleClose = () => {
    navigate('/cooperative');
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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...cooperativeEntity,
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
      ? {
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          status: 'ACTIVE',
          ...cooperativeEntity,
          createdDate: convertDateTimeFromServer(cooperativeEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(cooperativeEntity.lastModifiedDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.cooperative.home.createOrEditLabel" data-cy="CooperativeCreateUpdateHeading">
            <Translate contentKey="coopfullApp.cooperative.home.createOrEditLabel">Create or edit a Cooperative</Translate>
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
                  id="cooperative-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.cooperative.code')}
                id="cooperative-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.cooperative.name')}
                id="cooperative-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.cooperative.legalName')}
                id="cooperative-legalName"
                name="legalName"
                data-cy="legalName"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.cooperative.registrationNumber')}
                id="cooperative-registrationNumber"
                name="registrationNumber"
                data-cy="registrationNumber"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.cooperative.taxNumber')}
                id="cooperative-taxNumber"
                name="taxNumber"
                data-cy="taxNumber"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.cooperative.description')}
                id="cooperative-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.cooperative.status')}
                id="cooperative-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {cooperativeStatusValues.map(cooperativeStatus => (
                  <option value={cooperativeStatus} key={cooperativeStatus}>
                    {translate(`coopfullApp.CooperativeStatus.${cooperativeStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.cooperative.foundedDate')}
                id="cooperative-foundedDate"
                name="foundedDate"
                data-cy="foundedDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.cooperative.email')}
                id="cooperative-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.cooperative.phone')}
                id="cooperative-phone"
                name="phone"
                data-cy="phone"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.cooperative.website')}
                id="cooperative-website"
                name="website"
                data-cy="website"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.cooperative.createdDate')}
                id="cooperative-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.cooperative.lastModifiedDate')}
                id="cooperative-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/cooperative" replace variant="info">
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

export default CooperativeUpdate;
