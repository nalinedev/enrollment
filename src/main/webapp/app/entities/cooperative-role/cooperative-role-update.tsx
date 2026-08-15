import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getPermissions } from 'app/entities/permission/permission.reducer';
import { CooperativeRoleStatus } from 'app/shared/model/enumerations/cooperative-role-status.model';
import { mapIdList } from 'app/shared/util/entity-utils';

import { createEntity, getEntity, reset, updateEntity } from './cooperative-role.reducer';

export const CooperativeRoleUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const permissions = useAppSelector(state => state.permission.entities);
  const cooperativeRoleEntity = useAppSelector(state => state.cooperativeRole.entity);
  const loading = useAppSelector(state => state.cooperativeRole.loading);
  const updating = useAppSelector(state => state.cooperativeRole.updating);
  const updateSuccess = useAppSelector(state => state.cooperativeRole.updateSuccess);
  const cooperativeRoleStatusValues = Object.keys(CooperativeRoleStatus);

  const handleClose = () => {
    navigate('/cooperative-role');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getPermissions({}));
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
      ...cooperativeRoleEntity,
      ...values,
      permissionses: mapIdList(values.permissionses),
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
          ...cooperativeRoleEntity,
          permissionses: cooperativeRoleEntity?.permissionses?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.cooperativeRole.home.createOrEditLabel" data-cy="CooperativeRoleCreateUpdateHeading">
            <Translate contentKey="coopfullApp.cooperativeRole.home.createOrEditLabel">Create or edit a CooperativeRole</Translate>
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
                  id="cooperative-role-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.cooperativeRole.code')}
                id="cooperative-role-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.cooperativeRole.name')}
                id="cooperative-role-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.cooperativeRole.description')}
                id="cooperative-role-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.cooperativeRole.status')}
                id="cooperative-role-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {cooperativeRoleStatusValues.map(cooperativeRoleStatus => (
                  <option value={cooperativeRoleStatus} key={cooperativeRoleStatus}>
                    {translate(`coopfullApp.CooperativeRoleStatus.${cooperativeRoleStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.cooperativeRole.permissions')}
                id="cooperative-role-permissions"
                data-cy="permissions"
                type="select"
                multiple
                name="permissionses"
              >
                <option value="" key="0" />
                {permissions
                  ? permissions.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.code}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/cooperative-role" replace variant="info">
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

export default CooperativeRoleUpdate;
