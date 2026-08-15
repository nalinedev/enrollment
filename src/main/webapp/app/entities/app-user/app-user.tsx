import React, { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router';

import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC } from 'app/shared/util/pagination.constants';

import { getEntities } from './app-user.reducer';

export const AppUser = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const appUserList = useAppSelector(state => state.appUser.entities);
  const loading = useAppSelector(state => state.appUser.loading);

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
      <h2 id="app-user-heading" data-cy="AppUserHeading">
        <Translate contentKey="coopfullApp.appUser.home.title">App Users</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.appUser.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/app-user/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.appUser.home.createLabel">Create new App User</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {appUserList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.appUser.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('employeeNumber')}>
                  <Translate contentKey="coopfullApp.appUser.employeeNumber">Employee Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('employeeNumber')} />
                </th>
                <th className="hand" onClick={sort('firstName')}>
                  <Translate contentKey="coopfullApp.appUser.firstName">First Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('firstName')} />
                </th>
                <th className="hand" onClick={sort('lastName')}>
                  <Translate contentKey="coopfullApp.appUser.lastName">Last Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastName')} />
                </th>
                <th className="hand" onClick={sort('phoneNumber')}>
                  <Translate contentKey="coopfullApp.appUser.phoneNumber">Phone Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('phoneNumber')} />
                </th>
                <th className="hand" onClick={sort('jobTitle')}>
                  <Translate contentKey="coopfullApp.appUser.jobTitle">Job Title</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('jobTitle')} />
                </th>
                <th className="hand" onClick={sort('department')}>
                  <Translate contentKey="coopfullApp.appUser.department">Department</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('department')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="coopfullApp.appUser.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="coopfullApp.appUser.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="coopfullApp.appUser.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.appUser.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {appUserList.map(appUser => (
                <tr key={`entity-${appUser.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/app-user/${appUser.id}`} variant="link" size="sm">
                      {appUser.id}
                    </Button>
                  </td>
                  <td>{appUser.employeeNumber}</td>
                  <td>{appUser.firstName}</td>
                  <td>{appUser.lastName}</td>
                  <td>{appUser.phoneNumber}</td>
                  <td>{appUser.jobTitle}</td>
                  <td>{appUser.department}</td>
                  <td>
                    <Translate contentKey={`coopfullApp.AppUserStatus.${appUser.status}`} />
                  </td>
                  <td>{appUser.createdDate ? <TextFormat type="date" value={appUser.createdDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {appUser.lastModifiedDate ? <TextFormat type="date" value={appUser.lastModifiedDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>{appUser.user ? appUser.user.login : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button as={Link as any} to={`/app-user/${appUser.id}`} variant="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button as={Link as any} to={`/app-user/${appUser.id}/edit`} variant="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (globalThis.location.href = `/app-user/${appUser.id}/delete`)}
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
              <Translate contentKey="coopfullApp.appUser.home.notFound">No App Users found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AppUser;
