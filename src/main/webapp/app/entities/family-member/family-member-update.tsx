import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getMembers } from 'app/entities/member/member.reducer';

import { createEntity, getEntity, reset, updateEntity } from './family-member.reducer';

export const FamilyMemberUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const members = useAppSelector(state => state.member.entities);
  const familyMemberEntity = useAppSelector(state => state.familyMember.entity);
  const loading = useAppSelector(state => state.familyMember.loading);
  const updating = useAppSelector(state => state.familyMember.updating);
  const updateSuccess = useAppSelector(state => state.familyMember.updateSuccess);

  const handleClose = () => {
    navigate('/family-member');
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

    const entity = {
      ...familyMemberEntity,
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
      ? {}
      : {
          ...familyMemberEntity,
          member: familyMemberEntity?.member?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.familyMember.home.createOrEditLabel" data-cy="FamilyMemberCreateUpdateHeading">
            <Translate contentKey="coopfullApp.familyMember.home.createOrEditLabel">Create or edit a FamilyMember</Translate>
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
                  id="family-member-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.familyMember.firstName')}
                id="family-member-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.familyMember.middleName')}
                id="family-member-middleName"
                name="middleName"
                data-cy="middleName"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.familyMember.lastName')}
                id="family-member-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.familyMember.relationship')}
                id="family-member-relationship"
                name="relationship"
                data-cy="relationship"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.familyMember.gender')}
                id="family-member-gender"
                name="gender"
                data-cy="gender"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.familyMember.birthDate')}
                id="family-member-birthDate"
                name="birthDate"
                data-cy="birthDate"
                type="date"
              />
              <ValidatedField
                label={translate('coopfullApp.familyMember.birthPlace')}
                id="family-member-birthPlace"
                name="birthPlace"
                data-cy="birthPlace"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.familyMember.nationality')}
                id="family-member-nationality"
                name="nationality"
                data-cy="nationality"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.familyMember.phoneNumber')}
                id="family-member-phoneNumber"
                name="phoneNumber"
                data-cy="phoneNumber"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.familyMember.occupation')}
                id="family-member-occupation"
                name="occupation"
                data-cy="occupation"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.familyMember.dependent')}
                id="family-member-dependent"
                name="dependent"
                data-cy="dependent"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('coopfullApp.familyMember.notes')}
                id="family-member-notes"
                name="notes"
                data-cy="notes"
                type="textarea"
              />
              <ValidatedField
                id="family-member-member"
                name="member"
                data-cy="member"
                label={translate('coopfullApp.familyMember.member')}
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
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/family-member" replace variant="info">
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

export default FamilyMemberUpdate;
