import React from 'react';
import { Translate } from 'react-jhipster';

import MenuItem from 'app/shared/layout/menus/menu-item';

const EntitiesMenu = () => {
  return (
    <>
      {/* prettier-ignore */}
      <MenuItem icon="asterisk" to="/arml-player">
        <Translate contentKey="global.menu.entities.armlPlayer" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/arml-game">
        <Translate contentKey="global.menu.entities.armlGame" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/arml-profile">
        <Translate contentKey="global.menu.entities.armlProfile" />
      </MenuItem>
      <MenuItem icon="asterisk" to="/arml-game-score">
        <Translate contentKey="global.menu.entities.armlGameScore" />
      </MenuItem>
      {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
    </>
  );
};

export default EntitiesMenu;
