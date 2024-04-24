import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ArmlGameScore from './arml-game-score';
import ArmlGameScoreDetail from './arml-game-score-detail';
import ArmlGameScoreUpdate from './arml-game-score-update';
import ArmlGameScoreDeleteDialog from './arml-game-score-delete-dialog';

const ArmlGameScoreRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ArmlGameScore />} />
    <Route path="new" element={<ArmlGameScoreUpdate />} />
    <Route path=":id">
      <Route index element={<ArmlGameScoreDetail />} />
      <Route path="edit" element={<ArmlGameScoreUpdate />} />
      <Route path="delete" element={<ArmlGameScoreDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ArmlGameScoreRoutes;
