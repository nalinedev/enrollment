import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './audit-log.reducer';

export const AuditLogDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const auditLogEntity = useAppSelector(state => state.auditLog.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="auditLogDetailsHeading">
          <Translate contentKey="coopfullApp.auditLog.detail.title">AuditLog</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.id}</dd>
          <dt>
            <span id="action">
              <Translate contentKey="coopfullApp.auditLog.action">Action</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.action}</dd>
          <dt>
            <span id="entityName">
              <Translate contentKey="coopfullApp.auditLog.entityName">Entity Name</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.entityName}</dd>
          <dt>
            <span id="entityId">
              <Translate contentKey="coopfullApp.auditLog.entityId">Entity Id</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.entityId}</dd>
          <dt>
            <span id="username">
              <Translate contentKey="coopfullApp.auditLog.username">Username</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.username}</dd>
          <dt>
            <span id="cooperativeId">
              <Translate contentKey="coopfullApp.auditLog.cooperativeId">Cooperative Id</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.cooperativeId}</dd>
          <dt>
            <span id="branchId">
              <Translate contentKey="coopfullApp.auditLog.branchId">Branch Id</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.branchId}</dd>
          <dt>
            <span id="timestamp">
              <Translate contentKey="coopfullApp.auditLog.timestamp">Timestamp</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.timestamp ? <TextFormat value={auditLogEntity.timestamp} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="ipAddress">
              <Translate contentKey="coopfullApp.auditLog.ipAddress">Ip Address</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.ipAddress}</dd>
          <dt>
            <span id="userAgent">
              <Translate contentKey="coopfullApp.auditLog.userAgent">User Agent</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.userAgent}</dd>
          <dt>
            <span id="oldValue">
              <Translate contentKey="coopfullApp.auditLog.oldValue">Old Value</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.oldValue}</dd>
          <dt>
            <span id="newValue">
              <Translate contentKey="coopfullApp.auditLog.newValue">New Value</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.newValue}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.auditLog.description">Description</Translate>
            </span>
          </dt>
          <dd>{auditLogEntity.description}</dd>
          <dt>
            <Translate contentKey="coopfullApp.auditLog.appUser">App User</Translate>
          </dt>
          <dd>{auditLogEntity.appUser ? auditLogEntity.appUser.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/audit-log" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/audit-log/${auditLogEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AuditLogDetail;
