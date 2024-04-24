import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './arml-game-score.reducer';

export const ArmlGameScoreDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const armlGameScoreEntity = useAppSelector(state => state.armlGameScore.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="armlGameScoreDetailsHeading">
          <Translate contentKey="majProfApp.armlGameScore.detail.title">ArmlGameScore</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{armlGameScoreEntity.id}</dd>
          <dt>
            <span id="score">
              <Translate contentKey="majProfApp.armlGameScore.score">Score</Translate>
            </span>
          </dt>
          <dd>{armlGameScoreEntity.score}</dd>
          <dt>
            <Translate contentKey="majProfApp.armlGameScore.armlGame">Arml Game</Translate>
          </dt>
          <dd>{armlGameScoreEntity.armlGame ? armlGameScoreEntity.armlGame.id : ''}</dd>
          <dt>
            <Translate contentKey="majProfApp.armlGameScore.armlPlayer">Arml Player</Translate>
          </dt>
          <dd>{armlGameScoreEntity.armlPlayer ? armlGameScoreEntity.armlPlayer.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/arml-game-score" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/arml-game-score/${armlGameScoreEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ArmlGameScoreDetail;
