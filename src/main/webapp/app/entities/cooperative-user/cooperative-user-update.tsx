import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getAppUsers } from 'app/entities/app-user/app-user.reducer';
import { getEntities as getCooperatives } from 'app/entities/cooperative/cooperative.reducer';
import { getEntities as getCooperativeRoles } from 'app/entities/cooperative-role/cooperative-role.reducer';

import { createEntity, getEntity, reset, updateEntity } from './cooperative-user.reducer';

export const CooperativeUserUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const appUsers = useAppSelector(state => state.appUser.entities);
  const cooperatives = useAppSelector(state => state.cooperative.entities);
  const cooperativeRoles = useAppSelector(state => state.cooperativeRole.entities);
  const cooperativeUserEntity = useAppSelector(state => state.cooperativeUser.entity);
  const loading = useAppSelector(state => state.cooperativeUser.loading);
  const updating = useAppSelector(state => state.cooperativeUser.updating);
  const updateSuccess = useAppSelector(state => state.cooperativeUser.updateSuccess);

  const handleClose = () => {
    navigate('/cooperative-user');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAppUsers({}));
    dispatch(getCooperatives({}));
    dispatch(getCooperativeRoles({}));
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
      ...cooperativeUserEntity,
      ...values,
      appUser: appUsers.find(it => it.id.toString() === values.appUser?.toString()),
      cooperative: cooperatives.find(it => it.id.toString() === values.cooperative?.toString()),
      role: cooperativeRoles.find(it => it.id.toString() === values.role?.toString()),
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
          ...cooperativeUserEntity,
          appUser: cooperativeUserEntity?.appUser?.id,
          cooperative: cooperativeUserEntity?.cooperative?.id,
          role: cooperativeUserEntity?.role?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.cooperativeUser.home.createOrEditLabel" data-cy="CooperativeUserCreateUpdateHeading">
            <Translate contentKey="coopfullApp.cooperativeUser.home.createOrEditLabel">Create or edit a CooperativeUser</Translate>
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
                  id="cooperative-user-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.cooperativeUser.startDate')}
                id="cooperative-user-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.cooperativeUser.endDate')}
                id="cooperative-user-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.cooperativeUser.active')}
                id="cooperative-user-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                id="cooperative-user-appUser"
                name="appUser"
                data-cy="appUser"
                label={translate('coopfullApp.cooperativeUser.appUser')}
                type="select"
              >
                <option value="" key="0" />
                {appUsers
                  ? appUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="cooperative-user-cooperative"
                name="cooperative"
                data-cy="cooperative"
                label={translate('coopfullApp.cooperativeUser.cooperative')}
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
                id="cooperative-user-role"
                name="role"
                data-cy="role"
                label={translate('coopfullApp.cooperativeUser.role')}
                type="select"
              >
                <option value="" key="0" />
                {cooperativeRoles
                  ? cooperativeRoles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/cooperative-user" replace variant="info">
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

export default CooperativeUserUpdate;
