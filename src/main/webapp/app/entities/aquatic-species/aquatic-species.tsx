import React, { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import { Translate, getSortState } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router';

import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC } from 'app/shared/util/pagination.constants';

import { getEntities } from './aquatic-species.reducer';

export const AquaticSpecies = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const aquaticSpeciesList = useAppSelector(state => state.aquaticSpecies.entities);
  const loading = useAppSelector(state => state.aquaticSpecies.loading);

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
      <h2 id="aquatic-species-heading" data-cy="AquaticSpeciesHeading">
        <Translate contentKey="coopfullApp.aquaticSpecies.home.title">Aquatic Species</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.aquaticSpecies.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/aquatic-species/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.aquaticSpecies.home.createLabel">Create new Aquatic Species</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {aquaticSpeciesList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.aquaticSpecies.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('code')}>
                  <Translate contentKey="coopfullApp.aquaticSpecies.code">Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('code')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="coopfullApp.aquaticSpecies.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('scientificName')}>
                  <Translate contentKey="coopfullApp.aquaticSpecies.scientificName">Scientific Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('scientificName')} />
                </th>
                <th className="hand" onClick={sort('category')}>
                  <Translate contentKey="coopfullApp.aquaticSpecies.category">Category</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('category')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="coopfullApp.aquaticSpecies.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('freshwater')}>
                  <Translate contentKey="coopfullApp.aquaticSpecies.freshwater">Freshwater</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('freshwater')} />
                </th>
                <th className="hand" onClick={sort('saltwater')}>
                  <Translate contentKey="coopfullApp.aquaticSpecies.saltwater">Saltwater</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('saltwater')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  <Translate contentKey="coopfullApp.aquaticSpecies.active">Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {aquaticSpeciesList.map(aquaticSpecies => (
                <tr key={`entity-${aquaticSpecies.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/aquatic-species/${aquaticSpecies.id}`} variant="link" size="sm">
                      {aquaticSpecies.id}
                    </Button>
                  </td>
                  <td>{aquaticSpecies.code}</td>
                  <td>{aquaticSpecies.name}</td>
                  <td>{aquaticSpecies.scientificName}</td>
                  <td>{aquaticSpecies.category}</td>
                  <td>{aquaticSpecies.description}</td>
                  <td>{aquaticSpecies.freshwater ? 'true' : 'false'}</td>
                  <td>{aquaticSpecies.saltwater ? 'true' : 'false'}</td>
                  <td>{aquaticSpecies.active ? 'true' : 'false'}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/aquatic-species/${aquaticSpecies.id}`}
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
                        to={`/aquatic-species/${aquaticSpecies.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/aquatic-species/${aquaticSpecies.id}/delete`)}
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
              <Translate contentKey="coopfullApp.aquaticSpecies.home.notFound">No Aquatic Species found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AquaticSpecies;
