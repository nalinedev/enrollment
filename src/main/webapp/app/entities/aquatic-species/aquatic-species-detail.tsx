import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './aquatic-species.reducer';

export const AquaticSpeciesDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const aquaticSpeciesEntity = useAppSelector(state => state.aquaticSpecies.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aquaticSpeciesDetailsHeading">
          <Translate contentKey="coopfullApp.aquaticSpecies.detail.title">AquaticSpecies</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{aquaticSpeciesEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="coopfullApp.aquaticSpecies.code">Code</Translate>
            </span>
          </dt>
          <dd>{aquaticSpeciesEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopfullApp.aquaticSpecies.name">Name</Translate>
            </span>
          </dt>
          <dd>{aquaticSpeciesEntity.name}</dd>
          <dt>
            <span id="scientificName">
              <Translate contentKey="coopfullApp.aquaticSpecies.scientificName">Scientific Name</Translate>
            </span>
          </dt>
          <dd>{aquaticSpeciesEntity.scientificName}</dd>
          <dt>
            <span id="category">
              <Translate contentKey="coopfullApp.aquaticSpecies.category">Category</Translate>
            </span>
          </dt>
          <dd>{aquaticSpeciesEntity.category}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.aquaticSpecies.description">Description</Translate>
            </span>
          </dt>
          <dd>{aquaticSpeciesEntity.description}</dd>
          <dt>
            <span id="freshwater">
              <Translate contentKey="coopfullApp.aquaticSpecies.freshwater">Freshwater</Translate>
            </span>
          </dt>
          <dd>{aquaticSpeciesEntity.freshwater ? 'true' : 'false'}</dd>
          <dt>
            <span id="saltwater">
              <Translate contentKey="coopfullApp.aquaticSpecies.saltwater">Saltwater</Translate>
            </span>
          </dt>
          <dd>{aquaticSpeciesEntity.saltwater ? 'true' : 'false'}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="coopfullApp.aquaticSpecies.active">Active</Translate>
            </span>
          </dt>
          <dd>{aquaticSpeciesEntity.active ? 'true' : 'false'}</dd>
        </dl>
        <Button as={Link as any} to="/aquatic-species" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/aquatic-species/${aquaticSpeciesEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AquaticSpeciesDetail;
