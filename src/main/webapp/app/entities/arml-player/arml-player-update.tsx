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
import { ArmlLeague } from 'app/shared/model/enumerations/arml-league.model';
import { getEntity, updateEntity, createEntity, reset } from './arml-player.reducer';

export const ArmlPlayerUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const armlGames = useAppSelector(state => state.armlGame.entities);
  const armlPlayerEntity = useAppSelector(state => state.armlPlayer.entity);
  const loading = useAppSelector(state => state.armlPlayer.loading);
  const updating = useAppSelector(state => state.armlPlayer.updating);
  const updateSuccess = useAppSelector(state => state.armlPlayer.updateSuccess);
  const armlLeagueValues = Object.keys(ArmlLeague);

  const handleClose = () => {
    navigate('/arml-player');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getArmlGames({}));
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
    if (values.playerID !== undefined && typeof values.playerID !== 'number') {
      values.playerID = Number(values.playerID);
    }

    const entity = {
      ...armlPlayerEntity,
      ...values,
      games: mapIdList(values.games),
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
          league: 'A1',
          ...armlPlayerEntity,
          games: armlPlayerEntity?.games?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="majProfApp.armlPlayer.home.createOrEditLabel" data-cy="ArmlPlayerCreateUpdateHeading">
            <Translate contentKey="majProfApp.armlPlayer.home.createOrEditLabel">Create or edit a ArmlPlayer</Translate>
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
                  id="arml-player-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('majProfApp.armlPlayer.playerID')}
                id="arml-player-playerID"
                name="playerID"
                data-cy="playerID"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('majProfApp.armlPlayer.firstName')}
                id="arml-player-firstName"
                name="firstName"
                data-cy="firstName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('majProfApp.armlPlayer.lastName')}
                id="arml-player-lastName"
                name="lastName"
                data-cy="lastName"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                }}
              />
              <ValidatedField
                label={translate('majProfApp.armlPlayer.tenhouName')}
                id="arml-player-tenhouName"
                name="tenhouName"
                data-cy="tenhouName"
                type="text"
                validate={{
                  maxLength: { value: 8, message: translate('entity.validation.maxlength', { max: 8 }) },
                  pattern: { value: /\S+/, message: translate('entity.validation.pattern', { pattern: '\\S+' }) },
                }}
              />
              <ValidatedField
                label={translate('majProfApp.armlPlayer.league')}
                id="arml-player-league"
                name="league"
                data-cy="league"
                type="select"
              >
                {armlLeagueValues.map(armlLeague => (
                  <option value={armlLeague} key={armlLeague}>
                    {translate('majProfApp.ArmlLeague.' + armlLeague)}
                  </option>
                ))}
              </ValidatedField>
              <ValidatedField
                label={translate('majProfApp.armlPlayer.games')}
                id="arml-player-games"
                data-cy="games"
                type="select"
                multiple
                name="games"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/arml-player" replace color="info">
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

export default ArmlPlayerUpdate;
