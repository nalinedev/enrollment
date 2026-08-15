import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './livestock-production.reducer';

export const LivestockProductionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const livestockProductionEntity = useAppSelector(state => state.livestockProduction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="livestockProductionDetailsHeading">
          <Translate contentKey="coopfullApp.livestockProduction.detail.title">LivestockProduction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{livestockProductionEntity.id}</dd>
          <dt>
            <span id="productionDate">
              <Translate contentKey="coopfullApp.livestockProduction.productionDate">Production Date</Translate>
            </span>
          </dt>
          <dd>
            {livestockProductionEntity.productionDate ? (
              <TextFormat value={livestockProductionEntity.productionDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="animalSex">
              <Translate contentKey="coopfullApp.livestockProduction.animalSex">Animal Sex</Translate>
            </span>
          </dt>
          <dd>{livestockProductionEntity.animalSex}</dd>
          <dt>
            <span id="numberOfAnimals">
              <Translate contentKey="coopfullApp.livestockProduction.numberOfAnimals">Number Of Animals</Translate>
            </span>
          </dt>
          <dd>{livestockProductionEntity.numberOfAnimals}</dd>
          <dt>
            <span id="averageAgeMonths">
              <Translate contentKey="coopfullApp.livestockProduction.averageAgeMonths">Average Age Months</Translate>
            </span>
          </dt>
          <dd>{livestockProductionEntity.averageAgeMonths}</dd>
          <dt>
            <span id="averageWeightKg">
              <Translate contentKey="coopfullApp.livestockProduction.averageWeightKg">Average Weight Kg</Translate>
            </span>
          </dt>
          <dd>{livestockProductionEntity.averageWeightKg}</dd>
          <dt>
            <span id="productionQuantity">
              <Translate contentKey="coopfullApp.livestockProduction.productionQuantity">Production Quantity</Translate>
            </span>
          </dt>
          <dd>{livestockProductionEntity.productionQuantity}</dd>
          <dt>
            <span id="productionUnit">
              <Translate contentKey="coopfullApp.livestockProduction.productionUnit">Production Unit</Translate>
            </span>
          </dt>
          <dd>{livestockProductionEntity.productionUnit}</dd>
          <dt>
            <span id="mortalityCount">
              <Translate contentKey="coopfullApp.livestockProduction.mortalityCount">Mortality Count</Translate>
            </span>
          </dt>
          <dd>{livestockProductionEntity.mortalityCount}</dd>
          <dt>
            <span id="birthCount">
              <Translate contentKey="coopfullApp.livestockProduction.birthCount">Birth Count</Translate>
            </span>
          </dt>
          <dd>{livestockProductionEntity.birthCount}</dd>
          <dt>
            <span id="soldCount">
              <Translate contentKey="coopfullApp.livestockProduction.soldCount">Sold Count</Translate>
            </span>
          </dt>
          <dd>{livestockProductionEntity.soldCount}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="coopfullApp.livestockProduction.status">Status</Translate>
            </span>
          </dt>
          <dd>{livestockProductionEntity.status}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="coopfullApp.livestockProduction.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{livestockProductionEntity.notes}</dd>
          <dt>
            <Translate contentKey="coopfullApp.livestockProduction.livestockActivity">Livestock Activity</Translate>
          </dt>
          <dd>{livestockProductionEntity.livestockActivity ? livestockProductionEntity.livestockActivity.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/livestock-production" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/livestock-production/${livestockProductionEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LivestockProductionDetail;
