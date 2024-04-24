import React from 'react';
import { Route } from 'react-router-dom';

import ErrorBoundaryRoutes from 'app/shared/error/error-boundary-routes';

import ArmlGame from './arml-game';
import ArmlGameDetail from './arml-game-detail';
import ArmlGameUpdate from './arml-game-update';
import ArmlGameDeleteDialog from './arml-game-delete-dialog';

const ArmlGameRoutes = () => (
  <ErrorBoundaryRoutes>
    <Route index element={<ArmlGame />} />
    <Route path="new" element={<ArmlGameUpdate />} />
    <Route path=":id">
      <Route index element={<ArmlGameDetail />} />
      <Route path="edit" element={<ArmlGameUpdate />} />
      <Route path="delete" element={<ArmlGameDeleteDialog />} />
    </Route>
  </ErrorBoundaryRoutes>
);

export default ArmlGameRoutes;
