import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './arml-player.reducer';

export const ArmlPlayerDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const armlPlayerEntity = useAppSelector(state => state.armlPlayer.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="armlPlayerDetailsHeading">
          <Translate contentKey="majProfApp.armlPlayer.detail.title">ArmlPlayer</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{armlPlayerEntity.id}</dd>
          <dt>
            <span id="playerID">
              <Translate contentKey="majProfApp.armlPlayer.playerID">Player ID</Translate>
            </span>
          </dt>
          <dd>{armlPlayerEntity.playerID}</dd>
          <dt>
            <span id="firstName">
              <Translate contentKey="majProfApp.armlPlayer.firstName">First Name</Translate>
            </span>
          </dt>
          <dd>{armlPlayerEntity.firstName}</dd>
          <dt>
            <span id="lastName">
              <Translate contentKey="majProfApp.armlPlayer.lastName">Last Name</Translate>
            </span>
          </dt>
          <dd>{armlPlayerEntity.lastName}</dd>
          <dt>
            <span id="tenhouName">
              <Translate contentKey="majProfApp.armlPlayer.tenhouName">Tenhou Name</Translate>
            </span>
          </dt>
          <dd>{armlPlayerEntity.tenhouName}</dd>
          <dt>
            <span id="league">
              <Translate contentKey="majProfApp.armlPlayer.league">League</Translate>
            </span>
          </dt>
          <dd>{armlPlayerEntity.league}</dd>
          <dt>
            <Translate contentKey="majProfApp.armlPlayer.games">Games</Translate>
          </dt>
          <dd>
            {armlPlayerEntity.games
              ? armlPlayerEntity.games.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {armlPlayerEntity.games && i === armlPlayerEntity.games.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/arml-player" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/arml-player/${armlPlayerEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ArmlPlayerDetail;
