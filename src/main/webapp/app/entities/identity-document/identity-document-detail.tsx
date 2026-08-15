import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './identity-document.reducer';

export const IdentityDocumentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const identityDocumentEntity = useAppSelector(state => state.identityDocument.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="identityDocumentDetailsHeading">
          <Translate contentKey="coopfullApp.identityDocument.detail.title">IdentityDocument</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.id}</dd>
          <dt>
            <span id="documentType">
              <Translate contentKey="coopfullApp.identityDocument.documentType">Document Type</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.documentType}</dd>
          <dt>
            <span id="documentNumber">
              <Translate contentKey="coopfullApp.identityDocument.documentNumber">Document Number</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.documentNumber}</dd>
          <dt>
            <span id="issueDate">
              <Translate contentKey="coopfullApp.identityDocument.issueDate">Issue Date</Translate>
            </span>
          </dt>
          <dd>
            {identityDocumentEntity.issueDate ? (
              <TextFormat value={identityDocumentEntity.issueDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="expiryDate">
              <Translate contentKey="coopfullApp.identityDocument.expiryDate">Expiry Date</Translate>
            </span>
          </dt>
          <dd>
            {identityDocumentEntity.expiryDate ? (
              <TextFormat value={identityDocumentEntity.expiryDate} type="date" format={APP_LOCAL_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="issuingAuthority">
              <Translate contentKey="coopfullApp.identityDocument.issuingAuthority">Issuing Authority</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.issuingAuthority}</dd>
          <dt>
            <span id="issuingCountry">
              <Translate contentKey="coopfullApp.identityDocument.issuingCountry">Issuing Country</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.issuingCountry}</dd>
          <dt>
            <span id="status">
              <Translate contentKey="coopfullApp.identityDocument.status">Status</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.status}</dd>
          <dt>
            <span id="verified">
              <Translate contentKey="coopfullApp.identityDocument.verified">Verified</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.verified ? 'true' : 'false'}</dd>
          <dt>
            <span id="verificationDate">
              <Translate contentKey="coopfullApp.identityDocument.verificationDate">Verification Date</Translate>
            </span>
          </dt>
          <dd>
            {identityDocumentEntity.verificationDate ? (
              <TextFormat value={identityDocumentEntity.verificationDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="verificationComment">
              <Translate contentKey="coopfullApp.identityDocument.verificationComment">Verification Comment</Translate>
            </span>
          </dt>
          <dd>{identityDocumentEntity.verificationComment}</dd>
          <dt>
            <Translate contentKey="coopfullApp.identityDocument.member">Member</Translate>
          </dt>
          <dd>{identityDocumentEntity.member ? identityDocumentEntity.member.memberNumber : ''}</dd>
        </dl>
        <Button as={Link as any} to="/identity-document" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/identity-document/${identityDocumentEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default IdentityDocumentDetail;
