import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './location.reducer';

export const LocationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const locationEntity = useAppSelector(state => state.location.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="locationDetailsHeading">
          <Translate contentKey="coopfullApp.location.detail.title">Location</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{locationEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="coopfullApp.location.code">Code</Translate>
            </span>
          </dt>
          <dd>{locationEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopfullApp.location.name">Name</Translate>
            </span>
          </dt>
          <dd>{locationEntity.name}</dd>
          <dt>
            <span id="type">
              <Translate contentKey="coopfullApp.location.type">Type</Translate>
            </span>
          </dt>
          <dd>{locationEntity.type}</dd>
          <dt>
            <span id="addressLine1">
              <Translate contentKey="coopfullApp.location.addressLine1">Address Line 1</Translate>
            </span>
          </dt>
          <dd>{locationEntity.addressLine1}</dd>
          <dt>
            <span id="addressLine2">
              <Translate contentKey="coopfullApp.location.addressLine2">Address Line 2</Translate>
            </span>
          </dt>
          <dd>{locationEntity.addressLine2}</dd>
          <dt>
            <span id="postalCode">
              <Translate contentKey="coopfullApp.location.postalCode">Postal Code</Translate>
            </span>
          </dt>
          <dd>{locationEntity.postalCode}</dd>
          <dt>
            <span id="latitude">
              <Translate contentKey="coopfullApp.location.latitude">Latitude</Translate>
            </span>
          </dt>
          <dd>{locationEntity.latitude}</dd>
          <dt>
            <span id="longitude">
              <Translate contentKey="coopfullApp.location.longitude">Longitude</Translate>
            </span>
          </dt>
          <dd>{locationEntity.longitude}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.location.description">Description</Translate>
            </span>
          </dt>
          <dd>{locationEntity.description}</dd>
          <dt>
            <Translate contentKey="coopfullApp.location.parent">Parent</Translate>
          </dt>
          <dd>{locationEntity.parent ? locationEntity.parent.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/location" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/location/${locationEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LocationDetail;
