import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './individual-member.reducer';

export const IndividualMemberDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const individualMemberEntity = useAppSelector(state => state.individualMember.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="individualMemberDetailsHeading">
          <Translate contentKey="coopfullApp.individualMember.detail.title">IndividualMember</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{individualMemberEntity.id}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="coopfullApp.individualMember.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{individualMemberEntity.firstName}</dd>
          <dt>
            <span id="middleName">
              <Translate contentKey="coopfullApp.individualMember.middleName">Middle Name</Translate>
            </span>
          </dt>
          <dd>{individualMemberEntity.middleName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="coopfullApp.individualMember.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{individualMemberEntity.lastName}</dd>
          <dt>
            <span id="maidenName">
              <Translate contentKey="coopfullApp.individualMember.maidenName">Maiden Name</Translate>
            </span>
          </dt>
          <dd>{individualMemberEntity.maidenName}</dd>
          <dt>
            <span id="gender">
              <Translate contentKey="coopfullApp.individualMember.gender">Gender</Translate>
            </span>
          </dt>
          <dd>{individualMemberEntity.gender}</dd>
          <dt>
            <span id="birthDate">
              <Translate contentKey="coopfullApp.individualMember.birthDate">Birth Date</Translate>
            </span>
          </dt>
          <dd>
            {individualMemberEntity.birthDate ? (
              <TextFormat value={individualMemberEntity.birthDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="birthPlace">
              <Translate contentKey="coopfullApp.individualMember.birthPlace">Birth Place</Translate>
            </span>
          </dt>
          <dd>{individualMemberEntity.birthPlace}</dd>
          <dt>
            <span id="nationality">
              <Translate contentKey="coopfullApp.individualMember.nationality">Nationality</Translate>
            </span>
          </dt>
          <dd>{individualMemberEntity.nationality}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="coopfullApp.individualMember.email">Email</Translate>
            </span>
          </dt>
          <dd>{individualMemberEntity.email}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="coopfullApp.individualMember.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{individualMemberEntity.phoneNumber}</dd>
          <dt>
            <span id="occupation">
              <Translate contentKey="coopfullApp.individualMember.occupation">Occupation</Translate>
            </span>
          </dt>
          <dd>{individualMemberEntity.occupation}</dd>
        </dl>
        <Button as={Link as any} to="/individual-member" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/individual-member/${individualMemberEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IndividualMemberDetail;
