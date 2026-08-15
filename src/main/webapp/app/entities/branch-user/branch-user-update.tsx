import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getAppUsers } from 'app/entities/app-user/app-user.reducer';
import { getEntities as getCooperativeBranches } from 'app/entities/cooperative-branch/cooperative-branch.reducer';
import { getEntities as getCooperativeRoles } from 'app/entities/cooperative-role/cooperative-role.reducer';

import { createEntity, getEntity, reset, updateEntity } from './branch-user.reducer';

export const BranchUserUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const appUsers = useAppSelector(state => state.appUser.entities);
  const cooperativeBranches = useAppSelector(state => state.cooperativeBranch.entities);
  const cooperativeRoles = useAppSelector(state => state.cooperativeRole.entities);
  const branchUserEntity = useAppSelector(state => state.branchUser.entity);
  const loading = useAppSelector(state => state.branchUser.loading);
  const updating = useAppSelector(state => state.branchUser.updating);
  const updateSuccess = useAppSelector(state => state.branchUser.updateSuccess);

  const handleClose = () => {
    navigate('/branch-user');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAppUsers({}));
    dispatch(getCooperativeBranches({}));
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
      ...branchUserEntity,
      ...values,
      appUser: appUsers.find(it => it.id.toString() === values.appUser?.toString()),
      branch: cooperativeBranches.find(it => it.id.toString() === values.branch?.toString()),
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
          ...branchUserEntity,
          appUser: branchUserEntity?.appUser?.id,
          branch: branchUserEntity?.branch?.id,
          role: branchUserEntity?.role?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.branchUser.home.createOrEditLabel" data-cy="BranchUserCreateUpdateHeading">
            <Translate contentKey="coopfullApp.branchUser.home.createOrEditLabel">Create or edit a BranchUser</Translate>
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
                  id="branch-user-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.branchUser.startDate')}
                id="branch-user-startDate"
                name="startDate"
                data-cy="startDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.branchUser.endDate')}
                id="branch-user-endDate"
                name="endDate"
                data-cy="endDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.branchUser.active')}
                id="branch-user-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                id="branch-user-appUser"
                name="appUser"
                data-cy="appUser"
                label={translate('coopfullApp.branchUser.appUser')}
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
                id="branch-user-branch"
                name="branch"
                data-cy="branch"
                label={translate('coopfullApp.branchUser.branch')}
                type="select"
              >
                <option value="" key="0" />
                {cooperativeBranches
                  ? cooperativeBranches.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="branch-user-role"
                name="role"
                data-cy="role"
                label={translate('coopfullApp.branchUser.role')}
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
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/branch-user" replace variant="info">
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

export default BranchUserUpdate;
