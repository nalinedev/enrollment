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

import { getEntities } from './agricultural-production.reducer';

export const AgriculturalProduction = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const agriculturalProductionList = useAppSelector(state => state.agriculturalProduction.entities);
  const loading = useAppSelector(state => state.agriculturalProduction.loading);

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
      <h2 id="agricultural-production-heading" data-cy="AgriculturalProductionHeading">
        <Translate contentKey="coopfullApp.agriculturalProduction.home.title">Agricultural Productions</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.agriculturalProduction.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/agricultural-production/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.agriculturalProduction.home.createLabel">Create new Agricultural Production</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {agriculturalProductionList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.agriculturalProduction.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('area')}>
                  <Translate contentKey="coopfullApp.agriculturalProduction.area">Area</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('area')} />
                </th>
                <th className="hand" onClick={sort('areaUnit')}>
                  <Translate contentKey="coopfullApp.agriculturalProduction.areaUnit">Area Unit</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('areaUnit')} />
                </th>
                <th className="hand" onClick={sort('plantingDate')}>
                  <Translate contentKey="coopfullApp.agriculturalProduction.plantingDate">Planting Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('plantingDate')} />
                </th>
                <th className="hand" onClick={sort('harvestStartDate')}>
                  <Translate contentKey="coopfullApp.agriculturalProduction.harvestStartDate">Harvest Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('harvestStartDate')} />
                </th>
                <th className="hand" onClick={sort('harvestEndDate')}>
                  <Translate contentKey="coopfullApp.agriculturalProduction.harvestEndDate">Harvest End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('harvestEndDate')} />
                </th>
                <th className="hand" onClick={sort('productionQuantity')}>
                  <Translate contentKey="coopfullApp.agriculturalProduction.productionQuantity">Production Quantity</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productionQuantity')} />
                </th>
                <th className="hand" onClick={sort('productionUnit')}>
                  <Translate contentKey="coopfullApp.agriculturalProduction.productionUnit">Production Unit</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productionUnit')} />
                </th>
                <th className="hand" onClick={sort('expectedAnnualProduction')}>
                  <Translate contentKey="coopfullApp.agriculturalProduction.expectedAnnualProduction">Expected Annual Production</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('expectedAnnualProduction')} />
                </th>
                <th className="hand" onClick={sort('numberOfPlants')}>
                  <Translate contentKey="coopfullApp.agriculturalProduction.numberOfPlants">Number Of Plants</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('numberOfPlants')} />
                </th>
                <th className="hand" onClick={sort('plantingDensity')}>
                  <Translate contentKey="coopfullApp.agriculturalProduction.plantingDensity">Planting Density</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('plantingDensity')} />
                </th>
                <th className="hand" onClick={sort('productionYear')}>
                  <Translate contentKey="coopfullApp.agriculturalProduction.productionYear">Production Year</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productionYear')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="coopfullApp.agriculturalProduction.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="coopfullApp.agriculturalProduction.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.agriculturalProduction.agriculturalActivity">Agricultural Activity</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.agriculturalProduction.crop">Crop</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.agriculturalProduction.cropVariety">Crop Variety</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {agriculturalProductionList.map(agriculturalProduction => (
                <tr key={`entity-${agriculturalProduction.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/agricultural-production/${agriculturalProduction.id}`} variant="link" size="sm">
                      {agriculturalProduction.id}
                    </Button>
                  </td>
                  <td>{agriculturalProduction.area}</td>
                  <td>{agriculturalProduction.areaUnit}</td>
                  <td>
                    {agriculturalProduction.plantingDate ? (
                      <TextFormat type="date" value={agriculturalProduction.plantingDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {agriculturalProduction.harvestStartDate ? (
                      <TextFormat type="date" value={agriculturalProduction.harvestStartDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {agriculturalProduction.harvestEndDate ? (
                      <TextFormat type="date" value={agriculturalProduction.harvestEndDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{agriculturalProduction.productionQuantity}</td>
                  <td>{agriculturalProduction.productionUnit}</td>
                  <td>{agriculturalProduction.expectedAnnualProduction}</td>
                  <td>{agriculturalProduction.numberOfPlants}</td>
                  <td>{agriculturalProduction.plantingDensity}</td>
                  <td>{agriculturalProduction.productionYear}</td>
                  <td>
                    <Translate contentKey={`coopfullApp.ProductionStatus.${agriculturalProduction.status}`} />
                  </td>
                  <td>{agriculturalProduction.notes}</td>
                  <td>
                    {agriculturalProduction.agriculturalActivity ? (
                      <Link to={`/agricultural-activity/${agriculturalProduction.agriculturalActivity.id}`}>
                        {agriculturalProduction.agriculturalActivity.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {agriculturalProduction.crop ? (
                      <Link to={`/crop/${agriculturalProduction.crop.id}`}>{agriculturalProduction.crop.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {agriculturalProduction.cropVariety ? (
                      <Link to={`/crop-variety/${agriculturalProduction.cropVariety.id}`}>{agriculturalProduction.cropVariety.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/agricultural-production/${agriculturalProduction.id}`}
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
                        to={`/agricultural-production/${agriculturalProduction.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/agricultural-production/${agriculturalProduction.id}/delete`)}
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
              <Translate contentKey="coopfullApp.agriculturalProduction.home.notFound">No Agricultural Productions found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AgriculturalProduction;
