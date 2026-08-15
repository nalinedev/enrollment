import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getAppUsers } from 'app/entities/app-user/app-user.reducer';
import { getEntities as getMembers } from 'app/entities/member/member.reducer';
import { DocumentVerificationStatus } from 'app/shared/model/enumerations/document-verification-status.model';
import { MemberDocumentType } from 'app/shared/model/enumerations/member-document-type.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { createEntity, getEntity, reset, updateEntity } from './member-document.reducer';

export const MemberDocumentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const members = useAppSelector(state => state.member.entities);
  const appUsers = useAppSelector(state => state.appUser.entities);
  const memberDocumentEntity = useAppSelector(state => state.memberDocument.entity);
  const loading = useAppSelector(state => state.memberDocument.loading);
  const updating = useAppSelector(state => state.memberDocument.updating);
  const updateSuccess = useAppSelector(state => state.memberDocument.updateSuccess);
  const memberDocumentTypeValues = Object.keys(MemberDocumentType);
  const documentVerificationStatusValues = Object.keys(DocumentVerificationStatus);

  const handleClose = () => {
    navigate('/member-document');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMembers({}));
    dispatch(getAppUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.fileSize !== undefined && typeof values.fileSize !== 'number') {
      values.fileSize = Number(values.fileSize);
    }
    values.uploadedAt = convertDateTimeToServer(values.uploadedAt);
    values.verifiedAt = convertDateTimeToServer(values.verifiedAt);

    const entity = {
      ...memberDocumentEntity,
      ...values,
      member: members.find(it => it.id.toString() === values.member?.toString()),
      uploadedBy: appUsers.find(it => it.id.toString() === values.uploadedBy?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          uploadedAt: displayDefaultDateTime(),
          verifiedAt: displayDefaultDateTime(),
        }
      : {
          documentType: 'IDENTITY_CARD',
          verificationStatus: 'PENDING',
          ...memberDocumentEntity,
          uploadedAt: convertDateTimeFromServer(memberDocumentEntity.uploadedAt),
          verifiedAt: convertDateTimeFromServer(memberDocumentEntity.verifiedAt),
          member: memberDocumentEntity?.member?.id,
          uploadedBy: memberDocumentEntity?.uploadedBy?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.memberDocument.home.createOrEditLabel" data-cy="MemberDocumentCreateUpdateHeading">
            <Translate contentKey="coopfullApp.memberDocument.home.createOrEditLabel">Create or edit a MemberDocument</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew && (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="member-document-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.memberDocument.documentType')}
                id="member-document-documentType"
                name="documentType"
                data-cy="documentType"
                type="select"
              >
                {memberDocumentTypeValues.map(memberDocumentType => (
                  <option value={memberDocumentType} key={memberDocumentType}>
                    {translate(`coopfullApp.MemberDocumentType.${memberDocumentType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.memberDocument.originalFileName')}
                id="member-document-originalFileName"
                name="originalFileName"
                data-cy="originalFileName"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.memberDocument.storedFileName')}
                id="member-document-storedFileName"
                name="storedFileName"
                data-cy="storedFileName"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.memberDocument.contentType')}
                id="member-document-contentType"
                name="contentType"
                data-cy="contentType"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.memberDocument.fileSize')}
                id="member-document-fileSize"
                name="fileSize"
                data-cy="fileSize"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.memberDocument.storagePath')}
                id="member-document-storagePath"
                name="storagePath"
                data-cy="storagePath"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.memberDocument.checksum')}
                id="member-document-checksum"
                name="checksum"
                data-cy="checksum"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.memberDocument.verificationStatus')}
                id="member-document-verificationStatus"
                name="verificationStatus"
                data-cy="verificationStatus"
                type="select"
              >
                {documentVerificationStatusValues.map(documentVerificationStatus => (
                  <option value={documentVerificationStatus} key={documentVerificationStatus}>
                    {translate(`coopfullApp.DocumentVerificationStatus.${documentVerificationStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.memberDocument.uploadedAt')}
                id="member-document-uploadedAt"
                name="uploadedAt"
                data-cy="uploadedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.memberDocument.verifiedAt')}
                id="member-document-verifiedAt"
                name="verifiedAt"
                data-cy="verifiedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('coopfullApp.memberDocument.notes')}
                id="member-document-notes"
                name="notes"
                data-cy="notes"
                type="textarea"
              />
              <ValidatedField
                id="member-document-member"
                name="member"
                data-cy="member"
                label={translate('coopfullApp.memberDocument.member')}
                type="select"
              >
                <option value="" key="0" />
                {members
                  ? members.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.memberNumber}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="member-document-uploadedBy"
                name="uploadedBy"
                data-cy="uploadedBy"
                label={translate('coopfullApp.memberDocument.uploadedBy')}
                type="select"
              >
                <option value="" key="0" />
                {appUsers
                  ? appUsers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/member-document" replace variant="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button variant="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default MemberDocumentUpdate;
