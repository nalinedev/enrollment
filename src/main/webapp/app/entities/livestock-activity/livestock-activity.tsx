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

import { getEntities } from './livestock-activity.reducer';

export const LivestockActivity = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const livestockActivityList = useAppSelector(state => state.livestockActivity.entities);
  const loading = useAppSelector(state => state.livestockActivity.loading);

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
      <h2 id="livestock-activity-heading" data-cy="LivestockActivityHeading">
        <Translate contentKey="coopfullApp.livestockActivity.home.title">Livestock Activities</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.livestockActivity.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/livestock-activity/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.livestockActivity.home.createLabel">Create new Livestock Activity</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {livestockActivityList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.livestockActivity.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="coopfullApp.livestockActivity.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="coopfullApp.livestockActivity.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('productionMode')}>
                  <Translate contentKey="coopfullApp.livestockActivity.productionMode">Production Mode</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productionMode')} />
                </th>
                <th className="hand" onClick={sort('ownershipType')}>
                  <Translate contentKey="coopfullApp.livestockActivity.ownershipType">Ownership Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ownershipType')} />
                </th>
                <th className="hand" onClick={sort('productionType')}>
                  <Translate contentKey="coopfullApp.livestockActivity.productionType">Production Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('productionType')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="coopfullApp.livestockActivity.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('totalArea')}>
                  <Translate contentKey="coopfullApp.livestockActivity.totalArea">Total Area</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalArea')} />
                </th>
                <th className="hand" onClick={sort('areaUnit')}>
                  <Translate contentKey="coopfullApp.livestockActivity.areaUnit">Area Unit</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('areaUnit')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="coopfullApp.livestockActivity.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('numberOfAnimals')}>
                  <Translate contentKey="coopfullApp.livestockActivity.numberOfAnimals">Number Of Animals</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('numberOfAnimals')} />
                </th>
                <th className="hand" onClick={sort('annualRevenue')}>
                  <Translate contentKey="coopfullApp.livestockActivity.annualRevenue">Annual Revenue</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('annualRevenue')} />
                </th>
                <th className="hand" onClick={sort('monthlyRevenue')}>
                  <Translate contentKey="coopfullApp.livestockActivity.monthlyRevenue">Monthly Revenue</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('monthlyRevenue')} />
                </th>
                <th className="hand" onClick={sort('employees')}>
                  <Translate contentKey="coopfullApp.livestockActivity.employees">Employees</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('employees')} />
                </th>
                <th className="hand" onClick={sort('veterinaryServiceAvailable')}>
                  <Translate contentKey="coopfullApp.livestockActivity.veterinaryServiceAvailable">Veterinary Service Available</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('veterinaryServiceAvailable')} />
                </th>
                <th className="hand" onClick={sort('feedSource')}>
                  <Translate contentKey="coopfullApp.livestockActivity.feedSource">Feed Source</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('feedSource')} />
                </th>
                <th className="hand" onClick={sort('waterSource')}>
                  <Translate contentKey="coopfullApp.livestockActivity.waterSource">Water Source</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('waterSource')} />
                </th>
                <th className="hand" onClick={sort('certification')}>
                  <Translate contentKey="coopfullApp.livestockActivity.certification">Certification</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('certification')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="coopfullApp.livestockActivity.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.livestockActivity.location">Location</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.livestockActivity.livestockType">Livestock Type</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {livestockActivityList.map(livestockActivity => (
                <tr key={`entity-${livestockActivity.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/livestock-activity/${livestockActivity.id}`} variant="link" size="sm">
                      {livestockActivity.id}
                    </Button>
                  </td>
                  <td>{livestockActivity.name}</td>
                  <td>{livestockActivity.description}</td>
                  <td>
                    <Translate contentKey={`coopfullApp.LivestockProductionMode.${livestockActivity.productionMode}`} />
                  </td>
                  <td>
                    <Translate contentKey={`coopfullApp.LivestockOwnershipType.${livestockActivity.ownershipType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`coopfullApp.LivestockProductionType.${livestockActivity.productionType}`} />
                  </td>
                  <td>
                    {livestockActivity.startDate ? (
                      <TextFormat type="date" value={livestockActivity.startDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{livestockActivity.totalArea}</td>
                  <td>{livestockActivity.areaUnit}</td>
                  <td>
                    <Translate contentKey={`coopfullApp.LivestockStatus.${livestockActivity.status}`} />
                  </td>
                  <td>{livestockActivity.numberOfAnimals}</td>
                  <td>{livestockActivity.annualRevenue}</td>
                  <td>{livestockActivity.monthlyRevenue}</td>
                  <td>{livestockActivity.employees}</td>
                  <td>{livestockActivity.veterinaryServiceAvailable ? 'true' : 'false'}</td>
                  <td>{livestockActivity.feedSource}</td>
                  <td>{livestockActivity.waterSource}</td>
                  <td>{livestockActivity.certification}</td>
                  <td>{livestockActivity.notes}</td>
                  <td>
                    {livestockActivity.location ? (
                      <Link to={`/location/${livestockActivity.location.id}`}>{livestockActivity.location.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {livestockActivity.livestockType ? (
                      <Link to={`/livestock-type/${livestockActivity.livestockType.id}`}>{livestockActivity.livestockType.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/livestock-activity/${livestockActivity.id}`}
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
                        to={`/livestock-activity/${livestockActivity.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/livestock-activity/${livestockActivity.id}/delete`)}
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
              <Translate contentKey="coopfullApp.livestockActivity.home.notFound">No Livestock Activities found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default LivestockActivity;
