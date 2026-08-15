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

import { getEntities } from './aquaculture-production.reducer';

export const AquacultureProduction = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const aquacultureProductionList = useAppSelector(state => state.aquacultureProduction.entities);
  const loading = useAppSelector(state => state.aquacultureProduction.loading);

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
      <h2 id="aquaculture-production-heading" data-cy="AquacultureProductionHeading">
        <Translate contentKey="coopfullApp.aquacultureProduction.home.title">Aquaculture Productions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.aquacultureProduction.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/aquaculture-production/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.aquacultureProduction.home.createLabel">Create new Aquaculture Production</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {aquacultureProductionList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.aquacultureProduction.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('productionDate')}>
                  <Translate contentKey="coopfullApp.aquacultureProduction.productionDate">Production Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productionDate')} />
                </th>
                <th className="hand" onClick={sort('numberOfAnimals')}>
                  <Translate contentKey="coopfullApp.aquacultureProduction.numberOfAnimals">Number Of Animals</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('numberOfAnimals')} />
                </th>
                <th className="hand" onClick={sort('stockingDensity')}>
                  <Translate contentKey="coopfullApp.aquacultureProduction.stockingDensity">Stocking Density</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('stockingDensity')} />
                </th>
                <th className="hand" onClick={sort('productionQuantity')}>
                  <Translate contentKey="coopfullApp.aquacultureProduction.productionQuantity">Production Quantity</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productionQuantity')} />
                </th>
                <th className="hand" onClick={sort('productionUnit')}>
                  <Translate contentKey="coopfullApp.aquacultureProduction.productionUnit">Production Unit</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productionUnit')} />
                </th>
                <th className="hand" onClick={sort('averageWeightGrams')}>
                  <Translate contentKey="coopfullApp.aquacultureProduction.averageWeightGrams">Average Weight Grams</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('averageWeightGrams')} />
                </th>
                <th className="hand" onClick={sort('mortalityCount')}>
                  <Translate contentKey="coopfullApp.aquacultureProduction.mortalityCount">Mortality Count</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mortalityCount')} />
                </th>
                <th className="hand" onClick={sort('stockingCount')}>
                  <Translate contentKey="coopfullApp.aquacultureProduction.stockingCount">Stocking Count</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('stockingCount')} />
                </th>
                <th className="hand" onClick={sort('harvestedCount')}>
                  <Translate contentKey="coopfullApp.aquacultureProduction.harvestedCount">Harvested Count</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('harvestedCount')} />
                </th>
                <th className="hand" onClick={sort('expectedProduction')}>
                  <Translate contentKey="coopfullApp.aquacultureProduction.expectedProduction">Expected Production</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('expectedProduction')} />
                </th>
                <th className="hand" onClick={sort('expectedHarvestDate')}>
                  <Translate contentKey="coopfullApp.aquacultureProduction.expectedHarvestDate">Expected Harvest Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('expectedHarvestDate')} />
                </th>
                <th className="hand" onClick={sort('actualHarvestDate')}>
                  <Translate contentKey="coopfullApp.aquacultureProduction.actualHarvestDate">Actual Harvest Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('actualHarvestDate')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="coopfullApp.aquacultureProduction.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="coopfullApp.aquacultureProduction.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.aquacultureProduction.aquacultureActivity">Aquaculture Activity</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {aquacultureProductionList.map(aquacultureProduction => (
                <tr key={`entity-${aquacultureProduction.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/aquaculture-production/${aquacultureProduction.id}`} variant="link" size="sm">
                      {aquacultureProduction.id}
                    </Button>
                  </td>
                  <td>
                    {aquacultureProduction.productionDate ? (
                      <TextFormat type="date" value={aquacultureProduction.productionDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{aquacultureProduction.numberOfAnimals}</td>
                  <td>{aquacultureProduction.stockingDensity}</td>
                  <td>{aquacultureProduction.productionQuantity}</td>
                  <td>{aquacultureProduction.productionUnit}</td>
                  <td>{aquacultureProduction.averageWeightGrams}</td>
                  <td>{aquacultureProduction.mortalityCount}</td>
                  <td>{aquacultureProduction.stockingCount}</td>
                  <td>{aquacultureProduction.harvestedCount}</td>
                  <td>{aquacultureProduction.expectedProduction}</td>
                  <td>
                    {aquacultureProduction.expectedHarvestDate ? (
                      <TextFormat type="date" value={aquacultureProduction.expectedHarvestDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {aquacultureProduction.actualHarvestDate ? (
                      <TextFormat type="date" value={aquacultureProduction.actualHarvestDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    <Translate contentKey={`coopfullApp.AquacultureProductionStatus.${aquacultureProduction.status}`} />
                  </td>
                  <td>{aquacultureProduction.notes}</td>
                  <td>
                    {aquacultureProduction.aquacultureActivity ? (
                      <Link to={`/aquaculture-activity/${aquacultureProduction.aquacultureActivity.id}`}>
                        {aquacultureProduction.aquacultureActivity.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/aquaculture-production/${aquacultureProduction.id}`}
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
                        to={`/aquaculture-production/${aquacultureProduction.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/aquaculture-production/${aquacultureProduction.id}/delete`)}
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
              <Translate contentKey="coopfullApp.aquacultureProduction.home.notFound">No Aquaculture Productions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AquacultureProduction;
