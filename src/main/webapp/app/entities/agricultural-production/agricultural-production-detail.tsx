import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './agricultural-production.reducer';

export const AgriculturalProductionDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const agriculturalProductionEntity = useAppSelector(state => state.agriculturalProduction.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="agriculturalProductionDetailsHeading">
          <Translate contentKey="coopfullApp.agriculturalProduction.detail.title">AgriculturalProduction</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{agriculturalProductionEntity.id}</dd>
          <dt>
            <span id="area">
              <Translate contentKey="coopfullApp.agriculturalProduction.area">Area</Translate>
            </span>
          </dt>
          <dd>{agriculturalProductionEntity.area}</dd>
          <dt>
            <span id="areaUnit">
              <Translate contentKey="coopfullApp.agriculturalProduction.areaUnit">Area Unit</Translate>
            </span>
          </dt>
          <dd>{agriculturalProductionEntity.areaUnit}</dd>
          <dt>
            <span id="plantingDate">
              <Translate contentKey="coopfullApp.agriculturalProduction.plantingDate">Planting Date</Translate>
            </span>
          </dt>
          <dd>
            {agriculturalProductionEntity.plantingDate ? (
              <TextFormat value={agriculturalProductionEntity.plantingDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="harvestStartDate">
              <Translate contentKey="coopfullApp.agriculturalProduction.harvestStartDate">Harvest Start Date</Translate>
            </span>
          </dt>
          <dd>
            {agriculturalProductionEntity.harvestStartDate ? (
              <TextFormat value={agriculturalProductionEntity.harvestStartDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="harvestEndDate">
              <Translate contentKey="coopfullApp.agriculturalProduction.harvestEndDate">Harvest End Date</Translate>
            </span>
          </dt>
          <dd>
            {agriculturalProductionEntity.harvestEndDate ? (
              <TextFormat value={agriculturalProductionEntity.harvestEndDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="productionQuantity">
              <Translate contentKey="coopfullApp.agriculturalProduction.productionQuantity">Production Quantity</Translate>
            </span>
          </dt>
          <dd>{agriculturalProductionEntity.productionQuantity}</dd>
          <dt>
            <span id="productionUnit">
              <Translate contentKey="coopfullApp.agriculturalProduction.productionUnit">Production Unit</Translate>
            </span>
          </dt>
          <dd>{agriculturalProductionEntity.productionUnit}</dd>
          <dt>
            <span id="expectedAnnualProduction">
              <Translate contentKey="coopfullApp.agriculturalProduction.expectedAnnualProduction">Expected Annual Production</Translate>
            </span>
          </dt>
          <dd>{agriculturalProductionEntity.expectedAnnualProduction}</dd>
          <dt>
            <span id="numberOfPlants">
              <Translate contentKey="coopfullApp.agriculturalProduction.numberOfPlants">Number Of Plants</Translate>
            </span>
          </dt>
          <dd>{agriculturalProductionEntity.numberOfPlants}</dd>
          <dt>
            <span id="plantingDensity">
              <Translate contentKey="coopfullApp.agriculturalProduction.plantingDensity">Planting Density</Translate>
            </span>
          </dt>
          <dd>{agriculturalProductionEntity.plantingDensity}</dd>
          <dt>
            <span id="productionYear">
              <Translate contentKey="coopfullApp.agriculturalProduction.productionYear">Production Year</Translate>
            </span>
          </dt>
          <dd>{agriculturalProductionEntity.productionYear}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="coopfullApp.agriculturalProduction.status">Status</Translate>
            </span>
          </dt>
          <dd>{agriculturalProductionEntity.status}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="coopfullApp.agriculturalProduction.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{agriculturalProductionEntity.notes}</dd>
          <dt>
            <Translate contentKey="coopfullApp.agriculturalProduction.agriculturalActivity">Agricultural Activity</Translate>
          </dt>
          <dd>{agriculturalProductionEntity.agriculturalActivity ? agriculturalProductionEntity.agriculturalActivity.id : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.agriculturalProduction.crop">Crop</Translate>
          </dt>
          <dd>{agriculturalProductionEntity.crop ? agriculturalProductionEntity.crop.name : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.agriculturalProduction.cropVariety">Crop Variety</Translate>
          </dt>
          <dd>{agriculturalProductionEntity.cropVariety ? agriculturalProductionEntity.cropVariety.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/agricultural-production" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/agricultural-production/${agriculturalProductionEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AgriculturalProductionDetail;
