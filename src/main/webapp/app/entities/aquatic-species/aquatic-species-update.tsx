import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { createEntity, getEntity, reset, updateEntity } from './aquatic-species.reducer';

export const AquaticSpeciesUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const aquaticSpeciesEntity = useAppSelector(state => state.aquaticSpecies.entity);
  const loading = useAppSelector(state => state.aquaticSpecies.loading);
  const updating = useAppSelector(state => state.aquaticSpecies.updating);
  const updateSuccess = useAppSelector(state => state.aquaticSpecies.updateSuccess);

  const handleClose = () => {
    navigate('/aquatic-species');
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
      ...aquaticSpeciesEntity,
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
          ...aquaticSpeciesEntity,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.aquaticSpecies.home.createOrEditLabel" data-cy="AquaticSpeciesCreateUpdateHeading">
            <Translate contentKey="coopfullApp.aquaticSpecies.home.createOrEditLabel">Create or edit a AquaticSpecies</Translate>
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
                  id="aquatic-species-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.aquaticSpecies.code')}
                id="aquatic-species-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.aquaticSpecies.name')}
                id="aquatic-species-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.aquaticSpecies.scientificName')}
                id="aquatic-species-scientificName"
                name="scientificName"
                data-cy="scientificName"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquaticSpecies.category')}
                id="aquatic-species-category"
                name="category"
                data-cy="category"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.aquaticSpecies.description')}
                id="aquatic-species-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.aquaticSpecies.freshwater')}
                id="aquatic-species-freshwater"
                name="freshwater"
                data-cy="freshwater"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('coopfullApp.aquaticSpecies.saltwater')}
                id="aquatic-species-saltwater"
                name="saltwater"
                data-cy="saltwater"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('coopfullApp.aquaticSpecies.active')}
                id="aquatic-species-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/aquatic-species" replace variant="info">
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

export default AquaticSpeciesUpdate;
