import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './family-member.reducer';

export const FamilyMemberDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const familyMemberEntity = useAppSelector(state => state.familyMember.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="familyMemberDetailsHeading">
          <Translate contentKey="coopfullApp.familyMember.detail.title">FamilyMember</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{familyMemberEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="coopfullApp.familyMember.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{familyMemberEntity.firstName}</dd>
          <dt>
            <span id="middleName">
              <Translate contentKey="coopfullApp.familyMember.middleName">Middle Name</Translate>
            </span>
          </dt>
          <dd>{familyMemberEntity.middleName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="coopfullApp.familyMember.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{familyMemberEntity.lastName}</dd>
          <dt>
            <span id="relationship">
              <Translate contentKey="coopfullApp.familyMember.relationship">Relationship</Translate>
            </span>
          </dt>
          <dd>{familyMemberEntity.relationship}</dd>
          <dt>
            <span id="gender">
              <Translate contentKey="coopfullApp.familyMember.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{familyMemberEntity.gender}</dd>
          <dt>
            <span id="birthDate">
              <Translate contentKey="coopfullApp.familyMember.birthDate">Birth Date</Translate>
            </span>
          </dt>
          <dd>
            {familyMemberEntity.birthDate ? (
              <TextFormat value={familyMemberEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="birthPlace">
              <Translate contentKey="coopfullApp.familyMember.birthPlace">Birth Place</Translate>
            </span>
          </dt>
          <dd>{familyMemberEntity.birthPlace}</dd>
          <dt>
            <span id="nationality">
              <Translate contentKey="coopfullApp.familyMember.nationality">Nationality</Translate>
            </span>
          </dt>
          <dd>{familyMemberEntity.nationality}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="coopfullApp.familyMember.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{familyMemberEntity.phoneNumber}</dd>
          <dt>
            <span id="occupation">
              <Translate contentKey="coopfullApp.familyMember.occupation">Occupation</Translate>
            </span>
          </dt>
          <dd>{familyMemberEntity.occupation}</dd>
          <dt>
            <span id="dependent">
              <Translate contentKey="coopfullApp.familyMember.dependent">Dependent</Translate>
            </span>
          </dt>
          <dd>{familyMemberEntity.dependent ? 'true' : 'false'}</dd>
          <dt>
            <span id="notes">
              <Translate contentKey="coopfullApp.familyMember.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{familyMemberEntity.notes}</dd>
          <dt>
            <Translate contentKey="coopfullApp.familyMember.member">Member</Translate>
          </dt>
          <dd>{familyMemberEntity.member ? familyMemberEntity.member.memberNumber : ''}</dd>
        </dl>
        <Button as={Link as any} to="/family-member" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/family-member/${familyMemberEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default FamilyMemberDetail;
