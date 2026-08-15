import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './aquaculture-production.reducer';

export const AquacultureProductionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const aquacultureProductionEntity = useAppSelector(state => state.aquacultureProduction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aquacultureProductionDetailsHeading">
          <Translate contentKey="coopfullApp.aquacultureProduction.detail.title">AquacultureProduction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{aquacultureProductionEntity.id}</dd>
          <dt>
            <span id="productionDate">
              <Translate contentKey="coopfullApp.aquacultureProduction.productionDate">Production Date</Translate>
            </span>
          </dt>
          <dd>
            {aquacultureProductionEntity.productionDate ? (
              <TextFormat value={aquacultureProductionEntity.productionDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="numberOfAnimals">
              <Translate contentKey="coopfullApp.aquacultureProduction.numberOfAnimals">Number Of Animals</Translate>
            </span>
          </dt>
          <dd>{aquacultureProductionEntity.numberOfAnimals}</dd>
          <dt>
            <span id="stockingDensity">
              <Translate contentKey="coopfullApp.aquacultureProduction.stockingDensity">Stocking Density</Translate>
            </span>
          </dt>
          <dd>{aquacultureProductionEntity.stockingDensity}</dd>
          <dt>
            <span id="productionQuantity">
              <Translate contentKey="coopfullApp.aquacultureProduction.productionQuantity">Production Quantity</Translate>
            </span>
          </dt>
          <dd>{aquacultureProductionEntity.productionQuantity}</dd>
          <dt>
            <span id="productionUnit">
              <Translate contentKey="coopfullApp.aquacultureProduction.productionUnit">Production Unit</Translate>
            </span>
          </dt>
          <dd>{aquacultureProductionEntity.productionUnit}</dd>
          <dt>
            <span id="averageWeightGrams">
              <Translate contentKey="coopfullApp.aquacultureProduction.averageWeightGrams">Average Weight Grams</Translate>
            </span>
          </dt>
          <dd>{aquacultureProductionEntity.averageWeightGrams}</dd>
          <dt>
            <span id="mortalityCount">
              <Translate contentKey="coopfullApp.aquacultureProduction.mortalityCount">Mortality Count</Translate>
            </span>
          </dt>
          <dd>{aquacultureProductionEntity.mortalityCount}</dd>
          <dt>
            <span id="stockingCount">
              <Translate contentKey="coopfullApp.aquacultureProduction.stockingCount">Stocking Count</Translate>
            </span>
          </dt>
          <dd>{aquacultureProductionEntity.stockingCount}</dd>
          <dt>
            <span id="harvestedCount">
              <Translate contentKey="coopfullApp.aquacultureProduction.harvestedCount">Harvested Count</Translate>
            </span>
          </dt>
          <dd>{aquacultureProductionEntity.harvestedCount}</dd>
          <dt>
            <span id="expectedProduction">
              <Translate contentKey="coopfullApp.aquacultureProduction.expectedProduction">Expected Production</Translate>
            </span>
          </dt>
          <dd>{aquacultureProductionEntity.expectedProduction}</dd>
          <dt>
            <span id="expectedHarvestDate">
              <Translate contentKey="coopfullApp.aquacultureProduction.expectedHarvestDate">Expected Harvest Date</Translate>
            </span>
          </dt>
          <dd>
            {aquacultureProductionEntity.expectedHarvestDate ? (
              <TextFormat value={aquacultureProductionEntity.expectedHarvestDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="actualHarvestDate">
              <Translate contentKey="coopfullApp.aquacultureProduction.actualHarvestDate">Actual Harvest Date</Translate>
            </span>
          </dt>
          <dd>
            {aquacultureProductionEntity.actualHarvestDate ? (
              <TextFormat value={aquacultureProductionEntity.actualHarvestDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="status">
              <Translate contentKey="coopfullApp.aquacultureProduction.status">Status</Translate>
            </span>
          </dt>
          <dd>{aquacultureProductionEntity.status}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="coopfullApp.aquacultureProduction.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{aquacultureProductionEntity.notes}</dd>
          <dt>
            <Translate contentKey="coopfullApp.aquacultureProduction.aquacultureActivity">Aquaculture Activity</Translate>
          </dt>
          <dd>{aquacultureProductionEntity.aquacultureActivity ? aquacultureProductionEntity.aquacultureActivity.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/aquaculture-production" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/aquaculture-production/${aquacultureProductionEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AquacultureProductionDetail;
