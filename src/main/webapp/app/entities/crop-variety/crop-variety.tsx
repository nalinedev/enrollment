import React, { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import { Translate, getSortState } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router';

import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC } from 'app/shared/util/pagination.constants';

import { getEntities } from './crop-variety.reducer';

export const CropVariety = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const cropVarietyList = useAppSelector(state => state.cropVariety.entities);
  const loading = useAppSelector(state => state.cropVariety.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const { order } = sortState;
    if (sortFieldName !== fieldName) {
      return faSort;
    }
    return order === ASC ? faSortUp : faSortDown;
  };

  return (
    <div>
      <h2 id="crop-variety-heading" data-cy="CropVarietyHeading">
        <Translate contentKey="coopfullApp.cropVariety.home.title">Crop Varieties</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.cropVariety.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/crop-variety/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.cropVariety.home.createLabel">Create new Crop Variety</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cropVarietyList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.cropVariety.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('code')}>
                  <Translate contentKey="coopfullApp.cropVariety.code">Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('code')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="coopfullApp.cropVariety.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="coopfullApp.cropVariety.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('origin')}>
                  <Translate contentKey="coopfullApp.cropVariety.origin">Origin</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('origin')} />
                </th>
                <th className="hand" onClick={sort('maturityDays')}>
                  <Translate contentKey="coopfullApp.cropVariety.maturityDays">Maturity Days</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('maturityDays')} />
                </th>
                <th className="hand" onClick={sort('yieldPotential')}>
                  <Translate contentKey="coopfullApp.cropVariety.yieldPotential">Yield Potential</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('yieldPotential')} />
                </th>
                <th className="hand" onClick={sort('diseaseResistance')}>
                  <Translate contentKey="coopfullApp.cropVariety.diseaseResistance">Disease Resistance</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('diseaseResistance')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  <Translate contentKey="coopfullApp.cropVariety.active">Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.cropVariety.crop">Crop</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cropVarietyList.map(cropVariety => (
                <tr key={`entity-${cropVariety.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/crop-variety/${cropVariety.id}`} variant="link" size="sm">
                      {cropVariety.id}
                    </Button>
                  </td>
                  <td>{cropVariety.code}</td>
                  <td>{cropVariety.name}</td>
                  <td>{cropVariety.description}</td>
                  <td>{cropVariety.origin}</td>
                  <td>{cropVariety.maturityDays}</td>
                  <td>{cropVariety.yieldPotential}</td>
                  <td>{cropVariety.diseaseResistance}</td>
                  <td>{cropVariety.active ? 'true' : 'false'}</td>
                  <td>{cropVariety.crop ? <Link to={`/crop/${cropVariety.crop.id}`}>{cropVariety.crop.name}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/crop-variety/${cropVariety.id}`}
                        variant="info"
                        size="sm"
                        data-cy="entityDetailsButton"
                      >
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        as={Link as any}
                        to={`/crop-variety/${cropVariety.id}/edit`}
                        variant="primary"
                        size="sm"
                        data-cy="entityEditButton"
                      >
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (globalThis.location.href = `/crop-variety/${cropVariety.id}/delete`)}
                        variant="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="coopfullApp.cropVariety.home.notFound">No Crop Varieties found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CropVariety;
