import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './livestock-type.reducer';

export const LivestockTypeUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const livestockTypeEntity = useAppSelector(state => state.livestockType.entity);
  const loading = useAppSelector(state => state.livestockType.loading);
  const updating = useAppSelector(state => state.livestockType.updating);
  const updateSuccess = useAppSelector(state => state.livestockType.updateSuccess);

  const handleClose = () => {
    navigate('/livestock-type');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }
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
      ...livestockTypeEntity,
      ...values,
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
          ...livestockTypeEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.livestockType.home.createOrEditLabel" data-cy="LivestockTypeCreateUpdateHeading">
            <Translate contentKey="coopfullApp.livestockType.home.createOrEditLabel">Create or edit a LivestockType</Translate>
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
                  id="livestock-type-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.livestockType.code')}
                id="livestock-type-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.livestockType.name')}
                id="livestock-type-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.livestockType.scientificName')}
                id="livestock-type-scientificName"
                name="scientificName"
                data-cy="scientificName"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockType.category')}
                id="livestock-type-category"
                name="category"
                data-cy="category"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockType.description')}
                id="livestock-type-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.livestockType.active')}
                id="livestock-type-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/livestock-type" replace variant="info">
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

export default LivestockTypeUpdate;
