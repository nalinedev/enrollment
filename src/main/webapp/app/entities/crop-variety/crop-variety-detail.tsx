import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './crop-variety.reducer';

export const CropVarietyDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const cropVarietyEntity = useAppSelector(state => state.cropVariety.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cropVarietyDetailsHeading">
          <Translate contentKey="coopfullApp.cropVariety.detail.title">CropVariety</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cropVarietyEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="coopfullApp.cropVariety.code">Code</Translate>
            </span>
          </dt>
          <dd>{cropVarietyEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopfullApp.cropVariety.name">Name</Translate>
            </span>
          </dt>
          <dd>{cropVarietyEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.cropVariety.description">Description</Translate>
            </span>
          </dt>
          <dd>{cropVarietyEntity.description}</dd>
          <dt>
            <span id="origin">
              <Translate contentKey="coopfullApp.cropVariety.origin">Origin</Translate>
            </span>
          </dt>
          <dd>{cropVarietyEntity.origin}</dd>
          <dt>
            <span id="maturityDays">
              <Translate contentKey="coopfullApp.cropVariety.maturityDays">Maturity Days</Translate>
            </span>
          </dt>
          <dd>{cropVarietyEntity.maturityDays}</dd>
          <dt>
            <span id="yieldPotential">
              <Translate contentKey="coopfullApp.cropVariety.yieldPotential">Yield Potential</Translate>
            </span>
          </dt>
          <dd>{cropVarietyEntity.yieldPotential}</dd>
          <dt>
            <span id="diseaseResistance">
              <Translate contentKey="coopfullApp.cropVariety.diseaseResistance">Disease Resistance</Translate>
            </span>
          </dt>
          <dd>{cropVarietyEntity.diseaseResistance}</dd>
          <dt>
            <span id="active">
              <Translate contentKey="coopfullApp.cropVariety.active">Active</Translate>
            </span>
          </dt>
          <dd>{cropVarietyEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="coopfullApp.cropVariety.crop">Crop</Translate>
          </dt>
          <dd>{cropVarietyEntity.crop ? cropVarietyEntity.crop.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/crop-variety" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/crop-variety/${cropVarietyEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CropVarietyDetail;
