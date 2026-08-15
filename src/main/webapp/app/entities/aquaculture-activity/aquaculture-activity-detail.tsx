import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './aquaculture-activity.reducer';

export const AquacultureActivityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const aquacultureActivityEntity = useAppSelector(state => state.aquacultureActivity.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="aquacultureActivityDetailsHeading">
          <Translate contentKey="coopfullApp.aquacultureActivity.detail.title">AquacultureActivity</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopfullApp.aquacultureActivity.name">Name</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.aquacultureActivity.description">Description</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.description}</dd>
          <dt>
            <span id="productionMode">
              <Translate contentKey="coopfullApp.aquacultureActivity.productionMode">Production Mode</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.productionMode}</dd>
          <dt>
            <span id="ownershipType">
              <Translate contentKey="coopfullApp.aquacultureActivity.ownershipType">Ownership Type</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.ownershipType}</dd>
          <dt>
            <span id="productionType">
              <Translate contentKey="coopfullApp.aquacultureActivity.productionType">Production Type</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.productionType}</dd>
          <dt>
            <span id="systemType">
              <Translate contentKey="coopfullApp.aquacultureActivity.systemType">System Type</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.systemType}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="coopfullApp.aquacultureActivity.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {aquacultureActivityEntity.startDate ? (
              <TextFormat value={aquacultureActivityEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="totalArea">
              <Translate contentKey="coopfullApp.aquacultureActivity.totalArea">Total Area</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.totalArea}</dd>
          <dt>
            <span id="areaUnit">
              <Translate contentKey="coopfullApp.aquacultureActivity.areaUnit">Area Unit</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.areaUnit}</dd>
          <dt>
            <span id="waterSource">
              <Translate contentKey="coopfullApp.aquacultureActivity.waterSource">Water Source</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.waterSource}</dd>
          <dt>
            <span id="numberOfProductionUnits">
              <Translate contentKey="coopfullApp.aquacultureActivity.numberOfProductionUnits">Number Of Production Units</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.numberOfProductionUnits}</dd>
          <dt>
            <span id="productionUnitDescription">
              <Translate contentKey="coopfullApp.aquacultureActivity.productionUnitDescription">Production Unit Description</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.productionUnitDescription}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="coopfullApp.aquacultureActivity.status">Status</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.status}</dd>
          <dt>
            <span id="annualRevenue">
              <Translate contentKey="coopfullApp.aquacultureActivity.annualRevenue">Annual Revenue</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.annualRevenue}</dd>
          <dt>
            <span id="monthlyRevenue">
              <Translate contentKey="coopfullApp.aquacultureActivity.monthlyRevenue">Monthly Revenue</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.monthlyRevenue}</dd>
          <dt>
            <span id="employees">
              <Translate contentKey="coopfullApp.aquacultureActivity.employees">Employees</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.employees}</dd>
          <dt>
            <span id="certification">
              <Translate contentKey="coopfullApp.aquacultureActivity.certification">Certification</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.certification}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="coopfullApp.aquacultureActivity.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{aquacultureActivityEntity.notes}</dd>
          <dt>
            <Translate contentKey="coopfullApp.aquacultureActivity.location">Location</Translate>
          </dt>
          <dd>{aquacultureActivityEntity.location ? aquacultureActivityEntity.location.name : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.aquacultureActivity.aquaticSpecies">Aquatic Species</Translate>
          </dt>
          <dd>{aquacultureActivityEntity.aquaticSpecies ? aquacultureActivityEntity.aquaticSpecies.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/aquaculture-activity" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/aquaculture-activity/${aquacultureActivityEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AquacultureActivityDetail;
