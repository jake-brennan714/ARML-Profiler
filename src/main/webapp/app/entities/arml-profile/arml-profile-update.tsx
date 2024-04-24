import React, { useState, useEffect } from 'react';
import { Link, useNavigate, useParams } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/modules/administration/user-management/user-management.reducer';
import { IArmlPlayer } from 'app/shared/model/arml-player.model';
import { getEntities as getArmlPlayers } from 'app/entities/arml-player/arml-player.reducer';
import { IArmlProfile } from 'app/shared/model/arml-profile.model';
import { getEntity, updateEntity, createEntity, reset } from './arml-profile.reducer';

export const ArmlProfileUpdate = () => {
  const dispatch = useAppDispatch();

  const navigate = useNavigate();

  const { id } = useParams<'id'>();
  const isNew = id === undefined;

  const users = useAppSelector(state => state.userManagement.users);
  const armlPlayers = useAppSelector(state => state.armlPlayer.entities);
  const armlProfileEntity = useAppSelector(state => state.armlProfile.entity);
  const loading = useAppSelector(state => state.armlProfile.loading);
  const updating = useAppSelector(state => state.armlProfile.updating);
  const updateSuccess = useAppSelector(state => state.armlProfile.updateSuccess);

  const handleClose = () => {
    navigate('/arml-profile');
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(id));
    }

    dispatch(getUsers({}));
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
    if (values.winRate !== undefined && typeof values.winRate !== 'number') {
      values.winRate = Number(values.winRate);
    }
    if (values.feedRate !== undefined && typeof values.feedRate !== 'number') {
      values.feedRate = Number(values.feedRate);
    }
    if (values.callRate !== undefined && typeof values.callRate !== 'number') {
      values.callRate = Number(values.callRate);
    }
    if (values.riiRate !== undefined && typeof values.riiRate !== 'number') {
      values.riiRate = Number(values.riiRate);
    }
    if (values.feedEV !== undefined && typeof values.feedEV !== 'number') {
      values.feedEV = Number(values.feedEV);
    }

    const entity = {
      ...armlProfileEntity,
      ...values,
      user: users.find(it => it.id.toString() === values.user?.toString()),
      playerID: armlPlayers.find(it => it.id.toString() === values.playerID?.toString()),
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
          ...armlProfileEntity,
          user: armlProfileEntity?.user?.id,
          playerID: armlProfileEntity?.playerID?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="majProfApp.armlProfile.home.createOrEditLabel" data-cy="ArmlProfileCreateUpdateHeading">
            <Translate contentKey="majProfApp.armlProfile.home.createOrEditLabel">Create or edit a ArmlProfile</Translate>
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
                  id="arml-profile-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('majProfApp.armlProfile.winRate')}
                id="arml-profile-winRate"
                name="winRate"
                data-cy="winRate"
                type="text"
                validate={{
                  min: { value: 0.0, message: translate('entity.validation.min', { min: 0.0 }) },
                  max: { value: 100.0, message: translate('entity.validation.max', { max: 100.0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('majProfApp.armlProfile.feedRate')}
                id="arml-profile-feedRate"
                name="feedRate"
                data-cy="feedRate"
                type="text"
                validate={{
                  min: { value: 0.0, message: translate('entity.validation.min', { min: 0.0 }) },
                  max: { value: 100.0, message: translate('entity.validation.max', { max: 100.0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('majProfApp.armlProfile.callRate')}
                id="arml-profile-callRate"
                name="callRate"
                data-cy="callRate"
                type="text"
                validate={{
                  min: { value: 0.0, message: translate('entity.validation.min', { min: 0.0 }) },
                  max: { value: 100.0, message: translate('entity.validation.max', { max: 100.0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('majProfApp.armlProfile.riiRate')}
                id="arml-profile-riiRate"
                name="riiRate"
                data-cy="riiRate"
                type="text"
                validate={{
                  min: { value: 0.0, message: translate('entity.validation.min', { min: 0.0 }) },
                  max: { value: 100.0, message: translate('entity.validation.max', { max: 100.0 }) },
                  validate: v => isNumber(v) || translate('entity.validation.number'),
                }}
              />
              <ValidatedField
                label={translate('majProfApp.armlProfile.feedEV')}
                id="arml-profile-feedEV"
                name="feedEV"
                data-cy="feedEV"
                type="text"
              />
              <ValidatedField
                id="arml-profile-user"
                name="user"
                data-cy="user"
                label={translate('majProfApp.armlProfile.user')}
                type="select"
              >
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="arml-profile-playerID"
                name="playerID"
                data-cy="playerID"
                label={translate('majProfApp.armlProfile.playerID')}
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/arml-profile" replace color="info">
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

export default ArmlProfileUpdate;
