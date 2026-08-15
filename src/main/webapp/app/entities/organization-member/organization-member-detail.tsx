import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './organization-member.reducer';

export const OrganizationMemberDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const organizationMemberEntity = useAppSelector(state => state.organizationMember.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="organizationMemberDetailsHeading">
          <Translate contentKey="coopfullApp.organizationMember.detail.title">OrganizationMember</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{organizationMemberEntity.id}</dd>
          <dt>
            <span id="legalName">
              <Translate contentKey="coopfullApp.organizationMember.legalName">Legal Name</Translate>
            </span>
          </dt>
          <dd>{organizationMemberEntity.legalName}</dd>
          <dt>
            <span id="tradeName">
              <Translate contentKey="coopfullApp.organizationMember.tradeName">Trade Name</Translate>
            </span>
          </dt>
          <dd>{organizationMemberEntity.tradeName}</dd>
          <dt>
            <span id="registrationNumber">
              <Translate contentKey="coopfullApp.organizationMember.registrationNumber">Registration Number</Translate>
            </span>
          </dt>
          <dd>{organizationMemberEntity.registrationNumber}</dd>
          <dt>
            <span id="taxNumber">
              <Translate contentKey="coopfullApp.organizationMember.taxNumber">Tax Number</Translate>
            </span>
          </dt>
          <dd>{organizationMemberEntity.taxNumber}</dd>
          <dt>
            <span id="legalForm">
              <Translate contentKey="coopfullApp.organizationMember.legalForm">Legal Form</Translate>
            </span>
          </dt>
          <dd>{organizationMemberEntity.legalForm}</dd>
          <dt>
            <span id="registrationDate">
              <Translate contentKey="coopfullApp.organizationMember.registrationDate">Registration Date</Translate>
            </span>
          </dt>
          <dd>
            {organizationMemberEntity.registrationDate ? (
              <TextFormat value={organizationMemberEntity.registrationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="email">
              <Translate contentKey="coopfullApp.organizationMember.email">Email</Translate>
            </span>
          </dt>
          <dd>{organizationMemberEntity.email}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="coopfullApp.organizationMember.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{organizationMemberEntity.phoneNumber}</dd>
          <dt>
            <span id="website">
              <Translate contentKey="coopfullApp.organizationMember.website">Website</Translate>
            </span>
          </dt>
          <dd>{organizationMemberEntity.website}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.organizationMember.description">Description</Translate>
            </span>
          </dt>
          <dd>{organizationMemberEntity.description}</dd>
        </dl>
        <Button as={Link as any} to="/organization-member" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/organization-member/${organizationMemberEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default OrganizationMemberDetail;
