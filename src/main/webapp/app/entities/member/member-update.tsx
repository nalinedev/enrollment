import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCooperatives } from 'app/entities/cooperative/cooperative.reducer';
import { getEntities as getCooperativeBranches } from 'app/entities/cooperative-branch/cooperative-branch.reducer';
import { getEntities as getIndividualMembers } from 'app/entities/individual-member/individual-member.reducer';
import { getEntities as getOrganizationMembers } from 'app/entities/organization-member/organization-member.reducer';
import { getEntities as getProfessionalProfiles } from 'app/entities/professional-profile/professional-profile.reducer';
import { getEntities as getSocialProfiles } from 'app/entities/social-profile/social-profile.reducer';
import { MemberStatus } from 'app/shared/model/enumerations/member-status.model';
import { MemberType } from 'app/shared/model/enumerations/member-type.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';

import { createEntity, getEntity, reset, updateEntity } from './member.reducer';

export const MemberUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const individualMembers = useAppSelector(state => state.individualMember.entities);
  const organizationMembers = useAppSelector(state => state.organizationMember.entities);
  const socialProfiles = useAppSelector(state => state.socialProfile.entities);
  const professionalProfiles = useAppSelector(state => state.professionalProfile.entities);
  const cooperatives = useAppSelector(state => state.cooperative.entities);
  const cooperativeBranches = useAppSelector(state => state.cooperativeBranch.entities);
  const memberEntity = useAppSelector(state => state.member.entity);
  const loading = useAppSelector(state => state.member.loading);
  const updating = useAppSelector(state => state.member.updating);
  const updateSuccess = useAppSelector(state => state.member.updateSuccess);
  const memberTypeValues = Object.keys(MemberType);
  const memberStatusValues = Object.keys(MemberStatus);

  const handleClose = () => {
    navigate('/member');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getIndividualMembers({}));
    dispatch(getOrganizationMembers({}));
    dispatch(getSocialProfiles({}));
    dispatch(getProfessionalProfiles({}));
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
    values.createdDate = convertDateTimeToServer(values.createdDate);
    values.lastModifiedDate = convertDateTimeToServer(values.lastModifiedDate);

    const entity = {
      ...memberEntity,
      ...values,
      individualMember: individualMembers.find(it => it.id.toString() === values.individualMember?.toString()),
      organizationMember: organizationMembers.find(it => it.id.toString() === values.organizationMember?.toString()),
      socialProfile: socialProfiles.find(it => it.id.toString() === values.socialProfile?.toString()),
      professionalProfile: professionalProfiles.find(it => it.id.toString() === values.professionalProfile?.toString()),
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
          createdDate: displayDefaultDateTime(),
          lastModifiedDate: displayDefaultDateTime(),
        }
      : {
          memberType: 'INDIVIDUAL',
          status: 'PENDING',
          ...memberEntity,
          createdDate: convertDateTimeFromServer(memberEntity.createdDate),
          lastModifiedDate: convertDateTimeFromServer(memberEntity.lastModifiedDate),
          individualMember: memberEntity?.individualMember?.id,
          organizationMember: memberEntity?.organizationMember?.id,
          socialProfile: memberEntity?.socialProfile?.id,
          professionalProfile: memberEntity?.professionalProfile?.id,
          cooperative: memberEntity?.cooperative?.id,
          branch: memberEntity?.branch?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.member.home.createOrEditLabel" data-cy="MemberCreateUpdateHeading">
            <Translate contentKey="coopfullApp.member.home.createOrEditLabel">Create or edit a Member</Translate>
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
                  id="member-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.member.memberNumber')}
                id="member-memberNumber"
                name="memberNumber"
                data-cy="memberNumber"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.member.memberType')}
                id="member-memberType"
                name="memberType"
                data-cy="memberType"
                type="select"
              >
                {memberTypeValues.map(memberType => (
                  <option value={memberType} key={memberType}>
                    {translate(`coopfullApp.MemberType.${memberType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.member.status')}
                id="member-status"
                name="status"
                data-cy="status"
                type="select"
              >
                {memberStatusValues.map(memberStatus => (
                  <option value={memberStatus} key={memberStatus}>
                    {translate(`coopfullApp.MemberStatus.${memberStatus}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.member.admissionDate')}
                id="member-admissionDate"
                name="admissionDate"
                data-cy="admissionDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.member.exitDate')}
                id="member-exitDate"
                name="exitDate"
                data-cy="exitDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.member.exitReason')}
                id="member-exitReason"
                name="exitReason"
                data-cy="exitReason"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.member.notes')}
                id="member-notes"
                name="notes"
                data-cy="notes"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.member.createdDate')}
                id="member-createdDate"
                name="createdDate"
                data-cy="createdDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.member.lastModifiedDate')}
                id="member-lastModifiedDate"
                name="lastModifiedDate"
                data-cy="lastModifiedDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                id="member-individualMember"
                name="individualMember"
                data-cy="individualMember"
                label={translate('coopfullApp.member.individualMember')}
                type="select"
              >
                <option value="" key="0" />
                {individualMembers
                  ? individualMembers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="member-organizationMember"
                name="organizationMember"
                data-cy="organizationMember"
                label={translate('coopfullApp.member.organizationMember')}
                type="select"
              >
                <option value="" key="0" />
                {organizationMembers
                  ? organizationMembers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="member-socialProfile"
                name="socialProfile"
                data-cy="socialProfile"
                label={translate('coopfullApp.member.socialProfile')}
                type="select"
              >
                <option value="" key="0" />
                {socialProfiles
                  ? socialProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="member-professionalProfile"
                name="professionalProfile"
                data-cy="professionalProfile"
                label={translate('coopfullApp.member.professionalProfile')}
                type="select"
              >
                <option value="" key="0" />
                {professionalProfiles
                  ? professionalProfiles.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="member-cooperative"
                name="cooperative"
                data-cy="cooperative"
                label={translate('coopfullApp.member.cooperative')}
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
                id="member-branch"
                name="branch"
                data-cy="branch"
                label={translate('coopfullApp.member.branch')}
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
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/member" replace variant="info">
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

export default MemberUpdate;
