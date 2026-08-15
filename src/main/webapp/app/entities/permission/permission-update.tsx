import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCooperativeRoles } from 'app/entities/cooperative-role/cooperative-role.reducer';
import { mapIdList } from 'app/shared/util/entity-utils';

import { createEntity, getEntity, reset, updateEntity } from './permission.reducer';

export const PermissionUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cooperativeRoles = useAppSelector(state => state.cooperativeRole.entities);
  const permissionEntity = useAppSelector(state => state.permission.entity);
  const loading = useAppSelector(state => state.permission.loading);
  const updating = useAppSelector(state => state.permission.updating);
  const updateSuccess = useAppSelector(state => state.permission.updateSuccess);

  const handleClose = () => {
    navigate('/permission');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
      ...permissionEntity,
      ...values,
      roleses: mapIdList(values.roleses),
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
          ...permissionEntity,
          roleses: permissionEntity?.roleses?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.permission.home.createOrEditLabel" data-cy="PermissionCreateUpdateHeading">
            <Translate contentKey="coopfullApp.permission.home.createOrEditLabel">Create or edit a Permission</Translate>
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
                  id="permission-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.permission.code')}
                id="permission-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.permission.name')}
                id="permission-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.permission.description')}
                id="permission-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.permission.resource')}
                id="permission-resource"
                name="resource"
                data-cy="resource"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.permission.action')}
                id="permission-action"
                name="action"
                data-cy="action"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.permission.active')}
                id="permission-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('coopfullApp.permission.roles')}
                id="permission-roles"
                data-cy="roles"
                type="select"
                multiple
                name="roleses"
              >
                <option value="" key="0" />
                {cooperativeRoles
                  ? cooperativeRoles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/permission" replace variant="info">
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

export default PermissionUpdate;
