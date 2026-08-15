import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { TextFormat, Translate } from 'react-jhipster';
import { Link, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './member-document.reducer';

export const MemberDocumentDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id!));
  }, []);

  const memberDocumentEntity = useAppSelector(state => state.memberDocument.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="memberDocumentDetailsHeading">
          <Translate contentKey="coopfullApp.memberDocument.detail.title">MemberDocument</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{memberDocumentEntity.id}</dd>
          <dt>
            <span id="documentType">
              <Translate contentKey="coopfullApp.memberDocument.documentType">Document Type</Translate>
            </span>
          </dt>
          <dd>{memberDocumentEntity.documentType}</dd>
          <dt>
            <span id="originalFileName">
              <Translate contentKey="coopfullApp.memberDocument.originalFileName">Original File Name</Translate>
            </span>
          </dt>
          <dd>{memberDocumentEntity.originalFileName}</dd>
          <dt>
            <span id="storedFileName">
              <Translate contentKey="coopfullApp.memberDocument.storedFileName">Stored File Name</Translate>
            </span>
          </dt>
          <dd>{memberDocumentEntity.storedFileName}</dd>
          <dt>
            <span id="contentType">
              <Translate contentKey="coopfullApp.memberDocument.contentType">Content Type</Translate>
            </span>
          </dt>
          <dd>{memberDocumentEntity.contentType}</dd>
          <dt>
            <span id="fileSize">
              <Translate contentKey="coopfullApp.memberDocument.fileSize">File Size</Translate>
            </span>
          </dt>
          <dd>{memberDocumentEntity.fileSize}</dd>
          <dt>
            <span id="storagePath">
              <Translate contentKey="coopfullApp.memberDocument.storagePath">Storage Path</Translate>
            </span>
          </dt>
          <dd>{memberDocumentEntity.storagePath}</dd>
          <dt>
            <span id="checksum">
              <Translate contentKey="coopfullApp.memberDocument.checksum">Checksum</Translate>
            </span>
          </dt>
          <dd>{memberDocumentEntity.checksum}</dd>
          <dt>
            <span id="verificationStatus">
              <Translate contentKey="coopfullApp.memberDocument.verificationStatus">Verification Status</Translate>
            </span>
          </dt>
          <dd>{memberDocumentEntity.verificationStatus}</dd>
          <dt>
            <span id="uploadedAt">
              <Translate contentKey="coopfullApp.memberDocument.uploadedAt">Uploaded At</Translate>
            </span>
          </dt>
          <dd>
            {memberDocumentEntity.uploadedAt ? (
              <TextFormat value={memberDocumentEntity.uploadedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="verifiedAt">
              <Translate contentKey="coopfullApp.memberDocument.verifiedAt">Verified At</Translate>
            </span>
          </dt>
          <dd>
            {memberDocumentEntity.verifiedAt ? (
              <TextFormat value={memberDocumentEntity.verifiedAt} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
          <dt>
            <span id="notes">
              <Translate contentKey="coopfullApp.memberDocument.notes">Notes</Translate>
            </span>
          </dt>
          <dd>{memberDocumentEntity.notes}</dd>
          <dt>
            <Translate contentKey="coopfullApp.memberDocument.member">Member</Translate>
          </dt>
          <dd>{memberDocumentEntity.member ? memberDocumentEntity.member.memberNumber : ''}</dd>
          <dt>
            <Translate contentKey="coopfullApp.memberDocument.uploadedBy">Uploaded By</Translate>
          </dt>
          <dd>{memberDocumentEntity.uploadedBy ? memberDocumentEntity.uploadedBy.id : ''}</dd>
        </dl>
        <Button as={Link as any} to="/member-document" replace variant="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button as={Link as any} to={`/member-document/${memberDocumentEntity.id}/edit`} replace variant="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default MemberDocumentDetail;
