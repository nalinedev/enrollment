import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './permission.reducer';

export const PermissionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const permissionEntity = useAppSelector(state => state.permission.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="permissionDetailsHeading">
          <Translate contentKey="coopfullApp.permission.detail.title">Permission</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{permissionEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="coopfullApp.permission.code">Code</Translate>
            </span>
          </dt>
          <dd>{permissionEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopfullApp.permission.name">Name</Translate>
            </span>
          </dt>
          <dd>{permissionEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.permission.description">Description</Translate>
            </span>
          </dt>
          <dd>{permissionEntity.description}</dd>
          <dt>
            <span id="resource">
              <Translate contentKey="coopfullApp.permission.resource">Resource</Translate>
            </span>
          </dt>
          <dd>{permissionEntity.resource}</dd>
          <dt>
            <span id="action">
              <Translate contentKey="coopfullApp.permission.action">Action</Translate>
            </span>
          </dt>
          <dd>{permissionEntity.action}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="coopfullApp.permission.active">Active</Translate>
            </span>
          </dt>
          <dd>{permissionEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="coopfullApp.permission.roles">Roles</Translate>
          </dt>
          <dd>
            {permissionEntity.roleses
              ? permissionEntity.roleses.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {permissionEntity.roleses && i === permissionEntity.roleses.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button as={Link as any} to="/permission" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/permission/${permissionEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default PermissionDetail;
