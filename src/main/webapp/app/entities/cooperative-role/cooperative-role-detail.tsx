import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cooperative-role.reducer';

export const CooperativeRoleDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const cooperativeRoleEntity = useAppSelector(state => state.cooperativeRole.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cooperativeRoleDetailsHeading">
          <Translate contentKey="coopfullApp.cooperativeRole.detail.title">CooperativeRole</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cooperativeRoleEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="coopfullApp.cooperativeRole.code">Code</Translate>
            </span>
          </dt>
          <dd>{cooperativeRoleEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopfullApp.cooperativeRole.name">Name</Translate>
            </span>
          </dt>
          <dd>{cooperativeRoleEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.cooperativeRole.description">Description</Translate>
            </span>
          </dt>
          <dd>{cooperativeRoleEntity.description}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="coopfullApp.cooperativeRole.status">Status</Translate>
            </span>
          </dt>
          <dd>{cooperativeRoleEntity.status}</dd>
          <dt>
            <Translate contentKey="coopfullApp.cooperativeRole.permissions">Permissions</Translate>
          </dt>
          <dd>
            {cooperativeRoleEntity.permissionses
              ? cooperativeRoleEntity.permissionses.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.code}</a>
                    {cooperativeRoleEntity.permissionses && i === cooperativeRoleEntity.permissionses.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button as={Link as any} to="/cooperative-role" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/cooperative-role/${cooperativeRoleEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CooperativeRoleDetail;
