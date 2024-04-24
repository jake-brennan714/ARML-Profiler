import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ArmlPlayer from './arml-player';
import ArmlGame from './arml-game';
import ArmlProfile from './arml-profile';
import ArmlGameScore from './arml-game-score';
/* jhipster-needle-add-route-import - JHipster will add routes here */

export default () => {
  return (
    <div>
      <ErrorBoundaryRoutes>
        {/* prettier-ignore */}
        <Route path="arml-player/*" element={<ArmlPlayer />} />
        <Route path="arml-game/*" element={<ArmlGame />} />
        <Route path="arml-profile/*" element={<ArmlProfile />} />
        <Route path="arml-game-score/*" element={<ArmlGameScore />} />
        {/* jhipster-needle-add-route-path - JHipster will add routes here */}
      </ErrorBoundaryRoutes>
    </div>
  );
};
