import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './livestock-type.reducer';

export const LivestockTypeDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const livestockTypeEntity = useAppSelector(state => state.livestockType.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="livestockTypeDetailsHeading">
          <Translate contentKey="coopfullApp.livestockType.detail.title">LivestockType</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{livestockTypeEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="coopfullApp.livestockType.code">Code</Translate>
            </span>
          </dt>
          <dd>{livestockTypeEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopfullApp.livestockType.name">Name</Translate>
            </span>
          </dt>
          <dd>{livestockTypeEntity.name}</dd>
          <dt>
            <span id="scientificName">
              <Translate contentKey="coopfullApp.livestockType.scientificName">Scientific Name</Translate>
            </span>
          </dt>
          <dd>{livestockTypeEntity.scientificName}</dd>
          <dt>
            <span id="category">
              <Translate contentKey="coopfullApp.livestockType.category">Category</Translate>
            </span>
          </dt>
          <dd>{livestockTypeEntity.category}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.livestockType.description">Description</Translate>
            </span>
          </dt>
          <dd>{livestockTypeEntity.description}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="coopfullApp.livestockType.active">Active</Translate>
            </span>
          </dt>
          <dd>{livestockTypeEntity.active ? 'true' : 'false'}</dd>
        </dl>
        <Button as={Link as any} to="/livestock-type" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/livestock-type/${livestockTypeEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LivestockTypeDetail;
