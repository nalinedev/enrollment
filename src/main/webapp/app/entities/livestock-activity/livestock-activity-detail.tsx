import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './livestock-activity.reducer';

export const LivestockActivityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const livestockActivityEntity = useAppSelector(state => state.livestockActivity.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="livestockActivityDetailsHeading">
          <Translate contentKey="coopfullApp.livestockActivity.detail.title">LivestockActivity</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopfullApp.livestockActivity.name">Name</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.livestockActivity.description">Description</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.description}</dd>
          <dt>
            <span id="productionMode">
              <Translate contentKey="coopfullApp.livestockActivity.productionMode">Production Mode</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.productionMode}</dd>
          <dt>
            <span id="ownershipType">
              <Translate contentKey="coopfullApp.livestockActivity.ownershipType">Ownership Type</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.ownershipType}</dd>
          <dt>
            <span id="productionType">
              <Translate contentKey="coopfullApp.livestockActivity.productionType">Production Type</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.productionType}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="coopfullApp.livestockActivity.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {livestockActivityEntity.startDate ? (
              <TextFormat value={livestockActivityEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="totalArea">
              <Translate contentKey="coopfullApp.livestockActivity.totalArea">Total Area</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.totalArea}</dd>
          <dt>
            <span id="areaUnit">
              <Translate contentKey="coopfullApp.livestockActivity.areaUnit">Area Unit</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.areaUnit}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="coopfullApp.livestockActivity.status">Status</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.status}</dd>
          <dt>
            <span id="numberOfAnimals">
              <Translate contentKey="coopfullApp.livestockActivity.numberOfAnimals">Number Of Animals</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.numberOfAnimals}</dd>
          <dt>
            <span id="annualRevenue">
              <Translate contentKey="coopfullApp.livestockActivity.annualRevenue">Annual Revenue</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.annualRevenue}</dd>
          <dt>
            <span id="monthlyRevenue">
              <Translate contentKey="coopfullApp.livestockActivity.monthlyRevenue">Monthly Revenue</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.monthlyRevenue}</dd>
          <dt>
            <span id="employees">
              <Translate contentKey="coopfullApp.livestockActivity.employees">Employees</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.employees}</dd>
          <dt>
            <span id="veterinaryServiceAvailable">
              <Translate contentKey="coopfullApp.livestockActivity.veterinaryServiceAvailable">Veterinary Service Available</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.veterinaryServiceAvailable ? 'true' : 'false'}</dd>
          <dt>
            <span id="feedSource">
              <Translate contentKey="coopfullApp.livestockActivity.feedSource">Feed Source</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.feedSource}</dd>
          <dt>
            <span id="waterSource">
              <Translate contentKey="coopfullApp.livestockActivity.waterSource">Water Source</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.waterSource}</dd>
          <dt>
            <span id="certification">
              <Translate contentKey="coopfullApp.livestockActivity.certification">Certification</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.certification}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="coopfullApp.livestockActivity.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{livestockActivityEntity.notes}</dd>
          <dt>
            <Translate contentKey="coopfullApp.livestockActivity.location">Location</Translate>
          </dt>
          <dd>{livestockActivityEntity.location ? livestockActivityEntity.location.name : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.livestockActivity.livestockType">Livestock Type</Translate>
          </dt>
          <dd>{livestockActivityEntity.livestockType ? livestockActivityEntity.livestockType.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/livestock-activity" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/livestock-activity/${livestockActivityEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default LivestockActivityDetail;
