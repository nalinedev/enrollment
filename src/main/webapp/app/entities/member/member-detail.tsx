import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './member.reducer';

export const MemberDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const memberEntity = useAppSelector(state => state.member.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="memberDetailsHeading">
          <Translate contentKey="coopfullApp.member.detail.title">Member</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{memberEntity.id}</dd>
          <dt>
            <span id="memberNumber">
              <Translate contentKey="coopfullApp.member.memberNumber">Member Number</Translate>
            </span>
          </dt>
          <dd>{memberEntity.memberNumber}</dd>
          <dt>
            <span id="memberType">
              <Translate contentKey="coopfullApp.member.memberType">Member Type</Translate>
            </span>
          </dt>
          <dd>{memberEntity.memberType}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="coopfullApp.member.status">Status</Translate>
            </span>
          </dt>
          <dd>{memberEntity.status}</dd>
          <dt>
            <span id="admissionDate">
              <Translate contentKey="coopfullApp.member.admissionDate">Admission Date</Translate>
            </span>
          </dt>
          <dd>
            {memberEntity.admissionDate ? (
              <TextFormat value={memberEntity.admissionDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="exitDate">
              <Translate contentKey="coopfullApp.member.exitDate">Exit Date</Translate>
            </span>
          </dt>
          <dd>{memberEntity.exitDate ? <TextFormat value={memberEntity.exitDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="exitReason">
              <Translate contentKey="coopfullApp.member.exitReason">Exit Reason</Translate>
            </span>
          </dt>
          <dd>{memberEntity.exitReason}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="coopfullApp.member.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{memberEntity.notes}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="coopfullApp.member.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>{memberEntity.createdDate ? <TextFormat value={memberEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="coopfullApp.member.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {memberEntity.lastModifiedDate ? (
              <TextFormat value={memberEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="coopfullApp.member.individualMember">Individual Member</Translate>
          </dt>
          <dd>{memberEntity.individualMember ? memberEntity.individualMember.id : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.member.organizationMember">Organization Member</Translate>
          </dt>
          <dd>{memberEntity.organizationMember ? memberEntity.organizationMember.id : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.member.socialProfile">Social Profile</Translate>
          </dt>
          <dd>{memberEntity.socialProfile ? memberEntity.socialProfile.id : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.member.professionalProfile">Professional Profile</Translate>
          </dt>
          <dd>{memberEntity.professionalProfile ? memberEntity.professionalProfile.id : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.member.cooperative">Cooperative</Translate>
          </dt>
          <dd>{memberEntity.cooperative ? memberEntity.cooperative.name : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.member.branch">Branch</Translate>
          </dt>
          <dd>{memberEntity.branch ? memberEntity.branch.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/member" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/member/${memberEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MemberDetail;
