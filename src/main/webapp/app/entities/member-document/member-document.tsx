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

import { getEntities } from './member-document.reducer';

export const MemberDocument = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const memberDocumentList = useAppSelector(state => state.memberDocument.entities);
  const loading = useAppSelector(state => state.memberDocument.loading);

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
      <h2 id="member-document-heading" data-cy="MemberDocumentHeading">
        <Translate contentKey="coopfullApp.memberDocument.home.title">Member Documents</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.memberDocument.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/member-document/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.memberDocument.home.createLabel">Create new Member Document</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {memberDocumentList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.memberDocument.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('documentType')}>
                  <Translate contentKey="coopfullApp.memberDocument.documentType">Document Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('documentType')} />
                </th>
                <th className="hand" onClick={sort('originalFileName')}>
                  <Translate contentKey="coopfullApp.memberDocument.originalFileName">Original File Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('originalFileName')} />
                </th>
                <th className="hand" onClick={sort('storedFileName')}>
                  <Translate contentKey="coopfullApp.memberDocument.storedFileName">Stored File Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('storedFileName')} />
                </th>
                <th className="hand" onClick={sort('contentType')}>
                  <Translate contentKey="coopfullApp.memberDocument.contentType">Content Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('contentType')} />
                </th>
                <th className="hand" onClick={sort('fileSize')}>
                  <Translate contentKey="coopfullApp.memberDocument.fileSize">File Size</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('fileSize')} />
                </th>
                <th className="hand" onClick={sort('storagePath')}>
                  <Translate contentKey="coopfullApp.memberDocument.storagePath">Storage Path</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('storagePath')} />
                </th>
                <th className="hand" onClick={sort('checksum')}>
                  <Translate contentKey="coopfullApp.memberDocument.checksum">Checksum</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('checksum')} />
                </th>
                <th className="hand" onClick={sort('verificationStatus')}>
                  <Translate contentKey="coopfullApp.memberDocument.verificationStatus">Verification Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('verificationStatus')} />
                </th>
                <th className="hand" onClick={sort('uploadedAt')}>
                  <Translate contentKey="coopfullApp.memberDocument.uploadedAt">Uploaded At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('uploadedAt')} />
                </th>
                <th className="hand" onClick={sort('verifiedAt')}>
                  <Translate contentKey="coopfullApp.memberDocument.verifiedAt">Verified At</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('verifiedAt')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="coopfullApp.memberDocument.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.memberDocument.member">Member</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.memberDocument.uploadedBy">Uploaded By</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {memberDocumentList.map(memberDocument => (
                <tr key={`entity-${memberDocument.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/member-document/${memberDocument.id}`} variant="link" size="sm">
                      {memberDocument.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`coopfullApp.MemberDocumentType.${memberDocument.documentType}`} />
                  </td>
                  <td>{memberDocument.originalFileName}</td>
                  <td>{memberDocument.storedFileName}</td>
                  <td>{memberDocument.contentType}</td>
                  <td>{memberDocument.fileSize}</td>
                  <td>{memberDocument.storagePath}</td>
                  <td>{memberDocument.checksum}</td>
                  <td>
                    <Translate contentKey={`coopfullApp.DocumentVerificationStatus.${memberDocument.verificationStatus}`} />
                  </td>
                  <td>
                    {memberDocument.uploadedAt ? (
                      <TextFormat type="date" value={memberDocument.uploadedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>
                    {memberDocument.verifiedAt ? (
                      <TextFormat type="date" value={memberDocument.verifiedAt} format={APP_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{memberDocument.notes}</td>
                  <td>
                    {memberDocument.member ? (
                      <Link to={`/member/${memberDocument.member.id}`}>{memberDocument.member.memberNumber}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td>
                    {memberDocument.uploadedBy ? (
                      <Link to={`/app-user/${memberDocument.uploadedBy.id}`}>{memberDocument.uploadedBy.id}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/member-document/${memberDocument.id}`}
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
                        to={`/member-document/${memberDocument.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/member-document/${memberDocument.id}/delete`)}
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
              <Translate contentKey="coopfullApp.memberDocument.home.notFound">No Member Documents found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default MemberDocument;
