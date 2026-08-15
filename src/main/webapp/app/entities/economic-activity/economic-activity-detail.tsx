import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './economic-activity.reducer';

export const EconomicActivityDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const economicActivityEntity = useAppSelector(state => state.economicActivity.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="economicActivityDetailsHeading">
          <Translate contentKey="coopfullApp.economicActivity.detail.title">EconomicActivity</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{economicActivityEntity.id}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopfullApp.economicActivity.name">Name</Translate>
            </span>
          </dt>
          <dd>{economicActivityEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.economicActivity.description">Description</Translate>
            </span>
          </dt>
          <dd>{economicActivityEntity.description}</dd>
          <dt>
            <span id="mainActivity">
              <Translate contentKey="coopfullApp.economicActivity.mainActivity">Main Activity</Translate>
            </span>
          </dt>
          <dd>{economicActivityEntity.mainActivity ? 'true' : 'false'}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="coopfullApp.economicActivity.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {economicActivityEntity.startDate ? (
              <TextFormat value={economicActivityEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="coopfullApp.economicActivity.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {economicActivityEntity.endDate ? (
              <TextFormat value={economicActivityEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="annualRevenue">
              <Translate contentKey="coopfullApp.economicActivity.annualRevenue">Annual Revenue</Translate>
            </span>
          </dt>
          <dd>{economicActivityEntity.annualRevenue}</dd>
          <dt>
            <span id="monthlyRevenue">
              <Translate contentKey="coopfullApp.economicActivity.monthlyRevenue">Monthly Revenue</Translate>
            </span>
          </dt>
          <dd>{economicActivityEntity.monthlyRevenue}</dd>
          <dt>
            <span id="numberOfEmployees">
              <Translate contentKey="coopfullApp.economicActivity.numberOfEmployees">Number Of Employees</Translate>
            </span>
          </dt>
          <dd>{economicActivityEntity.numberOfEmployees}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="coopfullApp.economicActivity.status">Status</Translate>
            </span>
          </dt>
          <dd>{economicActivityEntity.status}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="coopfullApp.economicActivity.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{economicActivityEntity.notes}</dd>
          <dt>
            <Translate contentKey="coopfullApp.economicActivity.agriculturalActivity">Agricultural Activity</Translate>
          </dt>
          <dd>{economicActivityEntity.agriculturalActivity ? economicActivityEntity.agriculturalActivity.id : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.economicActivity.livestockActivity">Livestock Activity</Translate>
          </dt>
          <dd>{economicActivityEntity.livestockActivity ? economicActivityEntity.livestockActivity.id : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.economicActivity.aquacultureActivity">Aquaculture Activity</Translate>
          </dt>
          <dd>{economicActivityEntity.aquacultureActivity ? economicActivityEntity.aquacultureActivity.id : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.economicActivity.member">Member</Translate>
          </dt>
          <dd>{economicActivityEntity.member ? economicActivityEntity.member.memberNumber : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.economicActivity.activityType">Activity Type</Translate>
          </dt>
          <dd>{economicActivityEntity.activityType ? economicActivityEntity.activityType.name : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.economicActivity.location">Location</Translate>
          </dt>
          <dd>{economicActivityEntity.location ? economicActivityEntity.location.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/economic-activity" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/economic-activity/${economicActivityEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default EconomicActivityDetail;
