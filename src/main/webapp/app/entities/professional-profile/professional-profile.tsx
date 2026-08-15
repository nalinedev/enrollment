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

import { getEntities } from './professional-profile.reducer';

export const ProfessionalProfile = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const professionalProfileList = useAppSelector(state => state.professionalProfile.entities);
  const loading = useAppSelector(state => state.professionalProfile.loading);

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
      <h2 id="professional-profile-heading" data-cy="ProfessionalProfileHeading">
        <Translate contentKey="coopfullApp.professionalProfile.home.title">Professional Profiles</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.professionalProfile.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link
            to="/professional-profile/new"
            className="btn btn-primary jh-create-entity"
            id="jh-create-entity"
            data-cy="entityCreateButton"
          >
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.professionalProfile.home.createLabel">Create new Professional Profile</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {professionalProfileList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.professionalProfile.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('employmentStatus')}>
                  <Translate contentKey="coopfullApp.professionalProfile.employmentStatus">Employment Status</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('employmentStatus')} />
                </th>
                <th className="hand" onClick={sort('employerName')}>
                  <Translate contentKey="coopfullApp.professionalProfile.employerName">Employer Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('employerName')} />
                </th>
                <th className="hand" onClick={sort('jobTitle')}>
                  <Translate contentKey="coopfullApp.professionalProfile.jobTitle">Job Title</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('jobTitle')} />
                </th>
                <th className="hand" onClick={sort('profession')}>
                  <Translate contentKey="coopfullApp.professionalProfile.profession">Profession</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('profession')} />
                </th>
                <th className="hand" onClick={sort('sector')}>
                  <Translate contentKey="coopfullApp.professionalProfile.sector">Sector</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sector')} />
                </th>
                <th className="hand" onClick={sort('yearsOfExperience')}>
                  <Translate contentKey="coopfullApp.professionalProfile.yearsOfExperience">Years Of Experience</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('yearsOfExperience')} />
                </th>
                <th className="hand" onClick={sort('monthlyIncome')}>
                  <Translate contentKey="coopfullApp.professionalProfile.monthlyIncome">Monthly Income</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('monthlyIncome')} />
                </th>
                <th className="hand" onClick={sort('annualIncome')}>
                  <Translate contentKey="coopfullApp.professionalProfile.annualIncome">Annual Income</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('annualIncome')} />
                </th>
                <th className="hand" onClick={sort('employmentStartDate')}>
                  <Translate contentKey="coopfullApp.professionalProfile.employmentStartDate">Employment Start Date</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('employmentStartDate')} />
                </th>
                <th className="hand" onClick={sort('employerLocation')}>
                  <Translate contentKey="coopfullApp.professionalProfile.employerLocation">Employer Location</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('employerLocation')} />
                </th>
                <th className="hand" onClick={sort('notes')}>
                  <Translate contentKey="coopfullApp.professionalProfile.notes">Notes</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('notes')} />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {professionalProfileList.map(professionalProfile => (
                <tr key={`entity-${professionalProfile.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/professional-profile/${professionalProfile.id}`} variant="link" size="sm">
                      {professionalProfile.id}
                    </Button>
                  </td>
                  <td>{professionalProfile.employmentStatus}</td>
                  <td>{professionalProfile.employerName}</td>
                  <td>{professionalProfile.jobTitle}</td>
                  <td>{professionalProfile.profession}</td>
                  <td>{professionalProfile.sector}</td>
                  <td>{professionalProfile.yearsOfExperience}</td>
                  <td>{professionalProfile.monthlyIncome}</td>
                  <td>{professionalProfile.annualIncome}</td>
                  <td>
                    {professionalProfile.employmentStartDate ? (
                      <TextFormat type="date" value={professionalProfile.employmentStartDate} format={APP_LOCAL_DATE_FORMAT} />
                    ) : null}
                  </td>
                  <td>{professionalProfile.employerLocation}</td>
                  <td>{professionalProfile.notes}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/professional-profile/${professionalProfile.id}`}
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
                        to={`/professional-profile/${professionalProfile.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/professional-profile/${professionalProfile.id}/delete`)}
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
              <Translate contentKey="coopfullApp.professionalProfile.home.notFound">No Professional Profiles found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ProfessionalProfile;
