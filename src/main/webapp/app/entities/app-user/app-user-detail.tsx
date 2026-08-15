import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './app-user.reducer';

export const AppUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const appUserEntity = useAppSelector(state => state.appUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="appUserDetailsHeading">
          <Translate contentKey="coopfullApp.appUser.detail.title">AppUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{appUserEntity.id}</dd>
          <dt>
            <span id="employeeNumber">
              <Translate contentKey="coopfullApp.appUser.employeeNumber">Employee Number</Translate>
            </span>
          </dt>
          <dd>{appUserEntity.employeeNumber}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="coopfullApp.appUser.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{appUserEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="coopfullApp.appUser.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{appUserEntity.lastName}</dd>
          <dt>
            <span id="phoneNumber">
              <Translate contentKey="coopfullApp.appUser.phoneNumber">Phone Number</Translate>
            </span>
          </dt>
          <dd>{appUserEntity.phoneNumber}</dd>
          <dt>
            <span id="jobTitle">
              <Translate contentKey="coopfullApp.appUser.jobTitle">Job Title</Translate>
            </span>
          </dt>
          <dd>{appUserEntity.jobTitle}</dd>
          <dt>
            <span id="department">
              <Translate contentKey="coopfullApp.appUser.department">Department</Translate>
            </span>
          </dt>
          <dd>{appUserEntity.department}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="coopfullApp.appUser.status">Status</Translate>
            </span>
          </dt>
          <dd>{appUserEntity.status}</dd>
          <dt>
            <span id="createdDate">
              <Translate contentKey="coopfullApp.appUser.createdDate">Created Date</Translate>
            </span>
          </dt>
          <dd>
            {appUserEntity.createdDate ? <TextFormat value={appUserEntity.createdDate} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="lastModifiedDate">
              <Translate contentKey="coopfullApp.appUser.lastModifiedDate">Last Modified Date</Translate>
            </span>
          </dt>
          <dd>
            {appUserEntity.lastModifiedDate ? (
              <TextFormat value={appUserEntity.lastModifiedDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="coopfullApp.appUser.user">User</Translate>
          </dt>
          <dd>{appUserEntity.user ? appUserEntity.user.login : ''}</dd>
        </dl>
        <Button as={Link as any} to="/app-user" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/app-user/${appUserEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default AppUserDetail;
