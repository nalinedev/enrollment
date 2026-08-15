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

import { getEntities } from './member.reducer';

export const Member = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const memberList = useAppSelector(state => state.member.entities);
  const loading = useAppSelector(state => state.member.loading);

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
      <h2 id="member-heading" data-cy="MemberHeading">
        <Translate contentKey="coopfullApp.member.home.title">Members</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.member.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/member/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.member.home.createLabel">Create new Member</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {memberList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.member.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('memberNumber')}>
                  <Translate contentKey="coopfullApp.member.memberNumber">Member Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('memberNumber')} />
                </th>
                <th className="hand" onClick={sort('memberType')}>
                  <Translate contentKey="coopfullApp.member.memberType">Member Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('memberType')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="coopfullApp.member.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('admissionDate')}>
                  <Translate contentKey="coopfullApp.member.admissionDate">Admission Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('admissionDate')} />
                </th>
                <th className="hand" onClick={sort('exitDate')}>
                  <Translate contentKey="coopfullApp.member.exitDate">Exit Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('exitDate')} />
                </th>
                <th className="hand" onClick={sort('exitReason')}>
                  <Translate contentKey="coopfullApp.member.exitReason">Exit Reason</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('exitReason')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="coopfullApp.member.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th className="hand" onClick={sort('createdDate')}>
                  <Translate contentKey="coopfullApp.member.createdDate">Created Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('createdDate')} />
                </th>
                <th className="hand" onClick={sort('lastModifiedDate')}>
                  <Translate contentKey="coopfullApp.member.lastModifiedDate">Last Modified Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastModifiedDate')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.member.individualMember">Individual Member</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.member.organizationMember">Organization Member</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.member.socialProfile">Social Profile</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.member.professionalProfile">Professional Profile</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.member.cooperative">Cooperative</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.member.branch">Branch</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {memberList.map(member => (
                <tr key={`entity-${member.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/member/${member.id}`} variant="link" size="sm">
                      {member.id}
                    </Button>
                  </td>
                  <td>{member.memberNumber}</td>
                  <td>
                    <Translate contentKey={`coopfullApp.MemberType.${member.memberType}`} />
                  </td>
                  <td>
                    <Translate contentKey={`coopfullApp.MemberStatus.${member.status}`} />
                  </td>
                  <td>
                    {member.admissionDate ? <TextFormat type="date" value={member.admissionDate} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{member.exitDate ? <TextFormat type="date" value={member.exitDate} format={APP_LOCAL_DATE_FORMAT} /> : null}</td>
                  <td>{member.exitReason}</td>
                  <td>{member.notes}</td>
                  <td>{member.createdDate ? <TextFormat type="date" value={member.createdDate} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>
                    {member.lastModifiedDate ? <TextFormat type="date" value={member.lastModifiedDate} format={APP_DATE_FORMAT} /> : null}
                  </td>
                  <td>
                    {member.individualMember ? (
                      <Link to={`/individual-member/${member.individualMember.id}`}>{member.individualMember.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {member.organizationMember ? (
                      <Link to={`/organization-member/${member.organizationMember.id}`}>{member.organizationMember.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {member.socialProfile ? <Link to={`/social-profile/${member.socialProfile.id}`}>{member.socialProfile.id}</Link> : ''}
                  </td>
                  <td>
                    {member.professionalProfile ? (
                      <Link to={`/professional-profile/${member.professionalProfile.id}`}>{member.professionalProfile.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>{member.cooperative ? <Link to={`/cooperative/${member.cooperative.id}`}>{member.cooperative.name}</Link> : ''}</td>
                  <td>{member.branch ? <Link to={`/cooperative-branch/${member.branch.id}`}>{member.branch.name}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button as={Link as any} to={`/member/${member.id}`} variant="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button as={Link as any} to={`/member/${member.id}/edit`} variant="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (globalThis.location.href = `/member/${member.id}/delete`)}
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
              <Translate contentKey="coopfullApp.member.home.notFound">No Members found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Member;
