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

import { getEntities } from './family-member.reducer';

export const FamilyMember = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const familyMemberList = useAppSelector(state => state.familyMember.entities);
  const loading = useAppSelector(state => state.familyMember.loading);

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
      <h2 id="family-member-heading" data-cy="FamilyMemberHeading">
        <Translate contentKey="coopfullApp.familyMember.home.title">Family Members</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.familyMember.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/family-member/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.familyMember.home.createLabel">Create new Family Member</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {familyMemberList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.familyMember.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('firstName')}>
                  <Translate contentKey="coopfullApp.familyMember.firstName">First Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('firstName')} />
                </th>
                <th className="hand" onClick={sort('middleName')}>
                  <Translate contentKey="coopfullApp.familyMember.middleName">Middle Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('middleName')} />
                </th>
                <th className="hand" onClick={sort('lastName')}>
                  <Translate contentKey="coopfullApp.familyMember.lastName">Last Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastName')} />
                </th>
                <th className="hand" onClick={sort('relationship')}>
                  <Translate contentKey="coopfullApp.familyMember.relationship">Relationship</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('relationship')} />
                </th>
                <th className="hand" onClick={sort('gender')}>
                  <Translate contentKey="coopfullApp.familyMember.gender">Gender</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gender')} />
                </th>
                <th className="hand" onClick={sort('birthDate')}>
                  <Translate contentKey="coopfullApp.familyMember.birthDate">Birth Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('birthDate')} />
                </th>
                <th className="hand" onClick={sort('birthPlace')}>
                  <Translate contentKey="coopfullApp.familyMember.birthPlace">Birth Place</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('birthPlace')} />
                </th>
                <th className="hand" onClick={sort('nationality')}>
                  <Translate contentKey="coopfullApp.familyMember.nationality">Nationality</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('nationality')} />
                </th>
                <th className="hand" onClick={sort('phoneNumber')}>
                  <Translate contentKey="coopfullApp.familyMember.phoneNumber">Phone Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('phoneNumber')} />
                </th>
                <th className="hand" onClick={sort('occupation')}>
                  <Translate contentKey="coopfullApp.familyMember.occupation">Occupation</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('occupation')} />
                </th>
                <th className="hand" onClick={sort('dependent')}>
                  <Translate contentKey="coopfullApp.familyMember.dependent">Dependent</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('dependent')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="coopfullApp.familyMember.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.familyMember.member">Member</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {familyMemberList.map(familyMember => (
                <tr key={`entity-${familyMember.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/family-member/${familyMember.id}`} variant="link" size="sm">
                      {familyMember.id}
                    </Button>
                  </td>
                  <td>{familyMember.firstName}</td>
                  <td>{familyMember.middleName}</td>
                  <td>{familyMember.lastName}</td>
                  <td>{familyMember.relationship}</td>
                  <td>{familyMember.gender}</td>
                  <td>
                    {familyMember.birthDate ? (
                      <TextFormat type="date" value={familyMember.birthDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{familyMember.birthPlace}</td>
                  <td>{familyMember.nationality}</td>
                  <td>{familyMember.phoneNumber}</td>
                  <td>{familyMember.occupation}</td>
                  <td>{familyMember.dependent ? 'true' : 'false'}</td>
                  <td>{familyMember.notes}</td>
                  <td>
                    {familyMember.member ? <Link to={`/member/${familyMember.member.id}`}>{familyMember.member.memberNumber}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/family-member/${familyMember.id}`}
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
                        to={`/family-member/${familyMember.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/family-member/${familyMember.id}/delete`)}
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
              <Translate contentKey="coopfullApp.familyMember.home.notFound">No Family Members found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default FamilyMember;
