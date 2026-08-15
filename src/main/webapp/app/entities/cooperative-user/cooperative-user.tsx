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

import { getEntities } from './cooperative-user.reducer';

export const CooperativeUser = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const cooperativeUserList = useAppSelector(state => state.cooperativeUser.entities);
  const loading = useAppSelector(state => state.cooperativeUser.loading);

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
      <h2 id="cooperative-user-heading" data-cy="CooperativeUserHeading">
        <Translate contentKey="coopfullApp.cooperativeUser.home.title">Cooperative Users</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.cooperativeUser.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/cooperative-user/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.cooperativeUser.home.createLabel">Create new Cooperative User</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cooperativeUserList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.cooperativeUser.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('startDate')}>
                  <Translate contentKey="coopfullApp.cooperativeUser.startDate">Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('startDate')} />
                </th>
                <th className="hand" onClick={sort('endDate')}>
                  <Translate contentKey="coopfullApp.cooperativeUser.endDate">End Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('endDate')} />
                </th>
                <th className="hand" onClick={sort('active')}>
                  <Translate contentKey="coopfullApp.cooperativeUser.active">Active</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('active')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.cooperativeUser.appUser">App User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.cooperativeUser.cooperative">Cooperative</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.cooperativeUser.role">Role</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cooperativeUserList.map(cooperativeUser => (
                <tr key={`entity-${cooperativeUser.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/cooperative-user/${cooperativeUser.id}`} variant="link" size="sm">
                      {cooperativeUser.id}
                    </Button>
                  </td>
                  <td>
                    {cooperativeUser.startDate ? (
                      <TextFormat type="date" value={cooperativeUser.startDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {cooperativeUser.endDate ? (
                      <TextFormat type="date" value={cooperativeUser.endDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{cooperativeUser.active ? 'true' : 'false'}</td>
                  <td>
                    {cooperativeUser.appUser ? (
                      <Link to={`/app-user/${cooperativeUser.appUser.id}`}>{cooperativeUser.appUser.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {cooperativeUser.cooperative ? (
                      <Link to={`/cooperative/${cooperativeUser.cooperative.id}`}>{cooperativeUser.cooperative.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {cooperativeUser.role ? (
                      <Link to={`/cooperative-role/${cooperativeUser.role.id}`}>{cooperativeUser.role.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/cooperative-user/${cooperativeUser.id}`}
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
                        to={`/cooperative-user/${cooperativeUser.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/cooperative-user/${cooperativeUser.id}/delete`)}
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
              <Translate contentKey="coopfullApp.cooperativeUser.home.notFound">No Cooperative Users found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CooperativeUser;
