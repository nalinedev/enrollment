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

import { getEntities } from './economic-activity.reducer';

export const EconomicActivity = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const economicActivityList = useAppSelector(state => state.economicActivity.entities);
  const loading = useAppSelector(state => state.economicActivity.loading);

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
      <h2 id="economic-activity-heading" data-cy="EconomicActivityHeading">
        <Translate contentKey="coopfullApp.economicActivity.home.title">Economic Activities</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.economicActivity.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/economic-activity/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.economicActivity.home.createLabel">Create new Economic Activity</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {economicActivityList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.economicActivity.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="coopfullApp.economicActivity.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="coopfullApp.economicActivity.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('mainActivity')}>
                  <Translate contentKey="coopfullApp.economicActivity.mainActivity">Main Activity</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('mainActivity')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="coopfullApp.economicActivity.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  <Translate contentKey="coopfullApp.economicActivity.endDate">End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endDate')} />
                </th>
                <th className="hand" onClick={sort('annualRevenue')}>
                  <Translate contentKey="coopfullApp.economicActivity.annualRevenue">Annual Revenue</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('annualRevenue')} />
                </th>
                <th className="hand" onClick={sort('monthlyRevenue')}>
                  <Translate contentKey="coopfullApp.economicActivity.monthlyRevenue">Monthly Revenue</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('monthlyRevenue')} />
                </th>
                <th className="hand" onClick={sort('numberOfEmployees')}>
                  <Translate contentKey="coopfullApp.economicActivity.numberOfEmployees">Number Of Employees</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('numberOfEmployees')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="coopfullApp.economicActivity.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="coopfullApp.economicActivity.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.economicActivity.agriculturalActivity">Agricultural Activity</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.economicActivity.livestockActivity">Livestock Activity</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.economicActivity.aquacultureActivity">Aquaculture Activity</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.economicActivity.member">Member</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.economicActivity.activityType">Activity Type</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.economicActivity.location">Location</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {economicActivityList.map(economicActivity => (
                <tr key={`entity-${economicActivity.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/economic-activity/${economicActivity.id}`} variant="link" size="sm">
                      {economicActivity.id}
                    </Button>
                  </td>
                  <td>{economicActivity.name}</td>
                  <td>{economicActivity.description}</td>
                  <td>{economicActivity.mainActivity ? 'true' : 'false'}</td>
                  <td>
                    {economicActivity.startDate ? (
                      <TextFormat type="date" value={economicActivity.startDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {economicActivity.endDate ? (
                      <TextFormat type="date" value={economicActivity.endDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{economicActivity.annualRevenue}</td>
                  <td>{economicActivity.monthlyRevenue}</td>
                  <td>{economicActivity.numberOfEmployees}</td>
                  <td>
                    <Translate contentKey={`coopfullApp.EconomicActivityStatus.${economicActivity.status}`} />
                  </td>
                  <td>{economicActivity.notes}</td>
                  <td>
                    {economicActivity.agriculturalActivity ? (
                      <Link to={`/agricultural-activity/${economicActivity.agriculturalActivity.id}`}>
                        {economicActivity.agriculturalActivity.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {economicActivity.livestockActivity ? (
                      <Link to={`/livestock-activity/${economicActivity.livestockActivity.id}`}>
                        {economicActivity.livestockActivity.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {economicActivity.aquacultureActivity ? (
                      <Link to={`/aquaculture-activity/${economicActivity.aquacultureActivity.id}`}>
                        {economicActivity.aquacultureActivity.id}
                      </Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {economicActivity.member ? (
                      <Link to={`/member/${economicActivity.member.id}`}>{economicActivity.member.memberNumber}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {economicActivity.activityType ? (
                      <Link to={`/economic-activity-type/${economicActivity.activityType.id}`}>{economicActivity.activityType.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {economicActivity.location ? (
                      <Link to={`/location/${economicActivity.location.id}`}>{economicActivity.location.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/economic-activity/${economicActivity.id}`}
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
                        to={`/economic-activity/${economicActivity.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/economic-activity/${economicActivity.id}/delete`)}
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
              <Translate contentKey="coopfullApp.economicActivity.home.notFound">No Economic Activities found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default EconomicActivity;
