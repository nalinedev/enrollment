import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './membership-application.reducer';

export const MembershipApplicationDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const membershipApplicationEntity = useAppSelector(state => state.membershipApplication.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="membershipApplicationDetailsHeading">
          <Translate contentKey="coopfullApp.membershipApplication.detail.title">MembershipApplication</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{membershipApplicationEntity.id}</dd>
          <dt>
            <span id="applicationNumber">
              <Translate contentKey="coopfullApp.membershipApplication.applicationNumber">Application Number</Translate>
            </span>
          </dt>
          <dd>{membershipApplicationEntity.applicationNumber}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="coopfullApp.membershipApplication.status">Status</Translate>
            </span>
          </dt>
          <dd>{membershipApplicationEntity.status}</dd>
          <dt>
            <span id="applicationDate">
              <Translate contentKey="coopfullApp.membershipApplication.applicationDate">Application Date</Translate>
            </span>
          </dt>
          <dd>
            {membershipApplicationEntity.applicationDate ? (
              <TextFormat value={membershipApplicationEntity.applicationDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="submittedAt">
              <Translate contentKey="coopfullApp.membershipApplication.submittedAt">Submitted At</Translate>
            </span>
          </dt>
          <dd>
            {membershipApplicationEntity.submittedAt ? (
              <TextFormat value={membershipApplicationEntity.submittedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="reviewedAt">
              <Translate contentKey="coopfullApp.membershipApplication.reviewedAt">Reviewed At</Translate>
            </span>
          </dt>
          <dd>
            {membershipApplicationEntity.reviewedAt ? (
              <TextFormat value={membershipApplicationEntity.reviewedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="approvedAt">
              <Translate contentKey="coopfullApp.membershipApplication.approvedAt">Approved At</Translate>
            </span>
          </dt>
          <dd>
            {membershipApplicationEntity.approvedAt ? (
              <TextFormat value={membershipApplicationEntity.approvedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="rejectedAt">
              <Translate contentKey="coopfullApp.membershipApplication.rejectedAt">Rejected At</Translate>
            </span>
          </dt>
          <dd>
            {membershipApplicationEntity.rejectedAt ? (
              <TextFormat value={membershipApplicationEntity.rejectedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="rejectionReason">
              <Translate contentKey="coopfullApp.membershipApplication.rejectionReason">Rejection Reason</Translate>
            </span>
          </dt>
          <dd>{membershipApplicationEntity.rejectionReason}</dd>
          <dt>
            <span id="reviewComments">
              <Translate contentKey="coopfullApp.membershipApplication.reviewComments">Review Comments</Translate>
            </span>
          </dt>
          <dd>{membershipApplicationEntity.reviewComments}</dd>
          <dt>
            <span id="confirmation">
              <Translate contentKey="coopfullApp.membershipApplication.confirmation">Confirmation</Translate>
            </span>
          </dt>
          <dd>{membershipApplicationEntity.confirmation ? 'true' : 'false'}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="coopfullApp.membershipApplication.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{membershipApplicationEntity.notes}</dd>
          <dt>
            <Translate contentKey="coopfullApp.membershipApplication.member">Member</Translate>
          </dt>
          <dd>{membershipApplicationEntity.member ? membershipApplicationEntity.member.memberNumber : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.membershipApplication.cooperative">Cooperative</Translate>
          </dt>
          <dd>{membershipApplicationEntity.cooperative ? membershipApplicationEntity.cooperative.name : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.membershipApplication.branch">Branch</Translate>
          </dt>
          <dd>{membershipApplicationEntity.branch ? membershipApplicationEntity.branch.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/membership-application" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/membership-application/${membershipApplicationEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MembershipApplicationDetail;
