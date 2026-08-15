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

import { getEntities } from './cooperative-branch.reducer';

export const CooperativeBranch = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const cooperativeBranchList = useAppSelector(state => state.cooperativeBranch.entities);
  const loading = useAppSelector(state => state.cooperativeBranch.loading);

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
      <h2 id="cooperative-branch-heading" data-cy="CooperativeBranchHeading">
        <Translate contentKey="coopfullApp.cooperativeBranch.home.title">Cooperative Branches</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.cooperativeBranch.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/cooperative-branch/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.cooperativeBranch.home.createLabel">Create new Cooperative Branch</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cooperativeBranchList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.cooperativeBranch.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('code')}>
                  <Translate contentKey="coopfullApp.cooperativeBranch.code">Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('code')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="coopfullApp.cooperativeBranch.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="coopfullApp.cooperativeBranch.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('phone')}>
                  <Translate contentKey="coopfullApp.cooperativeBranch.phone">Phone</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('phone')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="coopfullApp.cooperativeBranch.email">Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="coopfullApp.cooperativeBranch.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('openingDate')}>
                  <Translate contentKey="coopfullApp.cooperativeBranch.openingDate">Opening Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('openingDate')} />
                </th>
                <th className="hand" onClick={sort('closingDate')}>
                  <Translate contentKey="coopfullApp.cooperativeBranch.closingDate">Closing Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('closingDate')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.cooperativeBranch.cooperative">Cooperative</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.cooperativeBranch.location">Location</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cooperativeBranchList.map(cooperativeBranch => (
                <tr key={`entity-${cooperativeBranch.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/cooperative-branch/${cooperativeBranch.id}`} variant="link" size="sm">
                      {cooperativeBranch.id}
                    </Button>
                  </td>
                  <td>{cooperativeBranch.code}</td>
                  <td>{cooperativeBranch.name}</td>
                  <td>{cooperativeBranch.description}</td>
                  <td>{cooperativeBranch.phone}</td>
                  <td>{cooperativeBranch.email}</td>
                  <td>
                    <Translate contentKey={`coopfullApp.CooperativeStatus.${cooperativeBranch.status}`} />
                  </td>
                  <td>
                    {cooperativeBranch.openingDate ? (
                      <TextFormat type="date" value={cooperativeBranch.openingDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {cooperativeBranch.closingDate ? (
                      <TextFormat type="date" value={cooperativeBranch.closingDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {cooperativeBranch.cooperative ? (
                      <Link to={`/cooperative/${cooperativeBranch.cooperative.id}`}>{cooperativeBranch.cooperative.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {cooperativeBranch.location ? (
                      <Link to={`/location/${cooperativeBranch.location.id}`}>{cooperativeBranch.location.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/cooperative-branch/${cooperativeBranch.id}`}
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
                        to={`/cooperative-branch/${cooperativeBranch.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/cooperative-branch/${cooperativeBranch.id}/delete`)}
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
              <Translate contentKey="coopfullApp.cooperativeBranch.home.notFound">No Cooperative Branches found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default CooperativeBranch;
