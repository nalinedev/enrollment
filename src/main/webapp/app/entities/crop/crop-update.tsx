import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './crop.reducer';

export const CropUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const cropEntity = useAppSelector(state => state.crop.entity);
  const loading = useAppSelector(state => state.crop.loading);
  const updating = useAppSelector(state => state.crop.updating);
  const updateSuccess = useAppSelector(state => state.crop.updateSuccess);

  const handleClose = () => {
    navigate('/crop');
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
      ...cropEntity,
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
          ...cropEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.crop.home.createOrEditLabel" data-cy="CropCreateUpdateHeading">
            <Translate contentKey="coopfullApp.crop.home.createOrEditLabel">Create or edit a Crop</Translate>
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
                  id="crop-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.crop.code')}
                id="crop-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.crop.name')}
                id="crop-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.crop.scientificName')}
                id="crop-scientificName"
                name="scientificName"
                data-cy="scientificName"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.crop.category')}
                id="crop-category"
                name="category"
                data-cy="category"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.crop.description')}
                id="crop-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.crop.perennial')}
                id="crop-perennial"
                name="perennial"
                data-cy="perennial"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('coopfullApp.crop.active')}
                id="crop-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/crop" replace variant="info">
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

export default CropUpdate;
