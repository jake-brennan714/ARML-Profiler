import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './arml-player.reducer';

export const ArmlPlayer = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const armlPlayerList = useAppSelector(state => state.armlPlayer.entities);
  const loading = useAppSelector(state => state.armlPlayer.loading);

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
      <h2 id="arml-player-heading" data-cy="ArmlPlayerHeading">
        <Translate contentKey="majProfApp.armlPlayer.home.title">Arml Players</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="majProfApp.armlPlayer.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/arml-player/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="majProfApp.armlPlayer.home.createLabel">Create new Arml Player</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {armlPlayerList && armlPlayerList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="majProfApp.armlPlayer.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('playerID')}>
                  <Translate contentKey="majProfApp.armlPlayer.playerID">Player ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('playerID')} />
                </th>
                <th className="hand" onClick={sort('firstName')}>
                  <Translate contentKey="majProfApp.armlPlayer.firstName">First Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('firstName')} />
                </th>
                <th className="hand" onClick={sort('lastName')}>
                  <Translate contentKey="majProfApp.armlPlayer.lastName">Last Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('lastName')} />
                </th>
                <th className="hand" onClick={sort('tenhouName')}>
                  <Translate contentKey="majProfApp.armlPlayer.tenhouName">Tenhou Name</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tenhouName')} />
                </th>
                <th className="hand" onClick={sort('league')}>
                  <Translate contentKey="majProfApp.armlPlayer.league">League</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('league')} />
                </th>
                <th>
                  <Translate contentKey="majProfApp.armlPlayer.games">Games</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {armlPlayerList.map((armlPlayer, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/arml-player/${armlPlayer.id}`} color="link" size="sm">
                      {armlPlayer.id}
                    </Button>
                  </td>
                  <td>{armlPlayer.playerID}</td>
                  <td>{armlPlayer.firstName}</td>
                  <td>{armlPlayer.lastName}</td>
                  <td>{armlPlayer.tenhouName}</td>
                  <td>
                    <Translate contentKey={`majProfApp.ArmlLeague.${armlPlayer.league}`} />
                  </td>
                  <td>
                    {armlPlayer.games
                      ? armlPlayer.games.map((val, j) => (
                          <span key={j}>
                            <Link to={`/arml-game/${val.id}`}>{val.id}</Link>
                            {j === armlPlayer.games.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/arml-player/${armlPlayer.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/arml-player/${armlPlayer.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/arml-player/${armlPlayer.id}/delete`)}
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
              <Translate contentKey="majProfApp.armlPlayer.home.notFound">No Arml Players found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default ArmlPlayer;
