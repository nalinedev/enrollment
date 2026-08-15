import React, { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router';

import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC } from 'app/shared/util/pagination.constants';

import { getEntities } from './livestock-production.reducer';

export const LivestockProduction = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const livestockProductionList = useAppSelector(state => state.livestockProduction.entities);
  const loading = useAppSelector(state => state.livestockProduction.loading);

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
      <h2 id="livestock-production-heading" data-cy="LivestockProductionHeading">
        <Translate contentKey="coopfullApp.livestockProduction.home.title">Livestock Productions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.livestockProduction.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/livestock-production/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.livestockProduction.home.createLabel">Create new Livestock Production</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {livestockProductionList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.livestockProduction.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('productionDate')}>
                  <Translate contentKey="coopfullApp.livestockProduction.productionDate">Production Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productionDate')} />
                </th>
                <th className="hand" onClick={sort('animalSex')}>
                  <Translate contentKey="coopfullApp.livestockProduction.animalSex">Animal Sex</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('animalSex')} />
                </th>
                <th className="hand" onClick={sort('numberOfAnimals')}>
                  <Translate contentKey="coopfullApp.livestockProduction.numberOfAnimals">Number Of Animals</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('numberOfAnimals')} />
                </th>
                <th className="hand" onClick={sort('averageAgeMonths')}>
                  <Translate contentKey="coopfullApp.livestockProduction.averageAgeMonths">Average Age Months</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('averageAgeMonths')} />
                </th>
                <th className="hand" onClick={sort('averageWeightKg')}>
                  <Translate contentKey="coopfullApp.livestockProduction.averageWeightKg">Average Weight Kg</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('averageWeightKg')} />
                </th>
                <th className="hand" onClick={sort('productionQuantity')}>
                  <Translate contentKey="coopfullApp.livestockProduction.productionQuantity">Production Quantity</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productionQuantity')} />
                </th>
                <th className="hand" onClick={sort('productionUnit')}>
                  <Translate contentKey="coopfullApp.livestockProduction.productionUnit">Production Unit</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productionUnit')} />
                </th>
                <th className="hand" onClick={sort('mortalityCount')}>
                  <Translate contentKey="coopfullApp.livestockProduction.mortalityCount">Mortality Count</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mortalityCount')} />
                </th>
                <th className="hand" onClick={sort('birthCount')}>
                  <Translate contentKey="coopfullApp.livestockProduction.birthCount">Birth Count</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('birthCount')} />
                </th>
                <th className="hand" onClick={sort('soldCount')}>
                  <Translate contentKey="coopfullApp.livestockProduction.soldCount">Sold Count</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('soldCount')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="coopfullApp.livestockProduction.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="coopfullApp.livestockProduction.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.livestockProduction.livestockActivity">Livestock Activity</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {livestockProductionList.map(livestockProduction => (
                <tr key={`entity-${livestockProduction.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/livestock-production/${livestockProduction.id}`} variant="link" size="sm">
                      {livestockProduction.id}
                    </Button>
                  </td>
                  <td>
                    {livestockProduction.productionDate ? (
                      <TextFormat type="date" value={livestockProduction.productionDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    <Translate contentKey={`coopfullApp.AnimalSex.${livestockProduction.animalSex}`} />
                  </td>
                  <td>{livestockProduction.numberOfAnimals}</td>
                  <td>{livestockProduction.averageAgeMonths}</td>
                  <td>{livestockProduction.averageWeightKg}</td>
                  <td>{livestockProduction.productionQuantity}</td>
                  <td>{livestockProduction.productionUnit}</td>
                  <td>{livestockProduction.mortalityCount}</td>
                  <td>{livestockProduction.birthCount}</td>
                  <td>{livestockProduction.soldCount}</td>
                  <td>
                    <Translate contentKey={`coopfullApp.AnimalProductionStatus.${livestockProduction.status}`} />
                  </td>
                  <td>{livestockProduction.notes}</td>
                  <td>
                    {livestockProduction.livestockActivity ? (
                      <Link to={`/livestock-activity/${livestockProduction.livestockActivity.id}`}>
                        {livestockProduction.livestockActivity.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/livestock-production/${livestockProduction.id}`}
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
                        to={`/livestock-production/${livestockProduction.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/livestock-production/${livestockProduction.id}/delete`)}
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
              <Translate contentKey="coopfullApp.livestockProduction.home.notFound">No Livestock Productions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default LivestockProduction;
