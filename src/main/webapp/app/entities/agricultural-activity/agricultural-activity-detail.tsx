import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './agricultural-activity.reducer';

export const AgriculturalActivityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const agriculturalActivityEntity = useAppSelector(state => state.agriculturalActivity.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="agriculturalActivityDetailsHeading">
          <Translate contentKey="coopfullApp.agriculturalActivity.detail.title">AgriculturalActivity</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{agriculturalActivityEntity.id}</dd>
          <dt>
            <span id="totalArea">
              <Translate contentKey="coopfullApp.agriculturalActivity.totalArea">Total Area</Translate>
            </span>
          </dt>
          <dd>{agriculturalActivityEntity.totalArea}</dd>
          <dt>
            <span id="areaUnit">
              <Translate contentKey="coopfullApp.agriculturalActivity.areaUnit">Area Unit</Translate>
            </span>
          </dt>
          <dd>{agriculturalActivityEntity.areaUnit}</dd>
          <dt>
            <span id="exploitationMode">
              <Translate contentKey="coopfullApp.agriculturalActivity.exploitationMode">Exploitation Mode</Translate>
            </span>
          </dt>
          <dd>{agriculturalActivityEntity.exploitationMode}</dd>
          <dt>
            <span id="ownershipType">
              <Translate contentKey="coopfullApp.agriculturalActivity.ownershipType">Ownership Type</Translate>
            </span>
          </dt>
          <dd>{agriculturalActivityEntity.ownershipType}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="coopfullApp.agriculturalActivity.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {agriculturalActivityEntity.startDate ? (
              <TextFormat value={agriculturalActivityEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="irrigationAvailable">
              <Translate contentKey="coopfullApp.agriculturalActivity.irrigationAvailable">Irrigation Available</Translate>
            </span>
          </dt>
          <dd>{agriculturalActivityEntity.irrigationAvailable ? 'true' : 'false'}</dd>
          <dt>
            <span id="organicProduction">
              <Translate contentKey="coopfullApp.agriculturalActivity.organicProduction">Organic Production</Translate>
            </span>
          </dt>
          <dd>{agriculturalActivityEntity.organicProduction ? 'true' : 'false'}</dd>
          <dt>
            <span id="certification">
              <Translate contentKey="coopfullApp.agriculturalActivity.certification">Certification</Translate>
            </span>
          </dt>
          <dd>{agriculturalActivityEntity.certification}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.agriculturalActivity.description">Description</Translate>
            </span>
          </dt>
          <dd>{agriculturalActivityEntity.description}</dd>
          <dt>
            <Translate contentKey="coopfullApp.agriculturalActivity.location">Location</Translate>
          </dt>
          <dd>{agriculturalActivityEntity.location ? agriculturalActivityEntity.location.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/agricultural-activity" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/agricultural-activity/${agriculturalActivityEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AgriculturalActivityDetail;
