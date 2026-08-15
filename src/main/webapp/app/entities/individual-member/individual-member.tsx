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

import { getEntities } from './individual-member.reducer';

export const IndividualMember = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const individualMemberList = useAppSelector(state => state.individualMember.entities);
  const loading = useAppSelector(state => state.individualMember.loading);

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
      <h2 id="individual-member-heading" data-cy="IndividualMemberHeading">
        <Translate contentKey="coopfullApp.individualMember.home.title">Individual Members</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.individualMember.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/individual-member/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.individualMember.home.createLabel">Create new Individual Member</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {individualMemberList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.individualMember.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('firstName')}>
                  <Translate contentKey="coopfullApp.individualMember.firstName">First Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('firstName')} />
                </th>
                <th className="hand" onClick={sort('middleName')}>
                  <Translate contentKey="coopfullApp.individualMember.middleName">Middle Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('middleName')} />
                </th>
                <th className="hand" onClick={sort('lastName')}>
                  <Translate contentKey="coopfullApp.individualMember.lastName">Last Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastName')} />
                </th>
                <th className="hand" onClick={sort('maidenName')}>
                  <Translate contentKey="coopfullApp.individualMember.maidenName">Maiden Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('maidenName')} />
                </th>
                <th className="hand" onClick={sort('gender')}>
                  <Translate contentKey="coopfullApp.individualMember.gender">Gender</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gender')} />
                </th>
                <th className="hand" onClick={sort('birthDate')}>
                  <Translate contentKey="coopfullApp.individualMember.birthDate">Birth Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('birthDate')} />
                </th>
                <th className="hand" onClick={sort('birthPlace')}>
                  <Translate contentKey="coopfullApp.individualMember.birthPlace">Birth Place</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('birthPlace')} />
                </th>
                <th className="hand" onClick={sort('nationality')}>
                  <Translate contentKey="coopfullApp.individualMember.nationality">Nationality</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nationality')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="coopfullApp.individualMember.email">Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('phoneNumber')}>
                  <Translate contentKey="coopfullApp.individualMember.phoneNumber">Phone Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('phoneNumber')} />
                </th>
                <th className="hand" onClick={sort('occupation')}>
                  <Translate contentKey="coopfullApp.individualMember.occupation">Occupation</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('occupation')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {individualMemberList.map(individualMember => (
                <tr key={`entity-${individualMember.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/individual-member/${individualMember.id}`} variant="link" size="sm">
                      {individualMember.id}
                    </Button>
                  </td>
                  <td>{individualMember.firstName}</td>
                  <td>{individualMember.middleName}</td>
                  <td>{individualMember.lastName}</td>
                  <td>{individualMember.maidenName}</td>
                  <td>{individualMember.gender}</td>
                  <td>
                    {individualMember.birthDate ? (
                      <TextFormat type="date" value={individualMember.birthDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{individualMember.birthPlace}</td>
                  <td>{individualMember.nationality}</td>
                  <td>{individualMember.email}</td>
                  <td>{individualMember.phoneNumber}</td>
                  <td>{individualMember.occupation}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/individual-member/${individualMember.id}`}
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
                        to={`/individual-member/${individualMember.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/individual-member/${individualMember.id}/delete`)}
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
              <Translate contentKey="coopfullApp.individualMember.home.notFound">No Individual Members found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default IndividualMember;
