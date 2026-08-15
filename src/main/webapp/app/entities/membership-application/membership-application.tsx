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

import { getEntities } from './membership-application.reducer';

export const MembershipApplication = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const membershipApplicationList = useAppSelector(state => state.membershipApplication.entities);
  const loading = useAppSelector(state => state.membershipApplication.loading);

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
      <h2 id="membership-application-heading" data-cy="MembershipApplicationHeading">
        <Translate contentKey="coopfullApp.membershipApplication.home.title">Membership Applications</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.membershipApplication.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/membership-application/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.membershipApplication.home.createLabel">Create new Membership Application</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {membershipApplicationList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.membershipApplication.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('applicationNumber')}>
                  <Translate contentKey="coopfullApp.membershipApplication.applicationNumber">Application Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('applicationNumber')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="coopfullApp.membershipApplication.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('applicationDate')}>
                  <Translate contentKey="coopfullApp.membershipApplication.applicationDate">Application Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('applicationDate')} />
                </th>
                <th className="hand" onClick={sort('submittedAt')}>
                  <Translate contentKey="coopfullApp.membershipApplication.submittedAt">Submitted At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('submittedAt')} />
                </th>
                <th className="hand" onClick={sort('reviewedAt')}>
                  <Translate contentKey="coopfullApp.membershipApplication.reviewedAt">Reviewed At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reviewedAt')} />
                </th>
                <th className="hand" onClick={sort('approvedAt')}>
                  <Translate contentKey="coopfullApp.membershipApplication.approvedAt">Approved At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('approvedAt')} />
                </th>
                <th className="hand" onClick={sort('rejectedAt')}>
                  <Translate contentKey="coopfullApp.membershipApplication.rejectedAt">Rejected At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('rejectedAt')} />
                </th>
                <th className="hand" onClick={sort('rejectionReason')}>
                  <Translate contentKey="coopfullApp.membershipApplication.rejectionReason">Rejection Reason</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('rejectionReason')} />
                </th>
                <th className="hand" onClick={sort('reviewComments')}>
                  <Translate contentKey="coopfullApp.membershipApplication.reviewComments">Review Comments</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('reviewComments')} />
                </th>
                <th className="hand" onClick={sort('confirmation')}>
                  <Translate contentKey="coopfullApp.membershipApplication.confirmation">Confirmation</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('confirmation')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="coopfullApp.membershipApplication.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.membershipApplication.member">Member</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.membershipApplication.cooperative">Cooperative</Translate>{' '}
                  <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.membershipApplication.branch">Branch</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {membershipApplicationList.map(membershipApplication => (
                <tr key={`entity-${membershipApplication.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/membership-application/${membershipApplication.id}`} variant="link" size="sm">
                      {membershipApplication.id}
                    </Button>
                  </td>
                  <td>{membershipApplication.applicationNumber}</td>
                  <td>
                    <Translate contentKey={`coopfullApp.MembershipApplicationStatus.${membershipApplication.status}`} />
                  </td>
                  <td>
                    {membershipApplication.applicationDate ? (
                      <TextFormat type="date" value={membershipApplication.applicationDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {membershipApplication.submittedAt ? (
                      <TextFormat type="date" value={membershipApplication.submittedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {membershipApplication.reviewedAt ? (
                      <TextFormat type="date" value={membershipApplication.reviewedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {membershipApplication.approvedAt ? (
                      <TextFormat type="date" value={membershipApplication.approvedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {membershipApplication.rejectedAt ? (
                      <TextFormat type="date" value={membershipApplication.rejectedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{membershipApplication.rejectionReason}</td>
                  <td>{membershipApplication.reviewComments}</td>
                  <td>{membershipApplication.confirmation ? 'true' : 'false'}</td>
                  <td>{membershipApplication.notes}</td>
                  <td>
                    {membershipApplication.member ? (
                      <Link to={`/member/${membershipApplication.member.id}`}>{membershipApplication.member.memberNumber}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {membershipApplication.cooperative ? (
                      <Link to={`/cooperative/${membershipApplication.cooperative.id}`}>{membershipApplication.cooperative.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {membershipApplication.branch ? (
                      <Link to={`/cooperative-branch/${membershipApplication.branch.id}`}>{membershipApplication.branch.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/membership-application/${membershipApplication.id}`}
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
                        to={`/membership-application/${membershipApplication.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/membership-application/${membershipApplication.id}/delete`)}
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
              <Translate contentKey="coopfullApp.membershipApplication.home.notFound">No Membership Applications found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default MembershipApplication;
