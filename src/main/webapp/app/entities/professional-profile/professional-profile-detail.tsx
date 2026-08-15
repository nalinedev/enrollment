import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './professional-profile.reducer';

export const ProfessionalProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const professionalProfileEntity = useAppSelector(state => state.professionalProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="professionalProfileDetailsHeading">
          <Translate contentKey="coopfullApp.professionalProfile.detail.title">ProfessionalProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{professionalProfileEntity.id}</dd>
          <dt>
            <span id="employmentStatus">
              <Translate contentKey="coopfullApp.professionalProfile.employmentStatus">Employment Status</Translate>
            </span>
          </dt>
          <dd>{professionalProfileEntity.employmentStatus}</dd>
          <dt>
            <span id="employerName">
              <Translate contentKey="coopfullApp.professionalProfile.employerName">Employer Name</Translate>
            </span>
          </dt>
          <dd>{professionalProfileEntity.employerName}</dd>
          <dt>
            <span id="jobTitle">
              <Translate contentKey="coopfullApp.professionalProfile.jobTitle">Job Title</Translate>
            </span>
          </dt>
          <dd>{professionalProfileEntity.jobTitle}</dd>
          <dt>
            <span id="profession">
              <Translate contentKey="coopfullApp.professionalProfile.profession">Profession</Translate>
            </span>
          </dt>
          <dd>{professionalProfileEntity.profession}</dd>
          <dt>
            <span id="sector">
              <Translate contentKey="coopfullApp.professionalProfile.sector">Sector</Translate>
            </span>
          </dt>
          <dd>{professionalProfileEntity.sector}</dd>
          <dt>
            <span id="yearsOfExperience">
              <Translate contentKey="coopfullApp.professionalProfile.yearsOfExperience">Years Of Experience</Translate>
            </span>
          </dt>
          <dd>{professionalProfileEntity.yearsOfExperience}</dd>
          <dt>
            <span id="monthlyIncome">
              <Translate contentKey="coopfullApp.professionalProfile.monthlyIncome">Monthly Income</Translate>
            </span>
          </dt>
          <dd>{professionalProfileEntity.monthlyIncome}</dd>
          <dt>
            <span id="annualIncome">
              <Translate contentKey="coopfullApp.professionalProfile.annualIncome">Annual Income</Translate>
            </span>
          </dt>
          <dd>{professionalProfileEntity.annualIncome}</dd>
          <dt>
            <span id="employmentStartDate">
              <Translate contentKey="coopfullApp.professionalProfile.employmentStartDate">Employment Start Date</Translate>
            </span>
          </dt>
          <dd>
            {professionalProfileEntity.employmentStartDate ? (
              <TextFormat value={professionalProfileEntity.employmentStartDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="employerLocation">
              <Translate contentKey="coopfullApp.professionalProfile.employerLocation">Employer Location</Translate>
            </span>
          </dt>
          <dd>{professionalProfileEntity.employerLocation}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="coopfullApp.professionalProfile.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{professionalProfileEntity.notes}</dd>
        </dl>
        <Button as={Link as any} to="/professional-profile" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/professional-profile/${professionalProfileEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ProfessionalProfileDetail;
