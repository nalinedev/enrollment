import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cooperative-user.reducer';

export const CooperativeUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const cooperativeUserEntity = useAppSelector(state => state.cooperativeUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cooperativeUserDetailsHeading">
          <Translate contentKey="coopfullApp.cooperativeUser.detail.title">CooperativeUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cooperativeUserEntity.id}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="coopfullApp.cooperativeUser.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {cooperativeUserEntity.startDate ? (
              <TextFormat value={cooperativeUserEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="coopfullApp.cooperativeUser.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {cooperativeUserEntity.endDate ? (
              <TextFormat value={cooperativeUserEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="active">
              <Translate contentKey="coopfullApp.cooperativeUser.active">Active</Translate>
            </span>
          </dt>
          <dd>{cooperativeUserEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="coopfullApp.cooperativeUser.appUser">App User</Translate>
          </dt>
          <dd>{cooperativeUserEntity.appUser ? cooperativeUserEntity.appUser.id : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.cooperativeUser.cooperative">Cooperative</Translate>
          </dt>
          <dd>{cooperativeUserEntity.cooperative ? cooperativeUserEntity.cooperative.name : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.cooperativeUser.role">Role</Translate>
          </dt>
          <dd>{cooperativeUserEntity.role ? cooperativeUserEntity.role.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/cooperative-user" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/cooperative-user/${cooperativeUserEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CooperativeUserDetail;
