import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './arml-profile.reducer';

export const ArmlProfile = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const armlProfileList = useAppSelector(state => state.armlProfile.entities);
  const loading = useAppSelector(state => state.armlProfile.loading);

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
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="arml-profile-heading" data-cy="ArmlProfileHeading">
        <Translate contentKey="majProfApp.armlProfile.home.title">Arml Profiles</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="majProfApp.armlProfile.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/arml-profile/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="majProfApp.armlProfile.home.createLabel">Create new Arml Profile</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {armlProfileList && armlProfileList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="majProfApp.armlProfile.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('winRate')}>
                  <Translate contentKey="majProfApp.armlProfile.winRate">Win Rate</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('winRate')} />
                </th>
                <th className="hand" onClick={sort('feedRate')}>
                  <Translate contentKey="majProfApp.armlProfile.feedRate">Feed Rate</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('feedRate')} />
                </th>
                <th className="hand" onClick={sort('callRate')}>
                  <Translate contentKey="majProfApp.armlProfile.callRate">Call Rate</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('callRate')} />
                </th>
                <th className="hand" onClick={sort('riiRate')}>
                  <Translate contentKey="majProfApp.armlProfile.riiRate">Rii Rate</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('riiRate')} />
                </th>
                <th className="hand" onClick={sort('feedEV')}>
                  <Translate contentKey="majProfApp.armlProfile.feedEV">Feed EV</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('feedEV')} />
                </th>
                <th>
                  <Translate contentKey="majProfApp.armlProfile.user">User</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="majProfApp.armlProfile.playerID">Player ID</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {armlProfileList.map((armlProfile, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/arml-profile/${armlProfile.id}`} color="link" size="sm">
                      {armlProfile.id}
                    </Button>
                  </td>
                  <td>{armlProfile.winRate}</td>
                  <td>{armlProfile.feedRate}</td>
                  <td>{armlProfile.callRate}</td>
                  <td>{armlProfile.riiRate}</td>
                  <td>{armlProfile.feedEV}</td>
                  <td>{armlProfile.user ? armlProfile.user.id : ''}</td>
                  <td>
                    {armlProfile.playerID ? <Link to={`/arml-player/${armlProfile.playerID.id}`}>{armlProfile.playerID.id}</Link> : ''}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/arml-profile/${armlProfile.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/arml-profile/${armlProfile.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/arml-profile/${armlProfile.id}/delete`)}
                        color="danger"
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
              <Translate contentKey="majProfApp.armlProfile.home.notFound">No Arml Profiles found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ArmlProfile;
