import React, { useEffect, useState } from 'react';
import { Button, Table } from 'react-bootstrap';
import { Translate, getSortState } from 'react-jhipster';
import { Link, useLocation, useNavigate } from 'react-router';

import { faSort, faSortDown, faSortUp } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { ASC, DESC } from 'app/shared/util/pagination.constants';

import { getEntities } from './number-sequence.reducer';

export const NumberSequence = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const numberSequenceList = useAppSelector(state => state.numberSequence.entities);
  const loading = useAppSelector(state => state.numberSequence.loading);

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
      <h2 id="number-sequence-heading" data-cy="NumberSequenceHeading">
        <Translate contentKey="coopfullApp.numberSequence.home.title">Number Sequences</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" variant="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="coopfullApp.numberSequence.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/number-sequence/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="coopfullApp.numberSequence.home.createLabel">Create new Number Sequence</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {numberSequenceList?.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="coopfullApp.numberSequence.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('sequenceType')}>
                  <Translate contentKey="coopfullApp.numberSequence.sequenceType">Sequence Type</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('sequenceType')} />
                </th>
                <th className="hand" onClick={sort('prefix')}>
                  <Translate contentKey="coopfullApp.numberSequence.prefix">Prefix</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('prefix')} />
                </th>
                <th className="hand" onClick={sort('year')}>
                  <Translate contentKey="coopfullApp.numberSequence.year">Year</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('year')} />
                </th>
                <th className="hand" onClick={sort('currentValue')}>
                  <Translate contentKey="coopfullApp.numberSequence.currentValue">Current Value</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('currentValue')} />
                </th>
                <th className="hand" onClick={sort('padding')}>
                  <Translate contentKey="coopfullApp.numberSequence.padding">Padding</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('padding')} />
                </th>
                <th>
                  <Translate contentKey="coopfullApp.numberSequence.cooperative">Cooperative</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {numberSequenceList.map(numberSequence => (
                <tr key={`entity-${numberSequence.id}`} data-cy="entityTable">
                  <td>
                    <Button as={Link as any} to={`/number-sequence/${numberSequence.id}`} variant="link" size="sm">
                      {numberSequence.id}
                    </Button>
                  </td>
                  <td>
                    <Translate contentKey={`coopfullApp.SequenceType.${numberSequence.sequenceType}`} />
                  </td>
                  <td>{numberSequence.prefix}</td>
                  <td>{numberSequence.year}</td>
                  <td>{numberSequence.currentValue}</td>
                  <td>{numberSequence.padding}</td>
                  <td>
                    {numberSequence.cooperative ? (
                      <Link to={`/cooperative/${numberSequence.cooperative.id}`}>{numberSequence.cooperative.name}</Link>
                    ) : (
                      ''
                    )}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button
                        as={Link as any}
                        to={`/number-sequence/${numberSequence.id}`}
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
                        to={`/number-sequence/${numberSequence.id}/edit`}
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
                        onClick={() => (globalThis.location.href = `/number-sequence/${numberSequence.id}/delete`)}
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
              <Translate contentKey="coopfullApp.numberSequence.home.notFound">No Number Sequences found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default NumberSequence;
