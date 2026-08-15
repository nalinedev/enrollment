import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, isNumber, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCooperatives } from 'app/entities/cooperative/cooperative.reducer';
import { SequenceType } from 'app/shared/model/enumerations/sequence-type.model';

import { createEntity, getEntity, reset, updateEntity } from './number-sequence.reducer';

export const NumberSequenceUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cooperatives = useAppSelector(state => state.cooperative.entities);
  const numberSequenceEntity = useAppSelector(state => state.numberSequence.entity);
  const loading = useAppSelector(state => state.numberSequence.loading);
  const updating = useAppSelector(state => state.numberSequence.updating);
  const updateSuccess = useAppSelector(state => state.numberSequence.updateSuccess);
  const sequenceTypeValues = Object.keys(SequenceType);

  const handleClose = () => {
    navigate('/number-sequence');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCooperatives({}));
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
    if (values.year !== undefined && typeof values.year !== 'number') {
      values.year = Number(values.year);
    }
    if (values.currentValue !== undefined && typeof values.currentValue !== 'number') {
      values.currentValue = Number(values.currentValue);
    }
    if (values.padding !== undefined && typeof values.padding !== 'number') {
      values.padding = Number(values.padding);
    }

    const entity = {
      ...numberSequenceEntity,
      ...values,
      cooperative: cooperatives.find(it => it.id.toString() === values.cooperative?.toString()),
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
          sequenceType: 'MEMBER',
          ...numberSequenceEntity,
          cooperative: numberSequenceEntity?.cooperative?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.numberSequence.home.createOrEditLabel" data-cy="NumberSequenceCreateUpdateHeading">
            <Translate contentKey="coopfullApp.numberSequence.home.createOrEditLabel">Create or edit a NumberSequence</Translate>
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
                  id="number-sequence-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.numberSequence.sequenceType')}
                id="number-sequence-sequenceType"
                name="sequenceType"
                data-cy="sequenceType"
                type="select"
              >
                {sequenceTypeValues.map(sequenceType => (
                  <option value={sequenceType} key={sequenceType}>
                    {translate(`coopfullApp.SequenceType.${sequenceType}`)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('coopfullApp.numberSequence.prefix')}
                id="number-sequence-prefix"
                name="prefix"
                data-cy="prefix"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.numberSequence.year')}
                id="number-sequence-year"
                name="year"
                data-cy="year"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.numberSequence.currentValue')}
                id="number-sequence-currentValue"
                name="currentValue"
                data-cy="currentValue"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.numberSequence.padding')}
                id="number-sequence-padding"
                name="padding"
                data-cy="padding"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="number-sequence-cooperative"
                name="cooperative"
                data-cy="cooperative"
                label={translate('coopfullApp.numberSequence.cooperative')}
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
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/number-sequence" replace variant="info">
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

export default NumberSequenceUpdate;
