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

import { getEntities } from './aquaculture-activity.reducer';

export const AquacultureActivity = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const aquacultureActivityList = useAppSelector(state => state.aquacultureActivity.entities);
  const loading = useAppSelector(state => state.aquacultureActivity.loading);

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
      <h2 id="aquaculture-activity-heading" data-cy="AquacultureActivityHeading">
        <Translate contentKey="coopfullApp.aquacultureActivity.home.title">Aquaculture Activities</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.aquacultureActivity.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/aquaculture-activity/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.aquacultureActivity.home.createLabel">Create new Aquaculture Activity</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {aquacultureActivityList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('productionMode')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.productionMode">Production Mode</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productionMode')} />
                </th>
                <th className="hand" onClick={sort('ownershipType')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.ownershipType">Ownership Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ownershipType')} />
                </th>
                <th className="hand" onClick={sort('productionType')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.productionType">Production Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productionType')} />
                </th>
                <th className="hand" onClick={sort('systemType')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.systemType">System Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('systemType')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('totalArea')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.totalArea">Total Area</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalArea')} />
                </th>
                <th className="hand" onClick={sort('areaUnit')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.areaUnit">Area Unit</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('areaUnit')} />
                </th>
                <th className="hand" onClick={sort('waterSource')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.waterSource">Water Source</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('waterSource')} />
                </th>
                <th className="hand" onClick={sort('numberOfProductionUnits')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.numberOfProductionUnits">Number Of Production Units</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('numberOfProductionUnits')} />
                </th>
                <th className="hand" onClick={sort('productionUnitDescription')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.productionUnitDescription">Production Unit Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productionUnitDescription')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('annualRevenue')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.annualRevenue">Annual Revenue</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('annualRevenue')} />
                </th>
                <th className="hand" onClick={sort('monthlyRevenue')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.monthlyRevenue">Monthly Revenue</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('monthlyRevenue')} />
                </th>
                <th className="hand" onClick={sort('employees')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.employees">Employees</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('employees')} />
                </th>
                <th className="hand" onClick={sort('certification')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.certification">Certification</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('certification')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="coopfullApp.aquacultureActivity.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.aquacultureActivity.location">Location</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.aquacultureActivity.aquaticSpecies">Aquatic Species</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {aquacultureActivityList.map(aquacultureActivity => (
                <tr key={`entity-${aquacultureActivity.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/aquaculture-activity/${aquacultureActivity.id}`} variant="link" size="sm">
                      {aquacultureActivity.id}
                    </Button>
                  </td>
                  <td>{aquacultureActivity.name}</td>
                  <td>{aquacultureActivity.description}</td>
                  <td>
                    <Translate contentKey={`coopfullApp.AquacultureProductionMode.${aquacultureActivity.productionMode}`} />
                  </td>
                  <td>
                    <Translate contentKey={`coopfullApp.AquacultureOwnershipType.${aquacultureActivity.ownershipType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`coopfullApp.AquacultureProductionType.${aquacultureActivity.productionType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`coopfullApp.AquacultureSystemType.${aquacultureActivity.systemType}`} />
                  </td>
                  <td>
                    {aquacultureActivity.startDate ? (
                      <TextFormat type="date" value={aquacultureActivity.startDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{aquacultureActivity.totalArea}</td>
                  <td>{aquacultureActivity.areaUnit}</td>
                  <td>{aquacultureActivity.waterSource}</td>
                  <td>{aquacultureActivity.numberOfProductionUnits}</td>
                  <td>{aquacultureActivity.productionUnitDescription}</td>
                  <td>
                    <Translate contentKey={`coopfullApp.AquacultureStatus.${aquacultureActivity.status}`} />
                  </td>
                  <td>{aquacultureActivity.annualRevenue}</td>
                  <td>{aquacultureActivity.monthlyRevenue}</td>
                  <td>{aquacultureActivity.employees}</td>
                  <td>{aquacultureActivity.certification}</td>
                  <td>{aquacultureActivity.notes}</td>
                  <td>
                    {aquacultureActivity.location ? (
                      <Link to={`/location/${aquacultureActivity.location.id}`}>{aquacultureActivity.location.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {aquacultureActivity.aquaticSpecies ? (
                      <Link to={`/aquatic-species/${aquacultureActivity.aquaticSpecies.id}`}>
                        {aquacultureActivity.aquaticSpecies.name}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/aquaculture-activity/${aquacultureActivity.id}`}
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
                        to={`/aquaculture-activity/${aquacultureActivity.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/aquaculture-activity/${aquacultureActivity.id}/delete`)}
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
              <Translate contentKey="coopfullApp.aquacultureActivity.home.notFound">No Aquaculture Activities found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AquacultureActivity;
