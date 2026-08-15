import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './branch-user.reducer';

export const BranchUserDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const branchUserEntity = useAppSelector(state => state.branchUser.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="branchUserDetailsHeading">
          <Translate contentKey="coopfullApp.branchUser.detail.title">BranchUser</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{branchUserEntity.id}</dd>
          <dt>
            <span id="startDate">
              <Translate contentKey="coopfullApp.branchUser.startDate">Start Date</Translate>
            </span>
          </dt>
          <dd>
            {branchUserEntity.startDate ? (
              <TextFormat value={branchUserEntity.startDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="endDate">
              <Translate contentKey="coopfullApp.branchUser.endDate">End Date</Translate>
            </span>
          </dt>
          <dd>
            {branchUserEntity.endDate ? <TextFormat value={branchUserEntity.endDate} type="date" format={APP_LOCAL_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="active">
              <Translate contentKey="coopfullApp.branchUser.active">Active</Translate>
            </span>
          </dt>
          <dd>{branchUserEntity.active ? 'true' : 'false'}</dd>
          <dt>
            <Translate contentKey="coopfullApp.branchUser.appUser">App User</Translate>
          </dt>
          <dd>{branchUserEntity.appUser ? branchUserEntity.appUser.id : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.branchUser.branch">Branch</Translate>
          </dt>
          <dd>{branchUserEntity.branch ? branchUserEntity.branch.name : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.branchUser.role">Role</Translate>
          </dt>
          <dd>{branchUserEntity.role ? branchUserEntity.role.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/branch-user" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/branch-user/${branchUserEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default BranchUserDetail;
