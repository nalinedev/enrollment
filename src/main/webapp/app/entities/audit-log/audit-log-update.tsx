import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getAppUsers } from 'app/entities/app-user/app-user.reducer';
import { AuditAction } from 'app/shared/model/enumerations/audit-action.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { createEntity, getEntity, reset, updateEntity } from './audit-log.reducer';

export const AuditLogUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const appUsers = useAppSelector(state => state.appUser.entities);
  const auditLogEntity = useAppSelector(state => state.auditLog.entity);
  const loading = useAppSelector(state => state.auditLog.loading);
  const updating = useAppSelector(state => state.auditLog.updating);
  const updateSuccess = useAppSelector(state => state.auditLog.updateSuccess);
  const auditActionValues = Object.keys(AuditAction);

  const handleClose = () => {
    navigate('/audit-log');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getAppUsers({}));
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
    if (values.cooperativeId !== undefined && typeof values.cooperativeId !== 'number') {
      values.cooperativeId = Number(values.cooperativeId);
    }
    if (values.branchId !== undefined && typeof values.branchId !== 'number') {
      values.branchId = Number(values.branchId);
    }
    values.timestamp = convertDateTimeToServer(values.timestamp);

    const entity = {
      ...auditLogEntity,
      ...values,
      appUser: appUsers.find(it => it.id.toString() === values.appUser?.toString()),
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
          timestamp: displayDefaultDateTime(),
        }
      : {
          action: 'CREATE',
          ...auditLogEntity,
          timestamp: convertDateTimeFromServer(auditLogEntity.timestamp),
          appUser: auditLogEntity?.appUser?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.auditLog.home.createOrEditLabel" data-cy="AuditLogCreateUpdateHeading">
            <Translate contentKey="coopfullApp.auditLog.home.createOrEditLabel">Create or edit a AuditLog</Translate>
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
                  id="audit-log-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.auditLog.action')}
                id="audit-log-action"
                name="action"
                data-cy="action"
                type="select"
              >
                {auditActionValues.map(auditAction => (
                  <option value={auditAction} key={auditAction}>
                    {translate(`coopfullApp.AuditAction.${auditAction}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.auditLog.entityName')}
                id="audit-log-entityName"
                name="entityName"
                data-cy="entityName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.auditLog.entityId')}
                id="audit-log-entityId"
                name="entityId"
                data-cy="entityId"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.auditLog.username')}
                id="audit-log-username"
                name="username"
                data-cy="username"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.auditLog.cooperativeId')}
                id="audit-log-cooperativeId"
                name="cooperativeId"
                data-cy="cooperativeId"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.auditLog.branchId')}
                id="audit-log-branchId"
                name="branchId"
                data-cy="branchId"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.auditLog.timestamp')}
                id="audit-log-timestamp"
                name="timestamp"
                data-cy="timestamp"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.auditLog.ipAddress')}
                id="audit-log-ipAddress"
                name="ipAddress"
                data-cy="ipAddress"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.auditLog.userAgent')}
                id="audit-log-userAgent"
                name="userAgent"
                data-cy="userAgent"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.auditLog.oldValue')}
                id="audit-log-oldValue"
                name="oldValue"
                data-cy="oldValue"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.auditLog.newValue')}
                id="audit-log-newValue"
                name="newValue"
                data-cy="newValue"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.auditLog.description')}
                id="audit-log-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                id="audit-log-appUser"
                name="appUser"
                data-cy="appUser"
                label={translate('coopfullApp.auditLog.appUser')}
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
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/audit-log" replace variant="info">
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

export default AuditLogUpdate;
