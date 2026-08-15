import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './economic-activity-type.reducer';

export const EconomicActivityTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const economicActivityTypeEntity = useAppSelector(state => state.economicActivityType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="economicActivityTypeDetailsHeading">
          <Translate contentKey="coopfullApp.economicActivityType.detail.title">EconomicActivityType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{economicActivityTypeEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="coopfullApp.economicActivityType.code">Code</Translate>
            </span>
          </dt>
          <dd>{economicActivityTypeEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopfullApp.economicActivityType.name">Name</Translate>
            </span>
          </dt>
          <dd>{economicActivityTypeEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.economicActivityType.description">Description</Translate>
            </span>
          </dt>
          <dd>{economicActivityTypeEntity.description}</dd>
          <dt>
            <span id="sector">
              <Translate contentKey="coopfullApp.economicActivityType.sector">Sector</Translate>
            </span>
          </dt>
          <dd>{economicActivityTypeEntity.sector}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="coopfullApp.economicActivityType.active">Active</Translate>
            </span>
          </dt>
          <dd>{economicActivityTypeEntity.active ? 'true' : 'false'}</dd>
        </dl>
        <Button as={Link as any} to="/economic-activity-type" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/economic-activity-type/${economicActivityTypeEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EconomicActivityTypeDetail;
