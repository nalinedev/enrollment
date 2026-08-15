import React, { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import { TextFormat, Translate, getSortState } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router';

import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC } from 'app/shared/util/pagination.constants';

import { getEntities } from './cooperative.reducer';

export const Cooperative = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const cooperativeList = useAppSelector(state => state.cooperative.entities);
  const loading = useAppSelector(state => state.cooperative.loading);

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
      <h2 id="cooperative-heading" data-cy="CooperativeHeading">
        <Translate contentKey="coopfullApp.cooperative.home.title">Cooperatives</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.cooperative.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/cooperative/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.cooperative.home.createLabel">Create new Cooperative</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {cooperativeList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.cooperative.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('code')}>
                  <Translate contentKey="coopfullApp.cooperative.code">Code</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('code')} />
                </th>
                <th className="hand" onClick={sort('name')}>
                  <Translate contentKey="coopfullApp.cooperative.name">Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('name')} />
                </th>
                <th className="hand" onClick={sort('legalName')}>
                  <Translate contentKey="coopfullApp.cooperative.legalName">Legal Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('legalName')} />
                </th>
                <th className="hand" onClick={sort('registrationNumber')}>
                  <Translate contentKey="coopfullApp.cooperative.registrationNumber">Registration Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('registrationNumber')} />
                </th>
                <th className="hand" onClick={sort('taxNumber')}>
                  <Translate contentKey="coopfullApp.cooperative.taxNumber">Tax Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('taxNumber')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="coopfullApp.cooperative.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="coopfullApp.cooperative.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('foundedDate')}>
                  <Translate contentKey="coopfullApp.cooperative.foundedDate">Founded Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('foundedDate')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="coopfullApp.cooperative.email">Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('phone')}>
                  <Translate contentKey="coopfullApp.cooperative.phone">Phone</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('phone')} />
                </th>
                <th className="hand" onClick={sort('website')}>
                  <Translate contentKey="coopfullApp.cooperative.website">Website</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('website')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="coopfullApp.cooperative.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="coopfullApp.cooperative.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {cooperativeList.map(cooperative => (
                <tr key={`entity-${cooperative.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/cooperative/${cooperative.id}`} variant="link" size="sm">
                      {cooperative.id}
                    </Button>
                  </td>
                  <td>{cooperative.code}</td>
                  <td>{cooperative.name}</td>
                  <td>{cooperative.legalName}</td>
                  <td>{cooperative.registrationNumber}</td>
                  <td>{cooperative.taxNumber}</td>
                  <td>{cooperative.description}</td>
                  <td>
                    <Translate contentKey={`coopfullApp.CooperativeStatus.${cooperative.status}`} />
                  </td>
                  <td>
                    {cooperative.foundedDate ? (
                      <TextFormat type="date" value={cooperative.foundedDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{cooperative.email}</td>
                  <td>{cooperative.phone}</td>
                  <td>{cooperative.website}</td>
                  <td>
                    {cooperative.createdDate ? <TextFormat type="date" value={cooperative.createdDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {cooperative.lastModifiedDate ? (
                      <TextFormat type="date" value={cooperative.lastModifiedDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button as={Link as any} to={`/cooperative/${cooperative.id}`} variant="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button
                        as={Link as any}
                        to={`/cooperative/${cooperative.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/cooperative/${cooperative.id}/delete`)}
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
              <Translate contentKey="coopfullApp.cooperative.home.notFound">No Cooperatives found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Cooperative;
