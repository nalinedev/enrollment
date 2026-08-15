import React, { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import { Translate, getSortState } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router';

import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC } from 'app/shared/util/pagination.constants';

import { getEntities } from './crop.reducer';

export const Crop = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const cropList = useAppSelector(state => state.crop.entities);
  const loading = useAppSelector(state => state.crop.loading);

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
      <h2 id="crop-heading" data-cy="CropHeading">
        <Translate contentKey="coopfullApp.crop.home.title">Crops</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.crop.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/crop/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.crop.home.createLabel">Create new Crop</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cropList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.crop.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('code')}>
                  <Translate contentKey="coopfullApp.crop.code">Code</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('code')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="coopfullApp.crop.name">Name</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('scientificName')}>
                  <Translate contentKey="coopfullApp.crop.scientificName">Scientific Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('scientificName')} />
                </th>
                <th className="hand" onClick={sort('category')}>
                  <Translate contentKey="coopfullApp.crop.category">Category</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('category')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="coopfullApp.crop.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('perennial')}>
                  <Translate contentKey="coopfullApp.crop.perennial">Perennial</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('perennial')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  <Translate contentKey="coopfullApp.crop.active">Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cropList.map(crop => (
                <tr key={`entity-${crop.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/crop/${crop.id}`} variant="link" size="sm">
                      {crop.id}
                    </Button>
                  </td>
                  <td>{crop.code}</td>
                  <td>{crop.name}</td>
                  <td>{crop.scientificName}</td>
                  <td>{crop.category}</td>
                  <td>{crop.description}</td>
                  <td>{crop.perennial ? 'true' : 'false'}</td>
                  <td>{crop.active ? 'true' : 'false'}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button as={Link as any} to={`/crop/${crop.id}`} variant="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button as={Link as any} to={`/crop/${crop.id}/edit`} variant="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (globalThis.location.href = `/crop/${crop.id}/delete`)}
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
              <Translate contentKey="coopfullApp.crop.home.notFound">No Crops found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Crop;
