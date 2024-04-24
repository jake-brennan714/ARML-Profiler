import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IArmlPlayer } from 'app/shared/model/arml-player.model';
import { getEntities as getArmlPlayers } from 'app/entities/arml-player/arml-player.reducer';
import { IArmlGame } from 'app/shared/model/arml-game.model';
import { getEntity, updateEntity, createEntity, reset } from './arml-game.reducer';

export const ArmlGameUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const armlPlayers = useAppSelector(state => state.armlPlayer.entities);
  const armlGameEntity = useAppSelector(state => state.armlGame.entity);
  const loading = useAppSelector(state => state.armlGame.loading);
  const updating = useAppSelector(state => state.armlGame.updating);
  const updateSuccess = useAppSelector(state => state.armlGame.updateSuccess);

  const handleClose = () => {
    navigate('/arml-game');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

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
    if (values.gameID !== undefined && typeof values.gameID !== 'number') {
      values.gameID = Number(values.gameID);
    }

    const entity = {
      ...armlGameEntity,
      ...values,
      players: mapIdList(values.players),
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
          ...armlGameEntity,
          players: armlGameEntity?.players?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="majProfApp.armlGame.home.createOrEditLabel" data-cy="ArmlGameCreateUpdateHeading">
            <Translate contentKey="majProfApp.armlGame.home.createOrEditLabel">Create or edit a ArmlGame</Translate>
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
                  id="arml-game-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('majProfApp.armlGame.gameID')}
                id="arml-game-gameID"
                name="gameID"
                data-cy="gameID"
                type="text"
                validate={{
                  required: { value: true, message: translate('entity.validation.required') },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('majProfApp.armlGame.players')}
                id="arml-game-players"
                data-cy="players"
                type="select"
                multiple
                name="players"
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/arml-game" replace color="info">
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

export default ArmlGameUpdate;
