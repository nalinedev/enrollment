import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCooperatives } from 'app/entities/cooperative/cooperative.reducer';
import { getEntities as getCooperativeBranches } from 'app/entities/cooperative-branch/cooperative-branch.reducer';
import { getEntities as getMembers } from 'app/entities/member/member.reducer';
import { MembershipApplicationStatus } from 'app/shared/model/enumerations/membership-application-status.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { createEntity, getEntity, reset, updateEntity } from './membership-application.reducer';

export const MembershipApplicationUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const members = useAppSelector(state => state.member.entities);
  const cooperatives = useAppSelector(state => state.cooperative.entities);
  const cooperativeBranches = useAppSelector(state => state.cooperativeBranch.entities);
  const membershipApplicationEntity = useAppSelector(state => state.membershipApplication.entity);
  const loading = useAppSelector(state => state.membershipApplication.loading);
  const updating = useAppSelector(state => state.membershipApplication.updating);
  const updateSuccess = useAppSelector(state => state.membershipApplication.updateSuccess);
  const membershipApplicationStatusValues = Object.keys(MembershipApplicationStatus);

  const handleClose = () => {
    navigate('/membership-application');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getMembers({}));
    dispatch(getCooperatives({}));
    dispatch(getCooperativeBranches({}));
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
    values.submittedAt = convertDateTimeToServer(values.submittedAt);
    values.reviewedAt = convertDateTimeToServer(values.reviewedAt);
    values.approvedAt = convertDateTimeToServer(values.approvedAt);
    values.rejectedAt = convertDateTimeToServer(values.rejectedAt);

    const entity = {
      ...membershipApplicationEntity,
      ...values,
      member: members.find(it => it.id.toString() === values.member?.toString()),
      cooperative: cooperatives.find(it => it.id.toString() === values.cooperative?.toString()),
      branch: cooperativeBranches.find(it => it.id.toString() === values.branch?.toString()),
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
          submittedAt: displayDefaultDateTime(),
          reviewedAt: displayDefaultDateTime(),
          approvedAt: displayDefaultDateTime(),
          rejectedAt: displayDefaultDateTime(),
        }
      : {
          status: 'DRAFT',
          ...membershipApplicationEntity,
          submittedAt: convertDateTimeFromServer(membershipApplicationEntity.submittedAt),
          reviewedAt: convertDateTimeFromServer(membershipApplicationEntity.reviewedAt),
          approvedAt: convertDateTimeFromServer(membershipApplicationEntity.approvedAt),
          rejectedAt: convertDateTimeFromServer(membershipApplicationEntity.rejectedAt),
          member: membershipApplicationEntity?.member?.id,
          cooperative: membershipApplicationEntity?.cooperative?.id,
          branch: membershipApplicationEntity?.branch?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.membershipApplication.home.createOrEditLabel" data-cy="MembershipApplicationCreateUpdateHeading">
            <Translate contentKey="coopfullApp.membershipApplication.home.createOrEditLabel">
              Create or edit a MembershipApplication
            </Translate>
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
                  id="membership-application-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.membershipApplication.applicationNumber')}
                id="membership-application-applicationNumber"
                name="applicationNumber"
                data-cy="applicationNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.membershipApplication.status')}
                id="membership-application-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {membershipApplicationStatusValues.map(membershipApplicationStatus => (
                  <option value={membershipApplicationStatus} key={membershipApplicationStatus}>
                    {translate(`coopfullApp.MembershipApplicationStatus.${membershipApplicationStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.membershipApplication.applicationDate')}
                id="membership-application-applicationDate"
                name="applicationDate"
                data-cy="applicationDate"
                type="date"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.membershipApplication.submittedAt')}
                id="membership-application-submittedAt"
                name="submittedAt"
                data-cy="submittedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('coopfullApp.membershipApplication.reviewedAt')}
                id="membership-application-reviewedAt"
                name="reviewedAt"
                data-cy="reviewedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('coopfullApp.membershipApplication.approvedAt')}
                id="membership-application-approvedAt"
                name="approvedAt"
                data-cy="approvedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('coopfullApp.membershipApplication.rejectedAt')}
                id="membership-application-rejectedAt"
                name="rejectedAt"
                data-cy="rejectedAt"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('coopfullApp.membershipApplication.rejectionReason')}
                id="membership-application-rejectionReason"
                name="rejectionReason"
                data-cy="rejectionReason"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.membershipApplication.reviewComments')}
                id="membership-application-reviewComments"
                name="reviewComments"
                data-cy="reviewComments"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.membershipApplication.confirmation')}
                id="membership-application-confirmation"
                name="confirmation"
                data-cy="confirmation"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('coopfullApp.membershipApplication.notes')}
                id="membership-application-notes"
                name="notes"
                data-cy="notes"
                type="textarea"
              />
              <ValidatedField
                id="membership-application-member"
                name="member"
                data-cy="member"
                label={translate('coopfullApp.membershipApplication.member')}
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
                id="membership-application-cooperative"
                name="cooperative"
                data-cy="cooperative"
                label={translate('coopfullApp.membershipApplication.cooperative')}
                type="select"
              >
                <option value="" key="0" />
                {cooperatives
                  ? cooperatives.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="membership-application-branch"
                name="branch"
                data-cy="branch"
                label={translate('coopfullApp.membershipApplication.branch')}
                type="select"
              >
                <option value="" key="0" />
                {cooperativeBranches
                  ? cooperativeBranches.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button
                as={Link as any}
                id="cancel-save"
                data-cy="entityCreateCancelButton"
                to="/membership-application"
                replace
                variant="info"
              >
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

export default MembershipApplicationUpdate;
