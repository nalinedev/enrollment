import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './number-sequence.reducer';

export const NumberSequenceDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const numberSequenceEntity = useAppSelector(state => state.numberSequence.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="numberSequenceDetailsHeading">
          <Translate contentKey="coopfullApp.numberSequence.detail.title">NumberSequence</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{numberSequenceEntity.id}</dd>
          <dt>
            <span id="sequenceType">
              <Translate contentKey="coopfullApp.numberSequence.sequenceType">Sequence Type</Translate>
            </span>
          </dt>
          <dd>{numberSequenceEntity.sequenceType}</dd>
          <dt>
            <span id="prefix">
              <Translate contentKey="coopfullApp.numberSequence.prefix">Prefix</Translate>
            </span>
          </dt>
          <dd>{numberSequenceEntity.prefix}</dd>
          <dt>
            <span id="year">
              <Translate contentKey="coopfullApp.numberSequence.year">Year</Translate>
            </span>
          </dt>
          <dd>{numberSequenceEntity.year}</dd>
          <dt>
            <span id="currentValue">
              <Translate contentKey="coopfullApp.numberSequence.currentValue">Current Value</Translate>
            </span>
          </dt>
          <dd>{numberSequenceEntity.currentValue}</dd>
          <dt>
            <span id="padding">
              <Translate contentKey="coopfullApp.numberSequence.padding">Padding</Translate>
            </span>
          </dt>
          <dd>{numberSequenceEntity.padding}</dd>
          <dt>
            <Translate contentKey="coopfullApp.numberSequence.cooperative">Cooperative</Translate>
          </dt>
          <dd>{numberSequenceEntity.cooperative ? numberSequenceEntity.cooperative.name : ''}</dd>
        </dl>
        <Button as={Link as any} to="/number-sequence" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/number-sequence/${numberSequenceEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default NumberSequenceDetail;
