import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './crop.reducer';

export const CropDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const cropEntity = useAppSelector(state => state.crop.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cropDetailsHeading">
          <Translate contentKey="coopfullApp.crop.detail.title">Crop</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cropEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="coopfullApp.crop.code">Code</Translate>
            </span>
          </dt>
          <dd>{cropEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopfullApp.crop.name">Name</Translate>
            </span>
          </dt>
          <dd>{cropEntity.name}</dd>
          <dt>
            <span id="scientificName">
              <Translate contentKey="coopfullApp.crop.scientificName">Scientific Name</Translate>
            </span>
          </dt>
          <dd>{cropEntity.scientificName}</dd>
          <dt>
            <span id="category">
              <Translate contentKey="coopfullApp.crop.category">Category</Translate>
            </span>
          </dt>
          <dd>{cropEntity.category}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.crop.description">Description</Translate>
            </span>
          </dt>
          <dd>{cropEntity.description}</dd>
          <dt>
            <span id="perennial">
              <Translate contentKey="coopfullApp.crop.perennial">Perennial</Translate>
            </span>
          </dt>
          <dd>{cropEntity.perennial ? 'true' : 'false'}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="coopfullApp.crop.active">Active</Translate>
            </span>
          </dt>
          <dd>{cropEntity.active ? 'true' : 'false'}</dd>
        </dl>
        <Button as={Link as any} to="/crop" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/crop/${cropEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CropDetail;
