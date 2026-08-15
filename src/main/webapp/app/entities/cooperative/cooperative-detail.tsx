import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cooperative.reducer';

export const CooperativeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const cooperativeEntity = useAppSelector(state => state.cooperative.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cooperativeDetailsHeading">
          <Translate contentKey="coopfullApp.cooperative.detail.title">Cooperative</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cooperativeEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="coopfullApp.cooperative.code">Code</Translate>
            </span>
          </dt>
          <dd>{cooperativeEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopfullApp.cooperative.name">Name</Translate>
            </span>
          </dt>
          <dd>{cooperativeEntity.name}</dd>
          <dt>
            <span id="legalName">
              <Translate contentKey="coopfullApp.cooperative.legalName">Legal Name</Translate>
            </span>
          </dt>
          <dd>{cooperativeEntity.legalName}</dd>
          <dt>
            <span id="registrationNumber">
              <Translate contentKey="coopfullApp.cooperative.registrationNumber">Registration Number</Translate>
            </span>
          </dt>
          <dd>{cooperativeEntity.registrationNumber}</dd>
          <dt>
            <span id="taxNumber">
              <Translate contentKey="coopfullApp.cooperative.taxNumber">Tax Number</Translate>
            </span>
          </dt>
          <dd>{cooperativeEntity.taxNumber}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.cooperative.description">Description</Translate>
            </span>
          </dt>
          <dd>{cooperativeEntity.description}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="coopfullApp.cooperative.status">Status</Translate>
            </span>
          </dt>
          <dd>{cooperativeEntity.status}</dd>
          <dt>
            <span id="foundedDate">
              <Translate contentKey="coopfullApp.cooperative.foundedDate">Founded Date</Translate>
            </span>
          </dt>
          <dd>
            {cooperativeEntity.foundedDate ? (
              <TextFormat value={cooperativeEntity.foundedDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="email">
              <Translate contentKey="coopfullApp.cooperative.email">Email</Translate>
            </span>
          </dt>
          <dd>{cooperativeEntity.email}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="coopfullApp.cooperative.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{cooperativeEntity.phone}</dd>
          <dt>
            <span id="website">
              <Translate contentKey="coopfullApp.cooperative.website">Website</Translate>
            </span>
          </dt>
          <dd>{cooperativeEntity.website}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="coopfullApp.cooperative.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {cooperativeEntity.createdDate ? (
              <TextFormat value={cooperativeEntity.createdDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="coopfullApp.cooperative.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {cooperativeEntity.lastModifiedDate ? (
              <TextFormat value={cooperativeEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button as={Link as any} to="/cooperative" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/cooperative/${cooperativeEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CooperativeDetail;
