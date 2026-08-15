import React, { useEffect } from 'react';
import { Button, Col, Row } from 'react-bootstrap';
import { Translate, ValidatedField, ValidatedForm, translate } from 'react-jhipster';
import { Link, useNavigate, useParams } from 'react-router';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntities as getCrops } from 'app/entities/crop/crop.reducer';

import { createEntity, getEntity, reset, updateEntity } from './crop-variety.reducer';

export const CropVarietyUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const crops = useAppSelector(state => state.crop.entities);
  const cropVarietyEntity = useAppSelector(state => state.cropVariety.entity);
  const loading = useAppSelector(state => state.cropVariety.loading);
  const updating = useAppSelector(state => state.cropVariety.updating);
  const updateSuccess = useAppSelector(state => state.cropVariety.updateSuccess);

  const handleClose = () => {
    navigate('/crop-variety');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getCrops({}));
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
    if (values.maturityDays !== undefined && typeof values.maturityDays !== 'number') {
      values.maturityDays = Number(values.maturityDays);
    }
    if (values.yieldPotential !== undefined && typeof values.yieldPotential !== 'number') {
      values.yieldPotential = Number(values.yieldPotential);
    }

    const entity = {
      ...cropVarietyEntity,
      ...values,
      crop: crops.find(it => it.id.toString() === values.crop?.toString()),
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
          ...cropVarietyEntity,
          crop: cropVarietyEntity?.crop?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="coopfullApp.cropVariety.home.createOrEditLabel" data-cy="CropVarietyCreateUpdateHeading">
            <Translate contentKey="coopfullApp.cropVariety.home.createOrEditLabel">Create or edit a CropVariety</Translate>
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
                  id="crop-variety-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              )}
              <ValidatedField
                label={translate('coopfullApp.cropVariety.code')}
                id="crop-variety-code"
                name="code"
                data-cy="code"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.cropVariety.name')}
                id="crop-variety-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('coopfullApp.cropVariety.description')}
                id="crop-variety-description"
                name="description"
                data-cy="description"
                type="textarea"
              />
              <ValidatedField
                label={translate('coopfullApp.cropVariety.origin')}
                id="crop-variety-origin"
                name="origin"
                data-cy="origin"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.cropVariety.maturityDays')}
                id="crop-variety-maturityDays"
                name="maturityDays"
                data-cy="maturityDays"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.cropVariety.yieldPotential')}
                id="crop-variety-yieldPotential"
                name="yieldPotential"
                data-cy="yieldPotential"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.cropVariety.diseaseResistance')}
                id="crop-variety-diseaseResistance"
                name="diseaseResistance"
                data-cy="diseaseResistance"
                type="text"
              />
              <ValidatedField
                label={translate('coopfullApp.cropVariety.active')}
                id="crop-variety-active"
                name="active"
                data-cy="active"
                check
                type="checkbox"
              />
              <ValidatedField
                id="crop-variety-crop"
                name="crop"
                data-cy="crop"
                label={translate('coopfullApp.cropVariety.crop')}
                type="select"
              >
                <option value="" key="0" />
                {crops
                  ? crops.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button as={Link as any} id="cancel-save" data-cy="entityCreateCancelButton" to="/crop-variety" replace variant="info">
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

export default CropVarietyUpdate;
