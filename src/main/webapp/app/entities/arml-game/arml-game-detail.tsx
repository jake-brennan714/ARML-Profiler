import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './arml-game.reducer';

export const ArmlGameDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const armlGameEntity = useAppSelector(state => state.armlGame.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="armlGameDetailsHeading">
          <Translate contentKey="majProfApp.armlGame.detail.title">ArmlGame</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{armlGameEntity.id}</dd>
          <dt>
            <span id="gameID">
              <Translate contentKey="majProfApp.armlGame.gameID">Game ID</Translate>
            </span>
          </dt>
          <dd>{armlGameEntity.gameID}</dd>
          <dt>
            <Translate contentKey="majProfApp.armlGame.players">Players</Translate>
          </dt>
          <dd>
            {armlGameEntity.players
              ? armlGameEntity.players.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {armlGameEntity.players && i === armlGameEntity.players.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/arml-game" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/arml-game/${armlGameEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ArmlGameDetail;
