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

import { getEntities } from './organization-member.reducer';

export const OrganizationMember = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const organizationMemberList = useAppSelector(state => state.organizationMember.entities);
  const loading = useAppSelector(state => state.organizationMember.loading);

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
      <h2 id="organization-member-heading" data-cy="OrganizationMemberHeading">
        <Translate contentKey="coopfullApp.organizationMember.home.title">Organization Members</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.organizationMember.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/organization-member/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.organizationMember.home.createLabel">Create new Organization Member</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {organizationMemberList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.organizationMember.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('legalName')}>
                  <Translate contentKey="coopfullApp.organizationMember.legalName">Legal Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('legalName')} />
                </th>
                <th className="hand" onClick={sort('tradeName')}>
                  <Translate contentKey="coopfullApp.organizationMember.tradeName">Trade Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tradeName')} />
                </th>
                <th className="hand" onClick={sort('registrationNumber')}>
                  <Translate contentKey="coopfullApp.organizationMember.registrationNumber">Registration Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('registrationNumber')} />
                </th>
                <th className="hand" onClick={sort('taxNumber')}>
                  <Translate contentKey="coopfullApp.organizationMember.taxNumber">Tax Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('taxNumber')} />
                </th>
                <th className="hand" onClick={sort('legalForm')}>
                  <Translate contentKey="coopfullApp.organizationMember.legalForm">Legal Form</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('legalForm')} />
                </th>
                <th className="hand" onClick={sort('registrationDate')}>
                  <Translate contentKey="coopfullApp.organizationMember.registrationDate">Registration Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('registrationDate')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="coopfullApp.organizationMember.email">Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('phoneNumber')}>
                  <Translate contentKey="coopfullApp.organizationMember.phoneNumber">Phone Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('phoneNumber')} />
                </th>
                <th className="hand" onClick={sort('website')}>
                  <Translate contentKey="coopfullApp.organizationMember.website">Website</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('website')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="coopfullApp.organizationMember.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {organizationMemberList.map(organizationMember => (
                <tr key={`entity-${organizationMember.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/organization-member/${organizationMember.id}`} variant="link" size="sm">
                      {organizationMember.id}
                    </Button>
                  </td>
                  <td>{organizationMember.legalName}</td>
                  <td>{organizationMember.tradeName}</td>
                  <td>{organizationMember.registrationNumber}</td>
                  <td>{organizationMember.taxNumber}</td>
                  <td>{organizationMember.legalForm}</td>
                  <td>
                    {organizationMember.registrationDate ? (
                      <TextFormat type="date" value={organizationMember.registrationDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{organizationMember.email}</td>
                  <td>{organizationMember.phoneNumber}</td>
                  <td>{organizationMember.website}</td>
                  <td>{organizationMember.description}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/organization-member/${organizationMember.id}`}
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
                        to={`/organization-member/${organizationMember.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/organization-member/${organizationMember.id}/delete`)}
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
              <Translate contentKey="coopfullApp.organizationMember.home.notFound">No Organization Members found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default OrganizationMember;
