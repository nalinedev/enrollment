import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCooperatives } from 'app/entities/cooperative/cooperative.reducer';
import { getEntities as getLocations } from 'app/entities/location/location.reducer';
import { CooperativeStatus } from 'app/shared/model/enumerations/cooperative-status.model';

import { createEntity, getEntity, reset, updateEntity } from './cooperative-branch.reducer';

export const CooperativeBranchUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cooperatives = useAppSelector(state => state.cooperative.entities);
  const locations = useAppSelector(state => state.location.entities);
  const cooperativeBranchEntity = useAppSelector(state => state.cooperativeBranch.entity);
  const loading = useAppSelector(state => state.cooperativeBranch.loading);
  const updating = useAppSelector(state => state.cooperativeBranch.updating);
  const updateSuccess = useAppSelector(state => state.cooperativeBranch.updateSuccess);
  const cooperativeStatusValues = Object.keys(CooperativeStatus);

  const handleClose = () => {
    navigate('/cooperative-branch');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCooperatives({}));
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

    const entity = {
      ...cooperativeBranchEntity,
      ...values,
      cooperative: cooperatives.find(it => it.id.toString() === values.cooperative?.toString()),
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
          ...cooperativeBranchEntity,
          cooperative: cooperativeBranchEntity?.cooperative?.id,
          location: cooperativeBranchEntity?.location?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.cooperativeBranch.home.createOrEditLabel" data-cy="CooperativeBranchCreateUpdateHeading">
            <Translate contentKey="coopfullApp.cooperativeBranch.home.createOrEditLabel">Create or edit a CooperativeBranch</Translate>
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
                  id="cooperative-branch-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.cooperativeBranch.code')}
                id="cooperative-branch-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.cooperativeBranch.name')}
                id="cooperative-branch-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.cooperativeBranch.description')}
                id="cooperative-branch-description"
                name="description"
                data-cy="description"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.cooperativeBranch.phone')}
                id="cooperative-branch-phone"
                name="phone"
                data-cy="phone"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.cooperativeBranch.email')}
                id="cooperative-branch-email"
                name="email"
                data-cy="email"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.cooperativeBranch.status')}
                id="cooperative-branch-status"
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
                label={translate('coopfullApp.cooperativeBranch.openingDate')}
                id="cooperative-branch-openingDate"
                name="openingDate"
                data-cy="openingDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.cooperativeBranch.closingDate')}
                id="cooperative-branch-closingDate"
                name="closingDate"
                data-cy="closingDate"
                type="date"
              />
              <ValidatedField
                id="cooperative-branch-cooperative"
                name="cooperative"
                data-cy="cooperative"
                label={translate('coopfullApp.cooperativeBranch.cooperative')}
                type="select"
              >
                <option value="" key="0" />
                {cooperatives
                  ? cooperatives.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="cooperative-branch-location"
                name="location"
                data-cy="location"
                label={translate('coopfullApp.cooperativeBranch.location')}
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
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/cooperative-branch" replace variant="info">
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

export default CooperativeBranchUpdate;
