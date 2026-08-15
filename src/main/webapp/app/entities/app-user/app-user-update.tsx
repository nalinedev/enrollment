import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { AppUserStatus } from 'app/shared/model/enumerations/app-user-status.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { createEntity, getEntity, reset, updateEntity } from './app-user.reducer';

export const AppUserUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const appUserEntity = useAppSelector(state => state.appUser.entity);
  const loading = useAppSelector(state => state.appUser.loading);
  const updating = useAppSelector(state => state.appUser.updating);
  const updateSuccess = useAppSelector(state => state.appUser.updateSuccess);
  const appUserStatusValues = Object.keys(AppUserStatus);

  const handleClose = () => {
    navigate('/app-user');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
      ...appUserEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
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
          ...appUserEntity,
          createdDate: convertDateTimeFromServer(appUserEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(appUserEntity.lastModifiedDate),
          user: appUserEntity?.user?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.appUser.home.createOrEditLabel" data-cy="AppUserCreateUpdateHeading">
            <Translate contentKey="coopfullApp.appUser.home.createOrEditLabel">Create or edit a AppUser</Translate>
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
                  id="app-user-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.appUser.employeeNumber')}
                id="app-user-employeeNumber"
                name="employeeNumber"
                data-cy="employeeNumber"
                type="text"
                validate={{}}
              />
              <ValidatedField
                label={translate('coopfullApp.appUser.firstName')}
                id="app-user-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.appUser.lastName')}
                id="app-user-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.appUser.phoneNumber')}
                id="app-user-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.appUser.jobTitle')}
                id="app-user-jobTitle"
                name="jobTitle"
                data-cy="jobTitle"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.appUser.department')}
                id="app-user-department"
                name="department"
                data-cy="department"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.appUser.status')}
                id="app-user-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {appUserStatusValues.map(appUserStatus => (
                  <option value={appUserStatus} key={appUserStatus}>
                    {translate(`coopfullApp.AppUserStatus.${appUserStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.appUser.createdDate')}
                id="app-user-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.appUser.lastModifiedDate')}
                id="app-user-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField id="app-user-user" name="user" data-cy="user" label={translate('coopfullApp.appUser.user')} type="select">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/app-user" replace variant="info">
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

export default AppUserUpdate;
