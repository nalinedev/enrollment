import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getMembers } from 'app/entities/member/member.reducer';
import { DocumentStatus } from 'app/shared/model/enumerations/document-status.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { createEntity, getEntity, reset, updateEntity } from './identity-document.reducer';

export const IdentityDocumentUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const members = useAppSelector(state => state.member.entities);
  const identityDocumentEntity = useAppSelector(state => state.identityDocument.entity);
  const loading = useAppSelector(state => state.identityDocument.loading);
  const updating = useAppSelector(state => state.identityDocument.updating);
  const updateSuccess = useAppSelector(state => state.identityDocument.updateSuccess);
  const documentStatusValues = Object.keys(DocumentStatus);

  const handleClose = () => {
    navigate('/identity-document');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMembers({}));
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
    values.verificationDate = convertDateTimeToServer(values.verificationDate);

    const entity = {
      ...identityDocumentEntity,
      ...values,
      member: members.find(it => it.id.toString() === values.member?.toString()),
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
          verificationDate: displayDefaultDateTime(),
        }
      : {
          status: 'VALID',
          ...identityDocumentEntity,
          verificationDate: convertDateTimeFromServer(identityDocumentEntity.verificationDate),
          member: identityDocumentEntity?.member?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.identityDocument.home.createOrEditLabel" data-cy="IdentityDocumentCreateUpdateHeading">
            <Translate contentKey="coopfullApp.identityDocument.home.createOrEditLabel">Create or edit a IdentityDocument</Translate>
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
                  id="identity-document-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.identityDocument.documentType')}
                id="identity-document-documentType"
                name="documentType"
                data-cy="documentType"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.identityDocument.documentNumber')}
                id="identity-document-documentNumber"
                name="documentNumber"
                data-cy="documentNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.identityDocument.issueDate')}
                id="identity-document-issueDate"
                name="issueDate"
                data-cy="issueDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.identityDocument.expiryDate')}
                id="identity-document-expiryDate"
                name="expiryDate"
                data-cy="expiryDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.identityDocument.issuingAuthority')}
                id="identity-document-issuingAuthority"
                name="issuingAuthority"
                data-cy="issuingAuthority"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.identityDocument.issuingCountry')}
                id="identity-document-issuingCountry"
                name="issuingCountry"
                data-cy="issuingCountry"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.identityDocument.status')}
                id="identity-document-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {documentStatusValues.map(documentStatus => (
                  <option value={documentStatus} key={documentStatus}>
                    {translate(`coopfullApp.DocumentStatus.${documentStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.identityDocument.verified')}
                id="identity-document-verified"
                name="verified"
                data-cy="verified"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('coopfullApp.identityDocument.verificationDate')}
                id="identity-document-verificationDate"
                name="verificationDate"
                data-cy="verificationDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('coopfullApp.identityDocument.verificationComment')}
                id="identity-document-verificationComment"
                name="verificationComment"
                data-cy="verificationComment"
                type="text"
              />
              <ValidatedField
                id="identity-document-member"
                name="member"
                data-cy="member"
                label={translate('coopfullApp.identityDocument.member')}
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
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/identity-document" replace variant="info">
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

export default IdentityDocumentUpdate;
