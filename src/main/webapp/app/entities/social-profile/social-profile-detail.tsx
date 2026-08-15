import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './social-profile.reducer';

export const SocialProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const socialProfileEntity = useAppSelector(state => state.socialProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="socialProfileDetailsHeading">
          <Translate contentKey="coopfullApp.socialProfile.detail.title">SocialProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{socialProfileEntity.id}</dd>
          <dt>
            <span id="maritalStatus">
              <Translate contentKey="coopfullApp.socialProfile.maritalStatus">Marital Status</Translate>
            </span>
          </dt>
          <dd>{socialProfileEntity.maritalStatus}</dd>
          <dt>
            <span id="numberOfChildren">
              <Translate contentKey="coopfullApp.socialProfile.numberOfChildren">Number Of Children</Translate>
            </span>
          </dt>
          <dd>{socialProfileEntity.numberOfChildren}</dd>
          <dt>
            <span id="numberOfDependents">
              <Translate contentKey="coopfullApp.socialProfile.numberOfDependents">Number Of Dependents</Translate>
            </span>
          </dt>
          <dd>{socialProfileEntity.numberOfDependents}</dd>
          <dt>
            <span id="educationLevel">
              <Translate contentKey="coopfullApp.socialProfile.educationLevel">Education Level</Translate>
            </span>
          </dt>
          <dd>{socialProfileEntity.educationLevel}</dd>
          <dt>
            <span id="housingStatus">
              <Translate contentKey="coopfullApp.socialProfile.housingStatus">Housing Status</Translate>
            </span>
          </dt>
          <dd>{socialProfileEntity.housingStatus}</dd>
          <dt>
            <span id="residenceSince">
              <Translate contentKey="coopfullApp.socialProfile.residenceSince">Residence Since</Translate>
            </span>
          </dt>
          <dd>
            {socialProfileEntity.residenceSince ? (
              <TextFormat value={socialProfileEntity.residenceSince} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="disabilityStatus">
              <Translate contentKey="coopfullApp.socialProfile.disabilityStatus">Disability Status</Translate>
            </span>
          </dt>
          <dd>{socialProfileEntity.disabilityStatus ? 'true' : 'false'}</dd>
          <dt>
            <span id="disabilityDescription">
              <Translate contentKey="coopfullApp.socialProfile.disabilityDescription">Disability Description</Translate>
            </span>
          </dt>
          <dd>{socialProfileEntity.disabilityDescription}</dd>
          <dt>
            <span id="socialCategory">
              <Translate contentKey="coopfullApp.socialProfile.socialCategory">Social Category</Translate>
            </span>
          </dt>
          <dd>{socialProfileEntity.socialCategory}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="coopfullApp.socialProfile.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{socialProfileEntity.notes}</dd>
        </dl>
        <Button as={Link as any} to="/social-profile" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/social-profile/${socialProfileEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default SocialProfileDetail;
