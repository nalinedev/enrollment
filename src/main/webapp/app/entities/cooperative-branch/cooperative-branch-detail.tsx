import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './cooperative-branch.reducer';

export const CooperativeBranchDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const cooperativeBranchEntity = useAppSelector(state => state.cooperativeBranch.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="cooperativeBranchDetailsHeading">
          <Translate contentKey="coopfullApp.cooperativeBranch.detail.title">CooperativeBranch</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{cooperativeBranchEntity.id}</dd>
          <dt>
            <span id="code">
              <Translate contentKey="coopfullApp.cooperativeBranch.code">Code</Translate>
            </span>
          </dt>
          <dd>{cooperativeBranchEntity.code}</dd>
          <dt>
            <span id="name">
              <Translate contentKey="coopfullApp.cooperativeBranch.name">Name</Translate>
            </span>
          </dt>
          <dd>{cooperativeBranchEntity.name}</dd>
          <dt>
            <span id="description">
              <Translate contentKey="coopfullApp.cooperativeBranch.description">Description</Translate>
            </span>
          </dt>
          <dd>{cooperativeBranchEntity.description}</dd>
          <dt>
            <span id="phone">
              <Translate contentKey="coopfullApp.cooperativeBranch.phone">Phone</Translate>
            </span>
          </dt>
          <dd>{cooperativeBranchEntity.phone}</dd>
          <dt>
            <span id="email">
              <Translate contentKey="coopfullApp.cooperativeBranch.email">Email</Translate>
            </span>
          </dt>
          <dd>{cooperativeBranchEntity.email}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="coopfullApp.cooperativeBranch.status">Status</Translate>
            </span>
          </dt>
          <dd>{cooperativeBranchEntity.status}</dd>
          <dt>
            <span id="openingDate">
              <Translate contentKey="coopfullApp.cooperativeBranch.openingDate">Opening Date</Translate>
            </span>
          </dt>
          <dd>
            {cooperativeBranchEntity.openingDate ? (
              <TextFormat value={cooperativeBranchEntity.openingDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="closingDate">
              <Translate contentKey="coopfullApp.cooperativeBranch.closingDate">Closing Date</Translate>
            </span>
          </dt>
          <dd>
            {cooperativeBranchEntity.closingDate ? (
              <TextFormat value={cooperativeBranchEntity.closingDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <Translate contentKey="coopfullApp.cooperativeBranch.cooperative">Cooperative</Translate>
          </dt>
          <dd>{cooperativeBranchEntity.cooperative ? cooperativeBranchEntity.cooperative.name : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.cooperativeBranch.location">Location</Translate>
          </dt>
          <dd>{cooperativeBranchEntity.location ? cooperativeBranchEntity.location.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/cooperative-branch" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/cooperative-branch/${cooperativeBranchEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default CooperativeBranchDetail;
