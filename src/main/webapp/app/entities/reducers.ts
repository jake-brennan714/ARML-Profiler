import armlPlayer from 'app/entities/arml-player/arml-player.reducer';
import armlGame from 'app/entities/arml-game/arml-game.reducer';
import armlProfile from 'app/entities/arml-profile/arml-profile.reducer';
import armlGameScore from 'app/entities/arml-game-score/arml-game-score.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const entitiesReducers = {
  armlPlayer,
  armlGame,
  armlProfile,
  armlGameScore,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
};

export default entitiesReducers;
