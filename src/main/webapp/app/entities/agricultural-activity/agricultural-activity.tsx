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

import { getEntities } from './agricultural-activity.reducer';

export const AgriculturalActivity = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const agriculturalActivityList = useAppSelector(state => state.agriculturalActivity.entities);
  const loading = useAppSelector(state => state.agriculturalActivity.loading);

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
      <h2 id="agricultural-activity-heading" data-cy="AgriculturalActivityHeading">
        <Translate contentKey="coopfullApp.agriculturalActivity.home.title">Agricultural Activities</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.agriculturalActivity.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/agricultural-activity/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.agriculturalActivity.home.createLabel">Create new Agricultural Activity</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {agriculturalActivityList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.agriculturalActivity.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('totalArea')}>
                  <Translate contentKey="coopfullApp.agriculturalActivity.totalArea">Total Area</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('totalArea')} />
                </th>
                <th className="hand" onClick={sort('areaUnit')}>
                  <Translate contentKey="coopfullApp.agriculturalActivity.areaUnit">Area Unit</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('areaUnit')} />
                </th>
                <th className="hand" onClick={sort('exploitationMode')}>
                  <Translate contentKey="coopfullApp.agriculturalActivity.exploitationMode">Exploitation Mode</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('exploitationMode')} />
                </th>
                <th className="hand" onClick={sort('ownershipType')}>
                  <Translate contentKey="coopfullApp.agriculturalActivity.ownershipType">Ownership Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ownershipType')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="coopfullApp.agriculturalActivity.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('irrigationAvailable')}>
                  <Translate contentKey="coopfullApp.agriculturalActivity.irrigationAvailable">Irrigation Available</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('irrigationAvailable')} />
                </th>
                <th className="hand" onClick={sort('organicProduction')}>
                  <Translate contentKey="coopfullApp.agriculturalActivity.organicProduction">Organic Production</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('organicProduction')} />
                </th>
                <th className="hand" onClick={sort('certification')}>
                  <Translate contentKey="coopfullApp.agriculturalActivity.certification">Certification</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('certification')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="coopfullApp.agriculturalActivity.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.agriculturalActivity.location">Location</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {agriculturalActivityList.map(agriculturalActivity => (
                <tr key={`entity-${agriculturalActivity.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/agricultural-activity/${agriculturalActivity.id}`} variant="link" size="sm">
                      {agriculturalActivity.id}
                    </Button>
                  </td>
                  <td>{agriculturalActivity.totalArea}</td>
                  <td>{agriculturalActivity.areaUnit}</td>
                  <td>
                    <Translate contentKey={`coopfullApp.AgriculturalExploitationMode.${agriculturalActivity.exploitationMode}`} />
                  </td>
                  <td>
                    <Translate contentKey={`coopfullApp.LandOwnershipType.${agriculturalActivity.ownershipType}`} />
                  </td>
                  <td>
                    {agriculturalActivity.startDate ? (
                      <TextFormat type="date" value={agriculturalActivity.startDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{agriculturalActivity.irrigationAvailable ? 'true' : 'false'}</td>
                  <td>{agriculturalActivity.organicProduction ? 'true' : 'false'}</td>
                  <td>{agriculturalActivity.certification}</td>
                  <td>{agriculturalActivity.description}</td>
                  <td>
                    {agriculturalActivity.location ? (
                      <Link to={`/location/${agriculturalActivity.location.id}`}>{agriculturalActivity.location.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/agricultural-activity/${agriculturalActivity.id}`}
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
                        to={`/agricultural-activity/${agriculturalActivity.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/agricultural-activity/${agriculturalActivity.id}/delete`)}
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
              <Translate contentKey="coopfullApp.agriculturalActivity.home.notFound">No Agricultural Activities found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AgriculturalActivity;
