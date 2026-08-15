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

import { getEntities } from './social-profile.reducer';

export const SocialProfile = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const socialProfileList = useAppSelector(state => state.socialProfile.entities);
  const loading = useAppSelector(state => state.socialProfile.loading);

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
      <h2 id="social-profile-heading" data-cy="SocialProfileHeading">
        <Translate contentKey="coopfullApp.socialProfile.home.title">Social Profiles</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.socialProfile.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/social-profile/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.socialProfile.home.createLabel">Create new Social Profile</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {socialProfileList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.socialProfile.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('maritalStatus')}>
                  <Translate contentKey="coopfullApp.socialProfile.maritalStatus">Marital Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('maritalStatus')} />
                </th>
                <th className="hand" onClick={sort('numberOfChildren')}>
                  <Translate contentKey="coopfullApp.socialProfile.numberOfChildren">Number Of Children</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('numberOfChildren')} />
                </th>
                <th className="hand" onClick={sort('numberOfDependents')}>
                  <Translate contentKey="coopfullApp.socialProfile.numberOfDependents">Number Of Dependents</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('numberOfDependents')} />
                </th>
                <th className="hand" onClick={sort('educationLevel')}>
                  <Translate contentKey="coopfullApp.socialProfile.educationLevel">Education Level</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('educationLevel')} />
                </th>
                <th className="hand" onClick={sort('housingStatus')}>
                  <Translate contentKey="coopfullApp.socialProfile.housingStatus">Housing Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('housingStatus')} />
                </th>
                <th className="hand" onClick={sort('residenceSince')}>
                  <Translate contentKey="coopfullApp.socialProfile.residenceSince">Residence Since</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('residenceSince')} />
                </th>
                <th className="hand" onClick={sort('disabilityStatus')}>
                  <Translate contentKey="coopfullApp.socialProfile.disabilityStatus">Disability Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('disabilityStatus')} />
                </th>
                <th className="hand" onClick={sort('disabilityDescription')}>
                  <Translate contentKey="coopfullApp.socialProfile.disabilityDescription">Disability Description</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('disabilityDescription')} />
                </th>
                <th className="hand" onClick={sort('socialCategory')}>
                  <Translate contentKey="coopfullApp.socialProfile.socialCategory">Social Category</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('socialCategory')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="coopfullApp.socialProfile.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {socialProfileList.map(socialProfile => (
                <tr key={`entity-${socialProfile.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/social-profile/${socialProfile.id}`} variant="link" size="sm">
                      {socialProfile.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`coopfullApp.MaritalStatus.${socialProfile.maritalStatus}`} />
                  </td>
                  <td>{socialProfile.numberOfChildren}</td>
                  <td>{socialProfile.numberOfDependents}</td>
                  <td>{socialProfile.educationLevel}</td>
                  <td>{socialProfile.housingStatus}</td>
                  <td>
                    {socialProfile.residenceSince ? (
                      <TextFormat type="date" value={socialProfile.residenceSince} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{socialProfile.disabilityStatus ? 'true' : 'false'}</td>
                  <td>{socialProfile.disabilityDescription}</td>
                  <td>{socialProfile.socialCategory}</td>
                  <td>{socialProfile.notes}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/social-profile/${socialProfile.id}`}
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
                        to={`/social-profile/${socialProfile.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/social-profile/${socialProfile.id}/delete`)}
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
              <Translate contentKey="coopfullApp.socialProfile.home.notFound">No Social Profiles found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SocialProfile;
