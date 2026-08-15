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

import { getEntities } from './audit-log.reducer';

export const AuditLog = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const auditLogList = useAppSelector(state => state.auditLog.entities);
  const loading = useAppSelector(state => state.auditLog.loading);

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
      <h2 id="audit-log-heading" data-cy="AuditLogHeading">
        <Translate contentKey="coopfullApp.auditLog.home.title">Audit Logs</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.auditLog.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/audit-log/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.auditLog.home.createLabel">Create new Audit Log</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {auditLogList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.auditLog.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('action')}>
                  <Translate contentKey="coopfullApp.auditLog.action">Action</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('action')} />
                </th>
                <th className="hand" onClick={sort('entityName')}>
                  <Translate contentKey="coopfullApp.auditLog.entityName">Entity Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('entityName')} />
                </th>
                <th className="hand" onClick={sort('entityId')}>
                  <Translate contentKey="coopfullApp.auditLog.entityId">Entity Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('entityId')} />
                </th>
                <th className="hand" onClick={sort('username')}>
                  <Translate contentKey="coopfullApp.auditLog.username">Username</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('username')} />
                </th>
                <th className="hand" onClick={sort('cooperativeId')}>
                  <Translate contentKey="coopfullApp.auditLog.cooperativeId">Cooperative Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('cooperativeId')} />
                </th>
                <th className="hand" onClick={sort('branchId')}>
                  <Translate contentKey="coopfullApp.auditLog.branchId">Branch Id</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('branchId')} />
                </th>
                <th className="hand" onClick={sort('timestamp')}>
                  <Translate contentKey="coopfullApp.auditLog.timestamp">Timestamp</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('timestamp')} />
                </th>
                <th className="hand" onClick={sort('ipAddress')}>
                  <Translate contentKey="coopfullApp.auditLog.ipAddress">Ip Address</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ipAddress')} />
                </th>
                <th className="hand" onClick={sort('userAgent')}>
                  <Translate contentKey="coopfullApp.auditLog.userAgent">User Agent</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('userAgent')} />
                </th>
                <th className="hand" onClick={sort('oldValue')}>
                  <Translate contentKey="coopfullApp.auditLog.oldValue">Old Value</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('oldValue')} />
                </th>
                <th className="hand" onClick={sort('newValue')}>
                  <Translate contentKey="coopfullApp.auditLog.newValue">New Value</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('newValue')} />
                </th>
                <th className="hand" onClick={sort('description')}>
                  <Translate contentKey="coopfullApp.auditLog.description">Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('description')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.auditLog.appUser">App User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {auditLogList.map(auditLog => (
                <tr key={`entity-${auditLog.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/audit-log/${auditLog.id}`} variant="link" size="sm">
                      {auditLog.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`coopfullApp.AuditAction.${auditLog.action}`} />
                  </td>
                  <td>{auditLog.entityName}</td>
                  <td>{auditLog.entityId}</td>
                  <td>{auditLog.username}</td>
                  <td>{auditLog.cooperativeId}</td>
                  <td>{auditLog.branchId}</td>
                  <td>{auditLog.timestamp ? <TextFormat type="date" value={auditLog.timestamp} format={APP_DATE_FORMAT} /> : null}</td>
                  <td>{auditLog.ipAddress}</td>
                  <td>{auditLog.userAgent}</td>
                  <td>{auditLog.oldValue}</td>
                  <td>{auditLog.newValue}</td>
                  <td>{auditLog.description}</td>
                  <td>{auditLog.appUser ? <Link to={`/app-user/${auditLog.appUser.id}`}>{auditLog.appUser.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button as={Link as any} to={`/audit-log/${auditLog.id}`} variant="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button as={Link as any} to={`/audit-log/${auditLog.id}/edit`} variant="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (globalThis.location.href = `/audit-log/${auditLog.id}/delete`)}
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
              <Translate contentKey="coopfullApp.auditLog.home.notFound">No Audit Logs found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default AuditLog;
