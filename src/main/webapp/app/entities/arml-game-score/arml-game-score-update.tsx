import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IArmlGame } from 'app/shared/model/arml-game.model';
import { getEntities as getArmlGames } from 'app/entities/arml-game/arml-game.reducer';
import { IArmlPlayer } from 'app/shared/model/arml-player.model';
import { getEntities as getArmlPlayers } from 'app/entities/arml-player/arml-player.reducer';
import { IArmlGameScore } from 'app/shared/model/arml-game-score.model';
import { getEntity, updateEntity, createEntity, reset } from './arml-game-score.reducer';

export const ArmlGameScoreUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const armlGames = useAppSelector(state => state.armlGame.entities);
  const armlPlayers = useAppSelector(state => state.armlPlayer.entities);
  const armlGameScoreEntity = useAppSelector(state => state.armlGameScore.entity);
  const loading = useAppSelector(state => state.armlGameScore.loading);
  const updating = useAppSelector(state => state.armlGameScore.updating);
  const updateSuccess = useAppSelector(state => state.armlGameScore.updateSuccess);

  const handleClose = () => {
    navigate('/arml-game-score');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getArmlGames({}));
    dispatch(getArmlPlayers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  // eslint-disable-next-line complexity
  const saveEntity = values => {
    if (values.id !== undefined && typeof values.id !== 'number') {
      values.id = Number(values.id);
    }
    if (values.score !== undefined && typeof values.score !== 'number') {
      values.score = Number(values.score);
    }

    const entity = {
      ...armlGameScoreEntity,
      ...values,
      armlGame: armlGames.find(it => it.id.toString() === values.armlGame?.toString()),
      armlPlayer: armlPlayers.find(it => it.id.toString() === values.armlPlayer?.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...armlGameScoreEntity,
          armlGame: armlGameScoreEntity?.armlGame?.id,
          armlPlayer: armlGameScoreEntity?.armlPlayer?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="majProfApp.armlGameScore.home.createOrEditLabel" data-cy="ArmlGameScoreCreateUpdateHeading">
            <Translate contentKey="majProfApp.armlGameScore.home.createOrEditLabel">Create or edit a ArmlGameScore</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="arml-game-score-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('majProfApp.armlGameScore.score')}
                id="arml-game-score-score"
                name="score"
                data-cy="score"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                id="arml-game-score-armlGame"
                name="armlGame"
                data-cy="armlGame"
                label={translate('majProfApp.armlGameScore.armlGame')}
                type="select"
              >
                <option value="" key="0" />
                {armlGames
                  ? armlGames.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="arml-game-score-armlPlayer"
                name="armlPlayer"
                data-cy="armlPlayer"
                label={translate('majProfApp.armlGameScore.armlPlayer')}
                type="select"
              >
                <option value="" key="0" />
                {armlPlayers
                  ? armlPlayers.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/arml-game-score" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ArmlGameScoreUpdate;
