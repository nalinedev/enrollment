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

import { getEntities } from './identity-document.reducer';

export const IdentityDocument = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const identityDocumentList = useAppSelector(state => state.identityDocument.entities);
  const loading = useAppSelector(state => state.identityDocument.loading);

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
      <h2 id="identity-document-heading" data-cy="IdentityDocumentHeading">
        <Translate contentKey="coopfullApp.identityDocument.home.title">Identity Documents</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.identityDocument.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/identity-document/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.identityDocument.home.createLabel">Create new Identity Document</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {identityDocumentList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.identityDocument.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('documentType')}>
                  <Translate contentKey="coopfullApp.identityDocument.documentType">Document Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('documentType')} />
                </th>
                <th className="hand" onClick={sort('documentNumber')}>
                  <Translate contentKey="coopfullApp.identityDocument.documentNumber">Document Number</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('documentNumber')} />
                </th>
                <th className="hand" onClick={sort('issueDate')}>
                  <Translate contentKey="coopfullApp.identityDocument.issueDate">Issue Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('issueDate')} />
                </th>
                <th className="hand" onClick={sort('expiryDate')}>
                  <Translate contentKey="coopfullApp.identityDocument.expiryDate">Expiry Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('expiryDate')} />
                </th>
                <th className="hand" onClick={sort('issuingAuthority')}>
                  <Translate contentKey="coopfullApp.identityDocument.issuingAuthority">Issuing Authority</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('issuingAuthority')} />
                </th>
                <th className="hand" onClick={sort('issuingCountry')}>
                  <Translate contentKey="coopfullApp.identityDocument.issuingCountry">Issuing Country</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('issuingCountry')} />
                </th>
                <th className="hand" onClick={sort('status')}>
                  <Translate contentKey="coopfullApp.identityDocument.status">Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('status')} />
                </th>
                <th className="hand" onClick={sort('verified')}>
                  <Translate contentKey="coopfullApp.identityDocument.verified">Verified</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('verified')} />
                </th>
                <th className="hand" onClick={sort('verificationDate')}>
                  <Translate contentKey="coopfullApp.identityDocument.verificationDate">Verification Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('verificationDate')} />
                </th>
                <th className="hand" onClick={sort('verificationComment')}>
                  <Translate contentKey="coopfullApp.identityDocument.verificationComment">Verification Comment</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('verificationComment')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.identityDocument.member">Member</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {identityDocumentList.map(identityDocument => (
                <tr key={`entity-${identityDocument.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/identity-document/${identityDocument.id}`} variant="link" size="sm">
                      {identityDocument.id}
                    </Button>
                  </td>
                  <td>{identityDocument.documentType}</td>
                  <td>{identityDocument.documentNumber}</td>
                  <td>
                    {identityDocument.issueDate ? (
                      <TextFormat type="date" value={identityDocument.issueDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {identityDocument.expiryDate ? (
                      <TextFormat type="date" value={identityDocument.expiryDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{identityDocument.issuingAuthority}</td>
                  <td>{identityDocument.issuingCountry}</td>
                  <td>
                    <Translate contentKey={`coopfullApp.DocumentStatus.${identityDocument.status}`} />
                  </td>
                  <td>{identityDocument.verified ? 'true' : 'false'}</td>
                  <td>
                    {identityDocument.verificationDate ? (
                      <TextFormat type="date" value={identityDocument.verificationDate} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{identityDocument.verificationComment}</td>
                  <td>
                    {identityDocument.member ? (
                      <Link to={`/member/${identityDocument.member.id}`}>{identityDocument.member.memberNumber}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/identity-document/${identityDocument.id}`}
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
                        to={`/identity-document/${identityDocument.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/identity-document/${identityDocument.id}/delete`)}
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
              <Translate contentKey="coopfullApp.identityDocument.home.notFound">No Identity Documents found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default IdentityDocument;
