import React, { useEffect } from 'react';
import { Link, useParams } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntity } from './arml-profile.reducer';

export const ArmlProfileDetail = () => {
  const dispatch = useAppDispatch();

  const { id } = useParams<'id'>();

  useEffect(() => {
    dispatch(getEntity(id));
  }, []);

  const armlProfileEntity = useAppSelector(state => state.armlProfile.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="armlProfileDetailsHeading">
          <Translate contentKey="majProfApp.armlProfile.detail.title">ArmlProfile</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{armlProfileEntity.id}</dd>
          <dt>
            <span id="winRate">
              <Translate contentKey="majProfApp.armlProfile.winRate">Win Rate</Translate>
            </span>
          </dt>
          <dd>{armlProfileEntity.winRate}</dd>
          <dt>
            <span id="feedRate">
              <Translate contentKey="majProfApp.armlProfile.feedRate">Feed Rate</Translate>
            </span>
          </dt>
          <dd>{armlProfileEntity.feedRate}</dd>
          <dt>
            <span id="callRate">
              <Translate contentKey="majProfApp.armlProfile.callRate">Call Rate</Translate>
            </span>
          </dt>
          <dd>{armlProfileEntity.callRate}</dd>
          <dt>
            <span id="riiRate">
              <Translate contentKey="majProfApp.armlProfile.riiRate">Riichi Rate</Translate>
            </span>
          </dt>
          <dd>{armlProfileEntity.riiRate}</dd>
          <dt>
            <span id="feedEV">
              <Translate contentKey="majProfApp.armlProfile.feedEV">Feed EV</Translate>
            </span>
          </dt>
          <dd>{armlProfileEntity.feedEV}</dd>
          <dt>
            <Translate contentKey="majProfApp.armlProfile.user">User</Translate>
          </dt>
          <dd>{armlProfileEntity.user ? armlProfileEntity.user.id : ''}</dd>
          <dt>
            <Translate contentKey="majProfApp.armlProfile.playerID">Player ID</Translate>
          </dt>
          <dd>{armlProfileEntity.playerID ? armlProfileEntity.playerID.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/arml-profile" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/arml-profile/${armlProfileEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ArmlProfileDetail;
